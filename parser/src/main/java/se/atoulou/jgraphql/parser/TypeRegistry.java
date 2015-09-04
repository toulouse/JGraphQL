package se.atoulou.jgraphql.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import se.atoulou.jgraphql.models.query.TypeDefinition;
import se.atoulou.jgraphql.models.query.TypeDefinition.TypeKind;

public class TypeRegistry {
    protected Map<String, TypeDefinition.Builder> types;

    public TypeRegistry() {
        types = new HashMap<>();
    }

    TypeDefinition.Builder registerUsage(String typeName) {
        assert typeName != null;
        TypeDefinition.Builder typeB = this.types.get(typeName);
        if (typeB == null) {
            typeB = TypeDefinition.builder();
            typeB.name(typeName);
            this.types.put(typeName, typeB);
        }
        return typeB;
    }

    TypeDefinition.Builder registerDeclaration(String typeName, TypeKind kind) {
        assert typeName != null;
        TypeDefinition.Builder typeB = this.types.get(typeName);
        if (typeB == null) {
            typeB = TypeDefinition.builder();
            this.types.put(typeName, typeB);
        }

        assert typeB.kind() == null || typeB.kind() == kind;
        typeB.name(typeName);
        typeB.kind(kind);

        return typeB;
    }

    public void reconcilePossibleTypes() {
        List<TypeDefinition.Builder> objects = types.values().stream().filter(typeB -> typeB.kind() == TypeKind.OBJECT).collect(Collectors.toList());

        for (TypeDefinition.Builder typeB : objects) {
            List<TypeDefinition.Builder> implementedTypes = typeB.interfaces();
            for (TypeDefinition.Builder implementedType : implementedTypes) {
                assert implementedType.kind() == TypeKind.INTERFACE;
                if (!implementedType.possibleTypes().contains(typeB)) {
                    implementedType.possibleTypes().add(typeB);
                }
            }
        }
    }
}
