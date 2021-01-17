to_time(Totaltime, Hours, Minutes) :-
   Totalmins is floor(Totaltime * 60),
   Hours is Totalmins // 60,
   Minutes is Totalmins mod 60,
   format("%02d:%02d", [Hours, Minutes]).

test :-
   to_time(170.00, Hours, Mins).
