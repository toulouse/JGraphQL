package se.atoulou.jgraphql.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.atoulou.jgraphql.models.query.Argument;
import se.atoulou.jgraphql.models.query.Directive;
import se.atoulou.jgraphql.models.query.QueryDocument;
import se.atoulou.jgraphql.models.query.FragmentDefinition;
import se.atoulou.jgraphql.models.query.OperationDefinition;
import se.atoulou.jgraphql.models.query.OperationDefinition.OperationType;
import se.atoulou.jgraphql.models.query.Selection;
import se.atoulou.jgraphql.models.query.Selection.SelectionKind;
import se.atoulou.jgraphql.models.query.Value;
import se.atoulou.jgraphql.models.query.VariableDefinition;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryBaseVisitor;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.AliasContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.ArgumentContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.ArgumentsContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.ConstValueContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.DefaultValueContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.DirectiveContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.DirectivesContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.DocumentContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.FieldContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.FragmentDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.FragmentSpreadContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.InlineFragmentContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.OperationDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.SelectionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.SelectionSetContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.ValueContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.VariableDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser.VariableDefinitionsContext;

public class GraphQLQueryVisitor extends GraphQLQueryBaseVisitor<Void> {
    private static final Logger LOG = LoggerFactory.getLogger(GraphQLQueryVisitor.class);

    private final QueryDocument.Builder documentBuilder;
    private final Stack<Object>    objectStack;
    private Object                 previousObject;

    public GraphQLQueryVisitor() {
        this.documentBuilder = QueryDocument.builder();

        this.objectStack = new Stack<>();
    }

    public QueryDocument.Builder getDocumentBuilder() {
        return documentBuilder;
    }

    @Override
    public Void visitDocument(DocumentContext ctx) {
        LOG.trace("Entering {}", "query document");

        super.visitDocument(ctx);

        LOG.trace("Exiting {}", "query document");
        return null;
    }

    @Override
    public Void visitOperationDefinition(OperationDefinitionContext ctx) {
        OperationDefinition.Builder operationB = OperationDefinition.builder();
        this.objectStack.push(operationB);
        this.documentBuilder.operations().add(operationB);

        TerminalNode nameNode = ctx.NAME();
        String name;
        if (nameNode != null) {
            name = nameNode.getText();
        } else {
            name = null;
        }
        operationB.name(name);

        if (ctx.operationType() != null) {
            String operationType = ctx.operationType().getText();
            if ("query".equals(operationType)) {
                operationB.operationType(OperationType.QUERY);
            } else if ("mutation".equals(operationType)) {
                operationB.operationType(OperationType.MUATATION);
            } else {
                assert false;
            }
        } else {
            operationB.operationType(OperationType.QUERY);
        }

        VariableDefinitionsContext variablesCtx = ctx.variableDefinitions();
        if (variablesCtx != null) {
            visitVariableDefinitions(variablesCtx);
            assert this.previousObject instanceof List;
            @SuppressWarnings("unchecked")
            List<VariableDefinition.Builder> variables = (List<VariableDefinition.Builder>) this.previousObject;
            operationB.variableDefinitions(variables);
        }

        DirectivesContext directivesCtx = ctx.directives();
        if (directivesCtx != null) {
            visitDirectives(directivesCtx);
            assert this.previousObject instanceof List;
            @SuppressWarnings("unchecked")
            List<Directive.Builder> directives = (List<Directive.Builder>) this.previousObject;
            operationB.directives(directives);
        }

        visitSelectionSet(ctx.selectionSet());
        assert this.previousObject instanceof List;
        @SuppressWarnings("unchecked")
        List<Selection.Builder> selectionSet = (List<Selection.Builder>) this.previousObject;
        operationB.selectionSet(selectionSet);

        this.previousObject = this.objectStack.pop();
        return null;
    }

