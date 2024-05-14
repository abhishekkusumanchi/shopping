package com.ecart.controllers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ecart.dao.DAO;
import com.ecart.dao.DAOFactory;
import com.ecart.models.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("previousUrl", request.getRequestURI());
    
        
        DAO dao = (DAO) httpSession.getAttribute("dao");
		if (dao == null) {
			dao = new DAOFactory().getDAO();
			httpSession.setAttribute("dao", dao);
			
		}

        // Create a list of products
		
        Map<Integer, Integer> cartMap = (Map<Integer, Integer>) httpSession.getAttribute("cartMap");
        List<Product> productList;
        String pincodeString = request.getParameter("pincode");
        int pincode;
        if(pincodeString==null)
        	pincode = (int)httpSession.getAttribute("pincode");
        else
        	pincode = Integer.parseInt(request.getParameter("pincode"));
        
        if(cartMap!=null) {
        	productList = dao.getProducts(new ArrayList<>(cartMap.keySet()), pincode);
        
        }else {
        	productList = new ArrayList<Product>();
        	
        }
     
        httpSession.setAttribute("pincode", pincode);
        int pcode = (int)httpSession.getAttribute("pincode");
        System.out.println("pincode"+pincode+"pcode"+pcode);
        request.setAttribute("cartMap", cartMap);
        request.setAttribute("products", productList);
        request.getRequestDispatcher("Cart.jsp").forward(request, response);
    }
}
