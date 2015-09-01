package se.atoulou.jgraphql.flatbuffers;

import se.atoulou.jgraphql.flatbuffers.FlatbufferType.FlatbufferTable;
import se.atoulou.jgraphql.models.schema.Field;
import se.atoulou.jgraphql.models.schema.Type.EnumType;
import se.atoulou.jgraphql.models.schema.Type.ObjectType;
import se.atoulou.jgraphql.models.transform.SchemaBaseVisitor;

public class FlatbufferVisitor extends SchemaBaseVisitor<FlatbufferVisitorContext> {

    @Override
    public void visitObject(ObjectType objectType, FlatbufferVisitorContext context) {
        FlatbufferTable table = new FlatbufferTable();
        table.setName(objectType.getName());
        for (Field field : objectType.getFields()) {
            field.getName().
        }

        context.getFlatbufferSchema().getTypes().add(table);
    }

    @Override
    public void visitEnum(EnumType enumType, FlatbufferVisitorContext context) {
        // TODO Auto-generated method stub
        super.visitEnum(enumType, context);
    }

}
