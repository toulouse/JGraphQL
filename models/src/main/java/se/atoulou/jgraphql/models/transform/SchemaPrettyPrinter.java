package se.atoulou.jgraphql.models.transform;

import java.util.List;
import java.util.stream.Collectors;

import se.atoulou.jgraphql.models.schema.EnumValue;
import se.atoulou.jgraphql.models.schema.Field;
import se.atoulou.jgraphql.models.schema.InputValue;
import se.atoulou.jgraphql.models.schema.Schema;
import se.atoulou.jgraphql.models.schema.Type;
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

    private void appendTabs(StringBuilderVisitorContext context) {
        appendTabs(context, 0);
    }

    private void appendTabs(StringBuilderVisitorContext context, int offset) {
        int spaceCount = (context.getTabs() + offset) * tabWidth;
        for (int i = 0; i < spaceCount; i++) {
            context.getStringBuilder().append(' ');
        }
    }

    @Override
    public void visitSchema(Schema schema, StringBuilderVisitorContext context) {
        context.setPrologue(ctx -> {
        });
        context.setEpilogue(ctx -> {
        });
        context.setItemSeparator(ctx -> {
            ctx.getStringBuilder().append("\n\n");
        });
        SchemaVisitor.super.visitSchema(schema, context);
    }

    @Override
    public void visitEnum(EnumType enumType, StringBuilderVisitorContext context) {
        context.setPrologue(ctx -> {
            appendTabs(ctx);
            ctx.getStringBuilder().append(String.format("enum %s {\n", enumType.getName()));

            ctx.indent();
            appendTabs(ctx);
        });

        context.setItemSeparator(ctx -> {
            ctx.getStringBuilder().append(",\n");
            appendTabs(ctx);
        });

        context.setEpilogue(ctx -> {
            ctx.dedent();

            ctx.getStringBuilder().append('\n');
            appendTabs(ctx);
            ctx.getStringBuilder().append('}');
        });

        SchemaVisitor.super.visitEnum(enumType, context);
    }

    @Override
    public void visitEnumValue(EnumValue enumValue, StringBuilderVisitorContext context) {
        context.getStringBuilder().append(enumValue.getName());
    }

    @Override
    public void visitInputObject(InputObjectType inputObjectType, StringBuilderVisitorContext context) {
        context.setPrologue(ctx -> {
            appendTabs(ctx);
            ctx.getStringBuilder().append(String.format("input %s {\n", inputObjectType.getName()));

            ctx.indent();
            appendTabs(ctx);
        });

        context.setItemSeparator(ctx -> {
            ctx.getStringBuilder().append(",\n");
            appendTabs(ctx);
        });

        context.setEpilogue(ctx -> {
            ctx.dedent();

            ctx.getStringBuilder().append('\n');
            appendTabs(ctx);
            ctx.getStringBuilder().append('}');
        });

        SchemaVisitor.super.visitInputObject(inputObjectType, context);
    }

    @Override
    public void visitInterface(InterfaceType interfaceType, StringBuilderVisitorContext context) {
        context.setPrologue(ctx -> {
            appendTabs(ctx);
            ctx.getStringBuilder().append(String.format("interface %s {\n", interfaceType.getName()));

            ctx.indent();
            appendTabs(ctx);
        });

        context.setItemSeparator(ctx -> {
            ctx.getStringBuilder().append(",\n");
            appendTabs(ctx);
        });

        context.setEpilogue(ctx -> {
            ctx.dedent();

            ctx.getStringBuilder().append('\n');
            appendTabs(ctx);
            ctx.getStringBuilder().append('}');
        });

        SchemaVisitor.super.visitInterface(interfaceType, context);
    }

    @Override
    public void visitObject(ObjectType objectType, StringBuilderVisitorContext context) {
        context.setPrologue(ctx -> {
            appendTabs(ctx);

            List<String> interfaces = objectType.getInterfaces();
            if (interfaces.isEmpty()) {
                ctx.getStringBuilder().append(String.format("type %s {\n", objectType.getName()));
            } else {
                String implementsString = interfaces.stream().collect(Collectors.joining(", "));
                ctx.getStringBuilder().append(String.format("type %s : %s {\n", objectType.getName(), implementsString));
            }

            ctx.indent();
            appendTabs(ctx);
        });

        context.setItemSeparator(ctx -> {
            ctx.getStringBuilder().append(",\n");
            appendTabs(ctx);
        });

        context.setEpilogue(ctx -> {
            ctx.dedent();

            ctx.getStringBuilder().append('\n');
            appendTabs(ctx);
            ctx.getStringBuilder().append('}');
        });

        SchemaVisitor.super.visitObject(objectType, context);
    }

    @Override
    public void visitScalar(ScalarType scalarType, StringBuilderVisitorContext context) {
        StringBuilder stringBuilder = context.getStringBuilder();
        appendTabs(context);
        stringBuilder.append(String.format("scalar %s", scalarType.getName()));
    }

    @Override
    public void visitUnion(UnionType unionType, StringBuilderVisitorContext context) {
        context.setPrologue(ctx -> {
            appendTabs(ctx);
            ctx.getStringBuilder().append(String.format("union %s = ", unionType.getName()));
        });

        context.setItemSeparator(ctx -> {
            ctx.getStringBuilder().append(" | ");
        });

        // Visit manually: don't want to dump definition
        context.enter();
        for (Type type : unionType.getPossibleTypes()) {
            context.incrementIndex();
            context.getStringBuilder().append(type.getName());
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
        context.setPrologue(ctx -> {
            ctx.getStringBuilder().append(fieldType.getName());
            if (!fieldType.getArguments().isEmpty()) {
                ctx.getStringBuilder().append('(');
            }
        });

        context.setItemSeparator(ctx -> {
            ctx.getStringBuilder().append(", ");
        });

        context.setEpilogue(ctx -> {
            if (!fieldType.getArguments().isEmpty()) {
                ctx.getStringBuilder().append(')');
            }

            ctx.getStringBuilder().append(": ");
            ctx.getStringBuilder().append(fieldType.getType());
        });

        context.enter();
        for (InputValue inputValue : fieldType.getArguments()) {
            context.incrementIndex();
            visitInputValue(inputValue, context);
        }
        context.leave();
    }
}