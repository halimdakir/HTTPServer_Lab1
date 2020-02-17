package incomplete;

public class HttpRequest {
    String fileName;
    public HttpRequest(String request) {
        String[] line = request.split("\n");
        fileName = line[0].split(" ")[1];
    }
}
