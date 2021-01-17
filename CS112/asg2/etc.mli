(* $Id: etc.mli,v 1.1 2019-01-24 15:47:38-08 - - $ *)
(* 
   Chris Sclipei - csclipei 1452216 
   Kaho Tran - kahtran 1460825 
   Main program and system access.
*)

val warn : string list -> unit

val die : string list -> unit

val syntax_error : Lexing.position -> string list -> unit

val usage_exit : string list -> unit

val read_number : unit -> float

