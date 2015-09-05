package se.atoulou.jgraphql.models.transform;

import se.atoulou.jgraphql.models.Document;

public interface DocumentMessageReader<T> {
    public Document readDocument(T message);

}
