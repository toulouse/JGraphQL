package se.atoulou.jgraphql.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaBaseVisitor;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.ArgumentsDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.FieldDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.InputValueDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.InterfaceDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.ListTypeContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.NonNullTypeContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.ScalarDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.SchemaDocumentContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.TypeContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.TypeDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.TypeNameContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.UnionDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.UnionMembersContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.ValueContext;
import se.atoulou.jgraphql.schema.Field;
import se.atoulou.jgraphql.schema.InputValue;
import se.atoulou.jgraphql.schema.Schema;
import se.atoulou.jgraphql.schema.Type;
import se.atoulou.jgraphql.schema.Type.TypeKind;

public class GraphQLSchemaVisitor extends GraphQLSchemaBaseVisitor<Void> {
    private static final Logger LOG = LoggerFactory.getLogger(GraphQLSchemaVisitor.class);

    private final Schema.Builder schemaBuilder;
    private final Stack<Object>  objectStack;
    private Object               previousObject;

    public GraphQLSchemaVisitor() {
        super();

        this.schemaBuilder = Schema.builder();

        this.objectStack = new Stack<>();
    }

    public Schema.Builder getSchemaBuilder() {
        return schemaBuilder;
    }

    @Override
    public Void visitSchemaDocument(SchemaDocumentContext ctx) {
        LOG.trace("Entering {}", "schema document");

        super.visitSchemaDocument(ctx);

        LOG.trace("Exiting {}", "schema document");
        return null;
    }

    @Override
    public Void visitTypeDefinition(TypeDefinitionContext ctx) {
        LOG.trace("<Type name=\"{}\">", ctx.NAME().getText());

        // Push builder onto stack & populate
        Type.Builder typeB = Type.builder();
        this.objectStack.push(typeB);
        this.schemaBuilder.types().add(typeB);

        typeB.kind(TypeKind.OBJECT);
        typeB.name(ctx.NAME().getText());

        List<TypeNameContext> types = ctx.implementTypes().typeName();
        List<String> typeNames = types.stream().map(typeName -> typeName.NAME().getText()).collect(Collectors.toList());
        // TODO: add type builder registration mechanism
        LOG.trace(">implements {}", typeNames);

        for (FieldDefinitionContext fieldDefinition : ctx.fieldDefinition()) {
            visitFieldDefinition(fieldDefinition);
            assert this.previousObject instanceof Field.Builder;
            Field.Builder fieldB = (Field.Builder) this.previousObject;
            typeB.fields().add(fieldB);
        }

        this.previousObject = this.objectStack.pop();
        LOG.trace("</Type>");
        return null;
    }

    @Override
    public Void visitInterfaceDefinition(InterfaceDefinitionContext ctx) {
        LOG.trace("<Interface name=\"{}\">", ctx.NAME().getText());

        // Push builder onto stack & populate
        Type.Builder typeB = Type.builder();
        this.objectStack.push(typeB);
        this.schemaBuilder.types().add(typeB);

        typeB.kind(TypeKind.INTERFACE);
        typeB.name(ctx.NAME().getText());

        for (FieldDefinitionContext fieldDefinition : ctx.fieldDefinition()) {
            visitFieldDefinition(fieldDefinition);
            assert this.previousObject instanceof Field.Builder;
            Field.Builder fieldB = (Field.Builder) this.previousObject;
            typeB.fields().add(fieldB);
        }

        this.previousObject = this.objectStack.pop();
        LOG.trace("</Interface>");
        return null;
    }

    @Override
    public Void visitUnionDefinition(UnionDefinitionContext ctx) {
        String name = ctx.NAME().getText();
        LOG.trace("<Union name=\"{}\">", name);

        // Push builder onto stack & populate
        Type.Builder typeB = Type.builder();
        this.objectStack.push(typeB);
        this.schemaBuilder.types().add(typeB);

        typeB.kind(TypeKind.UNION);
        typeB.name(name);

        UnionMembersContext unionMembers = ctx.unionMembers();
        while (unionMembers != null) {
            String typeName = unionMembers.typeName().getText();
            LOG.trace("<Type name=\"{}\"/>", typeName);

            Type.Builder possibleTypeB = Type.builder();
            possibleTypeB.name(typeName);
            typeB.possibleTypes().add(possibleTypeB);
            unionMembers = unionMembers.unionMembers();
        }

        this.previousObject = this.objectStack.pop();
        LOG.trace("</Union>");
        return null;
    }

    @Override
    public Void visitScalarDefinition(ScalarDefinitionContext ctx) {
        String name = ctx.typeName().NAME().getText();
        LOG.trace("<Scalar name=\"{}\">", name);

        // Push builder onto stack & populate
        Type.Builder typeB = Type.builder();
        this.objectStack.push(typeB);
        this.schemaBuilder.types().add(typeB);

        typeB.kind(TypeKind.SCALAR);
        typeB.name(name);

        this.previousObject = this.objectStack.pop();
        LOG.trace("</Scalar>");
        return null;
    }

    @Override
    public Void visitFieldDefinition(FieldDefinitionContext ctx) {
        String name = ctx.getChild(0).getText();
        LOG.trace("<Field name=\"{}\">", name);

        // Push builder onto stack & populate
        Field.Builder fieldB = Field.builder();
        this.objectStack.push(fieldB);

        fieldB.name(name);

        visitType(ctx.type());
        assert this.previousObject instanceof Type.Builder;
        Type.Builder type = (Type.Builder) this.previousObject;
        fieldB.type(type);
        LOG.trace(" ({} : {})", fieldB.name(), fieldB.type());

        ArgumentsDefinitionContext argumentsDefinition = ctx.argumentsDefinition();
        if (argumentsDefinition != null) {
            visitArgumentsDefinition(ctx.argumentsDefinition());
            assert this.previousObject instanceof List;
            @SuppressWarnings("unchecked")
            List<InputValue.Builder> args = (List<InputValue.Builder>) this.previousObject;
            fieldB.args(args);
        }

        this.previousObject = this.objectStack.pop();
        LOG.trace("</Field>");
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
            assert this.previousObject instanceof Type.Builder;
            Type.Builder type = (Type.Builder) this.previousObject;
            inputValueB.type(type);

            ValueContext defaultValue = inputValueDefinition.value();
            if (defaultValue != null) {
                inputValueB.defaultValue(defaultValue.getText());
            }

            inputValues.add(inputValueB);
            LOG.trace(">>arg ({} : {}) = {}", name, type.name(), defaultValue.getText());

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
