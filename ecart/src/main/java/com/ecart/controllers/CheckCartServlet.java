package com.ecart.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/checkCart")
public class CheckCartServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Assume cart items are stored in a map in the session attribute "cartMap"
        Map<Integer, Integer> cartMap =  (Map<Integer, Integer>) request.getSession().getAttribute("cartMap");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonResponse = new HashMap<>();

        if (cartMap != null && !cartMap.isEmpty()) {
            // Cart is not empty
            jsonResponse.put("status", "success");
            jsonResponse.put("message", "Cart is not empty");
            jsonResponse.put("cartSize", cartMap.size());
        } else {
            // Cart is empty
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Cart is empty");
            jsonResponse.put("cartSize", 0);
        }

        // Convert JSON map to string and write to response
        String json = new Gson().toJson(jsonResponse);
        out.println(json);

        out.close();
    }
}

