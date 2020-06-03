// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.sps.data.Message;
import java.util.ArrayList;
import java.io.IOException;
import java.lang.Iterable;
import java.util.Enumeration;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Cursor;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/previous-comments")
public class HistoryCommentsServlet extends HttpServlet {

    private ArrayList<Message> messages;

    @Override
    public void init() {
        this.messages = new ArrayList<Message>();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {  

        System.out.println(request);

        Query query = new Query("Message").addSort("timestamp", SortDirection.DESCENDING);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        // Cursor startCursor = new Cursor();
        Iterable<Entity> results = datastore.prepare(query).asIterable(FetchOptions.Builder.withLimit(4));

        for (Entity messageEntity : results) {
            long id = messageEntity.getKey().getId();
            String messageBody = (String) messageEntity.getProperty("body"); 
            long timestamp = (long) messageEntity.getProperty("timestamp");

            Message newMessage = new Message(id, messageBody, timestamp);
            messages.add(newMessage);
        };

        String messagesJson = convertToJsonUsingGson(messages);
        
        response.setContentType("application/json");
        response.getWriter().println(messagesJson);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Enumeration<String> params = request.getParameterNames(); 
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
        }

        Query query = new Query("Message").addSort("timestamp", SortDirection.DESCENDING);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        // Cursor startCursor = new Cursor();
        Iterable<Entity> results = datastore.prepare(query).asIterable(FetchOptions.Builder.withLimit(4));

        for (Entity messageEntity : results) {
            long id = messageEntity.getKey().getId();
            String messageBody = (String) messageEntity.getProperty("body"); 
            long timestamp = (long) messageEntity.getProperty("timestamp");

            Message newMessage = new Message(id, messageBody, timestamp);
            messages.add(newMessage);
        };

        String messagesJson = convertToJsonUsingGson(messages);
        
        response.setContentType("application/json");
        response.getWriter().println(messagesJson);
    }

    private String convertToJsonUsingGson(ArrayList messages) {
        Gson gson = new Gson();
        String json = gson.toJson(messages);
        return json;
    }

    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
        return defaultValue;
        }
        return value;
  }
}
