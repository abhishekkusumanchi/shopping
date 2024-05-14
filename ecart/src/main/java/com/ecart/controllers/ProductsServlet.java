package com.ecart.controllers;

import java.io.IOException;
import java.util.List;

import com.ecart.dao.DAO;
import com.ecart.dao.DAOFactory;
import com.ecart.models.Product;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/getProducts/*")
public class ProductsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		String url = request.getPathInfo();
		int pageNo = Integer.parseInt(url.split("/")[1]);
		int categoryId;
		try {
			categoryId = Integer.parseInt(request.getParameter("categoryId"));
		} catch (Exception e) {
			categoryId = 0;
		}
		HttpSession httpSession = request.getSession();
		DAO dao = (DAO) httpSession.getAttribute("dao");
		if (dao == null) {
			dao = new DAOFactory().getDAO();
			httpSession.setAttribute("dao", dao);
		}
		List<Product> products;
		if (categoryId != 0) {
			products = dao.getProductsByCategory(pageNo, categoryId);

		} else {
			
			products = dao.getProducts(pageNo);
		}
		String jsonData = new Gson().toJson(products);
		//response.getWriter().write("");
		

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonData);
		
	}

}
