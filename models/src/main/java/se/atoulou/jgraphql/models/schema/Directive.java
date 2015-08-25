package se.atoulou.jgraphql.models.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Directive {
    private final String           name;
    private final String           description;
    private final List<InputValue> args;
    private final Boolean          onOperation;
    private final Boolean          onFragment;
    private final Boolean          onField;

    public static Builder builder() {
        return new Builder();
    }

    protected Directive(String name, String description, List<InputValue> args, Boolean onOperation, Boolean onFragment, Boolean onField) {
        this.name = name;
        this.description = description;
        this.args = args;
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

    public List<InputValue> getArgs() {
        return args;
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
        private List<InputValue.Builder> args;
        private Boolean                  onOperation;
        private Boolean                  onFragment;
        private Boolean                  onField;

        protected Builder() {
            args = new ArrayList<>();
        }

        public Directive build() {
            List<InputValue> args = this.args.stream().map(builder -> builder.build()).collect(Collectors.toList());
            return new Directive(name, description, args, onOperation, onFragment, onField);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder args(List<InputValue.Builder> args) {
            this.args = args;
            return this;
        }

        public Builder onOperation(Boolean onOperation) {
            this.onOperation = onOperation;
            return this;
        }

        public Builder onFragment(Boolean onFragment) {
            this.onFragment = onFragment;
            return this;
        }

        public Builder onField(Boolean onField) {
            this.onField = onField;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Directive [args=" + args + ", description=" + description + ", name=" + name + ", onField=" + onField + ", onFragment=" + onFragment
                + ", onOperation=" + onOperation + "]";
    }
}
