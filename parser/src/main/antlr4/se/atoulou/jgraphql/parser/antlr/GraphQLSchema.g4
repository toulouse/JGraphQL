/*
The MIT License (MIT)

Copyright (c) 2015 Joseph T. McBride
Copyright (c) 2015 Andrew A. Toulouse

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

GraphQL grammar derived from:

	https://github.com/antlr/grammars-v4/blob/master/graphql/GraphQL.g4
    https://github.com/graphql/graphql-js

*/
grammar GraphQLSchema;

import GraphQL;

schemaDocument
    : schemaDefinition+
    ;

schemaDefinition
    : typeDefinition
    | interfaceDefinition
    | unionDefinition
    | scalarDefinition
    | enumDefinition
    | inputObjectDefinition
    ;

typeDefinition
    :   'type' NAME implementTypes? BRACE_L fieldDefinition+ BRACE_R
    ;

implementTypes
    :   COLON typeName+
    ;

fieldDefinition
    :   (NAME | 'type') argumentsDefinition? COLON type 
    ;

argumentsDefinition
    :   PAREN_L inputValueDefinition+ PAREN_R
    ;

inputValueDefinition
    :   NAME COLON type (EQUAL value)?
    ;

interfaceDefinition
    :   'interface' NAME  BRACE_L fieldDefinition+ BRACE_R
    ;

unionDefinition
    :   'union' NAME EQUAL unionMembers
    ;

unionMembers
    :   typeName
    |   typeName PIPE unionMembers
    ;

scalarDefinition
    :   'scalar' typeName
    ;

enumDefinition
    :   'enum' NAME BRACE_L enumValueDefinition+ BRACE_R
    ;

enumValueDefinition
    :   NAME 
    ;

inputObjectDefinition
    :   'input' NAME BRACE_L inputValueDefinition+ BRACE_R
    ;

value
    :   STRING_VALUE # stringValue
    |   INTEGER_VALUE # intValue
    |   FLOAT_VALUE # floatValue
    |   BOOLEAN_VALUE # boolValue
    |   array # arrayValue
    ;

type
    :   typeName nonNullType?
    |   listType nonNullType?
    ;

typeName
    :   NAME
    ;

listType
    :   BRACKET_L type BRACKET_R
    ;

nonNullType
    : BANG
    ;

array
    :   BRACKET_L value (COMMA value)* BRACKET_R
    |   BRACKET_L BRACKET_R // empty array
    ;


