#!/afs/cats.ucsc.edu/courses/cmps112-wm/usr/racket/bin/mzscheme -qr
;; $Id: sbi.scm,v 1.2 2019-01-22 17:35:08-07 - - $
;; Chris Sclipei csclipei 1452216
;; Kaho Tran kahtran 1460825

;;
;; NAME
;;    symbols - illustrate use of hash table for a symbol table
;;
;; DESCRIPTION
;;    Put some entries into the symbol table and then use them.
;;

;;
;; Create the symbol table and initialize it.
;;

(define *function-table* (make-hash))
(define (function-get key)
        (hash-ref *function-table* key #f))
(define (function-put! key value)
        (hash-set! *function-table* key value))

(for-each
    (lambda (pair)
            (function-put! (car pair) (cadr pair)))
    `(
        (log10_2 0.301029995663981195213738894724493026768189881)
        (sqrt_2  1.414213562373095048801688724209698078569671875)
        (div     ,(lambda (x y) (floor (/ x y))))
        (log10   ,(lambda (x) (/ (log x) (log 10.0))))
        (mod     ,(lambda (x y) (- x (* (div x y) y))))
        (quot    ,(lambda (x y) (truncate (/ x y))))
        (rem     ,(lambda (x y) (- x (* (quot x y) y))))
        (+       ,+)
        (- ,-)
        (* ,*)
        (/ ,/)
        (% ,(lambda (x y) (- x (* (truncate (/ x y) y)))))
        (= ,=)
        (< ,<)
        (> ,>)
        (>= ,>=)
        (<= ,<=)
        (^       ,expt)
        (ceil    ,ceiling)
        (exp     ,exp)
        (floor   ,floor)
        (log     ,log)
        (sqrt    ,sqrt)
        (sin ,sin)
        (cos ,cos)
        (tan ,tan)
        (asin ,asin)
        (acos ,acos)
        (atan ,atan)
        (abs ,abs)
        (round ,round)
        (trunc ,truncate)
     ))

(define *variable-table* (make-hash))
(define (variable-get key)
        (hash-ref *variable-table* key #f))
(define (variable-put! key value)
        (hash-set! *variable-table* key value))

(define *label-table* (make-hash))
(define (label-get key)
        (hash-ref *label-table* key #f))
(define (label-put! key value)
        (hash-set! *label-table* key value))

(for-each
    (lambda (pair)
            (variable-put! (car pair) (cadr pair)))
    `(
        (nan     (/ 0.0 0.0))
        (e       2.718281828459045235360287471352662497757247093)
        (pi      3.141592653589793238462643383279502884197169399)

     ))

(define *stdin* (current-input-port))
(define *stdout* (current-output-port))
(define *stderr* (current-error-port))

(define *run-file*
    (let-values
        (((dirpath basepath root?)
            (split-path (find-system-path 'run-file))))
        (path->string basepath))
)

(define (die list)
    (for-each (lambda (item) (display item *stderr*)) list)
    (newline *stderr*)
    (exit 1)
)

(define (usage-exit)
    (die `("Usage: " ,*run-file* " filename"))
)

(define (readlist-from-inputfile filename)
    (let ((inputfile (open-input-file filename)))
         (if (not (input-port? inputfile))
             (die `(,*run-file* ": " ,filename ": open failed"))
             (let ((program (read inputfile)))
                  (close-input-port inputfile)
                         program))))

(define (dump-stdin)
    (let ((token (read)))
         (printf "token=~a~n" token)
         (when (not (eq? token eof)) (dump-stdin))))


(define (write-program-by-line filename program)
    (printf "==================================================~n")
    (printf "~a: ~s~n" *run-file* filename)
    (printf "==================================================~n")
    (printf "(~n")
    (for-each (lambda (line) (printf "~s~n" line)) program)
    (printf ")~n"))

(define (state-print line)
        (when (not (null? (cdr line)))
        (when (and (not (null? (cdr line)))(=(length line) 2))
                (displayln (cadr line)))
        (when (and (not (null? (cdr line)))(=(length line) 3))
        (when (not (symbol? (cadr line)))
                    (display (cadr line))
                    (when (not (null? (caddr line)))
                            (let ((ans (evaluate (caddr line))))
                            (displayln ans))))
        (when (symbol? (cadr line))
            (let ((ans (evaluate (cadr line))))
                (display ans))
                (display " ")
            (let ((ans (evaluate (caddr line))))
                (displayln ans))))))

(define (evaluate expr)
    (cond   ((number? expr) (+ 0.0 expr))
            ((symbol? expr) (variable-get expr))
            (else (let ((fn (hash-ref *function-table* (car expr)))
                  (args (map evaluate (cdr expr))))
                  (apply fn args)))))

(define (state-let line)
    (variable-put! (cadr line) (evaluate (caddr line)))) 

(define (state-goto line)
    ;(displayln (cadr line))
   (let ((ans (label-get (cadr line))))
        (displayln ans)))

(define (interpret-program alist)
    (when (not (null? alist))
        (let ((line (car alist)))
            (when (and (not (null? (cdr line)))
            (not (symbol? (cadr line))))
                  (let ((linestate (cdr line)))
            ;(displayln (caar linestate))
            (when (equal? 'let (caar linestate))
                  (state-let (car linestate)))
            (when (equal? 'goto (caar linestate))
                  (exit 1))
            (when (equal? 'input (caar linestate))
                  (exit 1))
            (when (equal? 'if (caar linestate))
                  (exit 1))
            (when (equal? 'dim (caar linestate))
                  (exit 1))
            (when (equal? 'print (caar linestate))
                  (state-print (car linestate))))))
            (interpret-program (cdr alist))))

(define (check-label alist)
        (when (not (null? alist))
              (let ((line (car alist)))
        (when (and (not (null? (cdr line)))(symbol? (cadr line)))
              (let ((linelabel (cdr line)))
              (if (null? (cdr linelabel))
                  (label-put! (car linelabel) 
                  (cdr linelabel))
                  (label-put! (car linelabel) 
                  (cdr alist)))))
        (check-label (cdr alist)))))

(define (main arglist)
    (if (or (null? arglist) (not (null? (cdr arglist))))
        (usage-exit)
        (let* ((sbprogfile (car arglist))
              (program (readlist-from-inputfile sbprogfile)))
              ;;(write-program-by-line sbprogfile program)
              ;(display "test")
              (check-label program)
              (interpret-program program))))

(define (show label it)
        (display label)
        (display " = ")
        (display it)
        (newline))

(main (vector->list (current-command-line-arguments)))
