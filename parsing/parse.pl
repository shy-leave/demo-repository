:- module(parse, [parse/1]).


parse(Tokens) :-
    lines(Tokens, []).


lines(Input, Rest) :-
    line(Input, R1),
    R1 = [';' | R2],
    lines(R2, Rest).


lines(Input, Rest) :-
    line(Input, Rest).


line(Input, Rest) :-
    num(Input, R1),
    R1 = [',' | R2],
    line(R2, Rest).


line(Input, Rest) :-
    num(Input, Rest).


num([D | Rest], Final) :-
    digit(D),
    num(Rest, Final).


num([D | Rest], Rest) :-
    digit(D).

digit(0). digit(1). digit(2). digit(3). digit(4).
digit(5). digit(6). digit(7). digit(8). digit(9).
