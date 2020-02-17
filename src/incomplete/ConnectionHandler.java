package incomplete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler extends Thread{
    Socket socket;
    PrintWriter printWriter;
    BufferedReader bufferedReader;

    public ConnectionHandler(Socket socket) throws IOException {
        this.socket = socket;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            String request = "";
            while (bufferedReader.ready())
                request += (char) bufferedReader.read();
            System.out.println(request);
            HttpRequest httpRequest = new HttpRequest(request);
            HttpReponse httpReponse = new HttpReponse(httpRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
