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

public interface SchemaVisitor<T extends VisitorContext<T>> {
    void visitSchema(Schema schema, T context);

    void visitType(TypeDefinition type, T context);

    void visitEnum(EnumType enumType, T context);

    void visitEnumValue(EnumValue enumvalue, T context);

    void visitInputObject(InputObjectType inputObjectType, T context);

    void visitInterface(InterfaceType interfaceType, T context);

    void visitList(ListType listType, T context);

    void visitNonNull(NonNullType nonNullType, T context);

    void visitObject(ObjectType objectType, T context);

    void visitScalar(ScalarType scalarType, T context);

    void visitUnion(UnionType unionType, T context);

    void visitUnionType(TypeDefinition unionType, T context);

    void visitInputValues(List<InputValue> inputValues, T context);

    void visitInputValue(InputValue inputValue, T context);

    void visitFields(List<Field> fields, T context);

    void visitField(Field field, T context);
}
