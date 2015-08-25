package se.atoulou.jgraphql.models;

import java.util.Stack;

public class VisitorContext<T extends VisitorContext<?>> {
    public static interface Punctuator<T extends VisitorContext<?>> {
        public void punctuate(T context);
    }

    protected Stack<Integer>       indices;
    protected Stack<Punctuator<T>> punctuators;

    public VisitorContext() {
        indices = new Stack<>();
        indices.push(-1);

        punctuators = new Stack<>();
        punctuators.push(null);
    }

    public int currentLevel() {
        return indices.size() - 2;
    }

    public int currentIndex() {
        return indices.peek().intValue();
    }

    @SuppressWarnings("unchecked")
    public void incrementIndex() {
        int newIndex = indices.pop().intValue() + 1;
        indices.push(newIndex);

        if (newIndex <= 1) {
            return;
        }

        // Get second to last punctuator, since null is always pushed on entering.
        int punctuatorIndex = punctuators.size() - 2;
        Punctuator<T> punctuator = punctuators.elementAt(punctuatorIndex);
        if (punctuator != null) {
            punctuator.punctuate((T) this);
        }
    }

    public void enter() {
        indices.push(0);
        punctuators.push(null);
    }

    public void leave() {
        indices.pop();
        punctuators.pop();
    }

    // Set the punctuator for the *next* level down
    public void setPunctuator(Punctuator<T> punctuator) {
        punctuators.pop();
        punctuators.push(punctuator);
    }

}
