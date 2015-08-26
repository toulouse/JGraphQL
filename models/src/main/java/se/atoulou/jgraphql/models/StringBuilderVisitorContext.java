package se.atoulou.jgraphql.models;

public class StringBuilderVisitorContext extends VisitorContext<StringBuilderVisitorContext> {
    private final StringBuilder stringBuilder;
    private int                 tabs;

    public StringBuilderVisitorContext() {
        stringBuilder = new StringBuilder();
        tabs = 0;
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public int getTabs() {
        return tabs;
    }

    public void indent() {
        tabs++;
    }

    public void dedent() {
        tabs--;
    }
}
