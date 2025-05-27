/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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
import utils.BcryptJava;
import utils.JwtUtil;

/**
 *
 * @author yello
 */
@WebServlet(name = "Login", urlPatterns = {"/login"})
public class Login extends HttpServlet {

    AlumnowebJpaController aluDAO = new AlumnowebJpaController();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        JsonReader jsonReader = Json.createReader(request.getReader());
        javax.json.JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        // 1. Buscar el cliente por username
        Alumnoweb alumno = aluDAO.findAlumnoByUsername(username);
        // 2. Verificar si el cliente existe y la contraseña coincide
        if (alumno == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        System.out.println(password); 
        if (!BcryptJava.checkPassword(password, alumno.getPassEstd())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        JsonObject jsonResponse;
        String token = JwtUtil.generarToken(username);

        jsonResponse = Json.createObjectBuilder()
                .add("success", true)
                .add("token", token)
                .build();
        out.print(jsonResponse.toString());
        out.flush();

    }

}
