package se.atoulou.jgraphql.flatbuffers;

import java.util.ArrayList;
import java.util.List;

public class FlatbufferSchema {
    private String               namespace;
    private List<String>         attributes;
    private List<FlatbufferType> types;
    private String               rootType;

    public FlatbufferSchema() {
        attributes = new ArrayList<>();
        types = new ArrayList<>();
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public List<FlatbufferType> getTypes() {
        return types;
    }

    public void setTypes(List<FlatbufferType> types) {
        this.types = types;
    }

    public String getRootType() {
        return rootType;
    }

    public void setRootType(String rootType) {
        this.rootType = rootType;
    }

    public static class FlatbufferField {
        private String name;
        private String type;
        private String defaultValue;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }
    }
}
