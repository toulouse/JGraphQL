package se.atoulou.graphql.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.atoulou.graphql.parser.antlr.GraphQLSchemaLexer;
import se.atoulou.graphql.parser.antlr.GraphQLSchemaParser;
import se.atoulou.graphql.schema.Schema;

public class SchemaParser {

    private static final Logger LOG = LoggerFactory.getLogger(SchemaParser.class);

    public void parse(char[] data, int numberOfActualCharsInArray) {
        parse(new ANTLRInputStream(data, numberOfActualCharsInArray));
    }

    public void parse(String string) {
        parse(new ANTLRInputStream(string));
    }

    public void parse(Reader r) throws IOException {
        parse(new ANTLRInputStream(r));
    }

    public void parse(InputStream in) throws IOException {
        parse(new ANTLRInputStream(in));
    }

    private void parse(ANTLRInputStream input) {
        GraphQLSchemaLexer lexer = new GraphQLSchemaLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        GraphQLSchemaParser parser = new GraphQLSchemaParser(tokenStream);

        Schema.Builder schemaBuilder = Schema.builder();
        ParseTreeWalker walker = new ParseTreeWalker();
        GraphQLSchemaListener listener = new GraphQLSchemaListener(schemaBuilder);
        walker.walk(listener, parser.schemaDocument());
    }
}
