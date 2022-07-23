/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gr.csd.uoc.cs359.winter2020.photobook.db.PostDB;
import gr.csd.uoc.cs359.winter2020.photobook.model.Post;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Stefanel
 */
public class SocialPostServlet extends HttpServlet {

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
            out.println("<title>Servlet SocialPostServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SocialPostServlet at " + request.getContextPath() + "</h1>");
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Type listType = new TypeToken<List<Post>>() {
            }.getType();
            /* TODO output your page here. You may use following sample code. */
            try {
                if (request.getQueryString() == null) {
                    System.out.println("No query");
                    Gson gson = new Gson();

                    List<Post> posts = PostDB.getPosts();
                    String json = gson.toJson(posts, listType);
                    out.println(json);

                } else {
                    Integer postId = 0;
                    String sPostId = request.getParameter("postId");
                    if (sPostId != null) {
                        postId = Integer.parseInt(sPostId);
                    }
                    String amountId = request.getParameter("amountId");
                    String username = request.getParameter("username");

                    if (amountId != null) {

                        Gson gson = new Gson();
                        if (amountId.equalsIgnoreCase("one")) {
                            Post post = PostDB.getPost(postId);
                            String json = gson.toJson(post);
                            out.println(json);
                        } else if (amountId.equalsIgnoreCase("top10user")) {
                            if (username != null) {
                                List<Post> posts = PostDB.getTop10RecentPostsOfUser(username);
                                String json = gson.toJson(posts, listType);
                                out.println(json);
                            } else {
                                response.sendError(400, "Missing username");
                            }

                        } else {
                            // top 10 all
                            List<Post> posts = PostDB.getTop10RecentPosts();
                            String json = gson.toJson(posts, listType);
                            out.println(json);
                        }

                    } else {
                        response.sendError(400, "Invalid parameters");
                    }

                }

            } catch (Exception e) {
                System.err.println(e);
                response.sendError(400, "Could not retrieve post");
            }

        }
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
        try (PrintWriter out = response.getWriter()) {

            String values = request.getReader().lines()
                    .reduce("", (accumulator, actual)
                            -> accumulator + actual);
            String[] splitValues = values.split("&");

            Post post = new Post();

            if (splitValues.length >= 2) {

                String username = getValue(splitValues[0]);
                post.setUserName(username);

                String description = getValue(splitValues[1]);
                post.setDescription(description);

                String resourceUrl = getValue(splitValues[2]);
                post.setResourceURL(resourceUrl);

                String imageUrl = getValue(splitValues[3]);
                post.setImageURL(imageUrl);

                String imageBase64 = getValue(splitValues[4]);
                post.setImageBase64(imageBase64);

                String latitude = getValue(splitValues[5]);
                post.setLatitude(latitude);

                String longtitude = getValue(splitValues[6]);
                post.setLongitude(longtitude);

                post.checkFields();

                PostDB.addPost(post);
                response.setStatus(200);

                System.out.println(post.toString());
                String toRet = "<div style=\"background-color:white; height:100%;width:50%;text-align: left;\">"
                        + "<p>You Posted: "
                        + post.getDescription()
                        + "</p>"
                        + "<p>At "
                        + new Date()
                        + "</p></div>";

                System.out.println("toRet is: " + toRet);
                out.println(toRet);
            } else {
                response.sendError(400, "Could not post");
            }

        } catch (Exception e) {

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
