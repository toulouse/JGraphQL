package se.atoulou.jgraphql.models.transform;

import java.util.Stack;

public class VisitorContext<T extends VisitorContext<?>> {
    protected Stack<Integer> indices;

    public VisitorContext() {
        indices = new Stack<>();
    }

    public int currentLevel() {
        return indices.size();
    }

    public int currentIndex() {
        return indices.peek().intValue();
    }

    public void incrementIndex() {
        int newIndex = indices.pop().intValue() + 1;
        indices.push(newIndex);

        if (newIndex < 1) {
            return;
        }
    }

    public void enter() {
        indices.push(-1);
    }

    public void leave() {
        indices.pop();
    }
}
