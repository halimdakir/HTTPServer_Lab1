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
            String input = in.readLine();
            StringTokenizer parse = new StringTokenizer(input);
            String methodString = parse.nextToken().toUpperCase();
            fileRequested = parse.nextToken().toLowerCase();


            if (methodString.equals("GET")) {

                //if (!fileRequested.contains("?")) {
                if (fileRequested.endsWith("/")) {
                    fileRequested += INDEX;
                }
                File file = new File(FILE_ROOT, fileRequested);
                int fileLength = (int) file.length();
                String content = HandleServer.getInstance().getTypeOfContent(fileRequested);
                byte[] fileData = HandleServer.getInstance().readData(file, fileLength);
                HandleServer.getInstance().hTTP200Ok(out, dataOut, fileData, content, fileLength);
                if (prolix) {
                    System.out.println("File " + fileRequested + " of type " + content + " returned");
                }
                else if (!fileRequested.contains("?")){
                    HandleServer.getInstance().fileNotFound(FILE_ROOT, ERROR_404, out, dataOut, fileRequested);
                }

                //} else {
            }else if (methodString.equals("POST")){
               // TimeUnit.SECONDS.sleep(10);

               SendPost sendPost = new SendPost();
                //String str = httpRequestBodyWriter
                String[] fileData = sendPost.parseData(fileRequested);
                SendPost connection = new SendPost(fileData, "localhost://" + PORT, 2.0);
                System.out.println(connection.getEndpoints());

                fileRequested = INDEX;
                File file = new File(FILE_ROOT, fileRequested);
                int fileLength = (int) file.length();
                String content = HandleServer.getInstance().getTypeOfContent(fileRequested);
                byte[] fileData2 = HandleServer.getInstance().readData(file, fileLength);
                HandleServer.getInstance().hTTP200Ok(out, dataOut, fileData2, content, fileLength);
                if (prolix) {
                    System.out.println("File " + fileRequested + " of type " + content + " returned");
                }

                // new way

                // Define the server endpoint to send the HTTP request to

                /*try {
                    URL serverUrl =
                            new URL("http://localhost:8080/form.html");
                    HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();

                    // Indicate that we want to write to the HTTP request body
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");

                    // Writing the post data to the HTTP request body
                    BufferedWriter httpRequestBodyWriter =
                            new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
                    httpRequestBodyWriter.write("visitorName=Johnny+Jacobs&luckyNumber=1234");
                    httpRequestBodyWriter.close();

                    // Reading from the HTTP response body
                    Scanner httpResponseScanner = new Scanner(urlConnection.getInputStream());
                    while (httpResponseScanner.hasNextLine()) {
                        System.out.println(httpResponseScanner.nextLine());
                    }
                    httpResponseScanner.close();
                }catch (Exception e){
                    System.err.println("Server error : " + e);
            }*/


            }

        } catch(IOException ioe){
            System.err.println("Server error : " + ioe);
        }finally{
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







    public static void main(String[] args) throws IOException {
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
            }


        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }

    }

}
