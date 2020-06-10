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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.*;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/add-comment")
public class DataServlet extends HttpServlet {

    private ArrayList<Message> messages;

    @Override
    public void init() {
        this.messages = new ArrayList<Message>();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        
        messages.clear();

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Message").addSort("timestamp", SortDirection.DESCENDING);
        Iterable<Entity> results = datastore.prepare(query).asIterable(FetchOptions.Builder.withLimit(4));

        for (Entity messageEntity : results) {
            long id = messageEntity.getKey().getId();
            String messageBody = (String) messageEntity.getProperty("body"); 
            long timestamp = (long) messageEntity.getProperty("timestamp");
            String senderName = (String) messageEntity.getProperty("senderName");
            String imageUrl = (String) messageEntity.getProperty("imageUrl");
            Message newMessage = new Message(id, messageBody, timestamp, senderName, imageUrl);
            messages.add(newMessage);
        };

        String messagesJson = convertToJsonUsingGson(messages);

        response.setContentType("application/json");
        response.getWriter().println(messagesJson);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // String comment = getParameter(request, "comment-input", "");
        // String senderName = getParameter(request, "chatname-input", "");
        // long timestamp = System.currentTimeMillis();

        // Entity messageEntity = new Entity("Message");
        // messageEntity.setProperty("body", comment);
        // messageEntity.setProperty("timestamp", timestamp);
        // messageEntity.setProperty("senderName", senderName);
        // messageEntity.setProperty("imageUrl", imageUrl);

        // DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        // datastore.put(messageEntity);

        // response.sendRedirect("/forum.html");
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
