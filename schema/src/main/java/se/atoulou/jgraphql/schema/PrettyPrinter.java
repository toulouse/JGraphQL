package se.atoulou.jgraphql.schema;

import java.util.List;
import java.util.stream.Collectors;

import se.atoulou.jgraphql.schema.Type.EnumType;
import se.atoulou.jgraphql.schema.Type.InputObjectType;
import se.atoulou.jgraphql.schema.Type.InterfaceType;
import se.atoulou.jgraphql.schema.Type.ObjectType;
import se.atoulou.jgraphql.schema.Type.ScalarType;
import se.atoulou.jgraphql.schema.Type.UnionType;

public final class PrettyPrinter {

    private final StringBuilder stringBuilder;
    private final Schema        schema;
    private final int           tabWidth;

    public PrettyPrinter(Schema schema) {
        this(schema, 2);
    }

    public PrettyPrinter(Schema schema, int tabWidth) {
        this.stringBuilder = new StringBuilder();
        this.schema = schema;
        this.tabWidth = tabWidth;
        visitSchema(0);
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    private void visitSchema(int tabs) {
        boolean first = true;
        for (Type type : schema.getTypes()) {
            if (!first) {
                stringBuilder.append('\n');
                stringBuilder.append('\n');
            } else {
                first = false;
            }

            visitType(type, tabs);
        }
    }

    private void visitType(Type type, int tabs) {
        switch (type.getKind()) {
        case ENUM:
            visitEnum((EnumType) type, tabs);
            break;
        case INPUT_OBJECT:
            visitInputObject((InputObjectType) type, tabs);
            break;
        case INTERFACE:
            visitInterface((InterfaceType) type, tabs);
            break;
        case LIST:
            assert false;
            break;
        case NON_NULL:
            assert false;
            break;
        case OBJECT:
            visitObject((ObjectType) type, tabs);
            break;
        case SCALAR:
            visitScalar((ScalarType) type, tabs);
            break;
        case UNION:
            visitUnion((UnionType) type, tabs);
            break;
        default:
            break;

        }
    }

    private void visitEnum(EnumType type, int tabs) {
        appendTabs(tabs);
        stringBuilder.append(String.format("enum %s {\n", type.getName()));

        boolean first = true;
        for (EnumValue enumValue : type.getEnumValues()) {
            if (!first) {
                stringBuilder.append(',');
                stringBuilder.append('\n');
            } else {
                first = false;
            }

            appendTabs(tabs + 1);
            stringBuilder.append(enumValue.getName());
        }
        stringBuilder.append('\n');

        appendTabs(tabs);
        stringBuilder.append('}');
    }

    private void visitInputObject(InputObjectType type, int tabs) {
        appendTabs(tabs);
        stringBuilder.append(String.format("input %s {\n", type.getName()));

        boolean first = true;
        for (InputValue inputValue : type.getInputFields()) {
            if (!first) {
                stringBuilder.append(',');
                stringBuilder.append('\n');
            } else {
                first = false;
            }

            appendTabs(tabs + 1);
            visitInputValue(inputValue);
        }
        stringBuilder.append('\n');

        appendTabs(tabs);
        stringBuilder.append('}');
    }

    private void visitInterface(InterfaceType type, int tabs) {
        appendTabs(tabs);
        stringBuilder.append(String.format("interface %s {\n", type.getName()));

        boolean first = true;
        for (Field field : type.getFields()) {
            if (!first) {
                stringBuilder.append(',');
                stringBuilder.append('\n');
            } else {
                first = false;
            }

            visitField(field, tabs + 1);
        }
        stringBuilder.append('\n');

        appendTabs(tabs);
        stringBuilder.append('}');
    }

    private void visitObject(ObjectType type, int tabs) {
        appendTabs(tabs);

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

            visitField(field, tabs + 1);
        }
        stringBuilder.append('\n');

        appendTabs(tabs);
        stringBuilder.append('}');
    }

    private void visitScalar(ScalarType type, int tabs) {
        appendTabs(tabs);
        stringBuilder.append(String.format("scalar %s", type.getName()));
    }

    private void visitUnion(UnionType type, int tabs) {
        appendTabs(tabs);
        String types = type.getPossibleTypes().stream().map(possibleType -> possibleType.getName()).collect(Collectors.joining(" | "));
        stringBuilder.append(String.format("union %s = %s", type.getName(), types));
    }

    private void visitInputValue(InputValue inputValue) {
        String inputValueString = String.format("%s: %s", inputValue.getName(), inputValue.getType().getName());
        stringBuilder.append(inputValueString);
        if (inputValue.getDefaultValue() != null) {
            String defaultValueString = String.format(" = %s", inputValue.getDefaultValue());
            stringBuilder.append(defaultValueString);
        }
    }

    private void visitField(Field field, int tabs) {
        appendTabs(tabs);
        stringBuilder.append(field.getName());

        if (!field.getArgs().isEmpty()) {
            stringBuilder.append('(');

            boolean first = true;
            for (InputValue inputValue : field.getArgs()) {
                if (!first) {
                    stringBuilder.append(", ");
                } else {
                    first = false;
                }

                visitInputValue(inputValue);
            }

            stringBuilder.append(')');
        }
        stringBuilder.append(": ");
        stringBuilder.append(field.getType());

    }

    private void appendTabs(int tabCount) {
        int spaceCount = tabCount * tabWidth;
        for (int i = 0; i < spaceCount; i++) {
            stringBuilder.append(' ');
        }
    }
}
