import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

//@WebServlet("/registration")
public class FormData {

    protected void dataForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = "";

        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String phone = request.getParameter("phone");
        String school = request.getParameter("school");

        Map<String,String> map = new HashMap<>();
        map.put("Name", name);
        map.put("Address", address);
        map.put("City", city);
        map.put("Country", country);
        map.put("Phone", phone);
        map.put("School", school);



        ObjectMapper objectMapper = new ObjectMapper();
        try {
            json = objectMapper.writeValueAsString(map);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // get response writer
        PrintWriter writer = response.getWriter();

        // build HTML code
        String htmlRespone = "<html>";
        htmlRespone += "<h2>Welcome :" + name + "<br/>";
        htmlRespone += "<h2>Here all your information :" + json + "<br/>";
        htmlRespone += "Your registration is saved" + "</h2>";
        htmlRespone += "</html>";

        // return response
       // writer.println(htmlRespone);
    }
}
