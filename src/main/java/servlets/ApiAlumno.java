/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import com.google.gson.Gson;
import dao.AlumnowebJpaController;
import dao.exceptions.NonexistentEntityException;
import dto.Alumnoweb;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ApiAlumno", urlPatterns = {"/apialumno"})
public class ApiAlumno extends HttpServlet {

    AlumnowebJpaController alumnoDAO = new AlumnowebJpaController();
    private Gson gson;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Alumnoweb> lista = alumnoDAO.findAlumnowebEntities();
        String json = gson.toJson(lista);
        response.setContentType("application/json");
        response.getWriter().write(json);
    }

    

}
