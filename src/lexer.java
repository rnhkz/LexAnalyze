import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class lexer extends tokens {
    int lineNum;
    String input;

    lexer(){
        lineNum = 1;
        input = "";
    }

    lexer(String fileName){
        lineNum = 1;
        try {
            File f = new File(fileName);
            Scanner scan = new Scanner(f);
            StringBuilder s = new StringBuilder();
            while(scan.hasNextLine()){
                s.append(scan.nextLine()).append("\n");
            }
            input = s.substring(0,s.length()-1);
            scan.close();
        }
        catch(FileNotFoundException e){
            System.out.println("Error: Reading file \"" + fileName + "\".");
            e.printStackTrace();
        }
   }

    lexeme lexInt(){
        int x = 1;
        while(Character.isDigit(input.charAt(x)))
            x++;
        String s = input.substring(0,x);
        input = input.substring(x);
        return new lexeme(Tokens.INT, s);
    }

    lexeme lexString(){
        StringBuilder s = new StringBuilder();
        input = input.substring(1);
        while(input.charAt(0) != '"'){
            if(input.charAt(0) == '\\'){
                switch (input.charAt(1)) {
                    case '\\' -> {
                        s.append("\\");
                        input = input.substring(2);
                    }
                    case 't' -> {
                        s.append("\t");
                        input = input.substring(2);
                    }
                    case 'n' -> {
                        s.append("\n");
                        input = input.substring(2);
                    }
                    case '"' -> {
                        s.append("\"");
                        input = input.substring(2);
                    }
                    default -> input = input.substring(2);
                }
            }
            else{
                s.append(input.charAt(0));
                input = input.substring(1);
            }
        }
        input = input.substring(input.indexOf('"')+1);
        return new lexeme(Tokens.STRING, s.toString());
    }

    lexeme lexIDorKeyword(){
        int x=0;
        while(input.charAt(x) == '_'||Character.isAlphabetic(input.charAt(x))||Character.isDigit(input.charAt(x))){
            x++;
        }
        if(input.charAt(x-1)==';'||input.charAt(x-1)==')')
            x--;
        String s = input.substring(0,x);
        input = input.substring(x);
        for(String keyword : Keywords)
        if(s.equals(keyword))
            return new lexeme(Tokens.KEYWORD, s);
        if (s.length()==1){
            return new lexeme(Tokens.ID, s);
        }
        if(s.charAt(1) == '_'||Character.isAlphabetic(s.charAt(1))||Character.isDigit(s.charAt(1)))
            return new lexeme(Tokens.ID, s);
        return new lexeme(Tokens.ERROR, s);
    }

    lexeme lexSymbol(Tokens token){
        int x = 1;
        if(token == Tokens.GE||token == Tokens.LE||token == Tokens.IS_EQUAL_TO||token == Tokens.NOT_EQUAL)
            x++;
        String s = input.substring(0,x);
        input = input.substring(x);
        return new lexeme(token, s);
    }

    public lexeme lex(){
        if (input.isEmpty()){
            return new lexeme(Tokens.END_OF_INPUT, "");
        }
        while(input.charAt(0) == ' ' || input.charAt(0) == '\n'){
            if(input.charAt(0) == '\n'){
                lineNum++;
            }
            input = input.substring(1);
        }
        switch(input.charAt(0)){
            case '+':
                return lexSymbol(Tokens.PLUS);
            case '-':
                return lexSymbol(Tokens.MINUS);
            case '*':
                return lexSymbol(Tokens.MULT);
            case '/':
                return lexSymbol(Tokens.DIV);
            case '%':
                return lexSymbol(Tokens.MOD);
            case '>':
                if(1 < input.length())
                    if(input.charAt(1)== '=')
                        return lexSymbol(Tokens.GE);
                return lexSymbol(Tokens.GT);
            case '<':
                if(1 < input.length())
                    if(input.charAt(1) == '=')
                        return lexSymbol(Tokens.LE);
                return lexSymbol(Tokens.LT);
            case '=':
                if(1 < input.length())
                    if(input.charAt(1) == '=')
                        return lexSymbol(Tokens.IS_EQUAL_TO);
                return lexSymbol(Tokens.EQUAL);
            case '!':
                if(input.charAt(1) == '=')
                    return lexSymbol(Tokens.NOT_EQUAL);
                return new lexeme(Tokens.ERROR, input);
            case '(':
                return lexSymbol(Tokens.L_PARENTHESIS);
            case ')':
                return lexSymbol(Tokens.R_PARENTHESIS);
            case ';':
                return lexSymbol(Tokens.END_OF_LINE);
            case '"':
                return lexString();
            default:
                break;
        }
        if(Character.isDigit(input.charAt(0)))
            return lexInt();
        if(input.charAt(0) == '_'||Character.isAlphabetic(input.charAt(0)))
            return lexIDorKeyword();
        return new lexeme(Tokens.ERROR, input);
    }

    public void xel(lexeme toPushBack){
        input = toPushBack.getData() + input;
    } //Pushes a parsed lexeme back into the front of the input.

    public int getLineNum(){
        return lineNum;
    }
    /**==================TEST FUNCTIONS===================**/
    public void showInput(){
        System.out.print(input);
    }

    void printAllTokens(){
        System.out.println("=====PrintAllTokens=====");
        input = "+ - * / % > >= < <= = != ( ) _a -5 \"Hello World!\" == print ;";
        while(input.length() != 0){
            lexeme test = lex();
            test.print();
        }
        System.out.println("========================");
    }

    void testStringPreprocessing(){
        System.out.println("=====StringTest=====");
        input = "\"\\test\\ntest\\ttest\"";
        while(input.length() != 0){
            lexeme test = lex();
            test.print();
        }
        System.out.println("====================");
    }

    public void lexFull(){
        ArrayList<lexeme> lexemes = new ArrayList<>();
        while(input.length() > 0)
            while(input.length() != 0){
                lexeme test = lex();
                lexemes.add(test);
                test.print();
            }
    }
}
