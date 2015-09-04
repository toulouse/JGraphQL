package se.atoulou.jgraphql.models.transform;

import java.util.List;

import se.atoulou.jgraphql.models.query.TypeDefinition;
import se.atoulou.jgraphql.models.query.TypeDefinition.EnumType;
import se.atoulou.jgraphql.models.query.TypeDefinition.InputObjectType;
import se.atoulou.jgraphql.models.query.TypeDefinition.InterfaceType;
import se.atoulou.jgraphql.models.query.TypeDefinition.ListType;
import se.atoulou.jgraphql.models.query.TypeDefinition.NonNullType;
import se.atoulou.jgraphql.models.query.TypeDefinition.ObjectType;
import se.atoulou.jgraphql.models.query.TypeDefinition.ScalarType;
import se.atoulou.jgraphql.models.query.TypeDefinition.UnionType;
import se.atoulou.jgraphql.models.schema.EnumValue;
import se.atoulou.jgraphql.models.schema.Field;
import se.atoulou.jgraphql.models.schema.InputValue;
import se.atoulou.jgraphql.models.schema.Schema;

public class SchemaBaseVisitor<T extends VisitorContext<T>> implements SchemaVisitor<T>, SchemaVisitorActions<T> {
    @Override
    public void visitSchema(Schema schema, T context) {
        beforeSchema(schema, context);

        List<TypeDefinition> types = schema.getTypes();
        if (!types.isEmpty()) {
            context.enter();
            for (TypeDefinition type : schema.getTypes()) {
                context.incrementIndex();

                if (context.currentIndex() >= 1) {
                    punctuateSchema(schema, context);
                }

                visitType(type, context);
            }
            context.leave();
        }

        afterSchema(schema, context);
    }

    @Override
    public void visitType(TypeDefinition type, T context) {
        beforeType(type, context);

        switch (type.getKind()) {
        case ENUM:
            visitEnum((EnumType) type, context);
            break;
        case INPUT_OBJECT:
            visitInputObject((InputObjectType) type, context);
            break;
        case INTERFACE:
            visitInterface((InterfaceType) type, context);
            break;
        case LIST:
            visitList((ListType) type, context);
            break;
        case NON_NULL:
            visitNonNull((NonNullType) type, context);
            break;
        case OBJECT:
            visitObject((ObjectType) type, context);
            break;
        case SCALAR:
            visitScalar((ScalarType) type, context);
            break;
        case UNION:
            visitUnion((UnionType) type, context);
            break;
        default:
            break;
        }

        afterType(type, context);
    }

    @Override
    public void visitEnum(EnumType enumType, T context) {
        beforeEnum(enumType, context);

        context.enter();
        for (EnumValue enumValue : enumType.getEnumValues()) {
            context.incrementIndex();

            if (context.currentIndex() >= 1) {
                punctuateEnum(enumType, context);
            }
            visitEnumValue(enumValue, context);
        }
        context.leave();

        afterEnum(enumType, context);
    }

    @Override
    public void visitEnumValue(EnumValue enumValue, T context) {
        beforeEnumValue(enumValue, context);
        afterEnumValue(enumValue, context);
    }

    @Override
    public void visitInputObject(InputObjectType inputObjectType, T context) {
        beforeInputObject(inputObjectType, context);

        visitInputValues(inputObjectType.getInputFields(), context);

        afterInputObject(inputObjectType, context);
    }

    @Override
    public void visitInterface(InterfaceType interfaceType, T context) {
        beforeInterface(interfaceType, context);

        visitFields(interfaceType.getFields(), context);

        afterInterface(interfaceType, context);
    }

    @Override
    public void visitList(ListType listType, T context) {
        beforeList(listType, context);
        afterList(listType, context);
    }

    @Override
    public void visitNonNull(NonNullType nonNullType, T context) {
        beforeNonNull(nonNullType, context);
        afterNonNull(nonNullType, context);
    }

    @Override
    public void visitObject(ObjectType objectType, T context) {
        beforeObject(objectType, context);

        visitFields(objectType.getFields(), context);

        afterObject(objectType, context);
    }

    @Override
    public void visitScalar(ScalarType scalarType, T context) {
        beforeScalar(scalarType, context);
        afterScalar(scalarType, context);
    }

    @Override
    public void visitUnion(UnionType unionType, T context) {
        beforeUnion(unionType, context);

        context.enter();
        for (TypeDefinition type : unionType.getPossibleTypes()) {
            context.incrementIndex();

            if (context.currentIndex() >= 1) {
                punctuateUnion(unionType, context);
            }
            visitUnionType(type, context);
        }
        context.leave();

        afterUnion(unionType, context);
    }

