//Albert Baker
//alb190010
//CS4337.501

1. What does this do?
    Simulates a lexer conforming to an attributed grammar.

2. How to use:
    2.1. Compile:

        Compile with command:
            >javac Runner.java lexer.java parser.java

        Tested with javac 16.0.2

    Runner class can lex a complete file when passed a compatible file name as an argument.

    2.2. Execute:

        To run with input:
            >java Runner <FILENAME>
            >java Runner <FILENAME> <FILENAME> <FILENAME> ...

        Expected: Parses file(s), checking for correct syntax.

            Example output:
            If correct
                "Input helloworld.prog:	Program is Correct."
            If incorrect
                "Input test4.prog:	Error line 21: End of line ";" expected. {END_OF_INPUT, }"

        The program can execute with no arguments. It will default to checking the four test programs provided (helloworld, test2-4).

            >java Runner

        *test4.prog is supposed to fail.