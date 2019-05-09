lexer grammar pli;

@header{package pp.block1.cc.antlr;}


Sentence: '"' WORD '"' '"' WORD '"';
fragment WORD: ('0'..'9' | 'a'..'z' | 'A'..'Z' | ' ')+;