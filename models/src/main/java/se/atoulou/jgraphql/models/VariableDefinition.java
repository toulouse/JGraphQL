package se.atoulou.jgraphql.models;

public class VariableDefinition {
    private final String variable;
    private final String type;
    private final Value  defaultValue;

    public static Builder builder() {
        return new Builder();
    }

    protected VariableDefinition(String variable, String type, Value defaultValue) {
        this.variable = variable;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public String getVariable() {
        return variable;
    }

    public String getType() {
        return type;
    }

    public Value getDefaultValue() {
        return defaultValue;
    }

    public static class Builder {
        private String        variable;
        private String        type;
        private Value.Builder defaultValue;

        protected Builder() {
        }

        public VariableDefinition build() {
            Value value = defaultValue == null ? null : defaultValue.build();
            return new VariableDefinition(variable, type, value);
        }

        public String variable() {
            return variable;
        }

        public Builder variable(String variable) {
            this.variable = variable;
            return this;
        }

        public String type() {
            return type;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Value.Builder defaultValue() {
            return defaultValue;
        }

        public Builder defaultValue(Value.Builder defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }
    }

    @Override
    public String toString() {
        return "VariableDefinition [variable=" + variable + ", type=" + type + ", defaultValue=" + defaultValue + "]";
    }
}
