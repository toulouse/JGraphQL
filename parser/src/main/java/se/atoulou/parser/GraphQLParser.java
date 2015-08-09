package se.atoulou.parser;

import java.security.cert.PKIXRevocationChecker.Option;

import javax.lang.model.element.VariableElement;

import org.parboiled.BaseParser;
import org.parboiled.Rule;

public class GraphQLParser extends BaseParser<Object> {

    /**
     * @category ignored_tokens
     */
    Rule Ignored() {
        return FirstOf(WhiteSpace(), LineTerminator(), Comment(), ',');
    }

    /**
     * @category ignored_tokens
     */
    Rule WhiteSpace() {
        final char characters[] = { '\t', //
                0x000B /* \v */, //
                0x000C /* form feed */, //
                ' ', //
                0x00A0 /* non-breaking space */
        };
        return AnyOf(characters);
    }

    /**
     * @category ignored_tokens
     */
    Rule LineTerminator() {
        final char characters[] = { '\n', '\r', 0x2028 /* line separator */,
                0x2029 /* paragraph separator */ };
        return AnyOf(characters);
    }

    /**
     * @category ignored_tokens
     */
    Rule Comment() {
        return Sequence('#', ZeroOrMore(TestNot(LineTerminator())));
    }

    /**
     * @category lexical_tokens
     */
    Rule Token() {
        return FirstOf(Punctuator(), Name(), IntValue(), FloatValue(), StringValue());
    }

    /**
     * @category lexical_tokens
     */
    Rule Punctuator() {
        return FirstOf(AnyOf("!$():=@[]{}"), "...");
    }

    /**
     * @category lexical_tokens
     */
    Rule Name() {
        final Rule firstChar = FirstOf('_', CharRange('A', 'Z'), CharRange('a', 'z'));
        final Rule otherChars = FirstOf('_', CharRange('0', '9'), CharRange('A', 'Z'), CharRange('a', 'z'));
        return Sequence(firstChar, ZeroOrMore(otherChars));
    }

    /**
     * @category lexical_tokens
     */
    Rule IntValue() {
        final Rule optNegative = Optional('-');
        final Rule nonZeroDigit = CharRange('1', '9');
        final Rule digit = CharRange('0', '9');

        final Rule zero = Sequence(optNegative, '0');
        final Rule number = Sequence(optNegative, nonZeroDigit, ZeroOrMore(digit));
        return FirstOf(zero, number);
    }

    /**
     * @category lexical_tokens
     */
    Rule FloatValue() {
        final Rule digit = CharRange('0', '9');
        final Rule sign = AnyOf("+_");
        final Rule exponentIndicator = AnyOf("eE");

        final Rule fractionalPart = Sequence('.', OneOrMore(digit));
        final Rule exponentPart = Sequence(exponentIndicator, Optional(sign), OneOrMore(digit));
        return FirstOf(Sequence(IntValue(), fractionalPart), //
                Sequence(IntValue(), exponentPart), //
                Sequence(IntValue(), fractionalPart, exponentPart) //
        );
    }

    /**
     * @category lexical_tokens
     */
    Rule StringValue() {
        final Rule emptyQuote = String("\"\"");
        final Rule notQuoteSlash = TestNot(AnyOf("\"\\")); // " or \
        final Rule hexDigit = FirstOf(CharRange('0', '9'), CharRange('a', 'f'), CharRange('A', 'F'));
        final Rule escapedUnicode = Sequence('u', hexDigit, hexDigit, hexDigit, hexDigit);
        final Rule escapedCharacter = AnyOf("\"\\/bfnrt"); // " or \ or one of
                                                           // /bfnrt

        final Rule stringCharacter = FirstOf(notQuoteSlash, //
                Sequence('\\', escapedUnicode), //
                Sequence('\\', escapedCharacter));
        return FirstOf(emptyQuote, Sequence('"', OneOrMore(stringCharacter), '"'));
    }

    /**
     * @category query_document
     */
    Rule Document() {
        return OneOrMore(Definition());
    }

    /**
     * @category query_document
     */
    Rule Definition() {
        return FirstOf(OperationDefinition(), FragmentDefinition());
    }

    /**
     * @category query_document
     */
    Rule OperationDefinition() {
        return FirstOf(SelectionSet(), //
                Sequence(OperationType(), Name(), Optional(VariableDefinitions()), Optional(Directives()), SelectionSet()) //
        );
    }

    /**
     * @category query_document
     */
    Rule OperationType() {
        return FirstOf("query", "mutation");
    }

    /**
     * @category query_document
     */
    Rule SelectionSet() {
        return Sequence('{', OneOrMore(Selection()), '}');
    }

    /**
     * @category query_document
     */
    Rule Selection() {
        return FirstOf(Field(), FragmentSpread(), InlineFragment());
    }

    /**
     * @category query_document
     */
    Rule Field() {
        return Sequence(Optional(Alias()), Name(), Optional(Argument()), Optional(Directives()), Optional(SelectionSet()));
    }

    /**
     * @category query_document
     */
    Rule Alias() {
        return Sequence(Name(), ':');
    }

    /**
     * @category query_document
     */
    Rule Arguments() {
        return Sequence('(', OneOrMore(Argument()), ')');
    }

    /**
     * @category query_document unimplemented
     */
    Rule Argument() {
        return null;
    }

    /**
     * @category query_document unimplemented
     */
    Rule FragmentSpread() {
        return null;
    }

    /**
     * @category query_document unimplemented
     */
    Rule InlineFragment() {
        return null;
    }

    /**
     * @category query_document unimplemented
     */
    Rule FragmentDefinition() {
        return null;
    }

    /**
     * @category query_document unimplemented
     */
    Rule VariableDefinitions() {
        return null;
    }

    /**
     * @category query_document unimplemented
     */
    Rule Directives() {
        return null;
    }

}
