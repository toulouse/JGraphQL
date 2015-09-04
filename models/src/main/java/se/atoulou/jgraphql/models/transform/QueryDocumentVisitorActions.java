package se.atoulou.jgraphql.models.transform;

import java.util.List;

import se.atoulou.jgraphql.models.query.Argument;
import se.atoulou.jgraphql.models.query.Directive;
import se.atoulou.jgraphql.models.query.FragmentDefinition;
import se.atoulou.jgraphql.models.query.OperationDefinition;
import se.atoulou.jgraphql.models.query.Document;
import se.atoulou.jgraphql.models.query.Selection;
import se.atoulou.jgraphql.models.query.VariableDefinition;
import se.atoulou.jgraphql.models.query.Selection.FragmentSpread;
import se.atoulou.jgraphql.models.query.Selection.InlineFragment;
import se.atoulou.jgraphql.models.query.Selection.SelectionField;

public interface QueryDocumentVisitorActions<T extends VisitorContext<T>> extends QueryDocumentVisitor<T> {

    void beforeQueryDocument(Document queryDocument, T context);

    void punctuateQueryDocument(Document queryDocument, T context);

    void afterQueryDocument(Document queryDocument, T context);

    void beforeOperation(OperationDefinition operation, T context);

    void afterOperation(OperationDefinition operation, T context);

    void beforeFragment(FragmentDefinition fragment, T context);

    void afterFragment(FragmentDefinition fragment, T context);

    void beforeDirectives(List<Directive> directives, T context);

    void punctuateDirectives(List<Directive> directives, T context);

    void afterDirectives(List<Directive> directives, T context);

    void beforeDirective(Directive directive, T context);

    void afterDirective(Directive directive, T context);

    void beforeArguments(List<Argument> arguments, T context);

    void punctuateArguments(List<Argument> arguments, T context);

    void afterArguments(List<Argument> arguments, T context);

    void beforeArgument(Argument argument, T context);

    void afterArgument(Argument argument, T context);

    void beforeVariableDefinitions(List<VariableDefinition> variableDefinitions, T context);

    void punctuateVariableDefinitions(List<VariableDefinition> variableDefinitions, T context);

    void afterVariableDefinitions(List<VariableDefinition> variableDefinitions, T context);

    void beforeVariableDefinition(VariableDefinition variableDefinition, T context);

    void afterVariableDefinition(VariableDefinition variableDefinition, T context);

    void beforeSelectionSet(List<Selection> selectionSet, T context);

    void punctuateSelectionSet(List<Selection> selectionSet, T context);

    void afterSelectionSet(List<Selection> selectionSet, T context);

    void beforeInlineFragment(InlineFragment inlineFragment, T context);

    void afterInlineFragment(InlineFragment inlineFragment, T context);

    void beforeFragmentSpread(FragmentSpread fragmentSpread, T context);

    void afterFragmentSpread(FragmentSpread fragmentSpread, T context);

    void beforeSelectionField(SelectionField selectionField, T context);

    void afterSelectionField(SelectionField selectionField, T context);
}
