package se.atoulou.jgraphql.models.transform;

import se.atoulou.jgraphql.models.schema.EnumValue;
import se.atoulou.jgraphql.models.schema.Field;
import se.atoulou.jgraphql.models.schema.InputValue;
import se.atoulou.jgraphql.models.schema.Schema;
import se.atoulou.jgraphql.models.schema.Type;
import se.atoulou.jgraphql.models.schema.Type.EnumType;
import se.atoulou.jgraphql.models.schema.Type.InputObjectType;
import se.atoulou.jgraphql.models.schema.Type.InterfaceType;
import se.atoulou.jgraphql.models.schema.Type.ObjectType;
import se.atoulou.jgraphql.models.schema.Type.ScalarType;
import se.atoulou.jgraphql.models.schema.Type.UnionType;

public interface SchemaVisitor<T extends VisitorContext<T>> {
    default void visitSchema(Schema schema, T context) {
        context.enter();
        for (Type type : schema.getTypes()) {
            context.incrementIndex();
            visitType(type, context);
        }
        context.leave();
    }

    default void visitType(Type type, T context) {
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
            assert false;
            break;
        case NON_NULL:
            assert false;
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
    }

    default void visitEnum(EnumType enumType, T context) {
        context.enter();
        for (EnumValue enumValue : enumType.getEnumValues()) {
            context.incrementIndex();
            visitEnumValue(enumValue, context);
        }
        context.leave();
    }

    default void visitEnumValue(EnumValue enumValue, T context) {
    }

    default void visitInputObject(InputObjectType inputObjectType, T context) {
        context.enter();
        for (InputValue inputValue : inputObjectType.getInputFields()) {
            context.incrementIndex();
            visitInputValue(inputValue, context);
        }
        context.leave();
    }

    default void visitInterface(InterfaceType interfaceType, T context) {
        context.enter();
        for (Field field : interfaceType.getFields()) {
            context.incrementIndex();
            visitField(field, context);
        }
        context.leave();
    }

    default void visitObject(ObjectType objectType, T context) {
        context.enter();
        for (Field field : objectType.getFields()) {
            context.incrementIndex();
            visitField(field, context);
        }
        context.leave();
    }

    default void visitScalar(ScalarType scalarType, T context) {
    }

    default void visitUnion(UnionType unionType, T context) {
        context.enter();
        for (Type type : unionType.getPossibleTypes()) {
            context.incrementIndex();
            visitType(type, context);
        }
        context.leave();
    }

    default void visitInputValue(InputValue inputValueType, T context) {
    }

    default void visitField(Field fieldType, T context) {
        context.enter();
        if (!fieldType.getArguments().isEmpty()) {
            for (InputValue inputValue : fieldType.getArguments()) {
                context.incrementIndex();
                visitInputValue(inputValue, context);
            }
        }
        context.leave();
    }
}
