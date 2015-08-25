package se.atoulou.jgraphql.models.schema;

import java.util.List;
import java.util.stream.Collectors;

import se.atoulou.jgraphql.models.SchemaMessageWriter;
import se.atoulou.jgraphql.models.StringBuilderVisitorContext;
import se.atoulou.jgraphql.models.schema.Type.EnumType;
import se.atoulou.jgraphql.models.schema.Type.InputObjectType;
import se.atoulou.jgraphql.models.schema.Type.InterfaceType;
import se.atoulou.jgraphql.models.schema.Type.ObjectType;
import se.atoulou.jgraphql.models.schema.Type.ScalarType;
import se.atoulou.jgraphql.models.schema.Type.UnionType;

public final class SchemaPrettyPrinter implements SchemaMessageWriter<String>, SchemaVisitor<StringBuilderVisitorContext> {
    protected final int tabWidth;

    @Override
    public String writeSchema(Schema schema) {
        StringBuilderVisitorContext context = new StringBuilderVisitorContext();
        visitSchema(schema, context);
        return context.getStringBuilder().toString();
    }

    public SchemaPrettyPrinter() {
        this(2);
    }

    public SchemaPrettyPrinter(int tabWidth) {
        this.tabWidth = tabWidth;
    }

    private void appendTabs(StringBuilder stringBuilder, int tabCount) {
        int spaceCount = tabCount * tabWidth;
        for (int i = 0; i < spaceCount; i++) {
            stringBuilder.append(' ');
        }
    }

    @Override
    public void visitSchema(Schema schema, StringBuilderVisitorContext context) {
        context.setPunctuator(ctx -> {
            ctx.getStringBuilder().append("\n\n");
        });
        SchemaVisitor.super.visitSchema(schema, context);
    }

    @Override
    public void visitEnum(EnumType enumType, StringBuilderVisitorContext context) {
        StringBuilder stringBuilder = context.getStringBuilder();

        appendTabs(stringBuilder, context.currentLevel());
        stringBuilder.append(String.format("enum %s {\n", enumType.getName()));

        context.setPunctuator(ctx -> {
            ctx.getStringBuilder().append(",\n");
            appendTabs(stringBuilder, ctx.currentLevel());
        });

        appendTabs(stringBuilder, context.currentLevel() + 1);
        SchemaVisitor.super.visitEnum(enumType, context);

        stringBuilder.append('\n');

        appendTabs(stringBuilder, context.currentLevel());
        stringBuilder.append('}');
    }

    @Override
    public void visitEnumValue(EnumValue enumValue, StringBuilderVisitorContext context) {
        context.getStringBuilder().append(enumValue.getName());
    }

    @Override
    public void visitInputObject(InputObjectType inputObjectType, StringBuilderVisitorContext context) {
        StringBuilder stringBuilder = context.getStringBuilder();

        appendTabs(stringBuilder, context.currentLevel());
        stringBuilder.append(String.format("input %s {\n", inputObjectType.getName()));

        context.setPunctuator(ctx -> {
            ctx.getStringBuilder().append(",\n");
            appendTabs(ctx.getStringBuilder(), ctx.currentLevel() - 1);
        });

        appendTabs(stringBuilder, context.currentLevel());
        SchemaVisitor.super.visitInputObject(inputObjectType, context);

        stringBuilder.append('\n');
        appendTabs(stringBuilder, context.currentLevel());
        stringBuilder.append('}');
    }

    @Override
    public void visitInterface(InterfaceType interfaceType, StringBuilderVisitorContext context) {
        StringBuilder stringBuilder = context.getStringBuilder();

        appendTabs(stringBuilder, context.currentLevel());
        stringBuilder.append(String.format("interface %s {\n", interfaceType.getName()));

        context.setPunctuator(ctx -> {
            ctx.getStringBuilder().append(",\n");
            appendTabs(ctx.getStringBuilder(), ctx.currentLevel() - 1);
        });

        appendTabs(stringBuilder, context.currentLevel());
        SchemaVisitor.super.visitInterface(interfaceType, context);

        stringBuilder.append('\n');
        appendTabs(stringBuilder, context.currentLevel());
        stringBuilder.append('}');
    }

    @Override
    public void visitObject(ObjectType objectType, StringBuilderVisitorContext context) {
        StringBuilder stringBuilder = context.getStringBuilder();

        appendTabs(stringBuilder, context.currentLevel());

        List<String> interfaces = objectType.getInterfaces();
        if (interfaces.isEmpty()) {
            stringBuilder.append(String.format("type %s {\n", objectType.getName()));
        } else {
            String implementsString = interfaces.stream().collect(Collectors.joining(", "));
            stringBuilder.append(String.format("type %s : %s {\n", objectType.getName(), implementsString));
        }

        context.setPunctuator(ctx -> {
            ctx.getStringBuilder().append(",\n");
            appendTabs(ctx.getStringBuilder(), ctx.currentLevel() - 1);
        });

        appendTabs(stringBuilder, context.currentLevel());
        SchemaVisitor.super.visitObject(objectType, context);

        stringBuilder.append('\n');
        appendTabs(stringBuilder, context.currentLevel());
        stringBuilder.append('}');
    }

    @Override
    public void visitScalar(ScalarType scalarType, StringBuilderVisitorContext context) {
        StringBuilder stringBuilder = context.getStringBuilder();
        appendTabs(stringBuilder, context.currentLevel());
        stringBuilder.append(String.format("scalar %s", scalarType.getName()));
    }

    @Override
    public void visitUnion(UnionType unionType, StringBuilderVisitorContext context) {
        StringBuilder stringBuilder = context.getStringBuilder();

        appendTabs(stringBuilder, context.currentLevel());
        stringBuilder.append(String.format("union %s = ", unionType.getName()));

        // Visit manually: don't want to dump definition
        // TODO: reconsider API for this
        context.setPunctuator(ctx -> {
            ctx.getStringBuilder().append(" | ");
        });
        context.enter();
        for (Type type : unionType.getPossibleTypes()) {
            context.incrementIndex();
            stringBuilder.append(type.getName());
        }
        context.leave();
    }

    @Override
    public void visitInputValue(InputValue inputValueType, StringBuilderVisitorContext context) {
        StringBuilder stringBuilder = context.getStringBuilder();

        String inputValueString = String.format("%s: %s", inputValueType.getName(), inputValueType.getType().getName());
        stringBuilder.append(inputValueString);
        if (inputValueType.getDefaultValue() != null) {
            String defaultValueString = String.format(" = %s", inputValueType.getDefaultValue());
            stringBuilder.append(defaultValueString);
        }
    }

    @Override
    public void visitField(Field fieldType, StringBuilderVisitorContext context) {
        StringBuilder stringBuilder = context.getStringBuilder();

        appendTabs(stringBuilder, context.currentLevel());
        stringBuilder.append(fieldType.getName());

        // TODO: add prependers/appenders
        if (!fieldType.getArguments().isEmpty()) {
            stringBuilder.append('(');

            boolean first = true;
            for (InputValue inputValue : fieldType.getArguments()) {
                if (!first) {
                    stringBuilder.append(", ");
                } else {
                    first = false;
                }

                visitInputValue(inputValue, context);
            }

            stringBuilder.append(')');
        }
        stringBuilder.append(": ");
        stringBuilder.append(fieldType.getType());
    }
}