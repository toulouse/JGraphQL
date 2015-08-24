lexer grammar GraphQLTokens;

// Ignored
WHITESPACE
    : [\t\u000b\u000c \u00a0]+ -> skip
    ;

LINE_TERMINATOR
    : [\n\r\u2028\u2029] -> skip
    ;

COMMA
    : ',' -> skip
    ;

COMMENT
    : '#' NOT_LINE_TERMINATOR* -> skip
    ;

fragment NOT_LINE_TERMINATOR
    : ~[\n\r\u2028\u2029]
    ;
// ---

// Punctuators
BANG : '!' ;
DOLLAR : '$' ;
PAREN_L : '(' ;
PAREN_R : ')' ;
ELLIPSIS : '...' ;
COLON : ':' ;
EQUAL : '=' ;
AT : '@' ;
BRACKET_L : '[' ;
BRACKET_R : ']' ;
BRACE_L : '{' ;
BRACE_R : '}' ;
PIPE : '|' ;
// ---

BOOLEAN_VALUE
    : TRUE
    | FALSE
    ;

TRUE
    : 'true'
    ;

FALSE
    : 'false'
    ;

fragment ESCAPED_UNICODE
    : 'u' HEX HEX HEX HEX
    ;

fragment HEX
    : [a-fA-F0-9]
    ;

fragment ESCAPED_CHARACTER
    : ["\\/bfnrt]
    ;

FLOAT_VALUE
    : INTEGER_PART FRACTIONAL_PART
    | INTEGER_PART EXPONENT_PART
    | INTEGER_PART FRACTIONAL_PART EXPONENT_PART
    ;

fragment FRACTIONAL_PART
    : '.' DIGIT+
    ;

fragment EXPONENT_PART
    : ('e' | 'E') ('+' | '-')? DIGIT+
    ;

fragment DIGIT
    : [0-9]
    ;

INTEGER_PART
    : (NEGATIVE_SIGN? '0')
    | (NEGATIVE_SIGN? NONZERO_DIGIT DIGIT*)
    ;

fragment NEGATIVE_SIGN
    : '-'
    ;

fragment NONZERO_DIGIT
    : [1-9]
    ;

STRING_VALUE
    : '""'
    | '"' STRING_CHARACTER+ '"'
    ;

fragment STRING_CHARACTER
    : ~[\n\r\u2028\u2029"\\]
    | '\\' ESCAPED_UNICODE
    | '\\' ESCAPED_CHARACTER
    ;

NAME
    : [_a-zA-Z] [_a-zA-Z0-9]*
    ;

VARIABLE
    : DOLLAR NAME
    ;
