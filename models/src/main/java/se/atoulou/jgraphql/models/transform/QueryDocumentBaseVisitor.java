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

public class QueryDocumentBaseVisitor<T extends VisitorContext<T>> implements QueryDocumentVisitor<T>, QueryDocumentVisitorActions<T> {

    @Override
    public void visitQueryDocument(Document queryDocument, T context) {
        beforeQueryDocument(queryDocument, context);

        List<OperationDefinition> operations = queryDocument.getOperations();
        if (!operations.isEmpty()) {
            context.enter();
            for (OperationDefinition operation : operations) {
                context.incrementIndex();
                if (context.currentIndex() >= 1) {
                    punctuateQueryDocument(queryDocument, context);
                }
                visitOperation(operation, context);
            }
            context.leave();
        }

        List<FragmentDefinition> fragments = queryDocument.getFragments();
        if (!fragments.isEmpty()) {
            if (!operations.isEmpty()) {
                punctuateQueryDocument(queryDocument, context);
            }

            context.enter();
            for (FragmentDefinition fragment : fragments) {
                context.incrementIndex();

                if (context.currentIndex() >= 1) {
                    punctuateQueryDocument(queryDocument, context);
                }
                visitFragment(fragment, context);
            }
            context.leave();
        }

        afterQueryDocument(queryDocument, context);
    }

    @Override
    public void visitOperation(OperationDefinition operation, T context) {
        beforeOperation(operation, context);

        visitVariableDefinitions(operation.getVariableDefinitions(), context);
        visitDirectives(operation.getDirectives(), context);
        visitSelectionSet(operation.getSelectionSet(), context);

        afterOperation(operation, context);
    }

    @Override
    public void visitFragment(FragmentDefinition fragment, T context) {
        beforeFragment(fragment, context);

        visitDirectives(fragment.getDirectives(), context);
        visitSelectionSet(fragment.getSelectionSet(), context);

        afterFragment(fragment, context);
    }

    @Override
    public void visitDirectives(List<Directive> directives, T context) {
        beforeDirectives(directives, context);

        if (!directives.isEmpty()) {
            context.enter();
            for (Directive directive : directives) {
                context.incrementIndex();

                if (context.currentIndex() >= 1) {
                    punctuateDirectives(directives, context);
                }
                visitDirective(directive, context);
            }
            context.leave();
        }

        afterDirectives(directives, context);
    }

    @Override
    public void visitDirective(Directive directive, T context) {
        beforeDirective(directive, context);

        visitArguments(directive.getArguments(), context);

        afterDirective(directive, context);
    }

    @Override
    public void visitArguments(List<Argument> arguments, T context) {
        beforeArguments(arguments, context);

        if (!arguments.isEmpty()) {
            context.enter();
            for (Argument argument : arguments) {
                context.incrementIndex();

                if (context.currentIndex() >= 1) {
                    punctuateArguments(arguments, context);
                }
                visitArgument(argument, context);
            }
            context.leave();
        }

        afterArguments(arguments, context);
    }

    @Override
    public void visitArgument(Argument argument, T context) {
        beforeArgument(argument, context);
        afterArgument(argument, context);
    }

    @Override
    public void visitVariableDefinitions(List<VariableDefinition> variableDefinitions, T context) {
        beforeVariableDefinitions(variableDefinitions, context);

        if (!variableDefinitions.isEmpty()) {
            context.enter();
            for (VariableDefinition variableDefinition : variableDefinitions) {
                context.incrementIndex();

                if (context.currentIndex() >= 1) {
                    punctuateVariableDefinitions(variableDefinitions, context);
                }
                visitVariableDefinition(variableDefinition, context);
            }
            context.leave();
        }

        afterVariableDefinitions(variableDefinitions, context);
    }

    @Override
    public void visitVariableDefinition(VariableDefinition variableDefinition, T context) {
        beforeVariableDefinition(variableDefinition, context);
        afterVariableDefinition(variableDefinition, context);
    }

