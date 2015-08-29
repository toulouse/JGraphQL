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

import GraphQLTokens, GraphQLSharedGrammar;

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
    : 'type' NAME implementTypes? directives? BRACE_L fieldDefinition+ BRACE_R
    ;

implementTypes
    : COLON namedType+
    ;

fieldDefinition
    : (NAME | 'type') argumentsDefinition? COLON type directives?
    ;

argumentsDefinition
    : PAREN_L inputValueDefinition+ PAREN_R
    ;

inputValueDefinition
    : NAME COLON type defaultValue?
    ;

interfaceDefinition
    : 'interface' NAME directives? BRACE_L fieldDefinition+ BRACE_R
    ;

unionDefinition
    : 'union' NAME directives? EQUAL unionMembers
    ;

unionMembers
    : namedType directives?
    | namedType directives? PIPE unionMembers
    ;

scalarDefinition
    : 'scalar' namedType directives? 
    ;

enumDefinition
    : 'enum' NAME directives? BRACE_L enumValueDefinition+ BRACE_R
    ;

enumValueDefinition
    : NAME directives? 
    ;

inputObjectDefinition
    : 'input' NAME directives? BRACE_L inputValueDefinition+ BRACE_R
    ;
