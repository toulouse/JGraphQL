package se.atoulou.graphql.example;

import java.io.IOException;
import java.io.InputStream;

import se.atoulou.parser.SchemaParser;

public class Test {

    public static void main(String[] args) throws IOException {
        InputStream in = Test.class.getResourceAsStream("/schemaSchema.graphqlSchema");
        new SchemaParser().parse(in);
    }
}
