package se.atoulou.graphql.schema;

import java.util.List;
import java.util.stream.Collectors;

import se.atoulou.graphql.common.NotNull;

public class Type {
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

    private final @NotNull TypeKind kind;
    private final String            name;
    private final String            description;

    public static Builder builder() {
        return new Builder();
    }

    protected Type(TypeKind kind, String name, String description) {
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

    public static class EnumType extends Type {
        private final List<@NotNull EnumValue> enumValues;

        public EnumType(TypeKind kind, String name, String description, List<@NotNull EnumValue> enumValues) {
            super(kind, name, description);
            this.enumValues = enumValues;
        }

        public List<@NotNull EnumValue> getEnumValues() {
            return enumValues;
        }
    }

    public static class InterfaceType extends Type {
        private final List<@NotNull Field> fields;
        private final List<@NotNull Type>  possibleTypes;

        public InterfaceType(TypeKind kind, String name, String description, List<@NotNull Field> fields, List<@NotNull Type> possibleTypes) {
            super(kind, name, description);
            this.fields = fields;
            this.possibleTypes = possibleTypes;
        }

        public List<@NotNull Field> getFields() {
            return fields;
        }

        public List<@NotNull Type> getPossibleTypes() {
            return possibleTypes;
        }
    }

    public static class InputObjectType extends Type {
        private final List<@NotNull InputValue> inputFields;

        public InputObjectType(TypeKind kind, String name, String description, List<@NotNull InputValue> inputFields) {
            super(kind, name, description);
            this.inputFields = inputFields;
        }

        public List<@NotNull InputValue> getInputFields() {
            return inputFields;
        }
    }

    public static class ListType extends Type {
        private final Type ofType;

        public ListType(TypeKind kind, String name, String description, Type ofType) {
            super(kind, name, description);
            this.ofType = ofType;
        }

        public Type getOfType() {
            return ofType;
        }
    }

    public static class NonNullType extends Type {
        private final Type ofType;

        public NonNullType(TypeKind kind, String name, String description, Type ofType) {
            super(kind, name, description);
            this.ofType = ofType;
        }

        public Type getOfType() {
            return ofType;
        }
    }

    public static class ObjectType extends Type {
        private final List<@NotNull Field> fields;
        private final List<@NotNull Type>  interfaces;

        public ObjectType(TypeKind kind, String name, String description, List<@NotNull Field> fields, List<@NotNull Type> interfaces) {
            super(kind, name, description);
            this.fields = fields;
            this.interfaces = interfaces;
        }

        public List<@NotNull Field> getFields() {
            return fields;
        }

        public List<@NotNull Type> getInterfaces() {
            return interfaces;
        }
    }

    public static class ScalarType extends Type {
        public ScalarType(TypeKind kind, String name, String description) {
            super(kind, name, description);
        }
    }

    public static class UnionType extends Type {
        private final List<@NotNull Type> possibleTypes;

        public UnionType(TypeKind kind, String name, String description, List<@NotNull Type> possibleTypes) {
            super(kind, name, description);
            this.possibleTypes = possibleTypes;
        }

        public List<@NotNull Type> getPossibleTypes() {
            return possibleTypes;
        }
    }

    public static class Builder {
        private TypeKind                 kind;
        private String                   name;
        private String                   description;
        private List<Field.Builder>      fields;
        private List<Type.Builder>       interfaces;
        private List<Type.Builder>       possibleTypes;
        private List<EnumValue.Builder>  enumValues;
        private Type                     ofType;
        private List<InputValue.Builder> inputFields;

        protected Builder() {
        }

        public Type build() {
            switch (kind) {
            case SCALAR:
                return new ScalarType(kind, name, description);
            case OBJECT: {
                List<Field> fields = this.fields.stream().map(builder -> builder.build()).collect(Collectors.toList());
                List<Type> interfaces = this.interfaces.stream().map(builder -> builder.build()).collect(Collectors.toList());
                return new ObjectType(kind, name, description, fields, interfaces);
            }
            case INTERFACE: {
                List<Field> fields = this.fields.stream().map(builder -> builder.build()).collect(Collectors.toList());
                List<Type> possibleTypes = this.possibleTypes.stream().map(builder -> builder.build()).collect(Collectors.toList());
                return new InterfaceType(kind, name, description, fields, possibleTypes);
            }
            case UNION: {
                List<Type> possibleTypes = this.possibleTypes.stream().map(builder -> builder.build()).collect(Collectors.toList());
                return new UnionType(kind, name, description, possibleTypes);

            }
            case ENUM: {
                List<EnumValue> enumValues = this.enumValues.stream().map(builder -> builder.build()).collect(Collectors.toList());
                return new EnumType(kind, name, description, enumValues);
            }
            case INPUT_OBJECT: {
                List<InputValue> inputFields = this.inputFields.stream().map(builder -> builder.build()).collect(Collectors.toList());
                return new InputObjectType(kind, name, description, inputFields);
            }
            case LIST: {
                return new ListType(kind, name, description, ofType);
            }
            case NON_NULL: {
                return new NonNullType(kind, name, description, ofType);
            }
            default:
                assert false;
                return null;
            }
        }

        public Builder setKind(TypeKind kind) {
            this.kind = kind;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder fields(List<Field.Builder> fields) {
            this.fields = fields;
            return this;
        }

        public Builder interfaces(List<Type.Builder> interfaces) {
            this.interfaces = interfaces;
            return this;
        }

        public Builder possibleTypes(List<Type.Builder> possibleTypes) {
            this.possibleTypes = possibleTypes;
            return this;
        }

        public Builder enumValues(List<EnumValue.Builder> enumValues) {
            this.enumValues = enumValues;
            return this;
        }
    }
}
