package se.atoulou.jgraphql.models.query;

public class Value {
    private final String value;

    public static Builder builder() {
        return new Builder();
    }

    protected Value(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static class Builder {
        private String value;

        protected Builder() {
        }

        public Value build() {
            return new Value(value);
        }

        public String value() {
            return value;
        }

        public void value(String value) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "Value [value=" + value + "]";
    }
}
