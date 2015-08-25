package se.atoulou.jgraphql.models;

import se.atoulou.jgraphql.models.schema.Schema;

public interface SchemaMessageWriter<T> {
    public T writeSchema(Schema schema);
}
