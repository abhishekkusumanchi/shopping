package com.ecart.bll;

import java.util.List;

import com.ecart.models.Address;
import com.ecart.models.Product;

public class BusinessLogic {

	public List<Address> setDeliverability(List<Address> addresses, int pincode) {
		
		for(Address address:addresses) {
			if(address.getPincode()==pincode) {
				address.setDeliverable(true);
			}
			else {
				address.setDeliverable(false);
			}
		}
		return addresses;
	}

	 public List<Product> applyDiscount(List<Product> products, double discountAmount) {
	        // Calculate total price of all products
	        double totalPrice = getTotalAmount(products);
	        
	        // Calculate discount proportion for each product and set the discount value
	        for (Product product : products) {
	            double productContribution = (product.getPrice() * product.getQuantity()*(1+product.getProductGST()/100)) / totalPrice;
	            double productDiscount = productContribution * discountAmount;
	            product.setDiscountAmount(productDiscount);
	        }
	        return products;
	    }

	public double getTotalAmount(List<Product> productList) {
		
		return productList.stream()
                .mapToDouble(product -> (product.getPrice() * product.getQuantity())*(1+product.getProductGST()/100))
                .sum();
	}

	public List<Product> applyShippingDiscount(List<Product> productList, double shippingDicountPercentage) {
		
		for (Product product : productList) {
            double shippingCharge = product.getShippingCharge();
            double shippingDiscountForProduct = shippingCharge * shippingDicountPercentage/100;
            product.setShippingDiscountAmount(shippingDiscountForProduct);
           
        }
		
		return productList;
	}

	public double getTotalAmountWithDiscount(List<Product> products) {
		return products.stream()
                .mapToDouble(product -> (product.getPrice() * product.getQuantity())*(1+product.getProductGST()/100)-
                		product.getDiscountAmount()+product.getShippingCharge()-product.getShippingDiscountAmount())
                .sum();
	}

}
