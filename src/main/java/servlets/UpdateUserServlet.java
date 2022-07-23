/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import gr.csd.uoc.cs359.winter2020.photobook.db.UserDB;
import gr.csd.uoc.cs359.winter2020.photobook.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Stefanel
 */
public class UpdateUserServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UpdateUserServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateUserServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            try {
                String values = request.getReader().lines()
                        .reduce("", (accumulator, actual)
                                -> accumulator + actual);
                String[] splitValues = values.split("&");

                if (splitValues.length > 1) {

                    String username = getValue(splitValues[0]);
                    if (!username.equalsIgnoreCase("None")) {
                        User user = UserDB.getUser(username);

                        String email = getValue(splitValues[1]);
                        if (!email.equalsIgnoreCase("None")) {
                            user.setEmail(email);
                        }

                        String firstName = getValue(splitValues[2]);
                        if (!firstName.equalsIgnoreCase("None")) {
                            user.setFirstName(firstName);
                        }

                        String lastName = getValue(splitValues[3]);
                        if (!lastName.equalsIgnoreCase("None")) {
                            user.setLastName(lastName);
                        }

                        String birthDate = getValue(splitValues[4]);
                        if (!birthDate.equalsIgnoreCase("None")) {
                            user.setBirthDate(birthDate);
                        }

                        String gender = getValue(splitValues[5]);
                        if (!gender.equalsIgnoreCase("None")) {
                            user.setGender(gender);
                        }

                        String country = getValue(splitValues[6]);
                        if (!country.equalsIgnoreCase("None")) {
                            user.setCountry(country);
                        }

                        String address = getValue(splitValues[7]);
                        if (!address.equalsIgnoreCase("None")) {
                            user.setAddress(address);
                        }

                        String occupation = getValue(splitValues[8]);
                        if (!occupation.equalsIgnoreCase("None")) {
                            user.setOccupation(occupation);
                        }

                        String interests = getValue(splitValues[9]);
                        if (!interests.equalsIgnoreCase("None")) {
                            user.setInterests(interests);
                        }

                        String town = getValue(splitValues[10]);
                        if (!town.equalsIgnoreCase("None")) {
                            user.setTown(town);
                        }

                        String info = getValue(splitValues[11]);
                        if (!info.equalsIgnoreCase("None")) {
                            user.setInfo(info);
                        }

                        UserDB.updateUser(user);

                        Gson gson = new Gson();
                        String json = gson.toJson(user);
                        out.write(json);
                    }

                } else {

                }

            } catch (Exception e) {
                System.err.println(e);
                response.sendError(400, "Could not update user");
            }

        }
        processRequest(request, response);

    }

    String getValue(String equalsValue) {
        String[] splitString = equalsValue.split("=");

        if (splitString.length > 1) {
            return splitString[1];
        } else {
            return "None";
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
