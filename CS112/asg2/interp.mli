(* $Id: interp.mli,v 1.5 2019-01-24 17:08:37-08 - - $ *)
(* 
   Chris Sclipei - csclipei 1452216 
   Kaho Tran - kahtran 1460825 
   Interpreter for Silly Basic
*)

val want_dump : bool ref

val interpret_program : Absyn.program -> unit

