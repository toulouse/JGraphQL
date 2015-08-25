package se.atoulou.jgraphql.models;

public class StringBuilderVisitorContext extends VisitorContext<StringBuilderVisitorContext> {
    private final StringBuilder stringBuilder;

    public StringBuilderVisitorContext() {
        stringBuilder = new StringBuilder();
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }
}
