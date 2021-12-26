grammar NiceArithmetic;

@header {
  import java.util.HashMap;
  import java.util.Map;
  import java.util.stream.IntStream;
}

result returns [Map<String, Integer> values] @init {$values = new HashMap<String, Integer>();}
  : (assignment[$values])+
  ;

assignment [Map<String, Integer> values] returns [String name]
  : IDENT {$name = $IDENT.text;} EQ expression[$values] {values.put($name, $expression.value);} SEMI
  ;

expression [Map<String, Integer> values] returns [int value, boolean add] @init {$value = 0;}
  : term[$values] {$value += $term.value;} ((PLUS {$add = true;} | MINUS {$add = false;}) term[$values] { if ($add) {$value += $term.value;} else {$value -= $term.value;}})*
  ;

term [Map<String, Integer> values] returns [int value, boolean mul] @init {$value = 0;}
  : factorial[$values] {$value += $factorial.value;} ((STAR {$mul = true;} | SLASH {$mul = false;}) factorial[$values] { if ($mul) {$value *= $factorial.value;} else {$value /= $factorial.value;}})*
  ;

factorial [Map<String, Integer> values] returns [int value;] @init {$value = 0;}
  : primary[$values] {$value += $primary.value;} (EXCL {$value = IntStream.rangeClosed(2, $value).reduce(1, (x, y) -> x * y);})*
  ;

primary [Map<String, Integer> values] returns [int value] @init {$value = 1;}
   : INT {$value = Integer.parseInt($INT.text);}
   | (PLUS {$value = 1;} | MINUS {$value = -1;})? IDENT {$value *= values.get($IDENT.text);}
   | (PLUS {$value = 1;} | MINUS {$value = -1;})? LPAREN expression[$values] {$value *= $expression.value;} RPAREN
   ;

LPAREN : '(';
RPAREN : ')';
EQ : '=';
PLUS : '+';
MINUS : '-';
STAR : '*';
SLASH : '/';
POINT : '.';
SEMI : ';' ;
EXCL : '!' ;
INT : (PLUS | MINUS)?[0-9]+;
IDENT : [a-zA-Z_][a-zA-Z_0-9]*;
WS : [ \r\n\t]+ -> skip;
