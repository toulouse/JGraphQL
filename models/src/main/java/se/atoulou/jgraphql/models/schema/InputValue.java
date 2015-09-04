package se.atoulou.jgraphql.models.schema;

import se.atoulou.jgraphql.models.query.TypeDefinition;

public class InputValue {
    private final String name;
    private final String description;
    private final TypeDefinition   type;
    private final String defaultValue;

    public static Builder builder() {
        return new Builder();
    }

    protected InputValue(String name, String description, TypeDefinition type, String defaultValue) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TypeDefinition getType() {
        return type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public static class Builder {
        private String       name;
        private String       description;
        private TypeDefinition.Builder type;
        private String       defaultValue;

        protected Builder() {
        }

        public InputValue build() {
            return new InputValue(name, description, type.build(), defaultValue);
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

        public TypeDefinition.Builder type() {
            return type;
        }

        public Builder type(TypeDefinition.Builder type) {
            this.type = type;
            return this;
        }

        public String defaultValue() {
            return defaultValue;
        }

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }
    }

    @Override
    public String toString() {
        return "InputValue [defaultValue=" + defaultValue + ", description=" + description + ", name=" + name + ", type=" + type + "]";
    }
}
