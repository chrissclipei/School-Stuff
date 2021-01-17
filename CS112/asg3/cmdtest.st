#!/afs/cats.ucsc.edu/courses/cmps112-wm/usr/smalltalk/bin/gst -f
"$Id: cmdline.st,v 1.8 2011-05-05 20:45:21-07 - - $"
""
"Parse command line arguments"
""

execname := '$RCSfile: cmdline.st,v $' =~ '.*: (.*),v.*' at: 1.
nl := Character nl.

|cflag|
cflag := false.
|uflag|
uflag := false.
|tflag|
tflag := false.

argc := 0.
|infile outfile|

(Smalltalk arguments size = 2) | (Smalltalk arguments size = 3) ifTrue:[
   (1 to: Smalltalk arguments size)
   do: [:index | arg := (Smalltalk arguments at: index).
      (arg includes: $-) ifTrue:[
         (argc = 1) ifTrue: [
            'Usage Error: Unable to use flags -t, -c, -u concurrently' displayNl.
            ObjectMemory quit: 1.
         ].
         (arg includes: $c) ifTrue: [cflag := true. 'Compressing file...' displayNl].
         (arg includes: $u) ifTrue: [uflag := true. 'Decompressing file...' displayNl].
         (arg includes: $t) ifTrue: [tflag := true. 'Printing tree...' displayNl].
      ] ifFalse: [
         infile := FileStream open: arg
                              mode: FileStream read.
      ]. (argc = 2) ifTrue: [
         outfile := FileStream open: arg
                               mode: FileStream write.
      ].
      argc := argc + 1.
   ].
]  ifFalse: [
      'Usage Error: hzip.st -dtcu infile [outfile]' displayNl.
].

(cflag & uflag) ifTrue: [
   'Usage Error: Only either -c, -u, or -t can be chosen' displayNl.
   ObjectMemory quit: 1.
].

(cflag & tflag) ifTrue: [
   'Usage Error: Only either -c, -u, or -t can be chosen' displayNl.
   ObjectMemory quit: 1.
].

(uflag & tflag) ifTrue: [
   'Usage Error: Only either -c, -u, or -t can be chosen' displayNl.
   ObjectMemory quit: 1.
].

freqTable := ZeroArray new: 257.
freqTable at: 256 put: 1.
freqList := SortedCollection new.
codedTable := ZeroArray new: 257.

[infile atEnd not] whileTrue: [
   |ordChar|
   ordChar := infile next asInteger.
   freqTable incr: ordChar.
].

freqTable keysAndValuesDo: [:key :value |
    (value notNil and: [value > 0]) ifTrue: [
        |entry|
        entry := Leaf new: key count: value.
        freqList add: entry.
    ]
]. 

[freqList size > 1] whileTrue: [
   |left right|
   left := freqList removeFirst.
   right := freqList removeFirst.
   hoffTree := Tree new: left char count: (left count) + (right count)
                         left: left right: right.
   freqList add: hoffTree.
].

|encodeTable|
(freqList size > 0) ifTrue: [
   encodeTable := freqList removeFirst.
   encodeTable depthFirst: [:char :string |
      codedTable at: char asInteger put: string.
   ] prefix: ''.
].
