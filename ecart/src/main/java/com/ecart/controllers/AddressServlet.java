package com.ecart.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ecart.bll.BusinessLogic;
import com.ecart.dao.DAO;
import com.ecart.dao.DAOFactory;
import com.ecart.models.Address;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/address")
public class AddressServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession httpSession = request.getSession();
		DAO dao = (DAO) httpSession.getAttribute("dao");
		if (dao == null) {
			dao = new DAOFactory().getDAO();
			httpSession.setAttribute("dao", dao);
			
		}
		String userName = (String) httpSession.getAttribute("USERNAME");
		List<Address> addresses = dao.getAddresses(userName);
		int pincode = (int)httpSession.getAttribute("pincode");
		//invoking BLL
		addresses = new BusinessLogic().setDeliverability(addresses,pincode);
		
        request.setAttribute("addresses", addresses);

        // Forward the request to Address.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("Address.jsp");
        dispatcher.forward(request, response);
		
	}
}
