import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
    public static void main(String[] args) {
        String str = "/submit.php?company=livs&contact=halim+halim&country=italy";
        int index2 = str.indexOf("@");
        System.out.println(index2);
        int index = str.indexOf("?");
        System.out.println(index);
        String newStr = str.substring(index + 1);
        String[] points = newStr.split("&");
        String[] endPoins = new String[points.length];
        for (int j = 0; j < points.length; j++) {
            endPoins[j] = points[j].replace("=", ":").replace("+", " ");
        }
        Map<String, String> map = new HashMap<>();
        for (int j = 0; j < endPoins.length; j++) {
            map.put(endPoins[j].split(":")[0], endPoins[j].split(":")[1]);
        }
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String json = objectMapper.writeValueAsString(map);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
