import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HTTPServer implements Runnable{
    static final int PORT = 8080;
    static final String INDEX = "index.html";
    static final String ERROR_404 = "404.html";
    static final String NOT_SUPPORTED = "not_supported.html";
    static final File FILE_ROOT = new File(".\\resources");
    static final boolean prolix = true;
    private Socket socket;


    public HTTPServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;


        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            dataOut = new BufferedOutputStream(socket.getOutputStream());
            //Get first line of the client request
            String input = in.readLine();
            StringTokenizer parse = new StringTokenizer(input);
            String methodString = parse.nextToken().toUpperCase();
            fileRequested = parse.nextToken().toLowerCase();


            if (!methodString.equals("GET")  &&  !methodString.equals("HEAD")) {
                            if (prolix) {
                                System.out.println("Not Implemented - HTTP 501 Error: " + methodString + " method.");
                            }
                            File file = new File(FILE_ROOT, NOT_SUPPORTED);
                            int fileLength = (int) file.length();
                            String contentMimeType = "text/html";
                            //read content to return to client
                            byte[] fileData = HandleServer.getInstance().readData(file, fileLength);
                            HandleServer.getInstance().hTTP501Error(out, dataOut, fileData, contentMimeType, fileLength);

            } else {
                if (fileRequested.endsWith("/")) {
                    fileRequested += INDEX;
                }

                File file = new File(FILE_ROOT, fileRequested);
                int fileLength = (int) file.length();
                String content = HandleServer.getInstance().getTypeOfContent(fileRequested);
                if (methodString.equals("GET")) {
                    byte[] fileData = HandleServer.getInstance().readData(file, fileLength);
                    HandleServer.getInstance().hTTP200Ok(out, dataOut, fileData, content, fileLength);
                }

                if (prolix) {
                    System.out.println("File " + fileRequested + " of type " + content + " returned");
                }
            }


        } catch (FileNotFoundException e) {
            try {
                HandleServer.getInstance().fileNotFound(FILE_ROOT, ERROR_404, out, dataOut, fileRequested);
            } catch (IOException ioe) {
                System.err.println("Error with file not found! exception : " + ioe.getMessage());
            }

        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                socket.close();
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (prolix) {
                System.out.println("Connection closed!\n");
            }
        }


    }



    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server successfully started!\nConnection on port : " + PORT + "\n");
            // until the user stops running the server
            while (true) {
                HTTPServer httpServer = new HTTPServer(serverSocket.accept());
                if (prolix) {
                    System.out.println("Connection opened. (" + new Date() + ")");
                }
                // create thread to manage the client connection
                ExecutorService executorService = Executors.newFixedThreadPool(10);
                executorService.submit(httpServer);
                //Thread thread = new Thread(httpServer);
                //thread.start();
            }
        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }

}
