package se.atoulou.jgraphql.models.transform;

import se.atoulou.jgraphql.models.query.Document;

public interface QueryDocumentMessageReader<T> {
    public Document readQueryDocument(T message);
}