    @Override
    public void visitSelectionSet(List<Selection> selectionSet, T context) {
        beforeSelectionSet(selectionSet, context);

        if (!selectionSet.isEmpty()) {
            context.enter();
            for (Selection selection : selectionSet) {
                context.incrementIndex();

                if (context.currentIndex() >= 1) {
                    punctuateSelectionSet(selectionSet, context);
                }

                switch (selection.getKind()) {
                case FIELD:
                    visitSelectionField((Selection.SelectionField) selection, context);
                    break;
                case FRAGMENT_SPREAD:
                    visitFragmentSpread((Selection.FragmentSpread) selection, context);
                    break;
                case INLINE_FRAGMENT:
                    visitInlineFragment((Selection.InlineFragment) selection, context);
                    break;
                default:
                    break;
                }
            }
            context.leave();
        }

        afterSelectionSet(selectionSet, context);
    }

    @Override
    public void visitInlineFragment(InlineFragment inlineFragment, T context) {
        beforeInlineFragment(inlineFragment, context);

        visitDirectives(inlineFragment.getDirectives(), context);
        visitSelectionSet(inlineFragment.getSelectionSet(), context);

        afterInlineFragment(inlineFragment, context);
    }

    @Override
    public void visitFragmentSpread(FragmentSpread fragmentSpread, T context) {
        beforeFragmentSpread(fragmentSpread, context);

        visitDirectives(fragmentSpread.getDirectives(), context);

        afterFragmentSpread(fragmentSpread, context);
    }

    @Override
    public void visitSelectionField(SelectionField selectionField, T context) {
        beforeSelectionField(selectionField, context);

        visitArguments(selectionField.getArguments(), context);
        visitDirectives(selectionField.getDirectives(), context);
        visitSelectionSet(selectionField.getSelectionSet(), context);

        afterSelectionField(selectionField, context);
    }

    @Override
    public void beforeQueryDocument(Document queryDocument, T context) {
    }

    @Override
    public void punctuateQueryDocument(Document queryDocument, T context) {
    }

    @Override
    public void afterQueryDocument(Document queryDocument, T context) {
    }

    @Override
    public void beforeOperation(OperationDefinition operation, T context) {
    }

    @Override
    public void afterOperation(OperationDefinition operation, T context) {
    }

    @Override
    public void beforeFragment(FragmentDefinition fragment, T context) {
    }

    @Override
    public void afterFragment(FragmentDefinition fragment, T context) {
    }

    @Override
    public void beforeDirectives(List<Directive> directives, T context) {
    }

    @Override
    public void punctuateDirectives(List<Directive> directives, T context) {
    }

    @Override
    public void afterDirectives(List<Directive> directives, T context) {
    }

    @Override
    public void beforeDirective(Directive directive, T context) {
    }

    @Override
    public void afterDirective(Directive directive, T context) {
    }

    @Override
    public void beforeArguments(List<Argument> arguments, T context) {
    }

    @Override
    public void punctuateArguments(List<Argument> arguments, T context) {
    }

    @Override
    public void afterArguments(List<Argument> arguments, T context) {
    }

    @Override
    public void beforeArgument(Argument argument, T context) {
    }

    @Override
    public void afterArgument(Argument argument, T context) {
    }

    @Override
    public void beforeVariableDefinitions(List<VariableDefinition> variableDefinitions, T context) {
    }

    @Override
    public void punctuateVariableDefinitions(List<VariableDefinition> variableDefinitions, T context) {
    }

    @Override
    public void afterVariableDefinitions(List<VariableDefinition> variableDefinitions, T context) {
    }

    @Override
    public void beforeVariableDefinition(VariableDefinition variableDefinition, T context) {
    }

    @Override
    public void afterVariableDefinition(VariableDefinition variableDefinition, T context) {
    }

    @Override
    public void beforeSelectionSet(List<Selection> selectionSet, T context) {
    }

    @Override
    public void punctuateSelectionSet(List<Selection> selectionSet, T context) {
    }

    @Override
    public void afterSelectionSet(List<Selection> selectionSet, T context) {
    }

    @Override
    public void beforeInlineFragment(InlineFragment inlineFragment, T context) {
    }

    @Override
    public void afterInlineFragment(InlineFragment inlineFragment, T context) {
    }

    @Override
    public void beforeFragmentSpread(FragmentSpread fragmentSpread, T context) {
    }

    @Override
    public void afterFragmentSpread(FragmentSpread fragmentSpread, T context) {
    }

    @Override
    public void beforeSelectionField(SelectionField selectionField, T context) {
    }

    @Override
    public void afterSelectionField(SelectionField selectionField, T context) {
    }
}
