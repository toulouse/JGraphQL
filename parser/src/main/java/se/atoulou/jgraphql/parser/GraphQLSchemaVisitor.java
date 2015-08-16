package se.atoulou.jgraphql.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.tree.RuleNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaBaseVisitor;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.ArgumentsDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.FieldDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.InputValueDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.ListTypeContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.NonNullTypeContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.SchemaDocumentContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.TypeContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.TypeDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.TypeNameContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.ValueContext;
import se.atoulou.jgraphql.schema.Field;
import se.atoulou.jgraphql.schema.InputValue;
import se.atoulou.jgraphql.schema.Schema;
import se.atoulou.jgraphql.schema.Type;
import se.atoulou.jgraphql.schema.Type.TypeKind;

public class GraphQLSchemaVisitor extends GraphQLSchemaBaseVisitor<Void> {
    private static final Logger LOG = LoggerFactory.getLogger(GraphQLSchemaVisitor.class);

    private final Schema.Builder  schemaBuilder;
    private final Stack<RuleNode> nodeStack;
    private final Stack<Object>   objectStack;
    private Object                previousObject;

    public GraphQLSchemaVisitor() {
        super();

        this.schemaBuilder = Schema.builder();
        this.schemaBuilder.types(new ArrayList<>());
        this.schemaBuilder.directives(new ArrayList<>());

        this.objectStack = new Stack<>();
        this.nodeStack = new Stack<>();
    }

    public Schema.Builder getSchemaBuilder() {
        return schemaBuilder;
    }

    @Override
    public Void visitSchemaDocument(SchemaDocumentContext ctx) {
        LOG.trace("Entering {}", "schema document");
        this.nodeStack.push(ctx);

        super.visitSchemaDocument(ctx);

        this.nodeStack.pop();
        LOG.trace("Exiting {}", "schema document");
        return null;
    }

    @Override
    public Void visitTypeDefinition(TypeDefinitionContext ctx) {
        LOG.trace("Entering {}: {}", "type definition", ctx.NAME().getText());
        this.nodeStack.push(ctx);

        // Populate builder
        Type.Builder typeB = Type.builder();
        typeB.kind(TypeKind.OBJECT);
        typeB.name(ctx.NAME().getText());
        typeB.fields(new ArrayList<>());
        List<TypeNameContext> types = ctx.implementTypes().typeName();
        List<String> typeNames = types.stream().map(typeName -> typeName.NAME().getText()).collect(Collectors.toList());
        LOG.trace("implements {}", typeNames);

        // Push onto stack
        this.objectStack.push(typeB);

        // Add to schema
        this.schemaBuilder.types().add(typeB);

        for (FieldDefinitionContext fieldDefinition : ctx.fieldDefinition()) {
            Field.Builder fieldB = Field.builder();

            String name = fieldDefinition.getChild(0).getText();
            fieldB.name(name);

            ArgumentsDefinitionContext argumentsDefinition = fieldDefinition.argumentsDefinition();
            if (argumentsDefinition == null) {
                fieldB.args(new ArrayList<>());
            } else {
                visitArgumentsDefinition(fieldDefinition.argumentsDefinition());
                assert this.previousObject instanceof List;
                @SuppressWarnings("unchecked")
                List<InputValue.Builder> args = (List<InputValue.Builder>) this.previousObject;
                fieldB.args(args);
            }

            visitType(fieldDefinition.type());
            assert this.previousObject instanceof Type.Builder;
            Type.Builder type = (Type.Builder) this.previousObject;
            fieldB.type(type);
        }

        this.previousObject = this.objectStack.pop();
        this.nodeStack.pop();
        LOG.trace("Exiting {}: {}", "type definition", ctx.NAME().getText());
        return null;
    }

    @Override
    public Void visitArgumentsDefinition(ArgumentsDefinitionContext ctx) {
        List<InputValue.Builder> inputValues = new ArrayList<>();

        for (InputValueDefinitionContext inputValueDefinition : ctx.inputValueDefinition()) {
            InputValue.Builder inputValueB = InputValue.builder();
            String name = inputValueDefinition.NAME().getText();
            inputValueB.name(name);

            visitType(inputValueDefinition.type());
            assert this.previousObject instanceof Type;
            Type type = (Type) this.previousObject;
            inputValueB.type(type);

            ValueContext defaultValue = inputValueDefinition.value();
            if (defaultValue != null) {
                inputValueB.defaultValue(defaultValue.getText());
            }

            inputValues.add(inputValueB);
        }

        this.previousObject = inputValues;
        return null;
    }

    @Override
    public Void visitType(TypeContext ctx) {
        Type.Builder typeB = Type.builder();
        this.objectStack.push(typeB);

        TypeNameContext typeName = ctx.typeName();
        ListTypeContext listType = ctx.listType();
        NonNullTypeContext nonNull = ctx.nonNullType();
        if (nonNull != null) {
            typeB.kind(TypeKind.NON_NULL);

            Type.Builder innerTypeB = Type.builder();

            if (typeName != null) {
                innerTypeB.name(typeName.NAME().getText());
            } else if (listType != null) {
                innerTypeB.kind(TypeKind.LIST);

                visitType(listType.type());
                innerTypeB.ofType((Type.Builder) this.previousObject);
            } else {
                innerTypeB = null;
                assert(false);
            }

            typeB.ofType(innerTypeB);
        }

        if (typeName != null) {
            typeB.name(typeName.NAME().getText());
        } else if (listType != null) {
            typeB.kind(TypeKind.LIST);

            visitType(listType.type());
            typeB.ofType((Type.Builder) this.previousObject);
        } else {
            typeB = null;
            assert(false);
        }

        this.previousObject = this.objectStack.pop();
        return null;
    }
}
