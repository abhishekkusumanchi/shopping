package com.ecart.controllers;

import java.io.IOException;

import com.ecart.dao.DAO;
import com.ecart.dao.DAOFactory;
import com.ecart.models.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/createUser")
public class RegisterServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("register");
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		HttpSession httpSession = req.getSession();
		DAO dao = (DAO) httpSession.getAttribute("dao");
		if (dao == null) {
			dao = new DAOFactory().getDAO();
			httpSession.setAttribute("dao", dao);
			
		}
		String username = req.getParameter("username");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		boolean result = dao.createUser(new User(username, email, password));
		if (result) {
			resp.getWriter().write("{ \"success\": true}");
		} else {
			resp.getWriter().write("{ \"success\": false}");
		}

	}

}
