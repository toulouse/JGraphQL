package se.atoulou.jgraphql.models.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Document {
    private final List<OperationDefinition> operations;
    private final List<FragmentDefinition>  fragments;
    private final List<TypeDefinition>      types;

    public static Builder builder() {
        return new Builder();
    }

    protected Document(List<OperationDefinition> operations, List<FragmentDefinition> fragments, List<TypeDefinition> types) {
        this.operations = operations;
        this.fragments = fragments;
        this.types = types;
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

    public static class Builder {
        private List<OperationDefinition.Builder> operations;
        private List<FragmentDefinition.Builder>  fragments;
        private List<TypeDefinition.Builder>      types;

        protected Builder() {
            operations = new ArrayList<>();
            fragments = new ArrayList<>();
        }

        public Document build() {
            List<OperationDefinition> operations = this.operations.stream().map(builder -> builder.build()).collect(Collectors.toList());
            List<FragmentDefinition> fragments = this.fragments.stream().map(builder -> builder.build()).collect(Collectors.toList());
            List<TypeDefinition> types = this.types.stream().map(builder -> builder.build()).collect(Collectors.toList());
            return new Document(operations, fragments, types);
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
    }

    @Override
    public String toString() {
        return "Document [operations=" + operations + ", fragments=" + fragments + ", types=" + types + "]";
    }
}
