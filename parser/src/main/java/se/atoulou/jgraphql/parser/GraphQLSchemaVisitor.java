package se.atoulou.jgraphql.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaBaseVisitor;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.ArgumentsDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.DefaultValueContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.EnumDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.EnumValueDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.FieldDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.InputObjectDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.InputValueDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.InterfaceDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.ListTypeContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.NamedTypeContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.NonNullTypeContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.ScalarDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.SchemaDocumentContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.TypeContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.TypeDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.UnionDefinitionContext;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser.UnionMembersContext;
import se.atoulou.jgraphql.schema.EnumValue;
import se.atoulou.jgraphql.schema.Field;
import se.atoulou.jgraphql.schema.InputValue;
import se.atoulou.jgraphql.schema.Schema;
import se.atoulou.jgraphql.schema.Type;
import se.atoulou.jgraphql.schema.Type.TypeKind;

public class GraphQLSchemaVisitor extends GraphQLSchemaBaseVisitor<Void> {
    private static final Logger LOG = LoggerFactory.getLogger(GraphQLSchemaVisitor.class);

    private final Schema.Builder schemaBuilder;
    private final TypeRegistry   typeRegistry;
    private final Stack<Object>  objectStack;
    private Object               previousObject;

    public GraphQLSchemaVisitor() {
        super();

        this.schemaBuilder = Schema.builder();

        // Add type registry; register base types
        this.typeRegistry = new TypeRegistry();
        this.typeRegistry.registerDeclaration("Int", TypeKind.SCALAR);
        this.typeRegistry.registerDeclaration("Float", TypeKind.SCALAR);
        this.typeRegistry.registerDeclaration("String", TypeKind.SCALAR);
        this.typeRegistry.registerDeclaration("Boolean", TypeKind.SCALAR);
        this.typeRegistry.registerDeclaration("ID", TypeKind.SCALAR);

        this.objectStack = new Stack<>();
    }

    public Schema.Builder getSchemaBuilder() {
        return schemaBuilder;
    }

    public TypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    @Override
    public Void visitSchemaDocument(SchemaDocumentContext ctx) {
        LOG.trace("Entering {}", "schema document");

        super.visitSchemaDocument(ctx);

        LOG.trace("Exiting {}", "schema document");
        LOG.trace("Adding objects possibleTypes to interfaces");
        this.typeRegistry.reconcilePossibleTypes();

        // TODO: Schema validation
        return null;
    }

    @Override
    public Void visitTypeDefinition(TypeDefinitionContext ctx) {
        String name = ctx.NAME().getText();
        LOG.trace("<Type name=\"{}\">", name);

        // Push builder onto stack & populate
        Type.Builder typeB = this.typeRegistry.registerDeclaration(name, TypeKind.OBJECT);
        this.objectStack.push(typeB);
        this.schemaBuilder.types().add(typeB);

        if (ctx.implementTypes() != null) {
            for (NamedTypeContext typeName : ctx.implementTypes().namedType()) {
                String typeNameString = typeName.NAME().getText();
                Type.Builder implementedTypeB = this.typeRegistry.registerUsage(typeNameString);
                typeB.interfaces().add(implementedTypeB);
                LOG.trace("<Implements name=\"{}\" />", typeNameString);
            }
        }

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
        String name = ctx.NAME().getText();
        LOG.trace("<Interface name=\"{}\">", name);

        // Push builder onto stack & populate
        Type.Builder typeB = this.typeRegistry.registerDeclaration(name, TypeKind.INTERFACE);
        this.objectStack.push(typeB);
        this.schemaBuilder.types().add(typeB);

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
        Type.Builder typeB = this.typeRegistry.registerDeclaration(name, TypeKind.UNION);
        this.objectStack.push(typeB);
        this.schemaBuilder.types().add(typeB);

        UnionMembersContext unionMembers = ctx.unionMembers();
        while (unionMembers != null) {
            String typeName = unionMembers.namedType().NAME().getText();
            LOG.trace("<Type name=\"{}\" />", typeName);

            Type.Builder possibleTypeB = this.typeRegistry.registerUsage(typeName);
            typeB.possibleTypes().add(possibleTypeB);
            unionMembers = unionMembers.unionMembers();
        }

        this.previousObject = this.objectStack.pop();
        LOG.trace("</Union>");
        return null;
    }

    @Override
    public Void visitScalarDefinition(ScalarDefinitionContext ctx) {
        String name = ctx.namedType().NAME().getText();
        LOG.trace("<Scalar name=\"{}\">", name);

        // Push builder onto stack & populate
        Type.Builder typeB = this.typeRegistry.registerDeclaration(name, TypeKind.SCALAR);
        this.objectStack.push(typeB);
        this.schemaBuilder.types().add(typeB);

        this.previousObject = this.objectStack.pop();
        LOG.trace("</Scalar>");
        return null;
    }

