getRoute( Node, End, Route ) :-
   getRoute( Node, 0, End, [Node], Route ).
   % current, destination, Tried[], Route[]

getRoute( Node, _, Node, _, [] ).
% found the destination! return that Node.
