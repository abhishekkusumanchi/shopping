package com.ecart.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import com.ecart.dao.DAO;
import com.ecart.dao.DAOFactory;
import com.ecart.models.Coupon;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/getCoupon")
public class CouponServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String couponCode = request.getParameter("couponCode");

		HttpSession httpSession = request.getSession();
		DAO dao = (DAO) httpSession.getAttribute("dao");
		if (dao == null) {
			dao = new DAOFactory().getDAO();
			httpSession.setAttribute("dao", dao);
		}

		Coupon coupon = dao.getCouponByCode(couponCode);

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(new Gson().toJson(coupon));
		out.flush();
	}



}
