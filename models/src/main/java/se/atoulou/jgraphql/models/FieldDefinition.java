package se.atoulou.jgraphql.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FieldDefinition {
    private final String           name;
    private final String           description;
    private final List<InputValue> arguments;
    private final String           type;
    private final Boolean          isDeprecated;
    private final String           deprecationReason;

    public static Builder builder() {
        return new Builder();
    }

    protected FieldDefinition(String name, String description, List<InputValue> argumentss, String type, Boolean isDeprecated, String deprecationReason) {
        this.name = name;
        this.description = description;
        this.arguments = argumentss;
        this.type = type;
        this.isDeprecated = isDeprecated;
        this.deprecationReason = deprecationReason;
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

    public String getType() {
        return type;
    }

    public Boolean isDeprecated() {
        return isDeprecated;
    }

    public String getDeprecationReason() {
        return deprecationReason;
    }

    public static class Builder {
        private String                   name;
        private String                   description;
        private List<InputValue.Builder> arguments;
        private TypeDefinition.Builder   type;
        private Boolean                  isDeprecated;
        private String                   deprecationReason;

        protected Builder() {
            arguments = new ArrayList<>();
        }

        public FieldDefinition build() {
            List<InputValue> arguments = this.arguments.stream().map(builder -> builder.build()).collect(Collectors.toList());
            return new FieldDefinition(name, description, arguments, type.name(), isDeprecated, deprecationReason);
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

        public TypeDefinition.Builder type() {
            return type;
        }

        public Builder type(TypeDefinition.Builder type) {
            this.type = type;
            return this;
        }

        public Boolean isDeprecated() {
            return isDeprecated;
        }

        public Builder isDeprecated(Boolean isDeprecated) {
            this.isDeprecated = isDeprecated;
            return this;
        }

        public String deprecationReason() {
            return deprecationReason;
        }

        public Builder deprecationReason(String deprecationReason) {
            this.deprecationReason = deprecationReason;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Field [arguments=" + arguments + ", deprecationReason=" + deprecationReason + ", description=" + description + ", isDeprecated=" + isDeprecated
                + ", name=" + name + ", type=" + type + "]";
    }
}
