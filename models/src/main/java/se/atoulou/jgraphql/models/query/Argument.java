package se.atoulou.jgraphql.models.query;

public class Argument {
    private final String name;
    private final Value  value;

    public static Builder builder() {
        return new Builder();
    }

    protected Argument(String name, Value value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    public static class Builder {
        private String        name;
        private Value.Builder value;

        protected Builder() {
        }

        public Argument build() {
            return new Argument(name, value.build());
        }

        public String name() {
            return name;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Value.Builder value() {
            return value;
        }

        public Builder value(Value.Builder value) {
            this.value = value;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Argument [name=" + name + ", value=" + value + "]";
    }
}
