public class lexeme extends tokens{
    private final Tokens token;
    private final String data;

    lexeme(Tokens token, String data) {
        this.token = token;
        this.data = data;
    }

    public void print(){
        System.out.println("{" + token + ", " + data + "}");
    }
}

