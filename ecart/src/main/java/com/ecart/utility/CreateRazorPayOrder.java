package com.ecart.utility;

import java.util.ResourceBundle;

import org.json.JSONObject;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

public class CreateRazorPayOrder {
	public String createOrder(double amount) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("razorpay");
		String key_id = resourceBundle.getString("key_id");
		String key_secret = resourceBundle.getString("key_secret");

		try {
			RazorpayClient razorpay = new RazorpayClient(key_id, key_secret);
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", (int) (amount * 100));
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_receipt_" + System.currentTimeMillis());
			com.razorpay.Order razorpayOrder = razorpay.orders.create(orderRequest);
			String orderId = razorpayOrder.get("id");
			System.out.println(orderId);
			return orderId;
		} catch (RazorpayException e) {
			e.printStackTrace();
		}

		return null;

	}

}
