package se.atoulou.jgraphql.models;

import java.util.List;
import java.util.stream.Collectors;

import se.atoulou.jgraphql.models.query.Argument;
import se.atoulou.jgraphql.models.query.Directive;
import se.atoulou.jgraphql.models.query.Document;
import se.atoulou.jgraphql.models.query.FragmentDefinition;
import se.atoulou.jgraphql.models.query.OperationDefinition;
import se.atoulou.jgraphql.models.query.Selection;
import se.atoulou.jgraphql.models.query.Selection.FragmentSpread;
import se.atoulou.jgraphql.models.query.Selection.InlineFragment;
import se.atoulou.jgraphql.models.query.Selection.SelectionField;
import se.atoulou.jgraphql.models.query.TypeDefinition;
import se.atoulou.jgraphql.models.query.TypeDefinition.EnumType;
import se.atoulou.jgraphql.models.query.TypeDefinition.InputObjectType;
import se.atoulou.jgraphql.models.query.TypeDefinition.InterfaceType;
import se.atoulou.jgraphql.models.query.TypeDefinition.ObjectType;
import se.atoulou.jgraphql.models.query.TypeDefinition.ScalarType;
import se.atoulou.jgraphql.models.query.TypeDefinition.UnionType;
import se.atoulou.jgraphql.models.query.VariableDefinition;
import se.atoulou.jgraphql.models.schema.EnumValue;
import se.atoulou.jgraphql.models.schema.Field;
import se.atoulou.jgraphql.models.schema.InputValue;
import se.atoulou.jgraphql.models.transform.DocumentBaseVisitor;
import se.atoulou.jgraphql.models.transform.DocumentMessageWriter;

public final class DocumentPrettyPrinter extends DocumentBaseVisitor<StringBuilderVisitorContext>implements DocumentMessageWriter<String> {
    private final String  newline;
    private final String  tab;
    private final boolean isCompact;

    @Override
    public String writeDocument(Document document) {
        StringBuilderVisitorContext context = new StringBuilderVisitorContext(newline, tab, isCompact);
        visitDocument(document, context);
        return context.getStringBuilder().toString();
    }

    public DocumentPrettyPrinter(boolean isCompact) {
        this(isCompact ? "" : "¥n", isCompact ? "" : "  ", isCompact);
    }

    public DocumentPrettyPrinter() {
        this("\n", "  ");
    }

    public DocumentPrettyPrinter(String newline, String tab) {
        this(newline, tab, false);
    }

    public DocumentPrettyPrinter(String newline, String tab, boolean isCompact) {
        this.newline = newline;
        this.tab = tab;
        this.isCompact = isCompact;
    }

    @Override
    public void punctuateDocument(Document queryDocument, StringBuilderVisitorContext context) {
        context.appendNewlines(2);
    }

    @Override
    public void beforeOperation(OperationDefinition operation, StringBuilderVisitorContext context) {
        if (operation.getName() != null) {
            context.appendTabs();

            String operationType = operation.getOperationType().name().toLowerCase();
            context.append(String.format("%s %s", operationType, operation.getName()));
        }
    }

    @Override
    public void beforeFragment(FragmentDefinition fragment, StringBuilderVisitorContext context) {
        String fragmentName = fragment.getName();
        String typeCondition = fragment.getTypeCondition();

        context.appendTabs();
        context.append(String.format("fragment %s on %s", fragmentName, typeCondition));
    }

    @Override
    public void beforeDirectives(List<Directive> directives, StringBuilderVisitorContext context) {
        if (!directives.isEmpty()) {
            context.append(' ');
        }
    }

    @Override
    public void punctuateDirectives(List<Directive> directives, StringBuilderVisitorContext context) {
        context.append(' ');
    }

    @Override
    public void beforeDirective(Directive directive, StringBuilderVisitorContext context) {
        context.append(String.format("@%s", directive.getName()));

        if (!directive.getArguments().isEmpty()) {
            context.setNewlinesEnabled(false);
        }
    }

    @Override
    public void afterDirective(Directive directive, StringBuilderVisitorContext context) {
        if (!directive.getArguments().isEmpty()) {
            context.setNewlinesEnabled(true);
        }
    }

