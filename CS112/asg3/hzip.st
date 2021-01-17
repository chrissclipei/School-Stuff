#!/afs/cats.ucsc.edu/courses/cmps112-wm/usr/smalltalk/bin/gst -f

 " Chris Sclipei - csclipei 1452216 
   Kaho Tran - kahtran 1460825 "
 
nl := Character nl.

Character extend [
   isPrint [
      ^ (Character space <= self) & (self <= $~)
   ]
   visible [
      self isPrint ifTrue: [^ self asString]
                   ifFalse: [^ self asInteger printStringRadix: 8]
   ]
]

FileStream extend [
   nl := Character nl.
   catFile [
      |count|
      count := 0.
      [self atEnd not] whileTrue: [
         |padded|
         count := count + 1.
         padded := count printPaddedWith: Character space to: 6.
         stdout << padded << ': ' << self nextLine << nl.
      ]
   ]
]

Object subclass: ZeroArray [
   |theArray|

   ZeroArray class >> new [
      self shouldNotImplement.
   ]

   ZeroArray class >> new: size [
      |result|
      result := super new.
      result init: size.
      ^ result
   ]

   init: size [
      theArray := Array new: size.
   ]

   size [
      ^ theArray size.
   ]

   at: index [
      ^ theArray at: index + 1.
   ]

   at: index put: value [
      ^ theArray at: index + 1 put: value.
   ]

   incr: index [
      (self at: index) isNil
         ifTrue: [ self at: index put: 0].
      self at: index put: (self at: index) + 1.
   ]

   keysAndValuesDo: aBlock [
      (0 to: (self size) - 1) do: [:index |
         aBlock value: index value: (self at: index).
      ]
   ]

]

"bitstream.st"

Object subclass: BitStream [
   |bitIndex byte myStream|
   BitStream class >> new: fileStream [
      |result|
      result := super new.
      result init: fileStream.
      ^result
   ]
   init: fileStream [
      myStream := fileStream.
      bitIndex := 1.
   ]
   nextBit [
      bitIndex = 1 ifTrue: [
         byte := myStream next.
         bitIndex := 9.
      ].
      bitIndex := bitIndex - 1.
      ^byte value bitAt: bitIndex
   ]
   atEnd [
      ^bitIndex = 1 and: [myStream atEnd]
   ]
   currByte [
      ^byte
   ]
]

"outbits.st"

Object subclass: OutBits [
   |bitIndex currentByte myStream|
   OutBits class >> new [
      self shouldNotImplement.
   ]
   OutBits class >> new: fileStream [
      |result|
      result := super new.
      result init: fileStream.
      ^result
   ]
   clearByte [
      bitIndex := 8.
      currentByte := 0.
   ]
   init: fileStream [
      myStream := fileStream.
      self clearByte.
   ]
   flushByte [
      bitIndex = 8 ifFalse: [
         myStream nextPutByte: currentByte.
         self clearByte.
      ]
   ]
   writeBit: bit [
      currentByte := currentByte bitAt: bitIndex put: bit.
      bitIndex := bitIndex - 1.
      bitIndex = 0 ifTrue: [self flushByte].
   ]
]


Object subclass: Leaf [
   |char count isALeaf|
   char [ ^ char ]
   count [ ^ count ]
   isALeaf [ ^ isALeaf ]

   Leaf class >> new [
      self shouldNotImplement
   ]

   Leaf class >> new: aChar count: aCount [
      |result|
      result := super new.
      result setChar: aChar andCount: aCount.
      ^result
   ]

   setChar: aChar andCount: aCount [
      char := aChar.
      count := aCount.
      isALeaf := true.
   ]

   <= other [
      ^ (count < other count)
      | ((count = other count) & (char <= other char))
   ]

   printBase: aStream [
      ^ aStream << self class << '(' << char << ',' << count
   ]

   printOn: aStream [
      (self printBase: aStream) << ')'.
   ]

   depthFirst: visitor prefix: string [
      visitor value: char value: string.
   ]

   postOrder: visitor [
      visitor value: char value: isALeaf.
   ]
].

