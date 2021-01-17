itinerary(Air1, Air2, Path) :-
   itinerary(Air1, Air2, 0, [Air1], Path).
   % current, destination, Tried[], Route[]

itinerary(Curr, Curr, _, _, []).
% found the destination! return that Node.

itinerary(Curr, Final, Arrival, Legs, [flight(Curr, Next, time)|Path]) :-
   flight(Curr, Next, time1),
   not(member(Next, Legs)),
   arrival_time(flight(Curr, Next, time(Hours, Minutes)), ArrivalTime),
   flight(Next, _, time2),
   layover_check(ArrivalTime, time2),
   itinerary(Next, ArrivalTime, Final, [Next|Legs], Path). %recur

% -----------------------------------------------------------------------------
fly(Curr, Final) :-
  itinerary(Air1, Air2, Path).
