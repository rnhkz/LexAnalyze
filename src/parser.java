public class parser extends tokens{
    private lexeme currentLexeme;
    private final lexer _lexer;

    parser(String fileName){
        _lexer = new lexer(fileName);
        currentLexeme = _lexer.lex();
    }

    private boolean parseExpr(){
        if(parseExprN()){
            lex();
            return parseExprB();
        }
        return false;
    }

    private boolean parseAssign(){
        if(currentLexeme.getToken() == Tokens.ID){
            lex();
            if(currentLexeme.getToken() == Tokens.EQUAL){
                lex();
                return parseExpr();
            }
        }
        return false;
    }

    private boolean parseExprB(){
        if(currentLexeme.getToken() == Tokens.KEYWORD) {
            if (currentLexeme.getData().equals("and") || currentLexeme.getData().equals("or")) {
                lex();
                return parseExprN();
            }
        }
        _lexer.xel(currentLexeme);
        return true;
    }

    private boolean parseExprN(){
        if(parseTerm()){
            lex();
            return parseExprT();
        }
        return false;
    }

    private boolean parseExprT(){
        switch (currentLexeme.getToken()) {
            case PLUS, MINUS -> {
                lex();
                return parseVal();
            }
            default -> {
                _lexer.xel(currentLexeme);
                return true;
            }
        }
    }

    private boolean parseTerm(){
        if(parseFactor()){
            lex();
            return parseExprF();
        }
        return error("Factor");
    }

    private boolean parseExprF(){
        switch (currentLexeme.getToken()) {
            case MULT, DIV, MOD -> {
                lex();
                return parseTerm();
            }
            default -> {
                _lexer.xel(currentLexeme);
                return true;
            }
        }
    }

    private boolean parseFactor(){
        if(parseVal()){
            lex();
            return parseExprV();
        }
        return error("Expected value");
    }

    private boolean parseExprV(){
        switch (currentLexeme.getToken()) {
            case LT, LE, GE, GT, IS_EQUAL_TO, NOT_EQUAL -> {
                lex();
                return parseVal();
            }
            default -> {
                _lexer.xel(currentLexeme);
                return true;
            }
        }
    }

    private boolean parseVal(){
        if(currentLexeme.getToken() == Tokens.KEYWORD && currentLexeme.getData().equals("not") ||
           currentLexeme.getToken() == Tokens.MINUS){
            lex();
            return parseVal();
        }
        if(currentLexeme.getToken() == Tokens.L_PARENTHESIS) {
            lex();
            if (parseExpr()) {
                lex();
                if (currentLexeme.getToken() == Tokens.R_PARENTHESIS)
                    return true;
                else
                    return error("Missing end parenthesis");
            }
        }
        return currentLexeme.getToken() == Tokens.ID || currentLexeme.getToken() == Tokens.INT ||
                currentLexeme.getToken() == Tokens.BOOLEAN;
    }

    private boolean parseIf(){
        lex();
        if(parseExpr()){
            lex();
            if(currentLexeme.getToken() == Tokens.KEYWORD && currentLexeme.getData().equals("then")){
                lex();
                if(parseProg()){
                    lex();
                    if(currentLexeme.getToken() == Tokens.KEYWORD && currentLexeme.getData().equals("else")){
                        lex();
                        if(parseProg()){
                            lex();
                            return currentLexeme.getToken() == Tokens.KEYWORD && currentLexeme.getData().equals("end");
                        }
                        else
                            return error("Expected \"end\"");
                    }
                    else
                        return error("Expected \"else\"");
                }
            }
            else
                return error("Expected \"then\"");
        }
        else
            return error("Expected expression after \"if\"");
        return false;
    }

    private boolean parseWhile(){
        lex();
        if(parseExpr()){
            lex();
            if(currentLexeme.getToken() == Tokens.KEYWORD && currentLexeme.getData().equals("do")){
                lex();
                if(parseProg()){
                    lex();
                    return currentLexeme.getToken() == Tokens.KEYWORD && currentLexeme.getData().equals("end");
                }
                else
                    return error("Expected \"end\"");
            }
            return error("Expected expression after \"do\"");
        }
        else
            return error("Expected expression after \"while\"");
    }

    private boolean parseFor(){
        lex();
        if(parseAssign()) {
            lex();
            if(currentLexeme.getToken() == Tokens.END_OF_LINE) {
                lex();
                if (parseExpr()) {
                    lex();
                    if (currentLexeme.getToken() == Tokens.END_OF_LINE) {
                        lex();
                        if (parseAssign()) {
                            lex();
                            if (currentLexeme.getToken() == Tokens.END_OF_LINE) {
                                lex();
                                if (currentLexeme.getToken() == Tokens.KEYWORD && currentLexeme.getData().equals("do")) {
                                    lex();
                                    if (parseProg()) {
                                        lex();
                                        return currentLexeme.getToken() == Tokens.KEYWORD && currentLexeme.getData().equals("end");
                                    } else
                                        return error("Expected \"end\"");
                                }
                                return error("Expected expression after \"do\"");
                            }
                        }
                    }
                }
            }
        }
        else
            return error("Expected assignment after \"for\"");
        return false;
    }

    private boolean parseCommand(){
        if(currentLexeme.getToken() == Tokens.ID){
            lex();
            if(currentLexeme.getToken() == Tokens.EQUAL){
                lex();
                return parseExpr();
            }
        }
        else if(currentLexeme.getToken() == Tokens.KEYWORD)
            for (String keyword : tokens.Keywords) {
                if (currentLexeme.getData().equals(keyword))
                    switch (currentLexeme.getData()) {
                        case "print":
                            lex();
                            if (currentLexeme.getToken() == Tokens.STRING)
                                return true;
                            else
                                return parseExpr();
                        case "get":
                            lex();
                            if (currentLexeme.getToken() == Tokens.ID)
                                return true;
                            else
                                return error("Expected ID");
                        case "if":
                            return parseIf();
                        case "while":
                            return parseWhile();
                        case "for":
                            return parseFor();
                    }
            }
        else
            return error("Expected Command");
        return false;
    }

    boolean parseProg(){
        if(parseCommand()){
            lex();
            if(currentLexeme.getToken() == Tokens.END_OF_LINE){
                lex();
                if(currentLexeme.getToken() == Tokens.END_OF_INPUT)
                    return true;
                if(currentLexeme.getToken() == Tokens.KEYWORD)
                    if(currentLexeme.getData().equals("else") || currentLexeme.getData().equals("end") || currentLexeme.getData().equals("do")){
                        _lexer.xel(currentLexeme);
                        return true;
                    }
                return parseProg();
            }
            else
                return error("End of line \";\" expected");
        }
        return false;
    }

    private void lex(){
        currentLexeme = _lexer.lex();
    }

    boolean error(String msg){
        System.out.print("Error line " + _lexer.getLineNum() + ": " + msg + ". ");
        currentLexeme.print();
        return false;
    }
}