    @Override
    public Void visitEnumDefinition(EnumDefinitionContext ctx) {
        String name = ctx.NAME().getText();
        LOG.trace("<Enum name=\"{}\">", name);

        // Push builder onto stack & populate
        Type.Builder typeB = this.typeRegistry.registerDeclaration(name, TypeKind.ENUM);
        this.objectStack.push(typeB);
        this.schemaBuilder.types().add(typeB);

        List<EnumValueDefinitionContext> enumValueDefinitions = ctx.enumValueDefinition();
        for (EnumValueDefinitionContext enumValueDefinition : enumValueDefinitions) {
            String enumName = enumValueDefinition.NAME().getText();
            LOG.trace("<EnumValue name=\"{}\" />", enumName);

            EnumValue.Builder enumValueB = EnumValue.builder();
            enumValueB.name(enumName);
            typeB.enumValues().add(enumValueB);
        }

        this.previousObject = this.objectStack.pop();
        LOG.trace("</Enum>");
        return null;
    }

    @Override
    public Void visitInputObjectDefinition(InputObjectDefinitionContext ctx) {
        String name = ctx.NAME().getText();
        LOG.trace("<InputObject name=\"{}\">", name);

        // Push builder onto stack & populate
        Type.Builder typeB = this.typeRegistry.registerDeclaration(name, TypeKind.INPUT_OBJECT);
        this.objectStack.push(typeB);
        this.schemaBuilder.types().add(typeB);

        List<InputValueDefinitionContext> inputValueDefinitions = ctx.inputValueDefinition();
        for (InputValueDefinitionContext inputValueDefinition : inputValueDefinitions) {
            visitInputValueDefinition(inputValueDefinition);
            assert this.previousObject instanceof InputValue.Builder;
            InputValue.Builder inputValueB = (InputValue.Builder) this.previousObject;

            typeB.inputFields().add(inputValueB);
        }

        this.previousObject = this.objectStack.pop();
        LOG.trace("</InputObject>");
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
            visitInputValueDefinition(inputValueDefinition);
            assert this.previousObject instanceof InputValue.Builder;
            InputValue.Builder inputValueB = (InputValue.Builder) this.previousObject;
            inputValues.add(inputValueB);
        }

        this.previousObject = inputValues;
        return null;
    }

    @Override
    public Void visitInputValueDefinition(InputValueDefinitionContext ctx) {
        String name = ctx.NAME().getText();
        LOG.trace("<InputValue name=\"{}\">", name);

        // Push builder onto stack & populate
        InputValue.Builder inputValueB = InputValue.builder();
        this.objectStack.push(inputValueB);

        inputValueB.name(name);

        visitType(ctx.type());
        assert this.previousObject instanceof Type.Builder;
        Type.Builder type = (Type.Builder) this.previousObject;
        inputValueB.type(type);

        DefaultValueContext defaultValue = ctx.defaultValue();
        if (defaultValue != null) {
            String constValue = defaultValue.constValue().getText();
            inputValueB.defaultValue(constValue);
            LOG.trace("<DefaultValue value=\"{}\" />", defaultValue.getText());
        }
        LOG.trace("<Type name=\"{}\" />", type.name());

        this.previousObject = this.objectStack.pop();
        LOG.trace("</InputValue>");
        return null;
    }

    @Override
    public Void visitType(TypeContext ctx) {
        String name = ctx.getText();

        NamedTypeContext namedType = ctx.namedType();
        ListTypeContext listType = ctx.listType();
        NonNullTypeContext nonNull = ctx.nonNullType();

        LOG.trace("Visiting type: {}", name);
        if (nonNull != null) {
            visitNonNullType(nonNull);
        } else if (namedType != null) {
            visitNamedType(ctx.namedType());
        } else if (listType != null) {
            visitListType(listType);
        } else {
            assert false;
        }
        return null;
    }

    @Override
    public Void visitNonNullType(NonNullTypeContext ctx) {
        String name = ctx.getText();
        NamedTypeContext namedType = ctx.namedType();
        ListTypeContext listType = ctx.listType();

        Type.Builder typeB = this.typeRegistry.registerUsage(name);
        typeB.kind(TypeKind.NON_NULL);
        this.objectStack.push(typeB);

        if (namedType != null) {
            visitNamedType(namedType);
            assert this.previousObject instanceof Type.Builder;
            Type.Builder type = (Type.Builder) this.previousObject;
            typeB.ofType(type);
        } else if (listType != null) {
            visitListType(listType);
            assert this.previousObject instanceof Type.Builder;
            Type.Builder type = (Type.Builder) this.previousObject;
            typeB.ofType(type);
        } else {
            assert false;
        }

        this.previousObject = this.objectStack.pop();
        return null;
    }

    @Override
    public Void visitNamedType(NamedTypeContext ctx) {
        String name = ctx.getText();
        this.previousObject = this.typeRegistry.registerUsage(name);
        return null;
    }

    @Override
    public Void visitListType(ListTypeContext ctx) {
        String name = ctx.getText();
        Type.Builder typeB = this.typeRegistry.registerDeclaration(name, TypeKind.LIST);
        this.objectStack.push(typeB);

        visitType(ctx.type());
        assert this.previousObject instanceof Type.Builder;
        typeB.ofType((Type.Builder) this.previousObject);

        this.previousObject = this.objectStack.pop();
        return null;
    }
}
