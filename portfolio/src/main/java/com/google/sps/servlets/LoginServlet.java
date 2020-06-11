package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/check-login")
public class LoginServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        UserService userService = UserServiceFactory.getUserService();
        String[] info = new String[3]; 

        if (userService.isUserLoggedIn()) {
            String userEmail = userService.getCurrentUser().getEmail();
            String urlToRedirectToAfterUserLogsOut = "/";
            String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
           
            info[0] = "true";
            info[1] = logoutUrl;
            info[2] = userEmail;

            Gson gson = new Gson();
            String loggedInJsonInfo = gson.toJson(info);
            response.setContentType("application/json");
            response.getWriter().println(loggedInJsonInfo);
        } else { 
            String urlToRedirectToAfterUserLogsIn = "/";
            String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

            info[0] = "false";
            info[1] = loginUrl;
            
            Gson gson = new Gson();
            String loggedOutJsonInfo = gson.toJson(info);
            response.setContentType("application/json");
            response.getWriter().println(loggedOutJsonInfo);
        }
    }

    @Override 
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        
    }
}