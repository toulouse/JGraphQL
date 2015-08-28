package se.atoulou.jgraphql.models;

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
import se.atoulou.jgraphql.models.transform.SchemaBaseVisitor;
import se.atoulou.jgraphql.models.transform.SchemaMessageWriter;

public final class SchemaPrettyPrinter extends SchemaBaseVisitor<StringBuilderVisitorContext>implements SchemaMessageWriter<String> {
    private final String  newline;
    private final String  tab;
    private final boolean isCompact;

    @Override
    public String writeSchema(Schema schema) {
        StringBuilderVisitorContext context = new StringBuilderVisitorContext(newline, tab, isCompact);
        visitSchema(schema, context);
        return context.getStringBuilder().toString();
    }

    public SchemaPrettyPrinter(boolean isCompact) {
        this(isCompact ? "" : "¥n", isCompact ? "" : "  ", isCompact);
    }

    public SchemaPrettyPrinter() {
        this("\n", "  ");
    }

    public SchemaPrettyPrinter(String newline, String tab) {
        this(newline, tab, false);
    }

    public SchemaPrettyPrinter(String newline, String tab, boolean isCompact) {
        this.newline = newline;
        this.tab = tab;
        this.isCompact = isCompact;
    }

    @Override
    public void punctuateSchema(Schema schema, StringBuilderVisitorContext context) {
        context.appendNewlines(2);
    }

    @Override
    public void beforeEnum(EnumType enumType, StringBuilderVisitorContext context) {
        context.appendTabs();

        context.append(String.format("enum %s", enumType.getName()));
        context.appendRemovableSpace();
        context.append('{');
        context.indent();

        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void punctuateEnum(EnumType enumType, StringBuilderVisitorContext context) {
        context.append(',');
        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void afterEnum(EnumType enumType, StringBuilderVisitorContext context) {
        context.dedent();

        context.appendNewline();
        context.appendTabs();
        context.append('}');
    }

    @Override
    public void visitEnumValue(EnumValue enumValue, StringBuilderVisitorContext context) {
        context.append(enumValue.getName());
    }

    @Override
    public void beforeInputObject(InputObjectType inputObjectType, StringBuilderVisitorContext context) {
        context.appendTabs();

        context.append(String.format("input %s", inputObjectType.getName()));
        context.appendRemovableSpace();
        context.append('{');

        context.indent();

        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void afterInputObject(InputObjectType inputObjectType, StringBuilderVisitorContext context) {
        context.dedent();

        context.appendNewline();
        context.appendTabs();
        context.append('}');
    }

    @Override
    public void punctuateInputValues(List<InputValue> inputValues, StringBuilderVisitorContext context) {
        context.append(',');
        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void beforeInterface(InterfaceType interfaceType, StringBuilderVisitorContext context) {
        context.appendTabs();

        context.append(String.format("interface %s", interfaceType.getName()));
        context.appendRemovableSpace();
        context.append('{');

        context.indent();

        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void afterInterface(InterfaceType interfaceType, StringBuilderVisitorContext context) {
        context.dedent();

        context.appendNewline();
        context.appendTabs();
        context.append('}');
    }

    @Override
    public void beforeObject(ObjectType objectType, StringBuilderVisitorContext context) {
        context.appendTabs();

        context.append(String.format("type %s", objectType.getName()));

        List<String> interfaces = objectType.getInterfaces();
        if (!interfaces.isEmpty()) {
            String joiner = context.isCompact() ? "," : ", ";

            String implementsString = interfaces.stream().collect(Collectors.joining(joiner));
            context.appendRemovableSpace();
            context.append(':');
            context.appendRemovableSpace();
            context.append(implementsString);
        }

        context.appendRemovableSpace();
        context.append('{');

        context.indent();

        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void afterObject(ObjectType objectType, StringBuilderVisitorContext context) {
        context.dedent();

        context.appendNewline();
        context.appendTabs();
        context.append('}');
    }

    @Override
    public void visitScalar(ScalarType scalarType, StringBuilderVisitorContext context) {
        context.appendTabs();
        context.append(String.format("scalar %s", scalarType.getName()));

        if (context.isCompact()) {
            context.append(',');
        }
    }

    @Override
    public void beforeUnion(UnionType unionType, StringBuilderVisitorContext context) {
        context.appendTabs();
        context.append(String.format("union %s", unionType.getName()));
        context.appendRemovableSpace();
        context.append('=');
        context.appendRemovableSpace();
    }

    @Override
    public void afterUnion(UnionType unionType, StringBuilderVisitorContext context) {
        if (context.isCompact()) {
            context.append(',');
        }
    }

    @Override
    public void punctuateUnion(UnionType unionType, StringBuilderVisitorContext context) {
        context.appendRemovableSpace();
        context.append('|');
        context.appendRemovableSpace();
    }

    @Override
    public void visitUnionType(Type unionType, StringBuilderVisitorContext context) {
        context.append(unionType.getName());
    }

    @Override
    public void beforeInputValue(InputValue inputValue, StringBuilderVisitorContext context) {
        context.append(inputValue.getName());
        context.append(':');
        context.appendRemovableSpace();
        context.append(inputValue.getType().getName());

        if (inputValue.getDefaultValue() != null) {
            context.appendRemovableSpace();
            context.append('=');
            context.appendRemovableSpace();

            context.append(inputValue.getDefaultValue());
        }
    }

    @Override
    public void beforeField(Field field, StringBuilderVisitorContext context) {
        context.append(field.getName());

        if (!field.getArguments().isEmpty()) {
            context.setNewlinesEnabled(false);
            context.append('(');
        }
    }

    @Override
    public void punctuateFields(List<Field> fields, StringBuilderVisitorContext context) {
        context.append(',');
        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void afterField(Field field, StringBuilderVisitorContext context) {
        if (!field.getArguments().isEmpty()) {
            context.append(')');
            context.setNewlinesEnabled(true);
        }

        context.append(':');
        context.appendRemovableSpace();
        context.append(field.getType());
    }
}