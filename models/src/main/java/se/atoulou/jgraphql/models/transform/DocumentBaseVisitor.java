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

public class DocumentBaseVisitor<T extends VisitorContext<T>> implements DocumentVisitor<T>, DocumentVisitorActions<T> {

    @Override
    public void visitQueryDocument(Document queryDocument, T context) {
        beforeQueryDocument(queryDocument, context);

        List<OperationDefinition> operations = queryDocument.getOperations();
        if (!operations.isEmpty()) {
            context.enter();
            for (OperationDefinition operation : operations) {
                context.incrementIndex();
                if (context.currentIndex() >= 1) {
                    punctuateQueryDocument(queryDocument, context);
                }
                visitOperation(operation, context);
            }
            context.leave();
        }

        List<FragmentDefinition> fragments = queryDocument.getFragments();
        if (!fragments.isEmpty()) {
            if (!operations.isEmpty()) {
                punctuateQueryDocument(queryDocument, context);
            }

            context.enter();
            for (FragmentDefinition fragment : fragments) {
                context.incrementIndex();

                if (context.currentIndex() >= 1) {
                    punctuateQueryDocument(queryDocument, context);
                }
                visitFragment(fragment, context);
            }
            context.leave();
        }

        afterQueryDocument(queryDocument, context);
    }

    @Override
    public void visitOperation(OperationDefinition operation, T context) {
        beforeOperation(operation, context);

        visitVariableDefinitions(operation.getVariableDefinitions(), context);
        visitDirectives(operation.getDirectives(), context);
        visitSelectionSet(operation.getSelectionSet(), context);

        afterOperation(operation, context);
    }

    @Override
    public void visitFragment(FragmentDefinition fragment, T context) {
        beforeFragment(fragment, context);

        visitDirectives(fragment.getDirectives(), context);
        visitSelectionSet(fragment.getSelectionSet(), context);

        afterFragment(fragment, context);
    }

    @Override
    public void visitDirectives(List<Directive> directives, T context) {
        beforeDirectives(directives, context);

        if (!directives.isEmpty()) {
            context.enter();
            for (Directive directive : directives) {
                context.incrementIndex();

                if (context.currentIndex() >= 1) {
                    punctuateDirectives(directives, context);
                }
                visitDirective(directive, context);
            }
            context.leave();
        }

        afterDirectives(directives, context);
    }

    @Override
    public void visitDirective(Directive directive, T context) {
        beforeDirective(directive, context);

        visitArguments(directive.getArguments(), context);

        afterDirective(directive, context);
    }

    @Override
    public void visitArguments(List<Argument> arguments, T context) {
        beforeArguments(arguments, context);

        if (!arguments.isEmpty()) {
            context.enter();
            for (Argument argument : arguments) {
                context.incrementIndex();

                if (context.currentIndex() >= 1) {
                    punctuateArguments(arguments, context);
                }
                visitArgument(argument, context);
            }
            context.leave();
        }

        afterArguments(arguments, context);
    }

    @Override
    public void visitArgument(Argument argument, T context) {
        beforeArgument(argument, context);
        afterArgument(argument, context);
    }

    @Override
    public void visitVariableDefinitions(List<VariableDefinition> variableDefinitions, T context) {
        beforeVariableDefinitions(variableDefinitions, context);

        if (!variableDefinitions.isEmpty()) {
            context.enter();
            for (VariableDefinition variableDefinition : variableDefinitions) {
                context.incrementIndex();

                if (context.currentIndex() >= 1) {
                    punctuateVariableDefinitions(variableDefinitions, context);
                }
                visitVariableDefinition(variableDefinition, context);
            }
            context.leave();
        }

        afterVariableDefinitions(variableDefinitions, context);
    }

    @Override
    public void visitVariableDefinition(VariableDefinition variableDefinition, T context) {
        beforeVariableDefinition(variableDefinition, context);
        afterVariableDefinition(variableDefinition, context);
    }

