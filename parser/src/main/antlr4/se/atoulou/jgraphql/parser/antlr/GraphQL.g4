grammar GraphQL;

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
ON : 'on' ;

// ---

// Type delimiters
TYPE_T : 'type' ;
INTERFACE_T : 'interface' ;
UNION_T : 'union' ;
SCALAR_T : 'scalar' ;
ENUM_T : 'enum' ;
INPUT_T : 'input' ;

QUERY_T : 'query' ;
MUTATION_T : 'mutation' ;
FRAGMENT_T : 'fragment' ;

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
    : NOT_LINE_TERMINATOR
    | '\\' ESCAPED_UNICODE
    | '\\' ESCAPED_CHARACTER
    ;

NAME
    : [_a-zA-Z] [_a-zA-Z0-9]*
    ;

VARIABLE
    : DOLLAR NAME
    ;

variable
    : VARIABLE
    ;

constValue
    : floatValue
    | integerValue
    | stringValue
    | booleanValue
    | NAME // enumValue: 'true', 'false', and 'null' are errors here
    | constArrayValue
    | constObjectValue
    ;

value
    : variable
    | floatValue
    | integerValue
    | stringValue
    | booleanValue
    | NAME // enumValue: 'true', 'false', and 'null' are errors here
    | arrayValue
    | objectValue
    ;

stringValue
    : STRING_VALUE
    ;

booleanValue
    : BOOLEAN_VALUE
    ;

floatValue
    : FLOAT_VALUE
    ;

integerValue
    : INTEGER_PART
    ;

constArrayValue
    : BRACKET_L constValue* BRACKET_R
    ;

arrayValue
    : BRACKET_L value* BRACKET_R
    ;

constObjectValue
    : BRACE_L constObjectField* BRACE_R
    ;

objectValue
    : BRACE_L objectField* BRACE_R
    ;

constObjectField
    : NAME COLON constValue
    ;

objectField
    : NAME COLON value
    ;

type
    : nonNullType
    | namedType
    | listType
    ;

namedType
    : NAME
    ;

listType
    : BRACKET_L type BRACKET_R
    ;

nonNullType
    : namedType BANG
    | listType BANG
    ;

defaultValue
    : EQUAL constValue
    ;

document
    : definition+
    ;

definition
    : operationDefinition
    | fragmentDefinition
    | typeDefinition
    ;

typeDefinition
    : objectTypeDefinition
    | interfaceDefinition
    | unionDefinition
    | scalarDefinition
    | enumDefinition
    | inputObjectDefinition
    ;

objectTypeDefinition
    : TYPE_T NAME implementTypes? BRACE_L fieldDefinition+ BRACE_R
    ;

implementTypes
    : COLON namedType+
    ;

fieldDefinition
    : NAME argumentsDefinition? COLON type
    ;

argumentsDefinition
    : PAREN_L inputValueDefinition+ PAREN_R
    ;

inputValueDefinition
    : NAME COLON type defaultValue?
    ;

interfaceDefinition
    : INTERFACE_T NAME  BRACE_L fieldDefinition+ BRACE_R
    ;

unionDefinition
    : UNION_T NAME EQUAL unionMembers
    ;

unionMembers
    : namedType
    |   namedType PIPE unionMembers
    ;

scalarDefinition
    : SCALAR_T namedType
    ;

enumDefinition
    : ENUM_T NAME BRACE_L enumValueDefinition+ BRACE_R
    ;

enumValueDefinition
    : NAME
    ;

inputObjectDefinition
    : INPUT_T NAME BRACE_L inputValueDefinition+ BRACE_R
    ;

operationDefinition
    : selectionSet
    | operationType NAME variableDefinitions? directives? selectionSet
    ;

operationType
    : QUERY_T
    | MUTATION_T
    ;

selectionSet
    : BRACE_L selection+ BRACE_R
    ;

selection
    : field
    | fragmentSpread
    | inlineFragment
    ;

field
    : alias? NAME arguments? directives? selectionSet?
    ;

alias
    : NAME COLON
    ;

arguments
    : PAREN_L argument+ PAREN_R
    ;

argument
    : NAME COLON value
    ;

fragmentSpread
    : ELLIPSIS fragmentName directives?
    ;

inlineFragment
    : ELLIPSIS ON typeCondition directives? selectionSet
    ;

fragmentDefinition
    : FRAGMENT_T fragmentName ON typeCondition directives? selectionSet
    ;

fragmentName
    : NAME // but not 'on'
    ; 

typeCondition
    : namedType
    ;

variableDefinitions
    : PAREN_L variableDefinition+ PAREN_R
    ;

variableDefinition
    : variable COLON type defaultValue?
    ;

directives
    : directive+
    ;

directive
    : AT NAME arguments?
    ; 
