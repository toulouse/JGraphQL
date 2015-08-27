package se.atoulou.jgraphql.models.transform;

import se.atoulou.jgraphql.models.query.QueryDocument;

public interface QueryDocumentMessageReader<T> {
    public QueryDocument readQueryDocument(T message);
}