    @Override
    public Void visitFragmentDefinition(FragmentDefinitionContext ctx) {
        String name = ctx.fragmentName().getText();
        String typeCondition = ctx.typeCondition().getText();

        FragmentDefinition.Builder fragmentB = FragmentDefinition.builder();
        this.objectStack.push(fragmentB);
        this.documentBuilder.fragments().add(fragmentB);

        fragmentB.name(name);
        fragmentB.typeCondition(typeCondition);

        DirectivesContext directivesCtx = ctx.directives();
        if (directivesCtx != null) {
            visitDirectives(directivesCtx);
            assert this.previousObject instanceof List;
            @SuppressWarnings("unchecked")
            List<Directive.Builder> directives = (List<Directive.Builder>) this.previousObject;
            fragmentB.directives(directives);
        }

        visitSelectionSet(ctx.selectionSet());
        assert this.previousObject instanceof List;
        @SuppressWarnings("unchecked")
        List<Selection.Builder> selectionSet = (List<Selection.Builder>) this.previousObject;
        fragmentB.selectionSet(selectionSet);

        this.previousObject = this.objectStack.pop();
        return null;
    }

    @Override
    public Void visitVariableDefinitions(VariableDefinitionsContext ctx) {
        List<VariableDefinition.Builder> variableDefinitions = new ArrayList<>();
        this.objectStack.push(variableDefinitions);

        for (VariableDefinitionContext variableDefinition : ctx.variableDefinition()) {
            visitVariableDefinition(variableDefinition);
            assert this.previousObject instanceof VariableDefinition.Builder;
            VariableDefinition.Builder variableB = (VariableDefinition.Builder) this.previousObject;
            variableDefinitions.add(variableB);
        }

        this.previousObject = this.objectStack.pop();
        return null;
    }

    @Override
    public Void visitVariableDefinition(VariableDefinitionContext ctx) {
        VariableDefinition.Builder variableB = VariableDefinition.builder();
        this.objectStack.push(variableB);

        variableB.variable(ctx.variable().VARIABLE().getText());
        variableB.type(ctx.type().namedType().getText());

        DefaultValueContext defaultValue = ctx.defaultValue();
        if (defaultValue != null) {
            visitConstValue(ctx.defaultValue().constValue());
            assert this.previousObject instanceof Value.Builder;
            Value.Builder valueB = (Value.Builder) this.previousObject;
            variableB.defaultValue(valueB);
        }

        this.previousObject = this.objectStack.pop();
        return null;
    }

    @Override
    public Void visitSelectionSet(SelectionSetContext ctx) {
        List<Selection.Builder> selectionSet = new ArrayList<>();
        this.objectStack.push(selectionSet);

        for (SelectionContext selection : ctx.selection()) {
            visitSelection(selection);
            assert this.previousObject instanceof Selection.Builder;
            Selection.Builder selectionB = (Selection.Builder) this.previousObject;
            selectionSet.add(selectionB);
        }

        this.previousObject = this.objectStack.pop();
        return null;
    }

    @Override
    public Void visitSelection(SelectionContext ctx) {
        FieldContext field = ctx.field();
        FragmentSpreadContext fragmentSpread = ctx.fragmentSpread();
        InlineFragmentContext inlineFragment = ctx.inlineFragment();

        if (field != null) {
            visitField(field);
        } else if (fragmentSpread != null) {
            visitFragmentSpread(fragmentSpread);
        } else if (inlineFragment != null) {
            visitInlineFragment(inlineFragment);
        } else {
            assert false;
        }

        return null;
    }

    @Override
    public Void visitField(FieldContext ctx) {
        Selection.Builder selectionB = Selection.builder();

        selectionB.kind(SelectionKind.FIELD);

        AliasContext aliasCtx = ctx.alias();
        if (aliasCtx != null) {
            selectionB.alias(aliasCtx.NAME().getText());
        }

        selectionB.name(ctx.NAME().getText());

        ArgumentsContext argumentsCtx = ctx.arguments();
        if (argumentsCtx != null) {
            visitArguments(argumentsCtx);
            assert this.previousObject instanceof List;
            @SuppressWarnings("unchecked")
            List<Argument.Builder> arguments = (List<Argument.Builder>) this.previousObject;
            selectionB.arguments(arguments);
        }

        DirectivesContext directivesCtx = ctx.directives();
        if (directivesCtx != null) {
            visitDirectives(directivesCtx);
            assert this.previousObject instanceof List;
            @SuppressWarnings("unchecked")
            List<Directive.Builder> directives = (List<Directive.Builder>) this.previousObject;
            selectionB.directives(directives);
        }

        SelectionSetContext selectionSetCtx = ctx.selectionSet();
        if (selectionSetCtx != null) {
            visitSelectionSet(selectionSetCtx);
            assert this.previousObject instanceof List;
            @SuppressWarnings("unchecked")
            List<Selection.Builder> selectionSet = (List<Selection.Builder>) this.previousObject;
            selectionB.selectionSet(selectionSet);
        }

        this.previousObject = selectionB;
        return null;
    }

