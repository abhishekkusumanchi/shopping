package com.ecart.controllers;

import java.io.IOException;
import java.util.List;

import com.ecart.dao.DAO;
import com.ecart.dao.DAOFactory;
import com.ecart.models.ProductCategory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	HttpSession httpSession = request.getSession();
    	httpSession.setAttribute("previousUrl", request.getRequestURI());
		DAO dao = (DAO) httpSession.getAttribute("dao");
		if (dao == null) {
			dao = new DAOFactory().getDAO();
			httpSession.setAttribute("dao", dao);
			
		}
		List<ProductCategory> allCategories = dao.getAllCategories();
		request.setAttribute("allCategories", allCategories);
        request.getRequestDispatcher("Home.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

