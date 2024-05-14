package com.ecart.controllers;

import java.io.IOException;

import com.ecart.dao.DAO;
import com.ecart.dao.DAOFactory;
import com.ecart.models.Address;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/addAddress")
public class AddAddressServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession httpSession = request.getSession();
    	httpSession.setAttribute("previousUrl", request.getRequestURI());
		DAO dao = (DAO) httpSession.getAttribute("dao");
		if (dao == null) {
			dao = new DAOFactory().getDAO();
			httpSession.setAttribute("dao", dao);
			
		}
		String customerName = request.getParameter("customerName");
		String mobile = request.getParameter("mobile");
		String email = request.getParameter("email");
		String pincodeStr = request.getParameter("pincode");
		int pincode = Integer.parseInt(pincodeStr);
		String addressText = request.getParameter("address");

		Address address = new Address();
		String userName = (String) httpSession.getAttribute("USERNAME");
		address.setUserName(userName );
		address.setCustomerName(customerName);
		address.setMobile(mobile);
		address.setEmail(email);
		address.setPincode(pincode);
		address.setAddress(addressText);

		boolean success = dao.addAddress(address);
		if (success) {
            response.sendRedirect("address"); 
        } else {
            response.sendRedirect("home"); 
        }
	}


}
