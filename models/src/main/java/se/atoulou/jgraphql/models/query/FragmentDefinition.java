package se.atoulou.jgraphql.models.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FragmentDefinition {
    private final String          name;
    private final String          typeCondition;
    private final List<Directive> directives;
    private final List<Selection> selectionSet;

    public static Builder builder() {
        return new Builder();
    }

    protected FragmentDefinition(String name, String typeCondition, List<Directive> directives, List<Selection> selectionSet) {
        this.name = name;
        this.typeCondition = typeCondition;
        this.directives = directives;
        this.selectionSet = selectionSet;
    }

    public String getName() {
        return name;
    }

    public String getTypeCondition() {
        return typeCondition;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public List<Selection> getSelectionSet() {
        return selectionSet;
    }

    public static class Builder {
        private String                  name;
        private String                  typeCondition;
        private List<Directive.Builder> directives;
        private List<Selection.Builder> selectionSet;

        protected Builder() {
            directives = new ArrayList<>();
            selectionSet = new ArrayList<>();
        }

        public FragmentDefinition build() {
            List<Directive> directives = this.directives.stream().map(builder -> builder.build()).collect(Collectors.toList());
            List<Selection> selectionSet = this.selectionSet.stream().map(builder -> builder.build()).collect(Collectors.toList());
            return new FragmentDefinition(name, typeCondition, directives, selectionSet);
        }

        public String name() {
            return name;
        }

        public Builder name(String name) {
            this.name = name;
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
        return "FragmentDefinition [name=" + name + ", typeCondition=" + typeCondition + ", directives=" + directives + ", selectionSet=" + selectionSet + "]";
    }
}