    @Override
    public void visitSelectionSet(List<Selection> selectionSet, T context) {
        beforeSelectionSet(selectionSet, context);

        if (!selectionSet.isEmpty()) {
            context.enter();
            for (Selection selection : selectionSet) {
                context.incrementIndex();

                if (context.currentIndex() >= 1) {
                    punctuateSelectionSet(selectionSet, context);
                }

                switch (selection.getKind()) {
                case FIELD:
                    visitSelectionField((Selection.SelectionField) selection, context);
                    break;
                case FRAGMENT_SPREAD:
                    visitFragmentSpread((Selection.FragmentSpread) selection, context);
                    break;
                case INLINE_FRAGMENT:
                    visitInlineFragment((Selection.InlineFragment) selection, context);
                    break;
                default:
                    break;
                }
            }
            context.leave();
        }

        afterSelectionSet(selectionSet, context);
    }

    @Override
    public void visitInlineFragment(InlineFragment inlineFragment, T context) {
        beforeInlineFragment(inlineFragment, context);

        visitDirectives(inlineFragment.getDirectives(), context);
        visitSelectionSet(inlineFragment.getSelectionSet(), context);

        afterInlineFragment(inlineFragment, context);
    }

    @Override
    public void visitFragmentSpread(FragmentSpread fragmentSpread, T context) {
        beforeFragmentSpread(fragmentSpread, context);

        visitDirectives(fragmentSpread.getDirectives(), context);

        afterFragmentSpread(fragmentSpread, context);
    }

    @Override
    public void visitSelectionField(SelectionField selectionField, T context) {
        beforeSelectionField(selectionField, context);

        visitArguments(selectionField.getArguments(), context);
        visitDirectives(selectionField.getDirectives(), context);
        visitSelectionSet(selectionField.getSelectionSet(), context);

        afterSelectionField(selectionField, context);
    }

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
    public void beforeQueryDocument(Document queryDocument, T context) {
    }

    @Override
    public void punctuateQueryDocument(Document queryDocument, T context) {
    }

    @Override
    public void afterQueryDocument(Document queryDocument, T context) {
    }

    @Override
    public void beforeOperation(OperationDefinition operation, T context) {
    }

    @Override
    public void afterOperation(OperationDefinition operation, T context) {
    }

    @Override
    public void beforeFragment(FragmentDefinition fragment, T context) {
    }

    @Override
    public void afterFragment(FragmentDefinition fragment, T context) {
    }

    @Override
    public void beforeDirectives(List<Directive> directives, T context) {
    }

    @Override
    public void punctuateDirectives(List<Directive> directives, T context) {
    }

    @Override
    public void afterDirectives(List<Directive> directives, T context) {
    }

    @Override
    public void beforeDirective(Directive directive, T context) {
    }

    @Override
    public void afterDirective(Directive directive, T context) {
    }

    @Override
    public void beforeArguments(List<Argument> arguments, T context) {
    }

    @Override
    public void punctuateArguments(List<Argument> arguments, T context) {
    }

    @Override
    public void afterArguments(List<Argument> arguments, T context) {
    }

    @Override
    public void beforeArgument(Argument argument, T context) {
    }

    @Override
    public void afterArgument(Argument argument, T context) {
    }

    @Override
    public void beforeVariableDefinitions(List<VariableDefinition> variableDefinitions, T context) {
    }

    @Override
    public void punctuateVariableDefinitions(List<VariableDefinition> variableDefinitions, T context) {
    }

    @Override
    public void afterVariableDefinitions(List<VariableDefinition> variableDefinitions, T context) {
    }

    @Override
    public void beforeVariableDefinition(VariableDefinition variableDefinition, T context) {
    }

    @Override
    public void afterVariableDefinition(VariableDefinition variableDefinition, T context) {
    }

    @Override
    public void beforeSelectionSet(List<Selection> selectionSet, T context) {
    }

    @Override
    public void punctuateSelectionSet(List<Selection> selectionSet, T context) {
    }

    @Override
    public void afterSelectionSet(List<Selection> selectionSet, T context) {
    }

    @Override
    public void beforeInlineFragment(InlineFragment inlineFragment, T context) {
    }

    @Override
    public void afterInlineFragment(InlineFragment inlineFragment, T context) {
    }

    @Override
    public void beforeFragmentSpread(FragmentSpread fragmentSpread, T context) {
    }

    @Override
    public void afterFragmentSpread(FragmentSpread fragmentSpread, T context) {
    }

    @Override
    public void beforeSelectionField(SelectionField selectionField, T context) {
    }

    @Override
    public void afterSelectionField(SelectionField selectionField, T context) {
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
