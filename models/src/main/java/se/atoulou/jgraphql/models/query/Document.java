package se.atoulou.jgraphql.models.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import se.atoulou.jgraphql.models.schema.Directive;

public class Document {
    private final List<OperationDefinition> operations;
    private final List<FragmentDefinition>  fragments;
    private final List<TypeDefinition>      types;
    private final TypeDefinition            queryType;
    private final TypeDefinition            mutationType;
    private final List<Directive>           directives;

    public static Builder builder() {
        return new Builder();
    }

    protected Document(List<OperationDefinition> operations, List<FragmentDefinition> fragments, List<TypeDefinition> types, TypeDefinition queryType,
            TypeDefinition mutationType, List<Directive> directives) {
        this.operations = operations;
        this.fragments = fragments;
        this.types = types;
        this.queryType = queryType;
        this.mutationType = mutationType;
        this.directives = directives;
    }

    public List<OperationDefinition> getOperations() {
        return operations;
    }

    public List<FragmentDefinition> getFragments() {
        return fragments;
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
        private List<OperationDefinition.Builder> operations;
        private List<FragmentDefinition.Builder>  fragments;
        private List<TypeDefinition.Builder>      types;
        private TypeDefinition.Builder            queryType;
        private TypeDefinition.Builder            mutationType;
        private List<Directive.Builder>           directives;

        protected Builder() {
            operations = new ArrayList<>();
            fragments = new ArrayList<>();
        }

        public Document build() {
            List<OperationDefinition> operations = this.operations.stream().map(builder -> builder.build()).collect(Collectors.toList());
            List<FragmentDefinition> fragments = this.fragments.stream().map(builder -> builder.build()).collect(Collectors.toList());
            List<TypeDefinition> types = this.types.stream().map(builder -> builder.build()).collect(Collectors.toList());
            TypeDefinition queryType = this.queryType.build();
            TypeDefinition mutationType = this.mutationType.build();
            List<Directive> directives = this.directives.stream().map(builder -> builder.build()).collect(Collectors.toList());
            return new Document(operations, fragments, types, queryType, mutationType, directives);
        }

        public List<OperationDefinition.Builder> operations() {
            return operations;
        }

        public Builder operations(List<OperationDefinition.Builder> operations) {
            this.operations = operations;
            return this;
        }

        public List<FragmentDefinition.Builder> fragments() {
            return fragments;
        }

        public Builder fragments(List<FragmentDefinition.Builder> fragments) {
            this.fragments = fragments;
            return this;
        }

        public List<TypeDefinition.Builder> types() {
            return types;
        }

        public Builder types(List<TypeDefinition.Builder> types) {
            this.types = types;
            return this;
        }

        public TypeDefinition.Builder queryType() {
            return queryType;
        }

        public Builder queryType(TypeDefinition.Builder queryType) {
            this.queryType = queryType;
            return this;
        }

        public TypeDefinition.Builder mutationType() {
            return mutationType;
        }

        public Builder mutationType(TypeDefinition.Builder mutationType) {
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
        return "Document [operations=" + operations + ", fragments=" + fragments + ", types=" + types + ", queryType=" + queryType + ", mutationType="
                + mutationType + ", directives=" + directives + "]";
    }
}
