package se.atoulou.jgraphql.models.transform;

import java.util.List;

import se.atoulou.jgraphql.models.query.Argument;
import se.atoulou.jgraphql.models.query.Directive;
import se.atoulou.jgraphql.models.query.FragmentDefinition;
import se.atoulou.jgraphql.models.query.OperationDefinition;
import se.atoulou.jgraphql.models.query.Document;
import se.atoulou.jgraphql.models.query.Selection;
import se.atoulou.jgraphql.models.query.TypeDefinition;
import se.atoulou.jgraphql.models.query.VariableDefinition;
import se.atoulou.jgraphql.models.query.Selection.FragmentSpread;
import se.atoulou.jgraphql.models.query.Selection.InlineFragment;
import se.atoulou.jgraphql.models.query.Selection.SelectionField;
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

public interface DocumentVisitor<T extends VisitorContext<T>> {
    void visitQueryDocument(Document queryDocument, T context);

    void visitOperation(OperationDefinition operation, T context);

    void visitFragment(FragmentDefinition fragment, T context);

    void visitDirectives(List<Directive> directives, T context);

    void visitDirective(Directive directive, T context);

    void visitArguments(List<Argument> arguments, T context);

    void visitArgument(Argument argument, T context);

    void visitVariableDefinitions(List<VariableDefinition> variableDefinitions, T context);

    void visitVariableDefinition(VariableDefinition variableDefinition, T context);

    void visitSelectionSet(List<Selection> selectionSet, T context);

    void visitInlineFragment(InlineFragment inlineFragment, T context);

    void visitFragmentSpread(FragmentSpread fragmentSpread, T context);

    void visitSelectionField(SelectionField selectionField, T context);

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
