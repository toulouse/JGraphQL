package se.atoulou.graphql.parser;

import java.util.Stack;

import org.antlr.v4.runtime.tree.RuleNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.atoulou.graphql.parser.antlr.GraphQLSchemaBaseListener;
import se.atoulou.graphql.parser.antlr.GraphQLSchemaParser.InputValueDefinitionContext;
import se.atoulou.graphql.parser.antlr.GraphQLSchemaParser.SchemaDefinitionContext;
import se.atoulou.graphql.parser.antlr.GraphQLSchemaParser.SchemaDocumentContext;
import se.atoulou.graphql.parser.antlr.GraphQLSchemaParser.TypeDefinitionContext;
import se.atoulou.graphql.schema.Schema;
import se.atoulou.graphql.schema.Schema.Builder;

public class GraphQLSchemaListener extends GraphQLSchemaBaseListener {
    private static final Logger LOG = LoggerFactory.getLogger(GraphQLSchemaListener.class);

    private final Stack<RuleNode> nodeStack;
    private final Schema.Builder  schemaBuilder;

    public GraphQLSchemaListener(Builder schemaBuilder) {
        super();
        this.schemaBuilder = schemaBuilder;
        this.nodeStack = new Stack<RuleNode>();
    }

    @Override
    public void enterSchemaDocument(SchemaDocumentContext ctx) {
        LOG.trace("Entering {}", "schema document");
        nodeStack.push(ctx);
        super.enterSchemaDocument(ctx);
    }

    @Override
    public void exitSchemaDocument(SchemaDocumentContext ctx) {
        super.exitSchemaDocument(ctx);
        nodeStack.pop();
        LOG.trace("Exiting {}", "schema document");
    }

    @Override
    public void enterSchemaDefinition(SchemaDefinitionContext ctx) {
        LOG.trace("Entering {}", "schema definition");
        nodeStack.push(ctx);
        super.enterSchemaDefinition(ctx);
    }

    @Override
    public void exitSchemaDefinition(SchemaDefinitionContext ctx) {
        super.exitSchemaDefinition(ctx);
        nodeStack.pop();
        LOG.trace("Exiting {}", "schema definition");

    }

    @Override
    public void enterTypeDefinition(TypeDefinitionContext ctx) {
        LOG.trace("Entering {}", "type definition");
        nodeStack.push(ctx);
        super.enterTypeDefinition(ctx);
    }

    @Override
    public void exitTypeDefinition(TypeDefinitionContext ctx) {
        super.exitTypeDefinition(ctx);
        nodeStack.pop();
        LOG.trace("Exiting {}", "type definition");
    }

    @Override
    public void enterInputValueDefinition(InputValueDefinitionContext ctx) {
        LOG.debug("ivd: {}", ctx);
        super.enterInputValueDefinition(ctx);
    };
}
