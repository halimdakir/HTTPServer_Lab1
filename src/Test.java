import java.util.StringTokenizer;

public class Test {
    public static void main(String[] args) {
        StringTokenizer parse = new StringTokenizer("GET / HTTP/1.1");
        System.out.println(parse.nextToken());
        System.out.println(parse.nextToken());

    }
}
