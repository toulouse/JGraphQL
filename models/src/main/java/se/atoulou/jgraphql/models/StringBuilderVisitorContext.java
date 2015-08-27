package se.atoulou.jgraphql.models;

import se.atoulou.jgraphql.models.transform.VisitorContext;

public class StringBuilderVisitorContext extends VisitorContext<StringBuilderVisitorContext> {
    private final String newline;
    private final String tab;

    private final StringBuilder stringBuilder;
    private int                 tabs;
    private boolean             newlinesEnabled;

    public StringBuilderVisitorContext(String newline, String tab) {
        this.stringBuilder = new StringBuilder();
        this.tabs = 0;

        this.newline = newline;
        this.tab = tab;
        this.newlinesEnabled = true;
    }

    public StringBuilderVisitorContext(String tab) {
        this("\n", tab);
    }

    public StringBuilderVisitorContext() {
        this("\n", "  ");
    }

    public final String getNewlin() {
        return newline;
    }

    public final String getTab() {
        return tab;
    }

    public final StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public final int getTabs() {
        return tabs;
    }

    public final void setNewlinesEnabled(boolean enabled) {
        this.newlinesEnabled = enabled;
    }

    public final boolean getNewlinesEnabled() {
        return newlinesEnabled;
    }

    public final void indent() {
        tabs++;
    }

    public final void dedent() {
        tabs--;
    }

    public final void append(char c) {
        stringBuilder.append(c);
    }

    public final void append(char[] str) {
        stringBuilder.append(str);
    }

    public final void append(String str) {
        stringBuilder.append(str);
    }

    public final void append(CharSequence s) {
        stringBuilder.append(s);
    }

    public final void appendNewline() {
        if (newlinesEnabled) {
            stringBuilder.append(newline);
        }
    }

    public final void appendNewlines(int count) {
        if (newlinesEnabled) {
            for (int i = 0; i < count; i++) {
                stringBuilder.append(newline);
            }
        }
    }

    public final void appendTabs() {
        appendTabs(0);
    }

    public final void appendTabs(int offset) {
        int spaceCount = tabs + offset;
        for (int i = 0; i < spaceCount; i++) {
            stringBuilder.append(tab);
        }
    }
}
