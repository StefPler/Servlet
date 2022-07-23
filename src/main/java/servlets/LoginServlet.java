/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import gr.csd.uoc.cs359.winter2020.photobook.db.UserDB;
import gr.csd.uoc.cs359.winter2020.photobook.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stefanel
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        try (PrintWriter out = response.getWriter()) {

            HttpSession session = request.getSession(true);
            Integer visitCount = new Integer(0);
            String visitCountKey = new String("visitCount");
            String userNameKey = new String("username");
            String userName = new String("");
            String passWord = new String("");
            String passwordKey = new String("password");

            /* TODO output your page here. You may use following sample code. */
            String values = request.getReader().lines()
                    .reduce("", (accumulator, actual)
                            -> accumulator + actual);
            String[] splitValues = values.split("&");

            if (splitValues.length == 2) {
                String username;
                String password;

                userName = (String) session.getAttribute(userNameKey);
                passWord = (String) session.getAttribute(passwordKey);

                session.setAttribute(visitCountKey, visitCount);

                System.out.println("stuff" + userName + passWord);

                if (userName == null || passWord == null || userName.equalsIgnoreCase("") || passWord.equalsIgnoreCase("")) {
                    System.out.println("stuffZ1" + userName + passWord);
                    username = getValue(splitValues[0]);
                    password = getValue(splitValues[1]);

                    if (session.isNew()) {
                        session.setAttribute(userNameKey, username);
                        session.setAttribute(passwordKey, password);
                    } else {
                        visitCount = (Integer) session.getAttribute(visitCountKey);
                        visitCount = visitCount + 1;
                    }

                } else {
                    System.out.println("stuffZ2" + userName + passWord);
                    username = userName;
                    password = passWord;
                }

                System.out.println("stuff2" + username + password);
                if (oAuth(username, password)) {
                    out.println("<h1 class=\"textTheme\"> Login Success!</h1>");
                    response.setStatus(200);
                } else {
                    out.println("<h1 class=\"textTheme\"> Login Failure!</h1>");
                    response.setStatus(400);
                }
            } else {
                response.sendError(400, "Invalid parameters");
            }

        } catch (Exception e) {
            response.setStatus(400);
        }
        processRequest(request, response);
    }

    boolean oAuth(String username, String password) {
        try {
            if (!UserDB.checkValidUserName(username)) {
                User user = UserDB.getUser(username);
                String dbPass = user.getPassword();
                System.out.println("Pass " + dbPass + "other pass: " + password + '|');
                if (dbPass.equals(password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
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
