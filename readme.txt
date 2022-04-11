//Albert Baker
//alb190010
//CE4337.501

1. What does this do?
    Simulates a lexer conforming to an attributed grammar.

2. How to use:
    Compile:

        >javac Runner.java lexer.java

        Tested with javac 16.0.2

    lexer class contains main test method for printing tokens.
    Runner class can lex a complete file when passed a compatible file name as an argument.

3. To run the lexer test methods:

    >java lexer

    Expected: Print out of all tokens to command prompt. Print out of string preprocessing test to command prompt.

4. To use lexer with input file (experimental)

    >java Runner <FILENAME> (defaults to "test2.prog")

    Expected: Print out of all lexemes in file to command prompt. Assumes input file follows proper semantics/grammar.
              Error if file does not exist/can't be read.