package se.atoulou.jgraphql.models.transform;

import java.util.List;

import se.atoulou.jgraphql.models.schema.EnumValue;
import se.atoulou.jgraphql.models.schema.Field;
import se.atoulou.jgraphql.models.schema.InputValue;
import se.atoulou.jgraphql.models.schema.Schema;
import se.atoulou.jgraphql.models.schema.Type;
import se.atoulou.jgraphql.models.schema.Type.EnumType;
import se.atoulou.jgraphql.models.schema.Type.InputObjectType;
import se.atoulou.jgraphql.models.schema.Type.InterfaceType;
import se.atoulou.jgraphql.models.schema.Type.ListType;
import se.atoulou.jgraphql.models.schema.Type.NonNullType;
import se.atoulou.jgraphql.models.schema.Type.ObjectType;
import se.atoulou.jgraphql.models.schema.Type.ScalarType;
import se.atoulou.jgraphql.models.schema.Type.UnionType;

public class SchemaBaseVisitor<T extends VisitorContext<T>> implements SchemaVisitor<T>, SchemaVisitorActions<T> {
    @Override
    public void visitSchema(Schema schema, T context) {
        beforeSchema(schema, context);

        List<Type> types = schema.getTypes();
        if (!types.isEmpty()) {
            context.enter();
            for (Type type : schema.getTypes()) {
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
    public void visitType(Type type, T context) {
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
        for (Type type : unionType.getPossibleTypes()) {
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
    public void visitUnionType(Type unionType, T context) {
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
        // TODO Auto-generated method stub
        
    }

    @Override
    public void punctuateSchema(Schema schema, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterSchema(Schema schema, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeType(Type type, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterType(Type type, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeEnum(EnumType enumType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void punctuateEnum(EnumType enumType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterEnum(EnumType enumType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeEnumValue(EnumValue enumValue, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterEnumValue(EnumValue enumValue, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeInputObject(InputObjectType inputObjectType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterInputObject(InputObjectType inputObjectType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeInterface(InterfaceType interfaceType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterInterface(InterfaceType interfaceType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeList(ListType listType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterList(ListType listType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeNonNull(NonNullType nonNullType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterNonNull(NonNullType nonNullType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeObject(ObjectType objectType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterObject(ObjectType objectType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeScalar(ScalarType scalarType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterScalar(ScalarType scalarType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeUnion(UnionType unionType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void punctuateUnion(UnionType unionType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterUnion(UnionType unionType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeUnionType(Type unionType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterUnionType(Type unionType, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeInputValues(List<InputValue> inputValues, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void punctuateInputValues(List<InputValue> inputValues, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterInputValues(List<InputValue> inputValues, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeInputValue(InputValue inputValue, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterInputValue(InputValue inputValue, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeFields(List<Field> fields, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void punctuateFields(List<Field> fields, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterFields(List<Field> fields, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeField(Field field, T context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterField(Field field, T context) {
        // TODO Auto-generated method stub
        
    }
}
