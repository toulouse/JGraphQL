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

public interface DocumentVisitorActions<T extends VisitorContext<T>> extends DocumentVisitor<T> {

    void beforeQueryDocument(Document queryDocument, T context);

    void punctuateQueryDocument(Document queryDocument, T context);

    void afterQueryDocument(Document queryDocument, T context);

    void beforeOperation(OperationDefinition operation, T context);

    void afterOperation(OperationDefinition operation, T context);

    void beforeFragment(FragmentDefinition fragment, T context);

    void afterFragment(FragmentDefinition fragment, T context);

    void beforeDirectives(List<Directive> directives, T context);

    void punctuateDirectives(List<Directive> directives, T context);

    void afterDirectives(List<Directive> directives, T context);

    void beforeDirective(Directive directive, T context);

    void afterDirective(Directive directive, T context);

    void beforeArguments(List<Argument> arguments, T context);

    void punctuateArguments(List<Argument> arguments, T context);

    void afterArguments(List<Argument> arguments, T context);

    void beforeArgument(Argument argument, T context);

    void afterArgument(Argument argument, T context);

    void beforeVariableDefinitions(List<VariableDefinition> variableDefinitions, T context);

    void punctuateVariableDefinitions(List<VariableDefinition> variableDefinitions, T context);

    void afterVariableDefinitions(List<VariableDefinition> variableDefinitions, T context);

    void beforeVariableDefinition(VariableDefinition variableDefinition, T context);

    void afterVariableDefinition(VariableDefinition variableDefinition, T context);

    void beforeSelectionSet(List<Selection> selectionSet, T context);

    void punctuateSelectionSet(List<Selection> selectionSet, T context);

    void afterSelectionSet(List<Selection> selectionSet, T context);

    void beforeInlineFragment(InlineFragment inlineFragment, T context);

    void afterInlineFragment(InlineFragment inlineFragment, T context);

    void beforeFragmentSpread(FragmentSpread fragmentSpread, T context);

    void afterFragmentSpread(FragmentSpread fragmentSpread, T context);

    void beforeSelectionField(SelectionField selectionField, T context);

    void afterSelectionField(SelectionField selectionField, T context);

    void beforeSchema(Schema schema, T context);

    void punctuateSchema(Schema schema, T context);

    void afterSchema(Schema schema, T context);

    void beforeType(TypeDefinition type, T context);

    void afterType(TypeDefinition type, T context);

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

    void beforeUnionType(TypeDefinition unionType, T context);

    void afterUnionType(TypeDefinition unionType, T context);

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
