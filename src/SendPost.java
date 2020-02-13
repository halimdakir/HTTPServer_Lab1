import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SendPost {
    private double API_VERSION = 0;
    private String API ="";
    private String METHOD="POST";
    private String TYPE = "application/x-www-form-urlencoded";
    private String USER_AGENT ="Mozilla/5.0";
    private String data = "";
    private URL connection;
    private HttpURLConnection finalConnection;

    private HashMap<String, String> fields = new HashMap<>();

    public SendPost() {
    }

    public SendPost(String[] endpoints, String url, double version){
        this.API_VERSION = version;
        this.API = url;
        fields.put("version", String.valueOf(version));
        for (int i = 0; i< endpoints.length; i++){
            String [] points = endpoints[i].split(",");
            for (int j = 0; j<points.length; j++){
                fields.put(points[j].split(":")[0],points[j].split(":")[1]);
            }
        }
    }
    // data how it looks /submit.php?company=livs&contact=halim+halim&country=italy
    public String[] parseData(String str){
        int index = str.indexOf("?");
        System.out.println(index);
        String newStr = str.substring(index + 1);
        String[] points = newStr.split("&");
        String[] endPoins = new String[points.length];
        for (int j = 0; j < points.length; j++) {
            endPoins[j] = points[j].replace("=", ":").replace("+", " ");
        }
        return  endPoins;
    }

    public String buildConnection(){
        StringBuilder content = new StringBuilder();
        if (!this.getEndpoints().equalsIgnoreCase("") && !this.getEndpoints().isEmpty()){
            String vars ="";
            String vals = "";
            try {
                for (Map.Entry<String, String> entry : fields.entrySet()){
                    vars = entry.getKey();
                    vals = entry.getValue();
                    data += ("&"+vars+"="+vals);
                }
                if (data.startsWith("&")){
                    data = data.replace("&", "");
                }
                connection = new URL(API);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader( readWithAccess(connection, data)));
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    content.append(line+"\n");
                }
                bufferedReader.close();
                return content.toString();
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }else {
            return null;
        }
        return null;
    }

    private InputStream readWithAccess(URL url, String data){
        try {
            byte[] out = data.toString().getBytes();
            finalConnection = (HttpURLConnection) url.openConnection();
            finalConnection.setRequestMethod(METHOD);
            finalConnection.setDoOutput(true);
            finalConnection.setRequestProperty("User-Agent", USER_AGENT);
            finalConnection.setRequestProperty("Content-Type", TYPE);
            finalConnection.connect();

            try {
                OutputStream outputStream = finalConnection.getOutputStream();
                outputStream.write(out);
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
            return finalConnection.getInputStream();
        }catch (Exception e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    public String getApiVersion(){
        return String.valueOf(API_VERSION);
    }

    public String getEndpoints() {
        return fields.toString();
    }
    public String getEndpointValue(String key){
        return fields.get(key);
    }
    public void setUserAgent(String userAgent){
        this.USER_AGENT = userAgent;
    }
    public void setMethod(String method){
        this.METHOD = method;
    }
    public void setSubmitionType(String type){
        this.TYPE = type;
    }





}
