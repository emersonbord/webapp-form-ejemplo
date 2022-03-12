package org.eborda.apiservlet.webapp.form;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/registro")
public class FormServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String pais = req.getParameter("pais");
        String[] lenguajes = req.getParameterValues("lenguajes");
        String[] roles = req.getParameterValues("roles");

        String idioma = req.getParameter("idioma");
        boolean habilitar = req.getParameter("habilitar") != null &&
                req.getParameter("habilitar").equals("on");
        String secreto = req.getParameter("secreto");

        //Validamos los parámetros del formulario
        Map<String, String> errores = new HashMap<>();

        if (username == null || username.isBlank()) {
            errores.put("username","El username es requerido");
        }
        if (password == null || password.isBlank()) {
            errores.put("password","El password es incorrecto");
        }
        //!email si no contiene el caracter @ se marca como error
        if (email == null || !email.contains("@")) {
            errores.put("email","El email es requerido y debe contener un formato correcto");
        }
        if (pais == null || pais.equals("") || pais.equals(" ")) {
            errores.put("pais","El país es requerido");
        }
        //Como lenguajes es un arreglo no tiene el metodo blank
        if (lenguajes == null || lenguajes.length == 0) {
            errores.put("lenguaje","Debe seleccionar el lenguaje");
        }
        if (roles == null || roles.length == 0) {
            errores.put("roles","Debe seleccionar un rol");
        }
        if (idioma == null) {
            errores.put("idioma","Debe seleccionar un idioma");
        }
        if (errores.isEmpty()) {
            try (PrintWriter out = resp.getWriter()) {

                out.println("<!DOCTTYPE html>");
                out.println("<html>");
                out.println("    <head>");
                out.println("        <meta charset=\"UTF-8\">");
                out.println("        <title>Resultado form</title>");
                out.println("     </head>");
                out.println("     <body>");
                out.println("        <h1>Resultado form</h1>");
                out.println("       <ul>");


                out.println("           <li>Username: " + username + "</li>");
                out.println("           <li>Password: " + password + "</li>");
                out.println("           <li>Email: " + email + "</li>");
                out.println("           <li>Pais: " + pais + "</li>");
                out.println("           <li>Lenguajes<ul>");
                //Creamos un Arrays como List y le invocamos el método foreach
                Arrays.asList(lenguajes).forEach(lenguaje -> {
                    out.println("           <li>" + lenguaje + "</li>");
                });
                out.println("           </ul></li>");

                out.println("           <li>Roles<ul>");
                Arrays.asList(roles).forEach(rol -> {
                    out.println("           <li>" + rol + "</li>");
                });
                out.println("           </ul></li>");
                out.println("               <li>Idioma: " + idioma + "</li>");
                out.println("               <li>Habilitado: " + habilitar + "</li>");
                out.println("               <li>Secreto: " + secreto + "</li>");
                out.println("       </ul>");
                out.println("     </body>");
                out.println("</html>");
            }
        } else {
            /** errores.forEach(error ->{
             out.println("<li>" + error + "</li>");
             });
             out.println("<p><a href=\"/webapp-form/index.jsp\">volver</a></p>");
             */
            //Pasamos los mensajes de error a la vista con el request - nombre parámetro "errores", pasamos el objeto errores que contiene la lista de mensajes de errores
            //Atributos del request son distintos a los aprametros. Parametros son los que envían el usuario, los atributos permiten pasar datos de un servlet a un JSP o de un servlet a otro servlet.
            req.setAttribute("errores", errores);
            //Para transferir, redireccionar este req a la JSP. o cargar la vista JSP, utilizamos el método getServletContext() que maneja el contexto de los servlet. Invocamos el método getRequestDispatcher("/index.jsp") con la ruta.
            //Luego invocamos el método forward() para redireccionar o para cargar la JSP
            getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }
}
