import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HTTPServer implements Runnable{
    static final int PORT = 8080;
    static final String INDEX = "index.html";
    static final String ERROR_404 = "404.html";
    static final File FILE_ROOT = new File(".\\resources");
    static final boolean prolix = true;
    private Socket socket;


    public HTTPServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        BufferedOutputStream bufferedOutputStream = null;
        String fileRequested = null;

        try {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(socket.getOutputStream());
                bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
                String input = bufferedReader.readLine();
                StringTokenizer parse = new StringTokenizer(input);
                String methodRequested = parse.nextToken().toUpperCase();
                fileRequested = parse.nextToken().toLowerCase();


              if (methodRequested.equals("GET")) {
                    if (fileRequested.endsWith("/")) {
                        fileRequested += INDEX;}
                    File file = new File(FILE_ROOT, fileRequested);
                    int fileLength = (int) file.length();
                    String content = HTTPResponse.getContentType(fileRequested);
                    byte[] fileData = HTTPResponse.readData(file, fileLength);
                    HTTPResponse.hTTP200Ok(printWriter, bufferedOutputStream, fileData, content, fileLength, fileRequested);

                }else if (methodRequested.equals("POST")){
                  HandlePostRequest.convertToJson(HandlePostRequest.getSubmittedData(bufferedReader));  //Find the Json format of the form submitted in the console ...
                  fileRequested = "/file.json";
                  File file = new File(FILE_ROOT, fileRequested);                                       //Redirection page
                  int fileLength = (int) file.length();
                  String content = HTTPResponse.getContentType(fileRequested);
                  byte[] fileData = HTTPResponse.readData(file, fileLength);
                  HTTPResponse.hTTP200Ok(printWriter, bufferedOutputStream, fileData, content, fileLength, fileRequested);
              }

        } catch (FileNotFoundException fileNotFound) {
                try {
                    assert printWriter != null;
                    assert bufferedOutputStream != null;
                    HTTPResponse.fileNotFound(FILE_ROOT, ERROR_404, printWriter, bufferedOutputStream, fileRequested);
                } catch (IOException e) {
                    System.err.println("Error with file not found exception : " + e.getMessage());
                }
        } catch (IOException e) {
                    System.err.println("Server error : " + e.getMessage());
        } finally {
                    try {
                        assert bufferedReader != null;
                        bufferedReader.close();
                        assert printWriter != null;
                        printWriter.close();
                        assert bufferedOutputStream != null;
                        bufferedOutputStream.close();
                        socket.close();
                    } catch (Exception e) {
                        System.err.println("Error closing stream : " + e.getMessage());
                    }
                    if (prolix) {
                        System.out.println("Connection closed.\n");
                    }
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server successfully started!\nConnection on port : " + PORT + "\n");
            while (true) {
                HTTPServer httpServer = new HTTPServer(serverSocket.accept());
                if (prolix) {
                    System.out.println("Connection opened. (" + new Date() + ")");
                }
                ExecutorService executorService = Executors.newFixedThreadPool(10);
                executorService.submit(httpServer);
            }
        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }

}
