package se.atoulou.jgraphql.models;

import se.atoulou.jgraphql.models.schema.Schema;

public interface SchemaMessageReader<T> {
    public Schema readSchema(T message);

}
