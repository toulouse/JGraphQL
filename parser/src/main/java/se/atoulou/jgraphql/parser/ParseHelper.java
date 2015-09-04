package se.atoulou.jgraphql.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import se.atoulou.jgraphql.models.query.Document;
import se.atoulou.jgraphql.parser.antlr.GraphQLLexer;
import se.atoulou.jgraphql.parser.antlr.GraphQLParser;

public final class ParseHelper {
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
        GraphQLLexer lexer = new GraphQLLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        GraphQLParser parser = new GraphQLParser(tokenStream);

        GraphQLVisitor visitor = new GraphQLVisitor();
        visitor.visit(parser.document());
        Document.Builder documentB = visitor.getDocumentBuilder();
        // TODO: validation
        return documentB.build();
    }
}
