package se.atoulou.jgraphql.models.transform;

import java.util.List;

import se.atoulou.jgraphql.models.Argument;
import se.atoulou.jgraphql.models.Directive;
import se.atoulou.jgraphql.models.Document;
import se.atoulou.jgraphql.models.EnumValue;
import se.atoulou.jgraphql.models.FieldDefinition;
import se.atoulou.jgraphql.models.FragmentDefinition;
import se.atoulou.jgraphql.models.InputValue;
import se.atoulou.jgraphql.models.OperationDefinition;
import se.atoulou.jgraphql.models.Selection;
import se.atoulou.jgraphql.models.TypeDefinition;
import se.atoulou.jgraphql.models.VariableDefinition;
import se.atoulou.jgraphql.models.Selection.FragmentSpread;
import se.atoulou.jgraphql.models.Selection.InlineFragment;
import se.atoulou.jgraphql.models.Selection.SelectionField;
import se.atoulou.jgraphql.models.TypeDefinition.EnumType;
import se.atoulou.jgraphql.models.TypeDefinition.InputObjectType;
import se.atoulou.jgraphql.models.TypeDefinition.InterfaceType;
import se.atoulou.jgraphql.models.TypeDefinition.ListType;
import se.atoulou.jgraphql.models.TypeDefinition.NonNullType;
import se.atoulou.jgraphql.models.TypeDefinition.ObjectType;
import se.atoulou.jgraphql.models.TypeDefinition.ScalarType;
import se.atoulou.jgraphql.models.TypeDefinition.UnionType;

public interface DocumentVisitor<T extends VisitorContext<T>> {
    void visitDocument(Document document, T context);

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

    void visitTypeDefinition(TypeDefinition type, T context);

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

    void visitFields(List<FieldDefinition> fields, T context);

    void visitField(FieldDefinition field, T context);
}
