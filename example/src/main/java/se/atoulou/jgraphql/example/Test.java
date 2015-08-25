package se.atoulou.jgraphql.example;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.atoulou.jgraphql.models.PrettyPrinter;
import se.atoulou.jgraphql.models.query.QueryDocument;
import se.atoulou.jgraphql.parser.ParseHelper;
import se.atoulou.jgraphql.parser.antlr.GraphQLQueryLexer;

public class Test {
    private static final Logger LOG = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws IOException {
        InputStream in = Test.class.getResourceAsStream("/introspectionQuery.graphqlQuery");

        ANTLRInputStream input = new ANTLRInputStream(in);
        GraphQLQueryLexer lexer = new GraphQLQueryLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        tokenStream.fill();
        for (Token token : tokenStream.getTokens()) {
            LOG.trace("token type: {} str: {}", GraphQLQueryLexer.VOCABULARY.getSymbolicName(token.getType()), token);
        }

        in = Test.class.getResourceAsStream("/introspectionQuery.graphqlQuery");
        QueryDocument document = ParseHelper.parseDocument(in);
        LOG.trace("Document: \n{}", PrettyPrinter.documentPrinter().print(document));
    }
}
