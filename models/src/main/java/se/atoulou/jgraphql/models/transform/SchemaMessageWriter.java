package se.atoulou.jgraphql.models.transform;

import se.atoulou.jgraphql.models.schema.Schema;

public interface SchemaMessageWriter<T> {
    public T writeSchema(Schema schema);
}
