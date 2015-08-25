package se.atoulou.jgraphql.models.schema;

import java.util.List;
import java.util.stream.Collectors;

import se.atoulou.jgraphql.models.SchemaMessageWriter;
import se.atoulou.jgraphql.models.schema.Type.EnumType;
import se.atoulou.jgraphql.models.schema.Type.InputObjectType;
import se.atoulou.jgraphql.models.schema.Type.InterfaceType;
import se.atoulou.jgraphql.models.schema.Type.ObjectType;
import se.atoulou.jgraphql.models.schema.Type.ScalarType;
import se.atoulou.jgraphql.models.schema.Type.UnionType;

public final class SchemaPrettyPrinter implements SchemaMessageWriter<String> {
    protected final int tabWidth;

    @Override
    public String writeSchema(Schema schema) {
        StringBuilder stringBuilder = new StringBuilder();
        visitRoot(schema, stringBuilder);
        return stringBuilder.toString();
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

    private void visitRoot(Schema model, StringBuilder stringBuilder) {
        visitSchema(model, stringBuilder, 0);
    }

    private void visitSchema(Schema schema, StringBuilder stringBuilder, int tabs) {
        boolean first = true;
        for (Type type : schema.getTypes()) {
            if (!first) {
                stringBuilder.append('\n');
                stringBuilder.append('\n');
            } else {
                first = false;
            }

            visitType(stringBuilder, type, tabs);
        }
    }

    private void visitType(StringBuilder stringBuilder, Type type, int tabs) {
        switch (type.getKind()) {
        case ENUM:
            visitEnum(stringBuilder, (EnumType) type, tabs);
            break;
        case INPUT_OBJECT:
            visitInputObject(stringBuilder, (InputObjectType) type, tabs);
            break;
        case INTERFACE:
            visitInterface(stringBuilder, (InterfaceType) type, tabs);
            break;
        case LIST:
            assert false;
            break;
        case NON_NULL:
            assert false;
            break;
        case OBJECT:
            visitObject(stringBuilder, (ObjectType) type, tabs);
            break;
        case SCALAR:
            visitScalar(stringBuilder, (ScalarType) type, tabs);
            break;
        case UNION:
            visitUnion(stringBuilder, (UnionType) type, tabs);
            break;
        default:
            break;

        }
    }

    private void visitEnum(StringBuilder stringBuilder, EnumType type, int tabs) {
        appendTabs(stringBuilder, tabs);
        stringBuilder.append(String.format("enum %s {\n", type.getName()));

        boolean first = true;
        for (EnumValue enumValue : type.getEnumValues()) {
            if (!first) {
                stringBuilder.append(',');
                stringBuilder.append('\n');
            } else {
                first = false;
            }

            appendTabs(stringBuilder, tabs + 1);
            stringBuilder.append(enumValue.getName());
        }
        stringBuilder.append('\n');

        appendTabs(stringBuilder, tabs);
        stringBuilder.append('}');
    }

    private void visitInputObject(StringBuilder stringBuilder, InputObjectType type, int tabs) {
        appendTabs(stringBuilder, tabs);
        stringBuilder.append(String.format("input %s {\n", type.getName()));

        boolean first = true;
        for (InputValue inputValue : type.getInputFields()) {
            if (!first) {
                stringBuilder.append(',');
                stringBuilder.append('\n');
            } else {
                first = false;
            }

            appendTabs(stringBuilder, tabs + 1);
            visitInputValue(stringBuilder, inputValue);
        }
        stringBuilder.append('\n');

        appendTabs(stringBuilder, tabs);
        stringBuilder.append('}');
    }

    private void visitInterface(StringBuilder stringBuilder, InterfaceType type, int tabs) {
        appendTabs(stringBuilder, tabs);
        stringBuilder.append(String.format("interface %s {\n", type.getName()));

        boolean first = true;
        for (Field field : type.getFields()) {
            if (!first) {
                stringBuilder.append(',');
                stringBuilder.append('\n');
            } else {
                first = false;
            }

            visitField(stringBuilder, field, tabs + 1);
        }
        stringBuilder.append('\n');

        appendTabs(stringBuilder, tabs);
        stringBuilder.append('}');
    }

    private void visitObject(StringBuilder stringBuilder, ObjectType type, int tabs) {
        appendTabs(stringBuilder, tabs);

        List<String> interfaces = type.getInterfaces();
        if (interfaces.isEmpty()) {
            stringBuilder.append(String.format("type %s {\n", type.getName()));
        } else {
            String implementsString = interfaces.stream().collect(Collectors.joining(", "));
            stringBuilder.append(String.format("type %s : %s {\n", type.getName(), implementsString));
        }

        boolean first = true;
        for (Field field : type.getFields()) {
            if (!first) {
                stringBuilder.append(",\n");
            } else {
                first = false;
            }

            visitField(stringBuilder, field, tabs + 1);
        }
        stringBuilder.append('\n');

        appendTabs(stringBuilder, tabs);
        stringBuilder.append('}');
    }

    private void visitScalar(StringBuilder stringBuilder, ScalarType type, int tabs) {
        appendTabs(stringBuilder, tabs);
        stringBuilder.append(String.format("scalar %s", type.getName()));
    }

    private void visitUnion(StringBuilder stringBuilder, UnionType type, int tabs) {
        appendTabs(stringBuilder, tabs);
        String types = type.getPossibleTypes().stream().map(possibleType -> possibleType.getName()).collect(Collectors.joining(" | "));
        stringBuilder.append(String.format("union %s = %s", type.getName(), types));
    }

    private void visitInputValue(StringBuilder stringBuilder, InputValue inputValue) {
        String inputValueString = String.format("%s: %s", inputValue.getName(), inputValue.getType().getName());
        stringBuilder.append(inputValueString);
        if (inputValue.getDefaultValue() != null) {
            String defaultValueString = String.format(" = %s", inputValue.getDefaultValue());
            stringBuilder.append(defaultValueString);
        }
    }

    private void visitField(StringBuilder stringBuilder, Field field, int tabs) {
        appendTabs(stringBuilder, tabs);
        stringBuilder.append(field.getName());

        if (!field.getArguments().isEmpty()) {
            stringBuilder.append('(');

            boolean first = true;
            for (InputValue inputValue : field.getArguments()) {
                if (!first) {
                    stringBuilder.append(", ");
                } else {
                    first = false;
                }

                visitInputValue(stringBuilder, inputValue);
            }

            stringBuilder.append(')');
        }
        stringBuilder.append(": ");
        stringBuilder.append(field.getType());
    }
}