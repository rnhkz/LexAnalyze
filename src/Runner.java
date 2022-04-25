public class Runner {
    public static void main(String[] args) {
        Runner r = new Runner();
        if(args.length == 0)
            args = new String[] {"helloworld.prog", "test2.prog", "test3.prog", "test4.prog", "test5.prog"};
        r.begin(args);
    }

    public void begin(String[] args){
        parser parse;
        for(String fileName : args) {
            parse = new parser(fileName);
            System.out.printf("Input %s:\t", fileName);
            if(parse.parseProg())
                System.out.println("Program is Correct.");
        }
        System.out.print("end");
    }
}


