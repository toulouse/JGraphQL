package se.atoulou.graphql.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import se.atoulou.graphql.parser.antlr.GraphQLSchemaLexer;
import se.atoulou.graphql.parser.antlr.GraphQLSchemaParser;
import se.atoulou.graphql.schema.Schema;

public class SchemaParser {
    public Schema parse(char[] data, int numberOfActualCharsInArray) {
        return parse(new ANTLRInputStream(data, numberOfActualCharsInArray));
    }

    public Schema parse(String string) {
        return parse(new ANTLRInputStream(string));
    }

    public Schema parse(Reader r) throws IOException {
        return parse(new ANTLRInputStream(r));
    }

    public Schema parse(InputStream in) throws IOException {
        return parse(new ANTLRInputStream(in));
    }

    private Schema parse(ANTLRInputStream input) {
        GraphQLSchemaLexer lexer = new GraphQLSchemaLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        GraphQLSchemaParser parser = new GraphQLSchemaParser(tokenStream);

        GraphQLSchemaVisitor listener = new GraphQLSchemaVisitor();
        listener.visit(parser.schemaDocument());
        Schema.Builder schemaB = listener.getSchemaBuilder();
        // do a possibleTypes sweep
        // TODO: validation
        return schemaB.build();
    }
}
