package se.atoulou.jgraphql.models.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import se.atoulou.jgraphql.models.query.TypeDefinition;

public class Schema {
    private final List<TypeDefinition>      types;
    private final TypeDefinition            queryType;
    private final TypeDefinition            mutationType;
    private final List<Directive> directives;

    public static Builder builder() {
        return new Builder();
    }

    protected Schema(List<TypeDefinition> types, TypeDefinition queryType, TypeDefinition mutationType, List<Directive> directives) {
        super();
        this.types = types;
        this.queryType = queryType;
        this.mutationType = mutationType;
        this.directives = directives;
    }

    public List<TypeDefinition> getTypes() {
        return types;
    }

    public TypeDefinition getQueryType() {
        return queryType;
    }

    public TypeDefinition getMutationType() {
        return mutationType;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public static class Builder {
        private List<TypeDefinition.Builder>      types;
        private TypeDefinition                    queryType;
        private TypeDefinition                    mutationType;
        private List<Directive.Builder> directives;

        protected Builder() {
            types = new ArrayList<>();
            directives = new ArrayList<>();
        }

        public Schema build() {
            List<TypeDefinition> types = this.types.stream().map(builder -> builder.build()).collect(Collectors.toList());
            List<Directive> directives = this.directives.stream().map(builder -> builder.build()).collect(Collectors.toList());
            return new Schema(types, queryType, mutationType, directives);
        }

        public List<TypeDefinition.Builder> types() {
            return types;
        }

        public Builder types(List<TypeDefinition.Builder> types) {
            this.types = types;
            return this;
        }

        public TypeDefinition queryType() {
            return queryType;
        }

        public Builder queryType(TypeDefinition queryType) {
            this.queryType = queryType;
            return this;
        }

        public TypeDefinition mutationType() {
            return mutationType;
        }

        public Builder mutationType(TypeDefinition mutationType) {
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
