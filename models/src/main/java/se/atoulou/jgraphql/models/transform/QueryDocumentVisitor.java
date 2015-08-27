package se.atoulou.jgraphql.models.transform;

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

public interface QueryDocumentVisitor<T extends VisitorContext<T>> {
    void visitQueryDocument(QueryDocument queryDocument, T context);

    void visitOperation(OperationDefinition operation, T context);

    void visitFragment(FragmentDefinition fragment, T context);

    void visitDirectives(List<Directive> directives, T context);

    void visitDirective(Directive directive, T context);

    void visitArguments(List<Argument> arguments, T context);
    
    void visitArgument(Argument argument, T context);

    void visitVariableDefinitions(List<VariableDefinition> variableDefinitions, T context);

    void visitVariableDefinition(VariableDefinition variableDefinition, T context);

    void visitSelectionSet(List<Selection> selectionSet, T context);

    void visitInlineFragment(InlineFragment inlineFragment, T context);

    void visitFragmentSpread(FragmentSpread fragmentSpread, T context);

    void visitSelectionField(SelectionField selectionField, T context);
}
