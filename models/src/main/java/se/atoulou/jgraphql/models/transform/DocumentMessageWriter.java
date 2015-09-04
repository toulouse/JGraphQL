package se.atoulou.jgraphql.models.transform;

import se.atoulou.jgraphql.models.query.Document;

public interface DocumentMessageWriter<T> {
    public T writeDocument(Document document);
}