Leaf subclass: Tree [
   |left right|

   Tree class >> new: aChar count: aCount [
      self shouldNotImplement
   ]

   Tree class >> new: aChar count: aCount left: aLeft right: aRight [
      |result|
      result := super new: aChar count: aCount.
      result setLeft: aLeft andRight: aRight.
      ^ result
   ]

   setLeft: aLeft andRight: aRight [
      left := aLeft.
      right := aRight.
      isALeaf := false.
   ]

   getLeft [
      ^ left.
   ]
   
   getRight [
      ^ right.
   ]

   printOn: aStream [
      (self printBase: aStream) << ',' << left << ',' << right << ')'.
   ]

   depthFirst: visitor prefix: string [
      left depthFirst: visitor prefix: string, '0'.
      right depthFirst: visitor prefix: string, '1'.
   ]

   postOrder: visitor [
      left postOrder: visitor.
      right postOrder: visitor.
      visitor value: char value: isALeaf.
   ]
].

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
            stderr << 'Usage Error: hzip.st -dtcu infile [outfile]' 
                   << nl.
            ObjectMemory quit: 1.
         ].
         (arg includes: $d) ifTrue: [
            dflag := true.
         ].
         (arg includes: $c) ifTrue: [
            cflag := true. 'Compressing file...' displayNl
         ].
         (arg includes: $u) ifTrue: [
            uflag := true. 'Decompressing file...' displayNl
         ].
         (arg includes: $t) ifTrue: [
            tflag := true. 'Compiling tree...' displayNl
         ].
      ] ifFalse: [
         (argc = 2) ifTrue: [
            outfile := FileStream open: arg
                                  mode: FileStream write.
         ] ifFalse: [
            (File exists: arg) ifTrue: [
               infile := FileStream open: arg
                                    mode: FileStream read.
               compfile := FileStream open: arg
                                      mode: FileStream read.
            ] ifFalse: [
               stderr << 'File does not exist!' << nl.
               ObjectMemory quit: 1.
            ].
         ].
      ].
      argc := argc + 1.
   ].
] ifFalse: [
      stderr << 'Usage Error: hzip.st -dtcu infile [outfile]' << nl.
      ObjectMemory quit: 1.
].

(cflag & uflag) ifTrue: [
   stderr << 'Usage Error: Only either -c, -u, or -t can be chosen' 
          << nl.
   ObjectMemory quit: 1.
].

(cflag & tflag) ifTrue: [
   stderr << 'Usage Error: Only either -c, -u, or -t can be chosen' 
          << nl.
   ObjectMemory quit: 1.
].

(uflag & tflag) ifTrue: [
   stderr << 'Usage Error: Only either -c, -u, or -t can be chosen' 
          << nl.
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
   freqList add: hoffTree
].

|encodeTable|
(freqList size > 0) ifTrue: [
   encodeTable := freqList removeFirst.
   encodeTable depthFirst: [:char :string |
      codedTable at: char asInteger put: string.
   ] prefix: ''.
].

(outfile isNil) ifTrue: [outfile := stdout.].

tflag ifTrue: [
   |tmp val char bitcount|
   codedTable keysAndValuesDo: [:key :val |
      (val notNil and: [val > 0]) ifTrue: [
         (key = 256) ifTrue: [
            char := 'EOF'.
         ] ifFalse: [
            (key < 16) ifTrue: [
               char := 'x0', (key printString: 16).
            ] ifFalse: [
               (key < 33) | (key > 126) ifTrue: [
                  char := 'x', (key printString: 16).
               ] ifFalse: [
                  char := key asCharacter visible.
               ].
            ].
         ].
         bitcount := codedTable at: key.
         outfile << char.
         char asString size to: 5 do: [:skip| outfile << ' ' ].
         outfile << (freqTable at: key).
         tmp := (freqTable at: key).
         tmp asString size to: 8 do: [:skip| outfile << ' ' ].
         outfile << bitcount << nl.
      ].
   ].
].

binaryTree := ''.
encodeTable postOrder: [:char :isALeaf |
   isALeaf ifTrue: [
      value := ''.
      (char = 256) ifTrue: [
         value := '000000001' 
      ] ifFalse: [ 
         (char = 0) ifTrue: [
            value := '000000000' 
         ] ifFalse: [
            1 to: 8 by: 1 do: [:i |
               value := ((char bitAt: i) printString), value.
            ]
         ].
      ].
      binaryTree := (binaryTree, '0', value).
   ] ifFalse: [
      binaryTree := binaryTree, '1'.
   ].
].

cflag ifTrue: [
   |bindump ordChar binarySeq numLength|
   bindump := OutBits new: outfile.
   1 to: (binaryTree size) by: 1 do: [:index |
      bit := ((binaryTree at: index) asInteger) - 48.
      bindump writeBit: bit.
   ].
   bindump writeBit: 1.
   [compfile atEnd not] whileTrue: [
      ordChar := compfile next asInteger.
      binarySeq := codedTable at: ordChar.
      numLength := binarySeq size.
      1 to: numLength by: 1 do: [:index |
         bit :=  ((binarySeq at: index) asInteger) - 48.
         bindump writeBit: bit. 
      ].
   ].
   eof := codedTable at: 256.
   numLength := eof size.
   1 to: numLength by: 1 do: [:index |
     bit := ((eof at: index) asInteger) - 48.
     bindump writeBit: bit.
   ]. 
   bindump flushByte.
].
