package com.ecart.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.ecart.dao.DAO;
import com.ecart.dao.DAOFactory;


@WebServlet("/getNoOfpages")
public class NoOfPagesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
//		String url = request.getPathInfo();
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
		int noOfPages;
		if(categoryId != 0) {
			noOfPages = (int)Math.ceil(dao.getNoOfProductsByCategory(categoryId)/12.0);
			
		}else {
			noOfPages = (int)Math.ceil(dao.getNoOfProducts()/12.0);
		}
		response.getWriter().write("{\"pages\":"+noOfPages+"}");
	}



}