    @Override
    public void beforeArguments(List<Argument> arguments, StringBuilderVisitorContext context) {
        if (!arguments.isEmpty()) {
            context.append('(');
        }
    }

    @Override
    public void punctuateArguments(List<Argument> arguments, StringBuilderVisitorContext context) {
        context.append(',');
        context.appendRemovableSpace();
    }

    @Override
    public void afterArguments(List<Argument> arguments, StringBuilderVisitorContext context) {
        if (!arguments.isEmpty()) {
            context.append(')');
        }
    }

    @Override
    public void beforeArgument(Argument argument, StringBuilderVisitorContext context) {
        context.append(argument.getName());
        context.append(':');
        context.appendRemovableSpace();
        context.append(argument.getValue().getValue());
    }

    @Override
    public void beforeVariableDefinitions(List<VariableDefinition> variableDefinitions, StringBuilderVisitorContext context) {
        if (!variableDefinitions.isEmpty()) {
            context.append('(');
        }
    }

    @Override
    public void punctuateVariableDefinitions(List<VariableDefinition> variableDefinitions, StringBuilderVisitorContext context) {
        context.append(',');
        context.appendRemovableSpace();
    }

    @Override
    public void afterVariableDefinitions(List<VariableDefinition> variableDefinitions, StringBuilderVisitorContext context) {
        if (!variableDefinitions.isEmpty()) {
            context.append(')');
        }
    }

    @Override
    public void beforeVariableDefinition(VariableDefinition variableDefinition, StringBuilderVisitorContext context) {
        context.append(variableDefinition.getVariable());
        context.append(':');
        context.appendRemovableSpace();
        context.append(variableDefinition.getType());

        if (variableDefinition.getDefaultValue() != null) {
            context.appendRemovableSpace();
            context.append('=');
            context.appendRemovableSpace();

            context.append(variableDefinition.getDefaultValue().getValue());
        }
    }

    @Override
    public void beforeSelectionSet(List<Selection> selectionSet, StringBuilderVisitorContext context) {
        if (selectionSet.isEmpty()) {
            return;
        }

        if (selectionSet.size() == 1 && selectionSet.get(0).isLeaf()) {
            context.appendRemovableSpace();
            context.append('{');
            context.appendRemovableSpace();
        } else {
            context.appendRemovableSpace();
            context.append('{');
            context.indent();

            context.appendNewline();
            context.appendTabs();
        }
    }

