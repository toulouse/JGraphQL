package se.atoulou.jgraphql.models;

import java.util.List;
import java.util.stream.Collectors;

import se.atoulou.jgraphql.models.query.Argument;
import se.atoulou.jgraphql.models.query.Directive;
import se.atoulou.jgraphql.models.query.FragmentDefinition;
import se.atoulou.jgraphql.models.query.OperationDefinition;
import se.atoulou.jgraphql.models.query.QueryDocument;
import se.atoulou.jgraphql.models.query.Selection;
import se.atoulou.jgraphql.models.query.Selection.FragmentSpread;
import se.atoulou.jgraphql.models.query.Selection.InlineFragment;
import se.atoulou.jgraphql.models.query.VariableDefinition;
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

public abstract class PrettyPrinter<T> {

    protected final int tabWidth;

    protected PrettyPrinter(int tabWidth) {
        this.tabWidth = tabWidth;
    }

    public static PrettyPrinter<Schema> schemaPrinter(int tabWidth) {
        return new SchemaPrettyPrinter(tabWidth);
    }

    public static PrettyPrinter<Schema> schemaPrinter() {
        return schemaPrinter(2);
    }

    public static PrettyPrinter<QueryDocument> documentPrinter(int tabWidth) {
        return new DocumentPrettyPrinter(tabWidth);
    }

    public static PrettyPrinter<QueryDocument> documentPrinter() {
        return documentPrinter(2);
    }

    public String print(T model) {
        StringBuilder stringBuilder = new StringBuilder();
        visitRoot(model, stringBuilder);
        return stringBuilder.toString();
    }

    protected abstract void visitRoot(T model, StringBuilder stringBuilder);

    protected void appendTabs(StringBuilder stringBuilder, int tabCount) {
        int spaceCount = tabCount * tabWidth;
        for (int i = 0; i < spaceCount; i++) {
            stringBuilder.append(' ');
        }
    }

    private static final class SchemaPrettyPrinter extends PrettyPrinter<Schema> {

        protected SchemaPrettyPrinter(int tabWidth) {
            super(tabWidth);
        }