    @Override
    public Void visitFragmentSpread(FragmentSpreadContext ctx) {
        Selection.Builder selectionB = Selection.builder();
        this.objectStack.push(selectionB);

        selectionB.kind(SelectionKind.FRAGMENT_SPREAD);
        selectionB.name(ctx.fragmentName().NAME().getText());

        DirectivesContext directivesCtx = ctx.directives();
        if (directivesCtx != null) {
            visitDirectives(directivesCtx);
            assert this.previousObject instanceof List;
            @SuppressWarnings("unchecked")
            List<Directive.Builder> directives = (List<Directive.Builder>) this.previousObject;
            selectionB.directives(directives);
        }

        this.previousObject = this.objectStack.pop();
        return null;
    }

    @Override
    public Void visitInlineFragment(InlineFragmentContext ctx) {
        Selection.Builder selectionB = Selection.builder();
        this.objectStack.push(selectionB);

        selectionB.kind(SelectionKind.INLINE_FRAGMENT);
        selectionB.typeCondition(ctx.typeCondition().namedType().getText());

        DirectivesContext directivesCtx = ctx.directives();
        if (directivesCtx != null) {
            visitDirectives(directivesCtx);
            assert this.previousObject instanceof List;
            @SuppressWarnings("unchecked")
            List<Directive.Builder> directives = (List<Directive.Builder>) this.previousObject;
            selectionB.directives(directives);
        }

        visitSelectionSet(ctx.selectionSet());
        assert this.previousObject instanceof List;
        @SuppressWarnings("unchecked")
        List<Selection.Builder> selectionSet = (List<Selection.Builder>) this.previousObject;
        selectionB.selectionSet(selectionSet);

        this.previousObject = this.objectStack.pop();
        return null;
    }

    @Override
    public Void visitDirectives(DirectivesContext ctx) {
        List<Directive.Builder> directives = new ArrayList<>();
        this.objectStack.push(directives);

        for (DirectiveContext directive : ctx.directive()) {
            visitDirective(directive);
            assert this.previousObject instanceof Directive.Builder;
            Directive.Builder directiveB = (Directive.Builder) this.previousObject;
            directives.add(directiveB);
        }

        this.previousObject = this.objectStack.pop();
        return null;
    }

    @Override
    public Void visitDirective(DirectiveContext ctx) {
        Directive.Builder directiveB = Directive.builder();
        this.objectStack.push(directiveB);

        directiveB.name(ctx.NAME().getText());

        ArgumentsContext arguments = ctx.arguments();
        if (arguments != null) {
            visitArguments(arguments);
            assert this.previousObject instanceof List;
            @SuppressWarnings("unchecked")
            List<Argument.Builder> args = (List<Argument.Builder>) this.previousObject;
            directiveB.args(args);
        }

        this.previousObject = this.objectStack.pop();
        return null;
    }

    @Override
    public Void visitArguments(ArgumentsContext ctx) {
        List<Argument.Builder> arguments = new ArrayList<>();
        this.objectStack.push(arguments);

        for (ArgumentContext argumentCtx : ctx.argument()) {
            visitArgument(argumentCtx);
            assert this.previousObject instanceof Argument.Builder;
            Argument.Builder argumentB = (Argument.Builder) this.previousObject;
            arguments.add(argumentB);
        }

        this.previousObject = this.objectStack.pop();
        return null;
    }

    @Override
    public Void visitArgument(ArgumentContext ctx) {
        Argument.Builder argumentB = Argument.builder();
        this.objectStack.push(argumentB);

        argumentB.name(ctx.NAME().getText());

        visitValue(ctx.value());
        assert this.previousObject instanceof Value.Builder;
        Value.Builder valueB = (Value.Builder) this.previousObject;
        argumentB.value(valueB);

        this.previousObject = this.objectStack.pop();
        return null;
    }

    @Override
    public Void visitValue(ValueContext ctx) {
        Value.Builder valueB = Value.builder();

        valueB.value(ctx.getText());

        this.previousObject = valueB;
        return null;
    }

    @Override
    public Void visitConstValue(ConstValueContext ctx) {
        Value.Builder valueB = Value.builder();

        valueB.value(ctx.getText());

        this.previousObject = valueB;
        return null;
    }
}
