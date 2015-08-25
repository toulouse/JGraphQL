package se.atoulou.jgraphql.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import se.atoulou.jgraphql.models.query.Document;
import se.atoulou.jgraphql.models.schema.Schema;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryLexer;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryParser;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaLexer;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaParser;

public final class ParseHelper {
    public static Schema parseSchema(char[] data, int numberOfActualCharsInArray) {
        return parseSchema(new ANTLRInputStream(data, numberOfActualCharsInArray));
    }

    public static Schema parseSchema(String string) {
        return parseSchema(new ANTLRInputStream(string));
    }

    public static Schema parseSchema(Reader r) throws IOException {
        return parseSchema(new ANTLRInputStream(r));
    }

    public static Schema parseSchema(InputStream in) throws IOException {
        return parseSchema(new ANTLRInputStream(in));
    }

    private static Schema parseSchema(ANTLRInputStream input) {
        GraphQLSchemaLexer lexer = new GraphQLSchemaLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        GraphQLSchemaParser parser = new GraphQLSchemaParser(tokenStream);

        GraphQLSchemaVisitor visitor = new GraphQLSchemaVisitor();
        visitor.visit(parser.schemaDocument());
        Schema.Builder schemaB = visitor.getSchemaBuilder();
        // TODO: validation
        return schemaB.build();
    }

    public static Document parseDocument(char[] data, int numberOfActualCharsInArray) {
        return parseDocument(new ANTLRInputStream(data, numberOfActualCharsInArray));
    }

    public static Document parseDocument(String string) {
        return parseDocument(new ANTLRInputStream(string));
    }

    public static Document parseDocument(Reader r) throws IOException {
        return parseDocument(new ANTLRInputStream(r));
    }

    public static Document parseDocument(InputStream in) throws IOException {
        return parseDocument(new ANTLRInputStream(in));
    }

    private static Document parseDocument(ANTLRInputStream input) {
        GraphQLQueryLexer lexer = new GraphQLQueryLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        GraphQLQueryParser parser = new GraphQLQueryParser(tokenStream);

        GraphQLQueryVisitor visitor = new GraphQLQueryVisitor();
        visitor.visit(parser.document());
        Document.Builder documentB = visitor.getDocumentBuilder();
        // TODO: validation
        return documentB.build();
    }
}
