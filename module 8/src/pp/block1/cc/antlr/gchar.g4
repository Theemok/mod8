lexer grammar gchar;

@header{package pp.block1.cc.antlr;}


Sentence: FirstLetter Letter Letter Letter Letter Letter;
fragment FirstLetter: CAP | SMALL;
fragment Letter: NUM | CAP | SMALL;
fragment NUM: '1'..'9';
fragment CAP: 'a'..'z';
fragment SMALL: 'A'..'Z';
