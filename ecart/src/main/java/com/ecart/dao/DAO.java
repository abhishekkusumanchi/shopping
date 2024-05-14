package com.ecart.dao;

import java.util.List;
import java.util.Map;

import com.ecart.models.Address;
import com.ecart.models.Coupon;
import com.ecart.models.Order;
import com.ecart.models.Product;
import com.ecart.models.ProductCategory;
import com.ecart.models.User;


public interface DAO {
	List<Product> getProductsByCategory(int pageNo, int categoryId);

	List<Product> getProducts(int pageNo);

	int getNoOfProducts();

	int getNoOfProductsByCategory(int categoryId);

	List<ProductCategory> getAllCategories();

	boolean isValidUser(String uname, String password);

	boolean createUser(User user);

	

	List<Address> getAddresses(String userName);

	List<Product> getProducts(List<Integer> pids, int pincode);

	boolean addAddress(Address address);

	Coupon getCouponByCode(String code);

	double getShippingPercentage(double total);

	Order createOrder(String username, List<Product> products, int selectedAddress,
			String couponCode);
}
