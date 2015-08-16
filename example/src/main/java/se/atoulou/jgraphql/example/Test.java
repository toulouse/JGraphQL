package se.atoulou.jgraphql.example;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.atoulou.jgraphql.parser.SchemaParser;
import se.atoulou.jgraphql.parser.antlr.GraphQLSchemaLexer;
import se.atoulou.jgraphql.schema.Schema;

public class Test {

    private static final Logger LOG = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws IOException {
        InputStream in = Test.class.getResourceAsStream("/testSchema.graphqlSchema");

        ANTLRInputStream input = new ANTLRInputStream(in);
        GraphQLSchemaLexer lexer = new GraphQLSchemaLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        tokenStream.fill();
        for (Token token : tokenStream.getTokens()) {
            LOG.debug("token type: {} str: {}", GraphQLSchemaLexer.VOCABULARY.getSymbolicName(token.getType()), token);
        }

        in = Test.class.getResourceAsStream("/testSchema.graphqlSchema");
        Schema schema = new SchemaParser().parse(in);
        LOG.debug("Schema: {}", schema);
    }
}
