package se.atoulou.jgraphql.models.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import se.atoulou.jgraphql.models.query.InputValue.Builder;

public class DirectiveDefinition {
    private final String           name;
    private final String           description;
    private final List<InputValue> arguments;
    private final Boolean          onOperation;
    private final Boolean          onFragment;
    private final Boolean          onField;

    public static Builder builder() {
        return new Builder();
    }

    protected DirectiveDefinition(String name, String description, List<InputValue> arguments, Boolean onOperation, Boolean onFragment, Boolean onField) {
        this.name = name;
        this.description = description;
        this.arguments = arguments;
        this.onOperation = onOperation;
        this.onFragment = onFragment;
        this.onField = onField;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<InputValue> getArguments() {
        return arguments;
    }

    public Boolean getOnOperation() {
        return onOperation;
    }

    public Boolean getOnFragment() {
        return onFragment;
    }

    public Boolean getOnField() {
        return onField;
    }

    public static class Builder {
        private String                   name;
        private String                   description;
        private List<InputValue.Builder> arguments;
        private Boolean                  onOperation;
        private Boolean                  onFragment;
        private Boolean                  onField;

        protected Builder() {
            arguments = new ArrayList<>();
        }

        public DirectiveDefinition build() {
            List<InputValue> arguments = this.arguments.stream().map(builder -> builder.build()).collect(Collectors.toList());
            return new DirectiveDefinition(name, description, arguments, onOperation, onFragment, onField);
        }

        public String name() {
            return name;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public String description() {
            return description;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public List<InputValue.Builder> arguments() {
            return arguments;
        }

        public Builder arguments(List<InputValue.Builder> arguments) {
            this.arguments = arguments;
            return this;
        }

        public Boolean onOperation() {
            return onOperation;
        }

        public Builder onOperation(Boolean onOperation) {
            this.onOperation = onOperation;
            return this;
        }

        public Boolean onFragment() {
            return onFragment;
        }

        public Builder onFragment(Boolean onFragment) {
            this.onFragment = onFragment;
            return this;
        }

        public Boolean onField() {
            return onField;
        }

        public Builder onField(Boolean onField) {
            this.onField = onField;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Directive [args=" + arguments + ", description=" + description + ", name=" + name + ", onField=" + onField + ", onFragment=" + onFragment
                + ", onOperation=" + onOperation + "]";
    }
}
