package se.atoulou.graphql.example;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.atoulou.graphql.parser.SchemaParser;

public class Test {

    private static final Logger LOG = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws IOException {
        InputStream in = Test.class.getResourceAsStream("/schemaSchema.graphqlSchema");


         new SchemaParser().parse(in);
    }
}
