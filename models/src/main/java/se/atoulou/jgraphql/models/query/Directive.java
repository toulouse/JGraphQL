package se.atoulou.jgraphql.models.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Directive {
    private final String         name;
    private final List<Argument> arguments;

    public static Builder builder() {
        return new Builder();
    }

    protected Directive(String name, List<Argument> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public static class Builder {
        private String                 name;
        private List<Argument.Builder> arguments;

        protected Builder() {
            arguments = new ArrayList<>();
        }

        public Directive build() {
            List<Argument> arguments = this.arguments.stream().map(builder -> builder.build()).collect(Collectors.toList());
            return new Directive(name, arguments);
        }

        public String name() {
            return name;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public List<Argument.Builder> arguments() {
            return arguments;
        }

        public Builder arguments(List<Argument.Builder> arguments) {
            this.arguments = arguments;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Directive [name=" + name + ", arguments=" + arguments + "]";
    }
}
