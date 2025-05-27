
package servlets;

import dao.AlumnowebJpaController;
import dto.Alumnoweb;
import java.io.IOException;
import java.io.PrintWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.JwtUtil;


@WebServlet(name = "ChangePassword", urlPatterns = {"/passchange"})
public class ChangePassword extends HttpServlet {

    AlumnowebJpaController clieDAO = new AlumnowebJpaController();
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring("Bearer ".length());
        
        System.out.println(token);
        boolean isValidToken = JwtUtil.validarToken(token);
        
        if (!isValidToken) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        PrintWriter out = response.getWriter();
        JsonReader jsonReader = Json.createReader(request.getReader());
        javax.json.JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
           
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("currentPassword");
        String newPassword = jsonObject.getString("newPassword");
        
        
        Alumnoweb cliente = clieDAO.findAlumnoByUsername(username);
        
        if (cliente == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (!cliente.getPassEstd().equals(password)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        JsonObject jsonResponse;
        // Actualizar la contraseña
        cliente.setPassEstd(newPassword);
        try {
            clieDAO.edit(cliente);
            jsonResponse = Json.createObjectBuilder()
                    .add("success", true)
                    .build();
            out.print(jsonResponse.toString());
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
      
    }
}
