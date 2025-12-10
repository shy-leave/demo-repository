:- module(search, [search/1]).
:- [scenario1].  


search(Path) :-
    start(Start),
    bfs([[state(Start, [], [Start])]], [], Path).



bfs([[state(Room, _, Path)] | _], _, Path) :-
    treasure(Room), !.   % reached treasure → done

bfs([State | RestQueue], Visited, Path) :-
    expand(State, NewStates, Visited, NewVisited),
    append(RestQueue, NewStates, NextQueue),
    bfs(NextQueue, NewVisited, Path).


expand(state(Room, Keys, Path), NewStates, Visited, [new(Room, Keys) | Visited]) :-
    findall(
        state(NextRoom, Keys2, Path2),
        next_state(Room, Keys, Path, NextRoom, Keys2, Path2, Visited),
        NewStates
    ).



next_state(Room, Keys, Path, NextRoom, Keys2, [NextRoom | Path], Visited) :-
    connected(Room, NextRoom),
    \+ member(new(NextRoom, Keys), Visited),

    % check lock
    (   lock(Room, NextRoom, Color)
    ->  member(Color, Keys)            
    ;   true
    ),

   
    (   key(NextRoom, KColor)
    ->  add_key(Keys, KColor, Keys2)
    ;   Keys2 = Keys
    ),
    \+ member(NextRoom, Path).  % avoid cycles



add_key(Keys, K, Keys2) :-
    (member(K, Keys) -> Keys2 = Keys ; Keys2 = [K | Keys]).
