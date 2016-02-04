package br.com.sicot.rest;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

/**
 * Created by thiago on 22/01/16.
 */
@WebServlet(name = "upload", urlPatterns = {"/upload"})
@MultipartConfig(location = "/tmp")
public class Upload  extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        for (Part part : request.getParts()) {

            String filename = "";
            for (String s : part.getHeader("content-disposition").split(";")) {
                if (s.trim().startsWith("filename")) {
                    filename = s.split("=")[1].replaceAll("\"", "");
                }
            }
            part.write(filename);
        }
    }
}