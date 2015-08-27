package se.atoulou.jgraphql.models;

import java.util.List;

import se.atoulou.jgraphql.models.query.Argument;
import se.atoulou.jgraphql.models.query.Directive;
import se.atoulou.jgraphql.models.query.FragmentDefinition;
import se.atoulou.jgraphql.models.query.OperationDefinition;
import se.atoulou.jgraphql.models.query.QueryDocument;
import se.atoulou.jgraphql.models.query.Selection;
import se.atoulou.jgraphql.models.query.VariableDefinition;
import se.atoulou.jgraphql.models.query.Selection.FragmentSpread;
import se.atoulou.jgraphql.models.query.Selection.InlineFragment;
import se.atoulou.jgraphql.models.query.Selection.SelectionField;
import se.atoulou.jgraphql.models.transform.QueryDocumentBaseVisitor;
import se.atoulou.jgraphql.models.transform.QueryDocumentMessageWriter;

public final class QueryDocumentPrettyPrinter extends QueryDocumentBaseVisitor<StringBuilderVisitorContext>implements QueryDocumentMessageWriter<String> {
    private final String newline;
    private final String tab;

    @Override
    public String writeQueryDocument(QueryDocument queryDocument) {
        StringBuilderVisitorContext context = new StringBuilderVisitorContext(newline, tab);
        visitQueryDocument(queryDocument, context);
        return context.getStringBuilder().toString();
    }

    public QueryDocumentPrettyPrinter() {
        this("\n", "  ");
    }

    public QueryDocumentPrettyPrinter(String newline, String tab) {
        this.newline = newline;
        this.tab = tab;
    }

    @Override
    public void punctuateQueryDocument(QueryDocument queryDocument, StringBuilderVisitorContext context) {
        context.appendNewlines(2);
    }

    @Override
    public void beforeOperation(OperationDefinition operation, StringBuilderVisitorContext context) {
        if (operation.getName() != null) {
            context.appendTabs();

            String operationType = operation.getOperationType().name().toLowerCase();
            context.append(String.format("%s %s", operationType, operation.getName()));
        }
    }

    @Override
    public void beforeFragment(FragmentDefinition fragment, StringBuilderVisitorContext context) {
        String fragmentName = fragment.getName();
        String typeCondition = fragment.getTypeCondition();

        context.appendTabs();
        context.append(String.format("fragment %s on %s", fragmentName, typeCondition));
    }

    @Override
    public void beforeDirectives(List<Directive> directives, StringBuilderVisitorContext context) {
        if (!directives.isEmpty()) {
            context.append(' ');
        }
    }

    @Override
    public void punctuateDirectives(List<Directive> directives, StringBuilderVisitorContext context) {
        context.append(' ');
    }

    @Override
    public void beforeDirective(Directive directive, StringBuilderVisitorContext context) {
        context.append(String.format("@%s", directive.getName()));

        if (!directive.getArguments().isEmpty()) {
            context.setNewlinesEnabled(false);
        }
    }

    @Override
    public void afterDirective(Directive directive, StringBuilderVisitorContext context) {
        if (!directive.getArguments().isEmpty()) {
            context.setNewlinesEnabled(true);
        }
    }

    @Override
    public void beforeArguments(List<Argument> arguments, StringBuilderVisitorContext context) {
        if (!arguments.isEmpty()) {
            context.append('(');
        }
    }

    @Override
    public void punctuateArguments(List<Argument> arguments, StringBuilderVisitorContext context) {
        context.append(", ");
    }

    @Override
    public void afterArguments(List<Argument> arguments, StringBuilderVisitorContext context) {
        if (!arguments.isEmpty()) {
            context.append(')');
        }
    }

    @Override
    public void beforeArgument(Argument argument, StringBuilderVisitorContext context) {
        String argumentString = String.format("%s: %s", argument.getName(), argument.getValue().getValue());
        context.append(argumentString);
    }

    @Override
    public void beforeVariableDefinitions(List<VariableDefinition> variableDefinitions, StringBuilderVisitorContext context) {
        if (!variableDefinitions.isEmpty()) {
            context.append('(');
        }
    }

    @Override
    public void punctuateVariableDefinitions(List<VariableDefinition> variableDefinitions, StringBuilderVisitorContext context) {
        context.append(", ");
    }

    @Override
    public void afterVariableDefinitions(List<VariableDefinition> variableDefinitions, StringBuilderVisitorContext context) {
        if (!variableDefinitions.isEmpty()) {
            context.append(')');
        }
    }

    @Override
    public void beforeVariableDefinition(VariableDefinition variableDefinition, StringBuilderVisitorContext context) {
        String variableDefinitionString = String.format("%s: %s", variableDefinition.getVariable(), variableDefinition.getType());
        context.append(variableDefinitionString);
        if (variableDefinition.getDefaultValue() != null) {
            String defaultValueString = String.format(" = %s", variableDefinition.getDefaultValue().getValue());
            context.append(defaultValueString);
        }
    }

    @Override
    public void beforeSelectionSet(List<Selection> selectionSet, StringBuilderVisitorContext context) {
        if (selectionSet.isEmpty()) {
            return;
        }

        if (selectionSet.size() == 1 && selectionSet.get(0).isLeaf()) {
            context.append(" { ");
        } else {
            context.append(" {");
            context.indent();

            context.appendNewline();
            context.appendTabs();
        }
    }

    @Override
    public void punctuateSelectionSet(List<Selection> selectionSet, StringBuilderVisitorContext context) {
        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void afterSelectionSet(List<Selection> selectionSet, StringBuilderVisitorContext context) {
        if (selectionSet.isEmpty()) {
            return;
        }
        if (selectionSet.size() == 1 && selectionSet.get(0).isLeaf()) {
            context.append(" }");
        } else {
            context.dedent();

            context.appendNewline();
            context.appendTabs();
            context.append('}');
        }
    }

    @Override
    public void beforeInlineFragment(InlineFragment inlineFragment, StringBuilderVisitorContext context) {
        context.append(String.format("... on %s", inlineFragment.getTypeCondition()));
    }

    @Override
    public void beforeFragmentSpread(FragmentSpread fragmentSpread, StringBuilderVisitorContext context) {
        context.append(String.format("...%s", fragmentSpread.getName()));
    }

    @Override
    public void beforeSelectionField(SelectionField selectionField, StringBuilderVisitorContext context) {
        String alias = selectionField.getAlias();
        if (alias != null) {
            context.append(alias);
            context.append(" : ");
        }

        String name = selectionField.getName();
        context.append(name);
    }
}