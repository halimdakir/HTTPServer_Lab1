package incomplete;

import java.io.File;
import java.io.FileInputStream;

public class HttpReponse {
    HttpRequest req;
    String response;
    String root = ".\\resources";

    public HttpReponse(HttpRequest httpRequest) {
        req = httpRequest;
        File file = new File(root+req.fileName);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