        @Override
        public void visitRoot(Schema model, StringBuilder stringBuilder) {
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

    private static final class DocumentPrettyPrinter extends PrettyPrinter<QueryDocument> {

        protected DocumentPrettyPrinter(int tabWidth) {
            super(tabWidth);
        }

        @Override
        protected void visitRoot(QueryDocument model, StringBuilder stringBuilder) {
            visitDocument(model, stringBuilder, 0);
        }

        private void visitDocument(QueryDocument document, StringBuilder stringBuilder, int tabs) {
            boolean first = true;
            for (OperationDefinition operation : document.getOperations()) {
                if (!first) {
                    stringBuilder.append('\n');
                    stringBuilder.append('\n');
                } else {
                    first = false;
                }

                visitOperation(stringBuilder, operation, tabs);
            }

            for (FragmentDefinition fragment : document.getFragments()) {
                if (!first) {
                    stringBuilder.append('\n');
                    stringBuilder.append('\n');
                } else {
                    first = false;
                }

                visitFragment(stringBuilder, fragment, tabs);
            }
        }

        private void visitOperation(StringBuilder stringBuilder, OperationDefinition operation, int tabs) {
            if (operation.getName() == null) {
                appendTabs(stringBuilder, tabs);
                visitSelectionSet(stringBuilder, operation.getSelectionSet(), tabs + 1);
                return;
            }

            String operationType = operation.getOperationType().name().toLowerCase();
            appendTabs(stringBuilder, tabs);
            stringBuilder.append(String.format("%s %s", operationType, operation.getName()));

            List<VariableDefinition> variableDefinitions = operation.getVariableDefinitions();
            if (!variableDefinitions.isEmpty()) {
                stringBuilder.append('(');

                boolean first = true;
                for (VariableDefinition variableDefinition : variableDefinitions) {
                    if (!first) {
                        stringBuilder.append(", ");
                    } else {
                        first = false;
                    }
                    visitVariableDefinition(stringBuilder, variableDefinition);
                }
                stringBuilder.append(')');
            }

            List<Directive> directives = operation.getDirectives();
            if (!directives.isEmpty()) {
                for (Directive directive : directives) {
                    stringBuilder.append(' ');
                    visitDirective(stringBuilder, directive);
                }
            }

            stringBuilder.append(' ');

            visitSelectionSet(stringBuilder, operation.getSelectionSet(), tabs);
        }

        private void visitFragment(StringBuilder stringBuilder, FragmentDefinition fragment, int tabs) {
            String fragmentName = fragment.getName();
            String typeCondition = fragment.getTypeCondition();

            appendTabs(stringBuilder, tabs);
            stringBuilder.append(String.format("fragment %s on %s", fragmentName, typeCondition));

            List<Directive> directives = fragment.getDirectives();
            if (!directives.isEmpty()) {
                for (Directive directive : directives) {
                    stringBuilder.append(' ');
                    visitDirective(stringBuilder, directive);
                }
            }

            stringBuilder.append(' ');
            visitSelectionSet(stringBuilder, fragment.getSelectionSet(), tabs);
        }

        private void visitDirective(StringBuilder stringBuilder, Directive directive) {
            stringBuilder.append(String.format("@%s", directive.getName()));

            List<Argument> arguments = directive.getArgs();
            if (!arguments.isEmpty()) {
                stringBuilder.append('(');
                boolean first = true;
                for (Argument argument : arguments) {
                    if (!first) {
                        stringBuilder.append(", ");
                    } else {
                        first = false;
                    }

                    visitArgument(stringBuilder, argument);
                }
                stringBuilder.append(')');
            }
        }

        private void visitArgument(StringBuilder stringBuilder, Argument argument) {
            String argumentString = String.format("%s: %s", argument.getName(), argument.getValue().getValue());
            stringBuilder.append(argumentString);
        }

        private void visitVariableDefinition(StringBuilder stringBuilder, VariableDefinition variableDefinition) {
            String inputValueString = String.format("%s: %s", variableDefinition.getVariable(), variableDefinition.getType());
            stringBuilder.append(inputValueString);
            if (variableDefinition.getDefaultValue() != null) {
                String defaultValueString = String.format(" = %s", variableDefinition.getDefaultValue().getValue());
                stringBuilder.append(defaultValueString);
            }
        }

        private void visitSelectionSet(StringBuilder stringBuilder, List<Selection> selectionSet, int tabs) {
            stringBuilder.append("{");
            stringBuilder.append('\n');

            for (Selection selection : selectionSet) {
                appendTabs(stringBuilder, tabs + 1);

                switch (selection.getKind()) {
                case FIELD:
                    visitSelectionField(stringBuilder, (Selection.Field) selection, tabs + 1);
                    break;
                case FRAGMENT_SPREAD:
                    visitFragmentSpread(stringBuilder, (Selection.FragmentSpread) selection, tabs + 1);
                    break;
                case INLINE_FRAGMENT:
                    visitInlineFragment(stringBuilder, (Selection.InlineFragment) selection, tabs + 1);
                    break;
                default:
                    break;
                }

                stringBuilder.append('\n');
            }

            appendTabs(stringBuilder, tabs);
            stringBuilder.append('}');

        }

        private void visitInlineFragment(StringBuilder stringBuilder, InlineFragment selection, int tabs) {
            stringBuilder.append(String.format("... on %s", selection.getTypeCondition()));

            List<Directive> directives = selection.getDirectives();
            if (!directives.isEmpty()) {
                for (Directive directive : directives) {
                    stringBuilder.append(' ');
                    visitDirective(stringBuilder, directive);
                }
            }

            stringBuilder.append(' ');
            visitSelectionSet(stringBuilder, selection.getSelectionSet(), tabs);
        }

        private void visitFragmentSpread(StringBuilder stringBuilder, FragmentSpread selection, int tabs) {
            stringBuilder.append(String.format("...%s", selection.getName()));

            List<Directive> directives = selection.getDirectives();
            if (!directives.isEmpty()) {
                for (Directive directive : directives) {
                    stringBuilder.append(' ');
                    visitDirective(stringBuilder, directive);
                }
            }
        }

        private void visitSelectionField(StringBuilder stringBuilder, se.atoulou.jgraphql.models.query.Selection.Field selection, int tabs) {
            String alias = selection.getAlias();
            if (alias != null) {
                stringBuilder.append(alias);
                stringBuilder.append(" : ");
            }

            String name = selection.getName();
            stringBuilder.append(name);

            List<Argument> arguments = selection.getArguments();
            if (!arguments.isEmpty()) {
                stringBuilder.append('(');
                boolean first = true;
                for (Argument argument : arguments) {
                    if (!first) {
                        stringBuilder.append(' ');
                    } else {
                        first = false;
                    }

                    visitArgument(stringBuilder, argument);
                }
                stringBuilder.append(')');
            }

            List<Directive> directives = selection.getDirectives();
            if (!directives.isEmpty()) {
                for (Directive directive : directives) {
                    stringBuilder.append(' ');
                    visitDirective(stringBuilder, directive);
                }
            }

            List<Selection> selectionSet = selection.getSelectionSet();
            if (!selectionSet.isEmpty()) {
                stringBuilder.append(' ');
                visitSelectionSet(stringBuilder, selection.getSelectionSet(), tabs);
            }
        }
    }
}
