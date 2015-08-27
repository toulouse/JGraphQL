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

public interface SchemaVisitorActions<T extends VisitorContext<T>> extends SchemaVisitor<T> {
    void beforeSchema(Schema schema, T context);

    void punctuateSchema(Schema schema, T context);

    void afterSchema(Schema schema, T context);

    void beforeType(Type type, T context);

    void afterType(Type type, T context);

    void beforeEnum(EnumType enumType, T context);

    void punctuateEnum(EnumType enumType, T context);

    void afterEnum(EnumType enumType, T context);

    void beforeEnumValue(EnumValue enumValue, T context);

    void afterEnumValue(EnumValue enumValue, T context);

    void beforeInputObject(InputObjectType inputObjectType, T context);

    void afterInputObject(InputObjectType inputObjectType, T context);

    void beforeInterface(InterfaceType interfaceType, T context);

    void afterInterface(InterfaceType interfaceType, T context);

    void beforeList(ListType listType, T context);

    void afterList(ListType listType, T context);

    void beforeNonNull(NonNullType nonNullType, T context);

    void afterNonNull(NonNullType nonNullType, T context);

    void beforeObject(ObjectType objectType, T context);

    void afterObject(ObjectType objectType, T context);

    void beforeScalar(ScalarType scalarType, T context);

    void afterScalar(ScalarType scalarType, T context);

    void beforeUnion(UnionType unionType, T context);

    void punctuateUnion(UnionType unionType, T context);
    
    void afterUnion(UnionType unionType, T context);

    void beforeUnionType(Type unionType, T context);

    void afterUnionType(Type unionType, T context);

    
    void beforeInputValues(List<InputValue> inputValues, T context);

    void punctuateInputValues(List<InputValue> inputValues, T context);

    void afterInputValues(List<InputValue> inputValues, T context);

    void beforeInputValue(InputValue inputValue, T context);

    void afterInputValue(InputValue inputValue, T context);

    void beforeFields(List<Field> fields, T context);

    void punctuateFields(List<Field> fields, T context);

    void afterFields(List<Field> fields, T context);

    void beforeField(Field field, T context);

    void afterField(Field field, T context);
}
