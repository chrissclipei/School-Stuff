(* $Id: interp.ml,v 1.6 2019-01-24 19:14:14-08 - - $ *)
(* 
   Chris Sclipei - csclipei 1452216 
   Kaho Tran - kahtran 1460825 
*)

open Absyn

exception Unimplemented of string
let unimpl reason = raise (Unimplemented reason)

let want_dump = ref false
let eval_unary oper = Hashtbl.find Tables.unary_fn_table oper
let eval_binary oper = Hashtbl.find Tables.binary_fn_table oper
let eval_boolean oper = Hashtbl.find Tables.boolean_table oper

let rec eval_expr (expr : Absyn.expr) : float = match expr with
    | Number number -> number
    | Memref memref -> 
         (match memref with
             |Variable ident -> 
                 Hashtbl.find Tables.variable_table ident
             |Arrayref (ident, expr) -> 
                 Array.get (Hashtbl.find Tables.array_table ident) 
                 (int_of_float(eval_expr expr -. 1.0)))
    | Unary (oper, expr) -> eval_unary oper (eval_expr expr)
    | Binary (oper, expr1, expr2) -> eval_binary oper (eval_expr expr1)
         (eval_expr expr2)

let rec eval_bool (expr : Absyn.expr) : bool = match expr with 
    | Binary (oper, memref, expr) -> eval_boolean oper 
         (eval_expr memref) (eval_expr expr)

let interp_let (memref : Absyn.memref) (expr : Absyn.expr) : 
    Absyn.program option =
       (match memref with
             |Variable ident -> Hashtbl.add Tables.variable_table ident
                 (eval_expr expr)
             |Arrayref (ident, index) -> Array.set 
                 (Hashtbl.find Tables.array_table ident) 
                 (int_of_float(eval_expr index -. 1.0))
                 (eval_expr expr)); None

let interp_dim (ident : Absyn.ident) (expr : Absyn.expr) : 
    Absyn.program option = 
       (Hashtbl.add Tables.array_table ident 
       (Array.make (int_of_float(eval_expr expr)) 0.0)); None

let interp_goto (label : Absyn.label) : Absyn.program option =
    (Some (Hashtbl.find Tables.label_table label))

let interp_if (expr : Absyn.expr) (label : Absyn.label) : 
    Absyn.program option =
       if eval_bool expr then interp_goto label else None 

let interp_print (print_list : Absyn.printable list) : 
    Absyn.program option = 
       (let print_item item =
           (print_string " ";
            match item with
            | String string ->
               let regex = Str.regexp "\"\\(.*\\)\""
               in print_string (Str.replace_first regex "\\1" string)
            | Printexpr expr ->
               print_float (eval_expr expr))
        in (List.iter print_item print_list; print_newline ())); None

let interp_input (memref_list : Absyn.memref list) : 
    Absyn.program option =
       (let input_number memref =
           try  let number = Etc.read_number ()
                in match memref with
                | Variable (ident) -> 
                  (Hashtbl.add Tables.variable_table ident number)
           with End_of_file -> 
                (Hashtbl.add Tables.variable_table "eof" 1.0)
       in List.iter input_number memref_list); None

let interp_stmt (stmt : Absyn.stmt) : Absyn.program option = 
    match stmt with
      | Dim (ident, expr) -> interp_dim ident expr
      | Let (memref, expr) -> interp_let memref expr
      | Goto label -> interp_goto label
      | If (expr, label) -> interp_if expr label
      | Print print_list -> interp_print print_list
      | Input memref_list -> interp_input memref_list

let rec interpret (program : Absyn.program) = match program with
    | [] -> ()
    | firstline::otherlines -> match firstline with
      | _, _, None -> interpret otherlines
      | _, _, Some stmt -> match (interp_stmt stmt) with
         | None -> interpret otherlines
         | Some line -> interpret line

let interpret_program program =
    (Tables.init_label_table program; 
     if !want_dump then Tables.dump_label_table ();
     if !want_dump then Dumper.dump_program program;
     interpret program)

