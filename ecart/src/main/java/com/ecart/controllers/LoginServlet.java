package com.ecart.controllers;

import java.io.IOException;

import com.ecart.dao.DAO;
import com.ecart.dao.DAOFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/validateLogin")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		HttpSession httpSession = request.getSession();
		DAO dao = (DAO) httpSession.getAttribute("dao");
		if (dao == null) {
			dao = new DAOFactory().getDAO();
			httpSession.setAttribute("dao", dao);
			
		}
		boolean validCredentials = dao.isValidUser(username, password);
		System.out.println(username);
		System.out.println(validCredentials);
		String previousUrl = (String) httpSession.getAttribute("previousUrl");
		if (previousUrl == null) {
			previousUrl = "home";
			httpSession.setAttribute("previousUrl",previousUrl);
		}
		if (validCredentials) {
			request.getSession().setAttribute("LOGGEDIN", "yes");
			request.getSession().setAttribute("USERNAME", username);
			response.getWriter().write("{ \"valid\": true,\"url\":\"" + previousUrl + "\"}");
		} else {
			System.out.println("not valid");
			request.getSession().setAttribute("LOGGEDIN", "no");
			response.getWriter().write("{ \"valid\": false}");
		}

	}

}
