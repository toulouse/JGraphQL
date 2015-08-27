package se.atoulou.jgraphql.models.transform;

import se.atoulou.jgraphql.models.schema.Schema;

public interface SchemaMessageReader<T> {
    public Schema readSchema(T message);

}
