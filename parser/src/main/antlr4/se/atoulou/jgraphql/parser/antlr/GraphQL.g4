lexer grammar GraphQL;

// Ignored
WHITESPACE : [\t\u000b\u000c \u00a0]+ -> skip;
LINE_TERMINATOR : [\n\r\u2028\u2029] -> skip;
COMMA : ',' -> skip;

fragment NOT_LINE_TERMINATOR : ~[\n\r\u2028\u2029];
COMMENT : '#' NOT_LINE_TERMINATOR* -> skip;

// Punctuators
BANG: '!';
DOLLAR: '$';
PAREN_L: '(';
PAREN_R: ')';
ELLIPSIS: '...';
COLON: ':';
EQUAL: '=';
AT: '@';
BRACKET_L: '[';
BRACKET_R: ']';
BRACE_L: '{';
BRACE_R: '}';
PIPE: '|';

fragment PUNCTUATOR
    : BANG
    | DOLLAR
    | PAREN_L 
    | PAREN_R 
    | ELLIPSIS
    | COLON
    | EQUAL
    | AT 
    | BRACKET_L 
    | BRACKET_R 
    | BRACE_L 
    | BRACE_R
    | PIPE;

BOOLEAN_VALUE : TRUE | FALSE;
TRUE : 'true';
FALSE : 'false';

INTEGER_VALUE : INTEGER_PART;
INTEGER_PART : (NEGATIVE_SIGN? '0') | (NEGATIVE_SIGN? NONZERO_DIGIT DIGIT*);
fragment NEGATIVE_SIGN : '-';
fragment DIGIT : [0-9];
fragment NONZERO_DIGIT : [1-9];

FLOAT_VALUE
    : INTEGER_PART FRACTIONAL_PART
    | INTEGER_PART EXPONENT_PART
    | INTEGER_PART FRACTIONAL_PART EXPONENT_PART;
fragment FRACTIONAL_PART : '.' DIGIT+;
fragment EXPONENT_PART : EXPONENT_INDICATOR SIGN? DIGIT+;
fragment EXPONENT_INDICATOR : [eE];
fragment SIGN : [+\-];

STRING_VALUE
    : '""'
    | '"' STRING_CHARACTER+ '"';
STRING_CHARACTER
    : ~[\n\r\u2028\u2029"\\]
    | '\\' ESCAPED_UNICODE
    | '\\' ESCAPED_CHARACTER
    ;

fragment HEX : [a-fA-F0-9];
fragment ESCAPED_UNICODE : 'u' HEX HEX HEX HEX;
fragment ESCAPED_CHARACTER : ["\\/bfnrt];

NAME
    : [_a-zA-Z] [_a-zA-Z0-9]*
    ;
    