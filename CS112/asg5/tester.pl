fly( Start, Destination ) :-
   write('first call of fly '), write(Start), write(' '), write(Destination), nl,
   (Start,Destination,Route).

inRadians(Deg,Min,Radians) :-
   Radians is (Deg + Min / 60) * pi / 180.

haversine_radians( Air1, Air2, Distance ) :-
  airport(Air1, _, degmin(Deglat1, M1), degmin(Deglon1, M2)),
  airport(Air2, _, degmin(Deglat2, M3), degmin(Deglon2, M4)),
  inRadians( Deglat1, M1, Lat1 ),
  inRadians( Deglat2, M3, Lat2 ),
  inRadians( Deglon1, M2, Lon1 ),
  inRadians( Deglon2, M4, Lon2 ),
  Dlon is Lon2 - Lon1,
  Dlat is Lat2 - Lat1,
  A is sin( Dlat / 2 ) ** 2
    + cos( Lat1 ) * cos( Lat2 ) * sin( Dlon / 2 ) ** 2,
  Dist is 2 * atan2( sqrt( A ), sqrt( 1 - A )),
  Distance is Dist * 3961, %3961 is earth radius in miles
  format('Your total distance is: ~w miles', [Distance]).

itinerary(Air1, Air2, Path) :-
   itinerary(Air

airport( atl, 'Atlanta          ', degmin(33,39), degmin(84,25)).
airport(bos, 'Boston-Logan      ', degmin(42,22), degmin(71,2)).

not(X) :- X, !, fail.
not(_).

test :-
   haversine_radians(atl,bos,Distance).

