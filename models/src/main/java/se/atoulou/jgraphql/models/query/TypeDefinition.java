package se.atoulou.jgraphql.models.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeDefinition {
    public static enum TypeKind {
        SCALAR,
        OBJECT,
        INTERFACE,
        UNION,
        ENUM,
        INPUT_OBJECT,
        LIST,
        NON_NULL,
    }

    private final TypeKind kind;
    private final String   name;
    private final String   description;

    public static Builder builder() {
        return new Builder();
    }

    protected TypeDefinition(TypeKind kind, String name, String description) {
        super();
        this.kind = kind;
        this.name = name;
        this.description = description;
    }

    public TypeKind getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static class EnumType extends TypeDefinition {
        private final List<EnumValue> enumValues;

        public EnumType(TypeKind kind, String name, String description, List<EnumValue> enumValues) {
            super(kind, name, description);
            this.enumValues = enumValues;
        }

        public List<EnumValue> getEnumValues() {
            return enumValues;
        }

        @Override
        public String toString() {
            return "EnumType [enumValues=" + enumValues + ", getDescription()=" + getDescription() + ", getKind()=" + getKind() + ", getName()=" + getName()
                    + "]";
        }
    }

    public static class InterfaceType extends TypeDefinition {
        private final List<FieldDefinition> fields;
        private final List<String>          possibleTypes;

        public InterfaceType(TypeKind kind, String name, String description, List<FieldDefinition> fields, List<String> possibleTypes) {
            super(kind, name, description);
            this.fields = fields;
            this.possibleTypes = possibleTypes;
        }

        public List<FieldDefinition> getFields() {
            return fields;
        }

        public List<String> getPossibleTypes() {
            return possibleTypes;
        }

        @Override
        public String toString() {
            return "InterfaceType [fields=" + fields + ", possibleTypes=" + possibleTypes + ", getDescription()=" + getDescription() + ", getKind()="
                    + getKind() + ", getName()=" + getName() + "]";
        }
    }

    public static class InputObjectType extends TypeDefinition {
        private final List<InputValue> inputFields;

        public InputObjectType(TypeKind kind, String name, String description, List<InputValue> inputFields) {
            super(kind, name, description);
            this.inputFields = inputFields;
        }

        public List<InputValue> getInputFields() {
            return inputFields;
        }

        @Override
        public String toString() {
            return "InputObjectType [inputFields=" + inputFields + ", getDescription()=" + getDescription() + ", getKind()=" + getKind() + ", getName()="
                    + getName() + "]";
        }
    }

    public static class ListType extends TypeDefinition {
        private final TypeDefinition ofType;

        public ListType(TypeKind kind, String name, String description, TypeDefinition ofType) {
            super(kind, name, description);
            this.ofType = ofType;
        }

        public TypeDefinition getOfType() {
            return ofType;
        }

        @Override
        public String toString() {
            return "ListType [ofType=" + ofType + ", getDescription()=" + getDescription() + ", getKind()=" + getKind() + ", getName()=" + getName() + "]";
        }
    }

    public static class NonNullType extends TypeDefinition {
        private final TypeDefinition ofType;

        public NonNullType(TypeKind kind, String name, String description, TypeDefinition ofType) {
            super(kind, name, description);
            this.ofType = ofType;
        }

        public TypeDefinition getOfType() {
            return ofType;
        }

        @Override
        public String toString() {
            return "NonNullType [ofType=" + ofType + ", getDescription()=" + getDescription() + ", getKind()=" + getKind() + ", getName()=" + getName() + "]";
        }
    }

    public static class ObjectType extends TypeDefinition {
        private final List<FieldDefinition> fields;
        private final List<String>          interfaces;

        public ObjectType(TypeKind kind, String name, String description, List<FieldDefinition> fields, List<String> interfaces) {
            super(kind, name, description);
            this.fields = fields;
            this.interfaces = interfaces;
        }

        public List<FieldDefinition> getFields() {
            return fields;
        }

        public List<String> getInterfaces() {
            return interfaces;
        }

        @Override
        public String toString() {
            return "ObjectType [fields=" + fields + ", interfaces=" + interfaces + ", getDescription()=" + getDescription() + ", getKind()=" + getKind()
                    + ", getName()=" + getName() + "]";
        }
    }

    public static class ScalarType extends TypeDefinition {
        public ScalarType(TypeKind kind, String name, String description) {
            super(kind, name, description);
        }

        @Override
        public String toString() {
            return "ScalarType [getDescription()=" + getDescription() + ", getKind()=" + getKind() + ", getName()=" + getName() + "]";
        }
    }

    public static class UnionType extends TypeDefinition {
        private final List<TypeDefinition> possibleTypes;

        public UnionType(TypeKind kind, String name, String description, List<TypeDefinition> possibleTypes) {
            super(kind, name, description);
            this.possibleTypes = possibleTypes;
        }

        public List<TypeDefinition> getPossibleTypes() {
            return possibleTypes;
        }

        @Override
        public String toString() {
            return "UnionType [possibleTypes=" + possibleTypes + ", getDescription()=" + getDescription() + ", getKind()=" + getKind() + ", getName()="
                    + getName() + "]";
        }
    }

    public static class Builder {
        private TypeDefinition cachedBuild;

        private TypeKind                      kind;
        private String                        name;
        private String                        description;
        private List<FieldDefinition.Builder> fields;
        private List<TypeDefinition.Builder>  interfaces;
        private List<TypeDefinition.Builder>  possibleTypes;
        private List<EnumValue.Builder>       enumValues;
        private TypeDefinition.Builder        ofType;
        private List<InputValue.Builder>      inputFields;

        protected Builder() {
            fields = new ArrayList<>();
            interfaces = new ArrayList<>();
            possibleTypes = new ArrayList<>();
            enumValues = new ArrayList<>();
            inputFields = new ArrayList<>();
        }

        public TypeDefinition build() {
            if (cachedBuild != null) {
                return cachedBuild;
            }

            switch (kind) {
            case SCALAR:
                cachedBuild = new ScalarType(kind, name, description);
                return cachedBuild;
            case OBJECT: {
                List<FieldDefinition> fields = this.fields.stream().map(builder -> builder.build()).collect(Collectors.toList());
                List<String> interfaces = this.interfaces.stream().map(builder -> builder.name()).collect(Collectors.toList());
                cachedBuild = new ObjectType(kind, name, description, fields, interfaces);
                return cachedBuild;
            }
            case INTERFACE: {
                List<FieldDefinition> fields = this.fields.stream().map(builder -> builder.build()).collect(Collectors.toList());
                List<String> possibleTypes = this.possibleTypes.stream().map(builder -> builder.name()).collect(Collectors.toList());
                cachedBuild = new InterfaceType(kind, name, description, fields, possibleTypes);
                return cachedBuild;
            }
            case UNION: {
                List<TypeDefinition> possibleTypes = this.possibleTypes.stream().map(builder -> builder.build()).collect(Collectors.toList());
                cachedBuild = new UnionType(kind, name, description, possibleTypes);
                return cachedBuild;
            }
            case ENUM: {
                List<EnumValue> enumValues = this.enumValues.stream().map(builder -> builder.build()).collect(Collectors.toList());
                cachedBuild = new EnumType(kind, name, description, enumValues);
                return cachedBuild;
            }
            case INPUT_OBJECT: {
                List<InputValue> inputFields = this.inputFields.stream().map(builder -> builder.build()).collect(Collectors.toList());
                cachedBuild = new InputObjectType(kind, name, description, inputFields);
                return cachedBuild;
            }
            case LIST: {
                TypeDefinition ofType = this.ofType.build();
                cachedBuild = new ListType(kind, name, description, ofType);
                return cachedBuild;
            }
            case NON_NULL: {
                TypeDefinition ofType = this.ofType.build();
                cachedBuild = new NonNullType(kind, name, description, ofType);
                return cachedBuild;
            }
            default:
                assert false;
                return null;
            }
        }

        public TypeKind kind() {
            return kind;
        }

        public Builder kind(TypeKind kind) {
            assert cachedBuild == null;
            this.kind = kind;
            return this;
        }

        public String name() {
            return name;
        }

        public Builder name(String name) {
            assert cachedBuild == null;
            this.name = name;
            return this;
        }

        public String description() {
            return description;
        }

        public Builder description(String description) {
            assert cachedBuild == null;
            this.description = description;
            return this;
        }

        public List<FieldDefinition.Builder> fields() {
            return fields;
        }

        public Builder fields(List<FieldDefinition.Builder> fields) {
            assert cachedBuild == null;
            this.fields = fields;
            return this;
        }

        public List<TypeDefinition.Builder> interfaces() {
            return interfaces;
        }

        public Builder interfaces(List<TypeDefinition.Builder> interfaces) {
            assert cachedBuild == null;
            this.interfaces = interfaces;
            return this;
        }

        public List<TypeDefinition.Builder> possibleTypes() {
            return possibleTypes;
        }

        public Builder possibleTypes(List<TypeDefinition.Builder> possibleTypes) {
            assert cachedBuild == null;
            this.possibleTypes = possibleTypes;
            return this;
        }

        public List<EnumValue.Builder> enumValues() {
            return enumValues;
        }

        public Builder enumValues(List<EnumValue.Builder> enumValues) {
            assert cachedBuild == null;
            this.enumValues = enumValues;
            return this;
        }

        public TypeDefinition.Builder ofType() {
            return ofType;
        }

        public Builder ofType(TypeDefinition.Builder ofType) {
            assert cachedBuild == null;
            this.ofType = ofType;
            return this;
        }

        public List<InputValue.Builder> inputFields() {
            return inputFields;
        }

        public Builder inputFields(List<InputValue.Builder> inputFields) {
            assert cachedBuild == null;
            this.inputFields = inputFields;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Type [description=" + description + ", kind=" + kind + ", name=" + name + "]";
    }
}
