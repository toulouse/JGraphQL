package se.atoulou.jgraphql.flatbuffers;

import se.atoulou.jgraphql.models.transform.VisitorContext;

public class FlatbufferVisitorContext extends VisitorContext<FlatbufferVisitorContext> {

    private final FlatbufferSchema flatbufferSchema;

    protected FlatbufferVisitorContext(FlatbufferSchema flatbufferSchema) {
        this.flatbufferSchema = flatbufferSchema;
    }

    public FlatbufferSchema getFlatbufferSchema() {
        return flatbufferSchema;
    }
}
