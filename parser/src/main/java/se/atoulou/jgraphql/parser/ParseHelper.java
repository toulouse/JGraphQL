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

public class ParseHelper {
    public Schema parseSchema(char[] data, int numberOfActualCharsInArray) {
        return parseSchema(new ANTLRInputStream(data, numberOfActualCharsInArray));
    }

    public Schema parseSchema(String string) {
        return parseSchema(new ANTLRInputStream(string));
    }

    public Schema parseSchema(Reader r) throws IOException {
        return parseSchema(new ANTLRInputStream(r));
    }

    public Schema parseSchema(InputStream in) throws IOException {
        return parseSchema(new ANTLRInputStream(in));
    }

    private Schema parseSchema(ANTLRInputStream input) {
        GraphQLSchemaLexer lexer = new GraphQLSchemaLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        GraphQLSchemaParser parser = new GraphQLSchemaParser(tokenStream);

        GraphQLSchemaVisitor visitor = new GraphQLSchemaVisitor();
        visitor.visit(parser.schemaDocument());
        Schema.Builder schemaB = visitor.getSchemaBuilder();
        // TODO: validation
        return schemaB.build();
    }

    public Document parseDocument(char[] data, int numberOfActualCharsInArray) {
        return parseDocument(new ANTLRInputStream(data, numberOfActualCharsInArray));
    }

    public Document parseDocument(String string) {
        return parseDocument(new ANTLRInputStream(string));
    }

    public Document parseDocument(Reader r) throws IOException {
        return parseDocument(new ANTLRInputStream(r));
    }

    public Document parseDocument(InputStream in) throws IOException {
        return parseDocument(new ANTLRInputStream(in));
    }

    private Document parseDocument(ANTLRInputStream input) {
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
