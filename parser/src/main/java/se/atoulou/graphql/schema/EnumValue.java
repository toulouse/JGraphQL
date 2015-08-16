package se.atoulou.graphql.schema;

import se.atoulou.graphql.common.NotNull;

public class EnumValue {
    private final @NotNull String  name;
    private final String           description;
    private final @NotNull Boolean isDeprecated;
    private final String           deprecationReason;

    public static Builder builder() {
        return new Builder();
    }

    protected EnumValue(@NotNull String name, String description, @NotNull Boolean isDeprecated, String deprecationReason) {
        super();
        this.name = name;
        this.description = description;
        this.isDeprecated = isDeprecated;
        this.deprecationReason = deprecationReason;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isDeprecated() {
        return isDeprecated;
    }

    public String getDeprecationReason() {
        return deprecationReason;
    }

    public static class Builder {
        private String  name;
        private String  description;
        private Boolean isDeprecated;
        private String  deprecationReason;

        protected Builder() {
        }

        public EnumValue build() {
            return new EnumValue(name, description, isDeprecated, deprecationReason);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder isDeprecated(Boolean isDeprecated) {
            this.isDeprecated = isDeprecated;
            return this;
        }

        public Builder deprecationReason(String deprecationReason) {
            this.deprecationReason = deprecationReason;
            return this;
        }
    }

    @Override
    public String toString() {
        return "EnumValue [deprecationReason=" + deprecationReason + ", description=" + description + ", isDeprecated=" + isDeprecated + ", name=" + name + "]";
    }
}
