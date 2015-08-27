package se.atoulou.jgraphql.models.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QueryDocument {
    private final List<OperationDefinition> operations;
    private final List<FragmentDefinition>  fragments;

    public static Builder builder() {
        return new Builder();
    }

    protected QueryDocument(List<OperationDefinition> operations, List<FragmentDefinition> fragments) {
        this.operations = operations;
        this.fragments = fragments;
    }

    public List<OperationDefinition> getOperations() {
        return operations;
    }

    public List<FragmentDefinition> getFragments() {
        return fragments;
    }

    public static class Builder {
        private List<OperationDefinition.Builder> operations;
        private List<FragmentDefinition.Builder>  fragments;

        protected Builder() {
            operations = new ArrayList<>();
            fragments = new ArrayList<>();
        }

        public QueryDocument build() {
            List<OperationDefinition> operations = this.operations.stream().map(builder -> builder.build()).collect(Collectors.toList());
            List<FragmentDefinition> fragments = this.fragments.stream().map(builder -> builder.build()).collect(Collectors.toList());
            return new QueryDocument(operations, fragments);
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
    }

    @Override
    public String toString() {
        return "Document [operations=" + operations + ", fragments=" + fragments + "]";
    }
}
