package se.atoulou.jgraphql.models.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Schema {
    private final List<Type>      types;
    private final Type            queryType;
    private final Type            mutationType;
    private final List<Directive> directives;

    public static Builder builder() {
        return new Builder();
    }

    protected Schema(List<Type> types, Type queryType, Type mutationType, List<Directive> directives) {
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
