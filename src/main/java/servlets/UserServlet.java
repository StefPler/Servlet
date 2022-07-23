/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import gr.csd.uoc.cs359.photo2020.photobook.model.Rating;
import gr.csd.uoc.cs359.winter2020.photobook.db.CommentDB;
import gr.csd.uoc.cs359.winter2020.photobook.db.PostDB;
import gr.csd.uoc.cs359.winter2020.photobook.db.RatingDB;
import gr.csd.uoc.cs359.winter2020.photobook.db.UserDB;
import gr.csd.uoc.cs359.winter2020.photobook.model.Comment;
import gr.csd.uoc.cs359.winter2020.photobook.model.Post;
import gr.csd.uoc.cs359.winter2020.photobook.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Stefanel
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/UserServlet"})
public class UserServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            try {
                if (request.getQueryString() == null) {

                    List<String> userNames = UserDB.getAllUsersNames();
                    out.write(CreateUserHtml(userNames));

                } else {
                    String userName = request.getParameter("userName");
                    User userInfo = UserDB.getUser(userName);
                    out.write(CreateUserHtml(userInfo));
                }

            } catch (Exception e) {
                System.err.println(e);
                response.sendError(400, "Could not retrieve users");
            }

        }
        processRequest(request, response);
    }

    protected String CreateUserHtml(List<String> userNames) {
        String table = "<table class=\"table success\"><thead><th>People you may Know</th></thead><tbody>";

        for (String userName : userNames) {
            table = table + "<tr><td class=\"textTheme\">" + filter(userName) + "</td></tr>\n";
        }

        table = table + "</tbody></table>";
        return table;
    }

    protected String CreateUserHtml(User userInfo) {

        return "<div class=\"modal\" id=\"ProfileModal\">"
                + "<div class=\"modal-dialog\">"
                + "<div class=\"modal-content\">"
                //<!-- Modal Header -->
                + "<div class=\"modal-header\">"
                + "<h4 class=\"modal-title\">User info</h4>"
                + "<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>"
                + "</div>"
                //<!-- Modal body -->
                + "<div class=\"modal-body\">"
                + "UserName:<p>" + filter(userInfo.getUserName()) + "</p>"
                + "Email:<p>" + filter(userInfo.getEmail()) + "</p>"
                + "Name:<p>" + filter(userInfo.getFirstName()) + "</p>"
                + "Surname:<p>" + filter(userInfo.getLastName()) + "</p>"
                //add a button to Delete the User calling the deleteUser() func
                + "<button class=\"button\" type=\"close\" onclick=\"deleteUserRequest();\">Delete your acount</button>"
                + "</div>"
                //<!-- Modal footer -->
                + "<div class=\"modal-footer\">"
                + "<button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">Close</button>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>";
    }

    private static boolean hasSpecialChars(String input) {
        if (input.contains("<")) {
            return true;
        }
        if (input.contains(">")) {
            return true;
        }
        if (input.contains("\"")) {
            return true;
        }
        if (input.contains("&")) {
            return true;
        }

        return false;
    }

    public static String filter(String input) {
        if (!hasSpecialChars(input)) {
            return (input);
        }
        StringBuffer filtered
                = new StringBuffer(input.length());
        char c;
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            switch (c) {
                case '<':
                    filtered.append("&lt;");
                    break;
                case '>':
                    filtered.append("&gt;");
                    break;
                case '"':
                    filtered.append("&quot;");
                    break;
                case '&':
                    filtered.append("&amp;");
                    break;
                default:
                    filtered.append(c);
            }
        }
        return filtered.toString();
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
            /* TODO output your page here. You may use following sample code. */

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UserServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p class=\"textTheme\"> HTTP Method:" + request.getMethod() + "</p>");

            String values = request.getReader().lines()
                    .reduce("", (accumulator, actual)
                            -> accumulator + actual);
            String[] splitValues = values.split("&");

            if (splitValues.length == 14) {
                String username = getValue(splitValues[0]);
                if (username.matches("[a-zA-Z_0-9]{8,}")) {
                    out.println("<p class=\"textTheme\"> Params are: " + splitValues[0] + "</p>"
                    );
                } else {
                    response.sendError(400, "Invalid Username");
                }

                String email = getValue(splitValues[1]);
                if (email.matches("[a-zA-Z]{1}[a-zA-Z\\d]*\\@[a-zA-Z]{1}[a-zA-Z\\d]*\\.[a-zA-Z]{2,}")) {
                    out.println("<p class=\"textTheme\"> Params are: " + splitValues[1] + "</p>");
                } else {
                    response.sendError(400, "Invalid email");
                }

                String pwd = getValue(splitValues[2]);
                if (pwd.matches("(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&*_+-=]).{8,10}")) {
                    out.println("<p class=\"textTheme\"> Params are: " + splitValues[2] + "</p>");
                } else {
                    response.sendError(400, "Invalid password");
                }

                String repwd = getValue(splitValues[3]);
                boolean passMatch = false;

                pwd = pwd.trim();
                repwd = repwd.trim();
                System.out.println("Params are: " + pwd + ' ' + repwd + ' ' + pwd.equals(repwd));
                if (pwd.equals(repwd)) {
                    passMatch = true;
                    out.println("<p class=\"textTheme\"> Params are: " + splitValues[3] + "</p>");
                } else {
                    response.sendError(400, "Passwords do not much");
                }

                String fName = getValue(splitValues[4]);
                if (fName.matches(".{3,15}")) {
                    out.println("<p class=\"textTheme\"> Params are: " + splitValues[4] + "</p>");
                } else {
                    response.sendError(400, "Invalid Name");
                }

                String lName = getValue(splitValues[5]);
                if (lName.matches(".{3,15}")) {
                    out.println("<p class=\"textTheme\"> Params are: " + splitValues[5] + "</p>");
                } else {
                    response.sendError(400, "Invalid Last Name");
                }

                String birthDate = getValue(splitValues[6]);
                if (!birthDate.equals("None")) {
                    out.println("<p class=\"textTheme\"> Params are: " + splitValues[6] + "</p>");
                } else {
                    response.sendError(400, "Missing Birthdate");
                }

                String gender = getValue(splitValues[7]);

                out.println("<p class=\"textTheme\"> Params are: " + splitValues[7] + "</p>");

                String country = getValue(splitValues[8]);
                if (!country.equals("None")) {
                    out.println("<p class=\"textTheme\"> Params are: " + splitValues[8] + "</p>");
                } else {
                    response.sendError(400, "Missing Country");
                }

                String town = getValue(splitValues[9]);
                if (!town.equals("None")) {
                    out.println("<p class=\"textTheme\"> Params are: " + splitValues[9] + "</p>");
                } else {
                    response.sendError(400, "Missing Town");
                }

                String address = getValue(splitValues[10]);
                out.println("<p class=\"textTheme\"> Params are: " + splitValues[10] + "</p>");

                String work = getValue(splitValues[11]);
                if (!work.equals("None")) {
                    out.println("<p class=\"textTheme\"> Params are: " + splitValues[11] + "</p>");
                } else {
                    response.sendError(400, "Missing Work");
                }

                String interests = getValue(splitValues[12]);
                out.println("<p class=\"textTheme\"> Params are: " + splitValues[12] + "</p>");

                String generalInfo = getValue(splitValues[13]);
                out.println("<p class=\"textTheme\"> Params are: " + splitValues[13] + "</p>");

                User newUser = new User(username, email, pwd, fName, lName, birthDate, work, country, town);
                newUser.setAddress(address);
                newUser.setGender(gender);
                newUser.setInfo(generalInfo);
                newUser.setInterests(interests);

                try {
                    newUser.checkFields();
                } catch (Exception e) {
                    response.sendError(400, "Invalid parameters: " + e.toString());
                }

                if (!createUser(newUser)) {
                    response.sendError(400, "Could not create user, Username and/or email already in use");
                }
            } else if (splitValues.length == 1) {

                String username = getValue(splitValues[0]);
                System.out.println("Called deleteUser for user: " + username);
                deleteUser(username);

            } else {
                response.sendError(400, "Invalid parameters");
            }

            out.println("<h1 class=\"textTheme\"> Registration Success!" + "</h1>");
            out.println("</body>");
            out.println("</html>");

        }
        processRequest(request, response);
    }

    boolean createUser(User user) {
        try {
            if (UserDB.checkValidUserName(user.getUserName()) && UserDB.checkValidEmail(user.getEmail())) {
                UserDB.addUser(user);
                return true;
            } else {
                return false;
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    boolean deleteUser(String userName) {
        try {
            if (!UserDB.checkValidUserName(userName)) {

                for (Post itterator : PostDB.getPosts()) {
                    if (itterator.getUserName().equals(userName)) {

                        for (Comment ittrc : CommentDB.getComments(itterator.getPostID())) {
                            CommentDB.deleteComment(ittrc.getID());
                        }

                        for (Rating ittrr : RatingDB.getRatings(itterator.getPostID())) {
                            CommentDB.deleteComment(ittrr.getID());
                        }

                        PostDB.deletePost(itterator.getPostID());

                    }
                }

                UserDB.deleteUser(userName);
                System.out.println("User deleted");
                return true;
            } else {
                return false;
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    String getAttribute(String equalsValue) {
        String[] splitString = equalsValue.split("=");

        if (splitString.length > 1) {
            return splitString[0];
        } else {
            return "None";
        }
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
