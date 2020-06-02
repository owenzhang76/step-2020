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

import java.util.ArrayList;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/add-comment")
public class DataServlet extends HttpServlet {

    private ArrayList<String> messages;

    @Override
    public void init() {
        this.messages = new ArrayList<String>();
        // this.messages.add("What is my purpose in life?");
        // this.messages.add("Why can't I stay happy?");
        // this.messages.add("Who lives in a pinneapple under the sea?");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("doGet ran");
        System.out.println("messages array size: " + messages.size());
        String messagesJson = convertToJsonUsingGson(messages);
        response.setContentType("application/json");
        response.getWriter().println(messagesJson);
        System.out.println(messagesJson);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("doPost ran");
        String comment = getParameter(request, "comment-input", "");
        System.out.println("comment is: " + comment);
        messages.add(comment);
        response.sendRedirect("/forum.html");
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
