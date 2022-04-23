public class lexeme extends tokens{
    private final Tokens token;
    private final String data;

    lexeme(Tokens token, String data) {
        this.token = token;
        this.data = data;
    }

    Tokens getToken(){
        return token;
    }

    String getData(){
        return data;
    }

    public void print(){
        System.out.println("{" + token + ", " + data + "}");
    }
}

