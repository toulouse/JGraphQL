package se.atoulou.jgraphql.models.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Directive {
    private final String         name;
    private final List<Argument> args;

    public static Builder builder() {
        return new Builder();
    }

    protected Directive(String name, List<Argument> args) {
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public List<Argument> getArgs() {
        return args;
    }

    public static class Builder {
        private String                 name;
        private List<Argument.Builder> args;

        protected Builder() {
            args = new ArrayList<>();
        }

        public Directive build() {
            List<Argument> args = this.args.stream().map(builder -> builder.build()).collect(Collectors.toList());
            return new Directive(name, args);
        }

        public String name() {
            return name;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public List<Argument.Builder> args() {
            return args;
        }

        public Builder args(List<Argument.Builder> args) {
            this.args = args;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Directive [name=" + name + ", args=" + args + "]";
    }
}
