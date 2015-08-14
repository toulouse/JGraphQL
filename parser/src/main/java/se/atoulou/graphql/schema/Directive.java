package se.atoulou.graphql.schema;

import java.util.List;

import se.atoulou.graphql.common.NotNull;

public class Directive {
    private final @NotNull String                    name;
    private final String                             description;
    private final @NotNull List<@NotNull InputValue> args;
    private final @NotNull Boolean                   onOperation;
    private final @NotNull Boolean                   onFragment;
    private final @NotNull Boolean                   onField;

    public static Builder builder() {
        return new Builder();
    }

    protected Directive(@NotNull String name, String description, @NotNull List<@NotNull InputValue> args, @NotNull Boolean onOperation,
            @NotNull Boolean onFragment, @NotNull Boolean onField) {
        super();
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
        private String           name;
        private String           description;
        private List<InputValue> args;
        private Boolean          onOperation;
        private Boolean          onFragment;
        private Boolean          onField;

        protected Builder() {
        }

        public Directive build() {
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

        public Builder args(List<InputValue> args) {
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
}
