public class Runner {
    public static void main(String[] args) {
        Runner r = new Runner();
        if(args.length == 1)
            r.begin(args[0]);
        else
            r.begin("test2.prog");
    }

    public void begin(String fileName){
        lexer lex = new lexer();
        lex.printAllTokens();
        lex.testStringPreprocessing();

        lex = new lexer(fileName);
        lex.showInput();
        lex.lexFull();
    }
}


