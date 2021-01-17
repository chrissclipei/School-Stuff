#!/afs/cats.ucsc.edu/courses/cmps112-wm/usr/smalltalk/bin/gst -f
"user_input.st"
"A program to demonstrate how to get input from user."

| userName |

Transcript show: 'What is your name? '.
userName := stdin nextLine.

Transcript show: ('Hello ', userName, '!'); cr.

