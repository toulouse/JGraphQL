package se.atoulou.jgraphql.parser;

import java.util.HashMap;
import java.util.Map;

import se.atoulou.jgraphql.schema.Type;
import se.atoulou.jgraphql.schema.Type.TypeKind;

public class TypeRegistry {
    protected Map<String, Type.Builder> types;

    public TypeRegistry() {
        types = new HashMap<>();
    }

    Type.Builder registerUsage(String typeName) {
        assert typeName != null;
        Type.Builder typeB = this.types.get(typeName);
        if (typeB == null) {
            typeB = Type.builder();
            typeB.name(typeName);
            this.types.put(typeName, typeB);
        }
        return typeB;
    }

    Type.Builder registerDeclaration(String typeName, TypeKind kind) {
        assert typeName != null;
        Type.Builder typeB = this.types.get(typeName);
        if (typeB == null) {
            typeB = Type.builder();
            this.types.put(typeName, typeB);
        }

        assert typeB.kind() == null || typeB.kind() == kind;
        typeB.name(typeName);
        typeB.kind(kind);

        return typeB;
    }
}
