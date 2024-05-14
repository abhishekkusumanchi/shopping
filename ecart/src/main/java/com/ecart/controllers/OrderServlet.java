package com.ecart.controllers;

import java.io.IOException;
import java.util.List;

import com.ecart.dao.DAO;
import com.ecart.dao.DAOFactory;
import com.ecart.models.Order;
import com.ecart.models.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/createOrder")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		int selectedAddress = (int) session.getAttribute("addressId");
		String username = (String) session.getAttribute("USERNAME");
		String couponCode = (String) session.getAttribute("couponCode");
		List<Product> products = (List<Product>) session.getAttribute("products");
		DAO dao = (DAO) session.getAttribute("dao");
		if (dao == null) {
			dao = new DAOFactory().getDAO();
			session.setAttribute("dao", dao);

		}

		Order order = dao.createOrder(username, products, selectedAddress, couponCode);
		request.setAttribute("products", products);
		request.setAttribute("order", order);
		request.getRequestDispatcher("Order.jsp").forward(request, response);
	}

}