    @Override
    public void visitUnionType(TypeDefinition unionType, T context) {
        beforeUnionType(unionType, context);
        afterUnionType(unionType, context);
    }

    @Override
    public void visitInputValues(List<InputValue> inputValues, T context) {
        beforeInputValues(inputValues, context);

        context.enter();
        if (!inputValues.isEmpty()) {
            for (InputValue inputValue : inputValues) {
                context.incrementIndex();

                if (context.currentIndex() >= 1) {
                    punctuateInputValues(inputValues, context);
                }
                visitInputValue(inputValue, context);
            }
        }
        context.leave();

        afterInputValues(inputValues, context);
    }

    @Override
    public void visitInputValue(InputValue inputValue, T context) {
        beforeInputValue(inputValue, context);
        afterInputValue(inputValue, context);
    }

    @Override
    public void visitFields(List<Field> fields, T context) {
        beforeFields(fields, context);

        context.enter();
        if (!fields.isEmpty()) {
            for (Field field : fields) {
                context.incrementIndex();

                if (context.currentIndex() >= 1) {
                    punctuateFields(fields, context);
                }
                visitField(field, context);
            }
        }
        context.leave();

        afterFields(fields, context);
    }

    @Override
    public void visitField(Field field, T context) {
        beforeField(field, context);

        visitInputValues(field.getArguments(), context);

        afterField(field, context);
    }

    @Override
    public void beforeSchema(Schema schema, T context) {
    }

    @Override
    public void punctuateSchema(Schema schema, T context) {
    }

    @Override
    public void afterSchema(Schema schema, T context) {
    }

    @Override
    public void beforeType(TypeDefinition type, T context) {
    }

    @Override
    public void afterType(TypeDefinition type, T context) {
    }

    @Override
    public void beforeEnum(EnumType enumType, T context) {
    }

    @Override
    public void punctuateEnum(EnumType enumType, T context) {
    }

    @Override
    public void afterEnum(EnumType enumType, T context) {
    }

    @Override
    public void beforeEnumValue(EnumValue enumValue, T context) {
    }

    @Override
    public void afterEnumValue(EnumValue enumValue, T context) {
    }

    @Override
    public void beforeInputObject(InputObjectType inputObjectType, T context) {
    }

    @Override
    public void afterInputObject(InputObjectType inputObjectType, T context) {
    }

    @Override
    public void beforeInterface(InterfaceType interfaceType, T context) {
    }

    @Override
    public void afterInterface(InterfaceType interfaceType, T context) {
    }

    @Override
    public void beforeList(ListType listType, T context) {
    }

    @Override
    public void afterList(ListType listType, T context) {
    }

    @Override
    public void beforeNonNull(NonNullType nonNullType, T context) {
    }

    @Override
    public void afterNonNull(NonNullType nonNullType, T context) {
    }

    @Override
    public void beforeObject(ObjectType objectType, T context) {
    }

    @Override
    public void afterObject(ObjectType objectType, T context) {
    }

    @Override
    public void beforeScalar(ScalarType scalarType, T context) {
    }

    @Override
    public void afterScalar(ScalarType scalarType, T context) {
    }

    @Override
    public void beforeUnion(UnionType unionType, T context) {
    }

    @Override
    public void punctuateUnion(UnionType unionType, T context) {
    }

    @Override
    public void afterUnion(UnionType unionType, T context) {
    }

    @Override
    public void beforeUnionType(TypeDefinition unionType, T context) {
    }

    @Override
    public void afterUnionType(TypeDefinition unionType, T context) {
    }

    @Override
    public void beforeInputValues(List<InputValue> inputValues, T context) {
    }

    @Override
    public void punctuateInputValues(List<InputValue> inputValues, T context) {
    }

    @Override
    public void afterInputValues(List<InputValue> inputValues, T context) {
    }

    @Override
    public void beforeInputValue(InputValue inputValue, T context) {
    }

    @Override
    public void afterInputValue(InputValue inputValue, T context) {
    }

    @Override
    public void beforeFields(List<Field> fields, T context) {
    }

    @Override
    public void punctuateFields(List<Field> fields, T context) {
    }

    @Override
    public void afterFields(List<Field> fields, T context) {
    }

    @Override
    public void beforeField(Field field, T context) {
    }

    @Override
    public void afterField(Field field, T context) {
    }
}
