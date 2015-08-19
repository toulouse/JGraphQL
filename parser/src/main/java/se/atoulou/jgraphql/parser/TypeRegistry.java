package se.atoulou.jgraphql.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import se.atoulou.jgraphql.schema.Type;
import se.atoulou.jgraphql.schema.Type.Builder;
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

    public void reconcilePossibleTypes() {
        List<Type.Builder> objects = types.values().stream().filter(typeB -> typeB.kind() == TypeKind.OBJECT).collect(Collectors.toList());

        for (Type.Builder typeB : objects) {
            List<Type.Builder> implementedTypes = typeB.interfaces();
            for (Builder implementedType : implementedTypes) {
                assert implementedType.kind() == TypeKind.INTERFACE;
                if (!implementedType.possibleTypes().contains(typeB)) {
                    implementedType.possibleTypes().add(typeB);
                }
            }
        }
    }
}