    @Override
    public void punctuateSelectionSet(List<Selection> selectionSet, StringBuilderVisitorContext context) {
        if (context.isCompact()) {
            context.append(',');
        }
        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void afterSelectionSet(List<Selection> selectionSet, StringBuilderVisitorContext context) {
        if (selectionSet.isEmpty()) {
            return;
        }
        if (selectionSet.size() == 1 && selectionSet.get(0).isLeaf()) {
            context.appendRemovableSpace();
            context.append('}');
        } else {
            context.dedent();

            context.appendNewline();
            context.appendTabs();
            context.append('}');
        }
    }

    @Override
    public void beforeInlineFragment(InlineFragment inlineFragment, StringBuilderVisitorContext context) {
        context.append(String.format("... on %s", inlineFragment.getTypeCondition()));
    }

    @Override
    public void beforeFragmentSpread(FragmentSpread fragmentSpread, StringBuilderVisitorContext context) {
        context.append(String.format("...%s", fragmentSpread.getName()));
    }

    @Override
    public void beforeSelectionField(SelectionField selectionField, StringBuilderVisitorContext context) {
        String alias = selectionField.getAlias();
        if (alias != null) {
            context.append(alias);
            context.appendRemovableSpace();
            context.append(':');
            context.appendRemovableSpace();
        }

        String name = selectionField.getName();
        context.append(name);
    }

    @Override
    public void beforeEnum(EnumType enumType, StringBuilderVisitorContext context) {
        context.appendTabs();

        context.append(String.format("enum %s", enumType.getName()));
        context.appendRemovableSpace();
        context.append('{');
        context.indent();

        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void punctuateEnum(EnumType enumType, StringBuilderVisitorContext context) {
        context.append(',');
        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void afterEnum(EnumType enumType, StringBuilderVisitorContext context) {
        context.dedent();

        context.appendNewline();
        context.appendTabs();
        context.append('}');
    }

    @Override
    public void visitEnumValue(EnumValue enumValue, StringBuilderVisitorContext context) {
        context.append(enumValue.getName());
    }

    @Override
    public void beforeInputObject(InputObjectType inputObjectType, StringBuilderVisitorContext context) {
        context.appendTabs();

        context.append(String.format("input %s", inputObjectType.getName()));
        context.appendRemovableSpace();
        context.append('{');

        context.indent();

        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void afterInputObject(InputObjectType inputObjectType, StringBuilderVisitorContext context) {
        context.dedent();

        context.appendNewline();
        context.appendTabs();
        context.append('}');
    }

    @Override
    public void punctuateInputValues(List<InputValue> inputValues, StringBuilderVisitorContext context) {
        context.append(',');
        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void beforeInterface(InterfaceType interfaceType, StringBuilderVisitorContext context) {
        context.appendTabs();

        context.append(String.format("interface %s", interfaceType.getName()));
        context.appendRemovableSpace();
        context.append('{');

        context.indent();

        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void afterInterface(InterfaceType interfaceType, StringBuilderVisitorContext context) {
        context.dedent();

        context.appendNewline();
        context.appendTabs();
        context.append('}');
    }

    @Override
    public void beforeObject(ObjectType objectType, StringBuilderVisitorContext context) {
        context.appendTabs();

        context.append(String.format("type %s", objectType.getName()));

        List<String> interfaces = objectType.getInterfaces();
        if (!interfaces.isEmpty()) {
            String joiner = context.isCompact() ? "," : ", ";

            String implementsString = interfaces.stream().collect(Collectors.joining(joiner));
            context.appendRemovableSpace();
            context.append(':');
            context.appendRemovableSpace();
            context.append(implementsString);
        }

        context.appendRemovableSpace();
        context.append('{');

        context.indent();

        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void afterObject(ObjectType objectType, StringBuilderVisitorContext context) {
        context.dedent();

        context.appendNewline();
        context.appendTabs();
        context.append('}');
    }

    @Override
    public void visitScalar(ScalarType scalarType, StringBuilderVisitorContext context) {
        context.appendTabs();
        context.append(String.format("scalar %s", scalarType.getName()));

        if (context.isCompact()) {
            context.append(',');
        }
    }

    @Override
    public void beforeUnion(UnionType unionType, StringBuilderVisitorContext context) {
        context.appendTabs();
        context.append(String.format("union %s", unionType.getName()));
        context.appendRemovableSpace();
        context.append('=');
        context.appendRemovableSpace();
    }

    @Override
    public void afterUnion(UnionType unionType, StringBuilderVisitorContext context) {
        if (context.isCompact()) {
            context.append(',');
        }
    }

    @Override
    public void punctuateUnion(UnionType unionType, StringBuilderVisitorContext context) {
        context.appendRemovableSpace();
        context.append('|');
        context.appendRemovableSpace();
    }

    @Override
    public void visitUnionType(TypeDefinition unionType, StringBuilderVisitorContext context) {
        context.append(unionType.getName());
    }

    @Override
    public void beforeInputValue(InputValue inputValue, StringBuilderVisitorContext context) {
        context.append(inputValue.getName());
        context.append(':');
        context.appendRemovableSpace();
        context.append(inputValue.getType().getName());

        if (inputValue.getDefaultValue() != null) {
            context.appendRemovableSpace();
            context.append('=');
            context.appendRemovableSpace();

            context.append(inputValue.getDefaultValue());
        }
    }

    @Override
    public void beforeField(Field field, StringBuilderVisitorContext context) {
        context.append(field.getName());

        if (!field.getArguments().isEmpty()) {
            context.setNewlinesEnabled(false);
            context.append('(');
        }
    }

    @Override
    public void punctuateFields(List<Field> fields, StringBuilderVisitorContext context) {
        context.append(',');
        context.appendNewline();
        context.appendTabs();
    }

    @Override
    public void afterField(Field field, StringBuilderVisitorContext context) {
        if (!field.getArguments().isEmpty()) {
            context.append(')');
            context.setNewlinesEnabled(true);
        }

        context.append(':');
        context.appendRemovableSpace();
        context.append(field.getType());
    }
}