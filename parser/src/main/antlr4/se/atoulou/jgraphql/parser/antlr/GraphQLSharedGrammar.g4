grammar GraphQLSharedGrammar;

import GraphQlTokens;

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

directives
    : directive+
    ;

directive
    : AT NAME arguments?
    ; 

arguments
    : PAREN_L argument+ PAREN_R
    ;

argument
    : NAME COLON value
    ;