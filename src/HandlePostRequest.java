import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class HandlePostRequest {
        static void convertToJson(String str){
            String[] points = str.split("&");
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

        static String getSubmittedData(BufferedReader bufferedReader) throws IOException {
            List<String> list = new ArrayList<>();
            try {
                Stream<String> stringStream = bufferedReader.lines();
                String[] result = stringStream
                        .toArray(String[]::new);
                for (String s : result) {
                    if (s != null && s.length() > 0) {
                        list.add(s);
                    }
                }
            }finally {
                if (bufferedReader != null)
                    bufferedReader.close();
            }
            return list.get(list.size()-1);
        }
}
