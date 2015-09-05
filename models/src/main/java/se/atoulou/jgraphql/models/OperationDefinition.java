package se.atoulou.jgraphql.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OperationDefinition {
    public static enum OperationType {
        QUERY,
        MUATATION
    }

    private final OperationType            operationType;
    private final String                   name;
    private final List<VariableDefinition> variableDefinitions;
    private final List<Directive>          directives;
    private final List<Selection>          selectionSet;

    public static Builder builder() {
        return new Builder();
    }

    public OperationDefinition(OperationType operationType, String name, List<VariableDefinition> variableDefinitions, List<Directive> directives,
            List<Selection> selectionSet) {
        super();
        this.operationType = operationType;
        this.name = name;
        this.variableDefinitions = variableDefinitions;
        this.directives = directives;
        this.selectionSet = selectionSet;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public String getName() {
        return name;
    }

    public List<VariableDefinition> getVariableDefinitions() {
        return variableDefinitions;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public List<Selection> getSelectionSet() {
        return selectionSet;
    }

    public static class Builder {
        private OperationType                    operationType;
        private String                           name;
        private List<VariableDefinition.Builder> variableDefinitions;
        private List<Directive.Builder>          directives;
        private List<Selection.Builder>          selectionSet;

        protected Builder() {
            variableDefinitions = new ArrayList<>();
            directives = new ArrayList<>();
            selectionSet = new ArrayList<>();
        }

        public OperationDefinition build() {
            List<VariableDefinition> variableDefinitions = this.variableDefinitions.stream().map(builder -> builder.build()).collect(Collectors.toList());
            List<Directive> directives = this.directives.stream().map(builder -> builder.build()).collect(Collectors.toList());
            List<Selection> selectionSet = this.selectionSet.stream().map(builder -> builder.build()).collect(Collectors.toList());
            return new OperationDefinition(operationType, name, variableDefinitions, directives, selectionSet);
        }

        public OperationType operationType() {
            return operationType;
        }

        public Builder operationType(OperationType operationType) {
            this.operationType = operationType;
            return this;
        }

        public String name() {
            return name;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public List<VariableDefinition.Builder> variableDefinitions() {
            return variableDefinitions;
        }

        public Builder variableDefinitions(List<VariableDefinition.Builder> variableDefinitions) {
            this.variableDefinitions = variableDefinitions;
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
        return "OperationDefinition [operationType=" + operationType + ", name=" + name + ", variableDefinitions=" + variableDefinitions + ", directives="
                + directives + ", selectionSet=" + selectionSet + "]";
    }
}
