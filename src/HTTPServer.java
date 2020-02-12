import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HTTPServer implements Runnable{
    static final int PORT = 8080;
    static final String INDEX = "index.html";
    static final String ERROR_404 = "404.html";
    static final String NOT_SUPPORTED = "not_supported.html";
    static final File FILE_ROOT = new File(".");
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
                            byte[] fileData = readData(file, fileLength);
                            // Send HTTP Headers
                            out.println("HTTP/1.1 Not Implemented - HTTP 501 Error");
                            out.println("Server: Java HTTP Server 1.0");
                            out.println("Date: " + new Date());
                            out.println("Content-type: " + contentMimeType);
                            out.println("Content-length: " + fileLength);
                            out.println();
                            out.flush();

                            dataOut.write(fileData, 0, fileLength);
                            dataOut.flush();

            } else {
                if (fileRequested.endsWith("/")) {
                    fileRequested += INDEX;
                }

                File file = new File(FILE_ROOT, fileRequested);
                int fileLength = (int) file.length();
                String content = getTypeOfContent(fileRequested);
                if (methodString.equals("GET")) {
                    byte[] fileData = readData(file, fileLength);

                    // send HTTP Headers
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Java HTTP Server 1.0");
                    out.println("Date: " + new Date());
                    out.println("Content-type: " + content);
                    out.println("Content-length: " + fileLength);
                    out.println();
                    out.flush();

                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();
                }

                if (prolix) {
                    System.out.println("File " + fileRequested + " of type " + content + " returned");
                }
            }


        } catch (FileNotFoundException e) {
            try {
                fileNotFound(out, dataOut, fileRequested);
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

    private byte[] readData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }
 // support only HTML

    private String getTypeOfContent(String fileRequested) {
        if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
            return "text/html";
        else if (fileRequested.endsWith(".pdf"))
            return "application/pdf";
        else if (fileRequested.endsWith(".css"))
            return "text/css";
        else if (fileRequested.endsWith(".jpeg"))
        return "image/jpeg";
        else if (fileRequested.endsWith(".jpg"))
            return "image/jpg";
        else if (fileRequested.endsWith(".png") || fileRequested.endsWith(".PNG"))
            return "image/png";
        else if (fileRequested.endsWith(".json"))
            return "application/json";
        else if (fileRequested.endsWith(".js"))
            return "application/javascript";
        else
            return "text/plain";
    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(FILE_ROOT, ERROR_404);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readData(file, fileLength);
        out.println("HTTP/1.1 404 File Not Found");
        out.println("Server: Java HTTP Server 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println();   // blank line is very important between header and content!
        out.flush();
        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (prolix) {
            System.out.println("File " + fileRequested + " not found");
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
