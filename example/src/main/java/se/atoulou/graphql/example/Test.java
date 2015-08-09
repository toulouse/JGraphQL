package se.atoulou.graphql.example;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.Trees;

import se.atoulou.parser.antlr.GraphQLSchemaLexer;
import se.atoulou.parser.antlr.GraphQLSchemaParser;

public class Test {

    public static void main(String[] args) throws IOException {
        InputStream in = Test.class.getResourceAsStream("/starWarsSchema.graphql");
        ANTLRInputStream input = new ANTLRInputStream(in);
        GraphQLSchemaLexer lexer = new GraphQLSchemaLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        GraphQLSchemaParser parser = new GraphQLSchemaParser(tokenStream);

        System.out.println(Trees.toStringTree(parser.schemaDocument()));
    }
}
