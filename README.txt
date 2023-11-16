UNIT 5 PROJECT INSTRUCTIONS
===========================

The starting point for this project is a tiny interpreter for the
BASIC programming language. The particular dialect of BASIC that
we're implementing, and there are many dialects of BASIC, is
AppleSoft BASIC as experienced on the Apple ][ computer released
in 1977.

You'll be adding support for some missing language features.

BASIC overview
==============

First, let's give a brief overview of the language. BASIC was meant to
be a very easy language to learn. It has some similarities to Python,
like you don't need to declare variables before using them.

Back when BASIC was king of personal computer programming languages,
a full-screen text editor like you use in repl.it was considered an
amazing luxury. What you usually dealt with was BASIC's REPL
(Read-Evaluate-Print Loop). The REPL lets you enter statements, and
the language executes them immediately. Many languages like Python, 
Ruby, and BASIC have a REPL that lets you directly interact with the language.
Java didn't for a long time, although one called JShell was released
relatively recently (2017).

When you run our BASIC interpreter, you'll get the prompt "]".
This is the REPL prompt. You can enter a BASIC statement like
PRINT "Hello, world!" There are also interpreter-level commands for
loading, saving and running programs.

Writing BASIC programs CAN be done by creating a file in your repl.it
with ".basic" extension and typing it right into the file. Or, you
can do it the way we did in the '80s... you enter statements prefixed
with line numbers into the REPL, and then SAVE your program to a file.
Later, you can load it back with the LOAD command and RUN it:

You type into the REPL:
10 REM We like infinite loops!
20 PRINT "Hello, world!"
30 GOTO 20
SAVE "InfiniteHello.basic"

Later:
LOAD "InfiniteHello.basic"
RUN

Your project goal is to run two programs, Age.basic and RollSnakeEyes.basic.
Let's run through how it works:

Age.basic:
10 PRINT "HI, WHAT IS YOUR NAME?"
20 INPUT NAME$
30 PRINT "AND WHAT IS YOUR AGE?"
40 INPUT AGE
50 PRINT "HELLO, " + NAME$ + "."
60 PRINT "YOU ARE APPROXIMATELY " + (AGE*365.25*24*60*60) + " SECONDS OLD."

RollSnakeEyes.basic:
10 REM This is the BASIC equivalent of the rollSnakeEyes exercise
20 REM from the Unit 4 Test.
30 REM If you've implemented the BASIC features required for this
40 REM project, this code should run correctly.
50 REM You will need to implement the INPUT statement, RND() function,
60 REM and the IF statement.
100 PRINT "COMPUTING HOW MANY ROLLS IT TAKES TO GET SNAKE EYES"
110 ROLLS = 0
120 DIE1 = INT(RND() * 6 + 1)
130 DIE2 = INT(RND() * 6 + 1)
140 ROLLS = ROLLS + 1
150 IF DIE1 <> 1 THEN GOTO 120
160 IF DIE2 <> 1 THEN GOTO 120
170 PRINT "IT TOOK " + ROLLS + " ROLLS TO GET SNAKE EYES"

Lines are numbered. The tradition is to start at line 10 and go
in multiples of 10. Why? So if you need to stick lines in between,
you have 9 spots to choose from before you have to "renumber"
your program.

REM is a "remark", i.e. a comment. A REM line is ignored.

There are no semicolons. A statement ends with the return key.

There are two varible types: number and string. Any string variable
ends with a "$", like NAME$. In Age.basic, you can see we print stuff
with the PRINT statement, and the Scanner equivalent is the INPUT
statement.

Instead of while and for loops, you jump back to another part of the
program using the GOTO statement. On line 150 and line 160 of
RollSnakeEyes.basic, we keep the snake eyes rolling loop going by
jumping back to the start of the loop if the dice didn't come up
snake eyes.

With the project as it is, the above programs won't run because
some language features are missing, namely, INPUT, IF, and RND().

So here's what you'll actually be adding to the language.

1. INPUT statement
==================

The INPUT statement takes input from keyboard, much like Scanner in Java.

INPUT X reads a double number and stores it in variable X

INPUT NAME$ reads a string and stores it in variable NAME$

How to implement INPUT
----------------------

Lexical Scanner Support:
There are a few things you need to do. INPUT is a reserved word in BASIC,
so you need to register the new reserved word with the lexical scanner.
The lexical scanner is the part of the BASIC interpreter responsible for
turning the input characters of BASIC source into BASIC "tokens" that the
recursive descent parser can understand.

In LexicalScanner.java, find the list of reserved words and add INPUT to it.
In TokenType.java, you'll need to add INPUT to the enumerated type TokenType.

AST Node:
You'll need an AST Node for the INPUT statement. Thee INPUT statement is kind of like
assignment statement
  X = 1
  NAME$ = "Hello"
except that instead of a value on the right side, it takes its input from the user.
But both assignment statement and INPUT take a variable name.
So you can model your new class InputNode on the existing AssignmentNode class.
Copy AssignmentNode.java into a new file InputNode.java and change all
occurrences of AssignmentNode to InputNode. IdentifierNode is a good suitable
type for storing the variable name that you're storing into, so you can leave
that the same from AssignmentNode.

Next, you can take out any mention of the "right" value since there is no right
side of the assignment... you'll be reading it from the user. Then change the
evaluate() method so that instead of evaluating the right side expression and
assigning it to the variable, it reads a line of text or a double number from
the user and assigns that to the variable.

There is already a Scanner you can use in your evaluate() method by calling
context.getScanner(). If the variable name ends in "$", it's a string variable
and you can construct a Value of type string with the new Value(String s)
constructor. If the variable name doesn't end in "$", you can use
Scanner.nextDouble() to read a double from the user and construct a value
with the new Value(double d) constructor. Remember to call Scanner.nextLine()
to flush out the input. Lastly, set the variable using
context.setVariable(). This part will look much the same as it does in
AssignmentNode.

Make sure you implement the toString() method, because this is how the
LIST and SAVE commands work on your new statement type. It should return
just the string "INPUT " followed by the toString() of the IdentifierNode
for your variable name.

Parser Support:
In RecursiveDescentParser, find the statement() method. This is where
statements are parsed from tokens into AST nodes. Look at how PRINT is
implemented... if the token encountered is PRINT, we call match() to
advance to the next token and then expression() to parse the expression
to print. Here, you'll be matching the token INPUT, and then calling
identifier() to parse the variable name to put the user input in.
Then construct an InputNode with the new operator.

2. IF statement
===============

The IF statement evaluates an expression, and if the result is non-zero
(BASIC doesn't have boolean), the statement after the expression is evaluated.

  10 I=1
  20 PRINT I
  30 I=I+1
  40 IF I < 100 THEN GOTO 10

So, the grammar for the IF statement is
  IF condition_expression THEN then_statement

(Back then we didn't HAVE the optional ELSE clause! What sweet heaven it
would have been to have an ELSE clause!)

The relational operators in BASIC like < and > and = return 1 and 0
instead of "true" and "false". Note that the equality operator is just
"=", not "=="! And the does-not-equal operator is "<>", not "!=".
The relational and equality operators are already supported, so you don't
have to implement those, but you'll be able to use them in your IF
conditional expressions.

How to implement IF
-------------------

Lexical Scanner Support:
There are two new reserved words for the IF statement, IF and THEN.
Register both of these with the lexical scanner in the same way you
did INPUT.

AST Node:
Create an IfNode class which is a subclass of ASTNode. It should have
two private instance variables, "condition" and "statement".
The evaluate() method for IfNode should evaluate condition.
If the Value that comes back is non-zero, you then want to evaluate
"statement", because that's how you execute the statement when
the condition is true. You can check if the Value is non-zero using the
"isTruthy" method.

The toString() method of IfNode should return something like
  "IF " + condition + " THEN " + statement
The toString() methods of condition and statement will turn them into
strings of BASIC for you.

Parser Support:
In RecursiveDescentParser.java, you'll again add a new case to the
statement() method checking for the IF token. If you encounter an IF
token, match it with match(), and then parse the condition with
expression(). Then make sure the THEN token is there... you can check
if the token in "lookahead" is of the TokenType.THEN type. If it is,
match(), and then parse the IF's statement with statement().
(If it isn't, throw an exception. You can see lots of other exception
throws in RecursiveDescentParser.java; copy one of those.)
Lastly, construct an IfNode with the condition and statement you found.

3. RND() function
=================

The RND() function returns a random number x such that
  x >= 0.0 and x < 1.0
Sound familiar? It's exactly the same as Math.random()

This one is easier because it's just a built-in function, so it doesn't
need any additions to the lexical scanner or the recursive descent parser.
Look in FunctionCallNode.java and follow the model for functions such
as INT. The difference is that RND() doesn't even take any arguments
so you don't need to deal with those. Just call Math.random() and set
that as the returned value from the function call.
Note that this language is so basic (ha) that you can't define your
own user-defined functions. Only built-in ones can be used in expressions.

Testing Your Work
=================

If you've implemented the above 3 language features correctly, you
should be able to run the Age.basic program and the RollSnakeEyes.basic
program by loading them in with the LOAD command and running them with
the RUN command. You should also be able to list out their source with
the LIST command, and the output of LIST should match what's in the
.basic files they came from.
You can do this by running your program and then typing

 LOAD "Age.basic"
 LIST
 RUN

 LOAD "RollSnakeEyes.basic"
 LIST
 RUN

Write Your Own BASIC Program
============================

LASTLY, write a program of your own in BASIC and save it in your
replit. Your program can do anything... it could be a translation of
an earlier exercise we did from Java into BASIC, for example.

Of course, if you finish all this, you are welcome to add any other
language features that you like. You may find that you need additional
language features to support a program that you want to write in BASIC.

THE REAL DEAL
=============

If you want to try the real original AppleSoft BASIC that ran on the
Apple ][ computer, you can do so by going to this URL:
https://www.scullinsteel.com/apple2/

There is a language reference for it here:

https://www.calormen.com/jsbasic/reference.html

... and if you're curious, there's even a PDF of the original
spiral-bound BASIC manual that came with the computer:

https://mirrors.apple2.org.za/Apple%20II%20Documentation%20Project/Software/Languages/Applesoft%20BASIC/Manuals/Applesoft%20II%20BASIC%20Programming%20Reference%20Manual.pdf

Initially, the computer is trying to boot off its virtual disk drive.
IRL the motor of the disk drive would be going. Click the RESET button
on the virtual keyboard to get to an AppleSoft BASIC prompt, "]".
Then you can type BASIC commands like PRINT "HELLO, WORLD".
Note the computer only has uppercase letters. There isn't even a
CAPS LOCK key on the keyboard!

