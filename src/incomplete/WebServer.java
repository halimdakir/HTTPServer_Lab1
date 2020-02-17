package incomplete;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class WebServer {
    ServerSocket serverSocket;
    public static void main(String[] args) throws IOException {
       new WebServer().runServer();
    }

    private void runServer() throws IOException {
        serverSocket = new ServerSocket(8080);
        clientRequest();
    }
    private void clientRequest() throws IOException {
        while (true){
            Socket socket = serverSocket.accept();
            ConnectionHandler connectionHandler = new ConnectionHandler(socket);
            connectionHandler.start();

        }
    }


}
