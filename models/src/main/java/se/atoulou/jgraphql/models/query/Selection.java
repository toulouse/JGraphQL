package se.atoulou.jgraphql.models.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Selection {
    public static enum SelectionKind {
        FIELD,
        FRAGMENT_SPREAD,
        INLINE_FRAGMENT
    }

    private final SelectionKind   kind;
    private final List<Directive> directives;

    public static Builder builder() {
        return new Builder();
    }

    protected Selection(SelectionKind kind, List<Directive> directives) {
        this.kind = kind;
        this.directives = directives;
    }

    public SelectionKind getKind() {
        return kind;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public static class Field extends Selection {
        private final String          alias;
        private final String          name;
        private final List<Argument>  arguments;
        private final List<Selection> selectionSet;

        public Field(String alias, String name, List<Argument> arguments, List<Directive> directives, List<Selection> selectionSet) {
            super(SelectionKind.FIELD, directives);
            this.alias = alias;
            this.name = name;
            this.arguments = arguments;
            this.selectionSet = selectionSet;
        }

        public String getAlias() {
            return alias;
        }

        public String getName() {
            return name;
        }

        public List<Argument> getArguments() {
            return arguments;
        }

        public List<Selection> getSelectionSet() {
            return selectionSet;
        }

        @Override
        public String toString() {
            return "Field [alias=" + alias + ", name=" + name + ", arguments=" + arguments + ", selectionSet=" + selectionSet + ", getDirectives()="
                    + getDirectives() + "]";
        }
    }

    public static class FragmentSpread extends Selection {
        private final String name;

        public FragmentSpread(String name, List<Directive> directives) {
            super(SelectionKind.FRAGMENT_SPREAD, directives);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "FragmentSpread [name=" + name + ", getDirectives()=" + getDirectives() + "]";
        }
    }

    public static class InlineFragment extends Selection {
        private final String          typeCondition;
        private final List<Selection> selectionSet;

        public InlineFragment(String typeCondition, List<Directive> directives, List<Selection> selectionSet) {
            super(SelectionKind.INLINE_FRAGMENT, directives);
            this.typeCondition = typeCondition;
            this.selectionSet = selectionSet;
        }

        public String getTypeCondition() {
            return typeCondition;
        }

        public List<Selection> getSelectionSet() {
            return selectionSet;
        }

        @Override
        public String toString() {
            return "InlineFragment [typeCondition=" + typeCondition + ", selectionSet=" + selectionSet + ", getDirectives()=" + getDirectives() + "]";
        }
    }

    public static class Builder {
        private SelectionKind           kind;
        private String                  alias;
        private String                  name;
        private List<Argument.Builder>  arguments;
        private String                  typeCondition;
        private List<Directive.Builder> directives;
        private List<Selection.Builder> selectionSet;

        protected Builder() {
            arguments = new ArrayList<>();
            directives = new ArrayList<>();
            selectionSet = new ArrayList<>();
        }

        public Selection build() {
            List<Directive> directives = this.directives.stream().map(builder -> builder.build()).collect(Collectors.toList());

            switch (kind) {
            case FIELD: {
                List<Argument> arguments = this.arguments.stream().map(builder -> builder.build()).collect(Collectors.toList());
                List<Selection> selectionSet = this.selectionSet.stream().map(builder -> builder.build()).collect(Collectors.toList());
                return new Field(alias, name, arguments, directives, selectionSet);
            }
            case FRAGMENT_SPREAD: {
                return new FragmentSpread(name, directives);
            }
            case INLINE_FRAGMENT: {
                List<Selection> selectionSet = this.selectionSet.stream().map(builder -> builder.build()).collect(Collectors.toList());
                return new InlineFragment(typeCondition, directives, selectionSet);
            }
            default:
                assert false;
                return null;
            }
        }

        public SelectionKind kind() {
            return kind;
        }

        public Builder kind(SelectionKind kind) {
            this.kind = kind;
            return this;
        }

        public String alias() {
            return alias;
        }

        public Builder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public String name() {
            return name;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public List<Argument.Builder> arguments() {
            return arguments;
        }

        public Builder arguments(List<Argument.Builder> arguments) {
            this.arguments = arguments;
            return this;
        }

        public String typeCondition() {
            return typeCondition;
        }

        public Builder typeCondition(String typeCondition) {
            this.typeCondition = typeCondition;
            return this;
        }

        public List<Directive.Builder> directives() {
            return directives;
        }

        public Builder directives(List<Directive.Builder> directives) {
            this.directives = directives;
            return this;
        }

        public List<Selection.Builder> selectionSet() {
            return selectionSet;
        }

        public Builder selectionSet(List<Selection.Builder> selectionSet) {
            this.selectionSet = selectionSet;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Selection [kind=" + kind + ", directives=" + directives + "]";
    }
}
