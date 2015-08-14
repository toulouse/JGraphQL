package se.atoulou.graphql.schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.atoulou.graphql.common.NotNull;

public class Schema {
    private final @NotNull List<@NotNull Type>      types;
    private final @NotNull Type                     queryType;
    private final Type                              mutationType;
    private final @NotNull List<@NotNull Directive> directives;

    public static Builder builder() {
        return new Builder();
    }

    private Schema(@NotNull List<@NotNull Type> types, @NotNull Type queryType, Type mutationType, @NotNull List<@NotNull Directive> directives) {
        super();
        this.types = types;
        this.queryType = queryType;
        this.mutationType = mutationType;
        this.directives = directives;
    }

    public List<Type> getTypes() {
        return types;
    }

    public Type getQueryType() {
        return queryType;
    }

    public Type getMutationType() {
        return mutationType;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public static class Builder {
        private @NotNull List<@NotNull Type>      types;
        private @NotNull Type                     queryType;
        private Type                              mutationType;
        private @NotNull List<@NotNull Directive> directives;

        private Builder() {
            types = new ArrayList<>();
            directives = new ArrayList<>();
        }

        public Schema build() {
            return new Schema(types, queryType, mutationType, directives);
        }

        public Builder addType(Type type) {
            this.types.add(type);
            return this;
        }

        public Builder addTypes(Collection<Type> types) {
            this.types.addAll(types);
            return this;
        }

        public Builder setQueryType(Type queryType) {
            this.queryType = queryType;
            return this;
        }

        public Builder setMutationType(Type mutationType) {
            this.mutationType = mutationType;
            return this;
        }

        public Builder addDirective(Directive directive) {
            this.directives.add(directive);
            return this;
        }

        public Builder addDirectives(Collection<Directive> directives) {
            this.directives.addAll(directives);
            return this;
        }
    }
}
