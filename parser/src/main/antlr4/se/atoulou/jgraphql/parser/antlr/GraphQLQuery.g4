grammar GraphQLQuery;

import GraphQLTokens, GraphQLSharedGrammar;

document
    : definition+
    ;

definition
    : operationDefinition
    | fragmentDefinition
    ;

operationDefinition
    : selectionSet
    | operationType NAME variableDefinitions? directives? selectionSet
    ;

operationType
    : 'query'
    | 'mutation'
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

fragmentSpread
    : ELLIPSIS fragmentName directives?
    ;

inlineFragment
    : ELLIPSIS 'on' typeCondition directives? selectionSet
    ;

fragmentDefinition
    : 'fragment' fragmentName 'on' typeCondition directives? selectionSet
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