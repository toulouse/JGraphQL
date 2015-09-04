package se.atoulou.jgraphql.models.transform;

import se.atoulou.jgraphql.models.query.Document;

public interface QueryDocumentMessageWriter<T> {
    public T writeQueryDocument(Document query);
}
