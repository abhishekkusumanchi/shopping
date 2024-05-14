package com.ecart.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int addressId = Integer.parseInt(request.getParameter("selectedAddressId"));
		System.out.println(addressId);
		request.getSession().setAttribute("addressId", addressId);

		request.getRequestDispatcher("Payment.jsp").forward(request, response);

	}

}
