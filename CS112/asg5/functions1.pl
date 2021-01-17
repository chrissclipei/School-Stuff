%
% Alyssa Melton
% Asg5: Prolog Flights
% functions.pl
%
% -----------------------------------------------------------------------------
% convert degrees and minutes to radians.
 in_radians( degmin( Deg, Min ), Radians ) :-
     Radians is (Deg + Min / 60) * pi / 180.

   not( X ) :- X, !, fail.
   not( _ ).

% -----------------------------------------------------------------------------
% calculate distance from one airport to another.
haversine_radians( Raw_lat1, Raw_long1, Raw_lat2, Raw_long2, Distance ) :-
  in_radians( Raw_lat1, Lat1 ),
  in_radians( Raw_long1, Long1 ),
  in_radians( Raw_lat2, Lat2 ),
  in_radians( Raw_long2, Long2 ),
  Dlon is Long2 - Long1,
  Dlat is Lat2 - Lat1,
  A is sin( Dlat / 2 ) ** 2
    + cos( Lat1 ) * cos( Lat2 ) * sin( Dlon / 2 ) ** 2,
  Dist is 2 * atan2( sqrt( A ), sqrt( 1 - A )),
  Distance is Dist * 3961. %3961 is earth radius in miles

% -----------------------------------------------------------------------------
% arrival time can be computed from the departure time.
% plane travels at 500 miles per hour
arrival_time(flight(Airport1, Airport2, time(Hours, Minutes)),DepartureTime, ArrivalTime) :-
  airport( Airport1, _, Lat1, Lon1 ),
  airport( Airport2, _, Lat2, Lon2 ),
  haversine_radians( Lat1, Lon1, Lat2, Lon2, Distance),
  FlightTime is Distance / 500,
  DepartureTime is Hours + Minutes / 60,
  ArrivalTime is DepartureTime + FlightTime.

% -----------------------------------------------------------------------------
% compare the times of an arrival time in Decimal
% and a departing time in Hours:Minutes.
layover_check( ArrivalTime, time(Hours, Minutes) ) :-
  DepartureTime is Hours  + Minutes/60,
  DepartureTime > ArrivalTime + 29/60. % departure must be 30 minutes or more after arrival of previous flight.

% -----------------------------------------------------------------------------

format_time(Time, Hours, Minutes) :-
   Hours is floor(Time),
   Minutes is (floor(Time*60)) mod 60. 

to_upper( Lower, Upper ) :-
   atom_chars( Lower, Lowerlist),
   maplist( lower_upper, Lowerlist, Upperlist),
   atom_chars( Upper, Upperlist).

print_trip(Action, Code, Name, Hour, Minute) :-
   to_upper(Code, Upper_code),
   format("%-6s  %3s  %-16s %02d:%02d", 
      [Action, Upper_code, Name, Hour, Minute]),
   nl.
% -----------------------------------------------------------------------------

writeRoute([]) :-
   nl.

writeRoute([[Air1, Air2, DepartureTime, ArrivalTime], Air3 | []]) :-
   airport(Air1, Air1City, _, _),
   airport(Air3, Air3City, _, _),
   format_time(DepartureTime, Hours, Minutes),
   print_trip(depart, Air1, Air1City, Hours, Minutes),
   format_time(ArrivalTime, Hours2, Minutes2),
   print_trip(arrive, Air3, Air3City, Hours2, Minutes2),
   !,
   true.

writeRoute([[Air1, Air2, DepartureTime, ArrivalTime], [Air3, Air4, DepartureTime2, ArrivalTime2] | Path]) :-
   airport(Air1, Air1City, _, _),
   airport(Air3, Air3City, _, _),
   format_time(DepartureTime, Hours, Minutes),
   print_trip(depart, Air1, Air1City, Hours, Minutes),
   format_time(ArrivalTime, Hours2, Minutes2),
   print_trip(arrive, Air3, Air3City, Hours2, Minutes2),
   !,
   writeRoute([[Air3, Air4, DepartureTime2, ArrivalTime2] | Path]).
% -----------------------------------------------------------------------------
itinerary(Curr, Curr, _, _, [Curr]).

itinerary(Curr, Final, Time1, Legs, [[Curr, Next, DepartureTime, ArrivalTime]|Path]) :-
   flight(Curr, Final, Time1),
   not(member(Final, Legs)),
   arrival_time(flight(Curr, Final, Time1),DepartureTime, ArrivalTime),
   itinerary(Final, Final, _, [Final|Legs], Path).

itinerary(Curr, Next, Time1, Legs, [[Curr, Next, DepartureTime, ArrivalTime]|Path]) :-
   flight(Curr, Final, Time1),
   not(member(Final, Legs)), 
   arrival_time(flight(Curr, Final, Time1), DepartureTime, ArrivalTime),
   flight(Final, _, Time2),
   layover_check(ArrivalTime, Time2),
   itinerary(Final, Next, Time2, [Final|Legs], Path). 

% -----------------------------------------------------------------------------
fly(Curr, Final) :-
  itinerary(Curr, Final, _, [Curr], Path),
  !,
  nl,
  writeRoute(Path),
  true.

fly( Start, Start ) :-
  write(' No need to fly yourself to your current location, silly! '),
  !, fail.
