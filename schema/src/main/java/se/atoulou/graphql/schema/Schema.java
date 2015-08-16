package se.atoulou.graphql.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import se.atoulou.graphql.annotations.NotNull;

public class Schema {
    private final @NotNull List<@NotNull Type>      types;
    private final @NotNull Type                     queryType;
    private final Type                              mutationType;
    private final @NotNull List<@NotNull Directive> directives;

    public static Builder builder() {
        return new Builder();
    }

    protected Schema(@NotNull List<@NotNull Type> types, @NotNull Type queryType, Type mutationType, @NotNull List<@NotNull Directive> directives) {
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
        private List<Type.Builder>      types;
        private Type                    queryType;
        private Type                    mutationType;
        private List<Directive.Builder> directives;

        protected Builder() {
            types = new ArrayList<>();
            directives = new ArrayList<>();
        }

        public Schema build() {
            List<Type> types = this.types.stream().map(builder -> builder.build()).collect(Collectors.toList());
            List<Directive> directives = this.directives.stream().map(builder -> builder.build()).collect(Collectors.toList());
            return new Schema(types, queryType, mutationType, directives);
        }

        public List<Type.Builder> types() {
            return types;
        }

        public Builder types(List<Type.Builder> types) {
            this.types = types;
            return this;
        }

        public Type queryType() {
            return queryType;
        }

        public Builder queryType(Type queryType) {
            this.queryType = queryType;
            return this;
        }

        public Type mutationType() {
            return mutationType;
        }

        public Builder mutationType(Type mutationType) {
            this.mutationType = mutationType;
            return this;
        }

        public List<Directive.Builder> directives() {
            return directives;
        }

        public Builder directives(List<Directive.Builder> directives) {
            this.directives = directives;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Schema [directives=" + directives + ", mutationType=" + mutationType + ", queryType=" + queryType + ", types=" + types + "]";
    }
}
