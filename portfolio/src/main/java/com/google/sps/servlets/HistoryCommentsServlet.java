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
import java.util.List;
import java.io.IOException;
import java.lang.Iterable;
import java.util.Enumeration;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import java.util.Scanner;
import org.json.*;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/previous-comments")
public class HistoryCommentsServlet extends HttpServlet {

    private Cursor cursor;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JsonObject dataObj = new JsonParser().parse(test).getAsJsonObject();
        // startIndex is already encoded after the first time. 
        String startIndex = (dataObj.get("startIndex")).getAsString();
        String encodedCursor = "";

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Message").addSort("timestamp", SortDirection.DESCENDING);
        PreparedQuery preparedQuery = datastore.prepare(query);
        FetchOptions options = FetchOptions.Builder.withLimit(4);
        if(startIndex.equals("0")) {
            Cursor cursor = preparedQuery.asQueryResultList(options).getCursor();
            encodedCursor = cursor.toWebSafeString();
        } else {
            encodedCursor = startIndex;
        }
        options.startCursor(Cursor.fromWebSafeString(encodedCursor));
       
        QueryResultList<Entity> resultList = preparedQuery.asQueryResultList(options);
        String updatedEncodedCursor = resultList.getCursor().toWebSafeString();

        List<Message> messages = StreamSupport.stream(resultList.spliterator(), false)
            .map(messageEntity -> {
                long id = messageEntity.getKey().getId();
                String messageBody = (String) messageEntity.getProperty("body");
                long timestamp = (long) messageEntity.getProperty("timestamp");
                String senderName = (String) messageEntity.getProperty("senderName");
                String imageUrl = (String) messageEntity.getProperty("imageUrl");
                Message newMessage = new Message(id, messageBody, timestamp, senderName, imageUrl);
                return newMessage;
            }).collect(Collectors.toList());

        long fakeId = 000;
        long fakeTimestamp = System.currentTimeMillis();
        Message cursorPretendingToBeMessage = new Message(fakeId, updatedEncodedCursor, fakeTimestamp, "system", "null");
        messages.add(cursorPretendingToBeMessage);
        Gson gson = new Gson();
        String messagesJson = gson.toJson(messages);

        response.setContentType("application/json");
        response.getWriter().println(messagesJson);
    }

    /**
    * gets the value from POST request through a form by the input element name. 
    * @param request the passed in request object
    * @param name the name of the input element
    * @param defaultValue the default value to be returned if the value is null. 
    */
    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        return value != null ? value : defaultValue;
    }

}
