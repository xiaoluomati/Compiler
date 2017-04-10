ID:id,NUMBER:number,OPERATION:operation,IF:_if,ELSE:_else,WHILE:_while,DO:_do,FOR:_for,NEW:_new,RELOP:relop

%%

letter    ->  [A-Za-z]
digit     ->  [0-9]
ds        ->  digitdigit*
id        ->  (_|letter)(_|letter|digit)*
number    ->  ds.ds|ds
operation ->  +|-|×|/|=
_if       ->  if
_else     ->  else
_while    ->  while
_do       ->  do
_for      ->  for
_new      ->  new
relop     ->  <|>|<=|>=|==|!=

%%

_if,_else,_while,_do,_for,_new
