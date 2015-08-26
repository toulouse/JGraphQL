package se.atoulou.jgraphql.models;

import java.util.Stack;

public class VisitorContext<T extends VisitorContext<?>> {
    public static interface VisitPoint<T extends VisitorContext<?>> {
        public void visit(T context);
    }

    protected Stack<Integer> indices;

    protected Stack<VisitPoint<T>> prologues;
    protected Stack<VisitPoint<T>> itemSeparators;
    protected Stack<VisitPoint<T>> epilogues;

    public VisitorContext() {
        indices = new Stack<>();

        prologues = new Stack<>();
        prologues.push(null);

        itemSeparators = new Stack<>();
        itemSeparators.push(null);
        itemSeparators.push(null);

        epilogues = new Stack<>();
        epilogues.push(null);
    }

    public int currentLevel() {
        return indices.size();
    }

    public int currentIndex() {
        return indices.peek().intValue();
    }

    @SuppressWarnings("unchecked")
    public void incrementIndex() {
        int newIndex = indices.pop().intValue() + 1;
        indices.push(newIndex);

        if (newIndex < 1) {
            return;
        }

        // Get previous separator (since we always push one down)
        VisitPoint<T> itemSeparator = itemSeparators.elementAt(currentLevel());
        if (itemSeparator != null) {
            itemSeparator.visit((T) this);
        }
    }

    @SuppressWarnings("unchecked")
    public void enter() {
        prologues.push(null);
        itemSeparators.push(null);
        epilogues.push(null);

        VisitPoint<T> prologue = prologues.elementAt(currentLevel());
        if (prologue != null) {
            prologue.visit((T) this);
        }

        indices.push(-1);
    }

    @SuppressWarnings("unchecked")
    public void leave() {
        indices.pop();

        VisitPoint<T> epilogue = epilogues.elementAt(currentLevel());
        if (epilogue != null) {
            epilogue.visit((T) this);
        }

        setPrologue(null);
        setItemSeparator(null);
        setEpilogue(null);

        prologues.pop();
        itemSeparators.pop();
        epilogues.pop();
    }

    public void setPrologue(VisitPoint<T> prologue) {
        prologues.set(currentLevel(), prologue);
    }

    public void setItemSeparator(VisitPoint<T> itemSeparator) {
        itemSeparators.set(currentLevel() + 1, itemSeparator);
    }

    public void setEpilogue(VisitPoint<T> epilogue) {
        epilogues.set(currentLevel(), epilogue);
    }
}
