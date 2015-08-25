package se.atoulou.jgraphql.models;

import se.atoulou.jgraphql.models.query.QueryDocument;

public interface QueryDocumentMessageWriter<T> {
    public T writeQueryDocument(QueryDocument query);
}
