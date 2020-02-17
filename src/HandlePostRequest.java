import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class HandlePostRequest {

        static void convertToJson(String str) throws IOException {
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
                toFile(json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }


        static String getSubmittedData(BufferedReader bufferedReader) throws IOException {
            List<String>list = new ArrayList<>();
            int Length = -1;
            while (true) {
                final String line = bufferedReader.readLine();
                final String contentLengthStr = "Content-Length: ";
                if (line.startsWith(contentLengthStr)) {
                    Length = Integer.parseInt(line.substring(contentLengthStr.length()));
                }
                if (line.length() == 0) {
                    break;
                }
            }
            final char[] content = new char[Length];
            bufferedReader.read(content);
            list.add(new String(content));
            return list.get(list.size()-1);
        }

        private static void toFile(String str) throws IOException {
            File file = new File("./resources/file.json");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (FileWriter out = new FileWriter(file)) {
                out.write("["+str+"]");
            } catch (IOException ex) {
                System.out.println("Error!");
            }
        }
}
