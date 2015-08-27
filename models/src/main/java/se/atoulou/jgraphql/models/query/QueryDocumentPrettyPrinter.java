package se.atoulou.jgraphql.models.query;

import java.util.List;

import se.atoulou.jgraphql.models.QueryDocumentMessageWriter;
import se.atoulou.jgraphql.models.query.Selection.FragmentSpread;
import se.atoulou.jgraphql.models.query.Selection.InlineFragment;
import se.atoulou.jgraphql.models.query.Selection.SelectionField;

public final class QueryDocumentPrettyPrinter implements QueryDocumentMessageWriter<String> {
    protected final int tabWidth;

    @Override
    public String writeQueryDocument(QueryDocument query) {
        StringBuilder stringBuilder = new StringBuilder();
        visitRoot(query, stringBuilder);
        return stringBuilder.toString();
    }

    public QueryDocumentPrettyPrinter() {
        this(2);
    }

    public QueryDocumentPrettyPrinter(int tabWidth) {
        this.tabWidth = tabWidth;
    }

    private void appendTabs(StringBuilder stringBuilder, int tabCount) {
        int spaceCount = tabCount * tabWidth;
        for (int i = 0; i < spaceCount; i++) {
            stringBuilder.append(' ');
        }
    }

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

        List<Argument> arguments = directive.getArguments();
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
                visitSelectionField(stringBuilder, (Selection.SelectionField) selection, tabs + 1);
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

    private void visitSelectionField(StringBuilder stringBuilder, SelectionField selection, int tabs) {
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