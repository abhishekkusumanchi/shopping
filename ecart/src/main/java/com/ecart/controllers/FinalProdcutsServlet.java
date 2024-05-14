package com.ecart.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ecart.bll.BusinessLogic;
import com.ecart.dao.DAO;
import com.ecart.dao.DAOFactory;
import com.ecart.models.Coupon;
import com.ecart.models.Product;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/getProductsForPayment")
public class FinalProdcutsServlet extends HttpServlet{

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		response.setContentType("application/json");
		HttpSession httpSession = request.getSession();
		DAO dao = (DAO) httpSession.getAttribute("dao");
		if (dao == null) {
			dao = new DAOFactory().getDAO();
			httpSession.setAttribute("dao", dao);
		}
		int pincode = (int)httpSession.getAttribute("pincode");
		Map<Integer, Integer> cartMap = (Map<Integer, Integer>) httpSession.getAttribute("cartMap");
		List<Product> productList = dao.getProducts(new ArrayList<>(cartMap.keySet()), pincode);
		for(Product product:productList) {
			product.setQuantity(cartMap.get(product.getProductId()));
		}
		String action = request.getParameter("action");
		if(action != null && action.equals("applyCoupon")) {
			String couponCode = request.getParameter("couponCode");
			httpSession.setAttribute("couponCode", couponCode);
			Coupon coupon = dao.getCouponByCode(couponCode);
			productList = new BusinessLogic().applyDiscount(productList,coupon.getValue());
		}
		
		double total =  new BusinessLogic().getTotalAmount(productList);
		double shippingDicountPercentage = dao.getShippingPercentage(total);
		productList = new BusinessLogic().applyShippingDiscount(productList,shippingDicountPercentage);
		
		httpSession.setAttribute("products", productList);
		response.getWriter().write(new Gson().toJson(productList));
	
	}
	
}
