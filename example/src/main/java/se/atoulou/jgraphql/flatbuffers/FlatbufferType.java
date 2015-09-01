package se.atoulou.jgraphql.flatbuffers;

import java.util.ArrayList;
import java.util.List;

import se.atoulou.jgraphql.flatbuffers.FlatbufferSchema.FlatbufferField;

public class FlatbufferType {
    public static class FlatbufferTable extends FlatbufferType {
        private String                name;
        private List<FlatbufferField> fields;

        public FlatbufferTable() {
            fields = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<FlatbufferField> getFields() {
            return fields;
        }

        public void setFields(List<FlatbufferField> fields) {
            this.fields = fields;
        }
    }

    public static class FlatbufferEnum extends FlatbufferType {
        private String       name;
        private List<String> values;

        public FlatbufferEnum() {
            values = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }
    }
}
