import java.io.*;
import java.util.Date;

public class HandleServer {
    private static HandleServer handleServer= null;
    private HandleServer() {
    }
    public static HandleServer getInstance(){
        if (handleServer == null)
            handleServer = new HandleServer();
        return handleServer;
    }

    public void fileNotFound(File FILE_ROOT, String fileName, PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(FILE_ROOT, fileName);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readData(file, fileLength);
        out.println("HTTP/1.1 404 File Not Found");
        out.println("Server: Java HTTP Server 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println();
        out.flush();
        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (HTTPServer.prolix) {
            System.out.println("File " + fileRequested + " not found");
        }
    }

    public String getTypeOfContent(String fileRequested) {
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
        else if (fileRequested.endsWith(".php"))
            return "text/php";
        else
            return "text/plain";
    }

    public byte[] readData(File file, int fileLength) throws IOException {
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

    public void hTTP501Error(PrintWriter out, OutputStream dataOut,byte[] fileData, String contentMimeType, int fileLength) throws IOException {
        out.println("HTTP/1.1 501 Not Implemented");
        out.println("Server: Java HTTP Server 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + contentMimeType);
        out.println("Content-length: " + fileLength);
        out.println();
        out.flush();

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();
    }
    public void hTTP200Ok(PrintWriter out, OutputStream dataOut,byte[] fileData, String content, int fileLength) throws IOException {
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
    public void hTTP200OkPost(PrintWriter out, OutputStream dataOut,byte [] str, int fileLength) throws IOException {
        out.println("HTTP/1.1 200 OK");
        out.println("Server: Java HTTP Server 1.0");
        out.println("Date: " + new Date());
        out.println("Content-Type: application/json");
        out.println("Content-length: " + fileLength);
        out.println();
        out.flush();

        dataOut.write(str, 0, fileLength);
        dataOut.flush();
    }

}
