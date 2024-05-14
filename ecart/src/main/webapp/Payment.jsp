<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Payment Page</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 0;
	padding: 0;
}

.container {
	width: 90%;
	max-width: 1200px;
	margin: 0 auto;
	padding: 20px;
}

.cart-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 20px;
}

.cart-items {
	display: flex;
	flex-wrap: wrap;
	gap: 20px;
}

.cart-item {
	display: flex;
	align-items: center;
	width: 100%;
	border: 1px solid #ddd;
	padding: 2px;
	border-radius: 8px;
}

.cart-item img {
	max-width: 70px;
	margin-right: 30px;
	margin-left: 20px;
}

.cart-item-details {
	flex-grow: 1;
	font-size: 14px;
}

.cart-item-details h3 {
	margin-bottom: 5px;
}

.cart-item-details p {
	margin: 5px 0;
}

.cart-item-actions {
	display: flex;
	align-items: center;
}

.quantity-input {
	width: 40px;
	text-align: center;
}

.remove-btn {
	background-color: #FF6347;
	color: white;
	border: none;
	padding: 5px 10px;
	border-radius: 3px;
	cursor: pointer;
}

nav {
	background-color: #333;
	color: white;
	padding: 15px;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

nav a {
	color: white;
	text-decoration: none;
}

#couponCode, #payNowBtn {
	margin-top: 10px;
}
</style>
</head>
<body>
	<nav>
		<a href="#"><img alt="logo" src="./assets/images/logo.png"
			width="120px" height="41px"></a>
		<div>
			<% if (session.getAttribute("LOGGEDIN") != null && session.getAttribute("LOGGEDIN").equals("yes")) {
                String username = (String) session.getAttribute("USERNAME");
            %>
			<span>User, <%= username %></span>
			<% } else { %>
			<a href="Login.jsp">Login</a>
			<% } %>
		</div>
	</nav>
	<div class="container">
		<div class="cart-header">
			<h1>Payment Page</h1>
			<a href="home">Continue Shopping</a>
		</div>
		<div id="productsList" class="cart-items">
			<!-- Display products dynamically here -->
		</div>

		<div id="coupon-box">
			<label for="couponCode">Coupon Code:</label> <input type="text"
				id="couponCode" name="couponCode">
			<button  id="applyCouponBtn">Apply Coupon</button>
		</div>

		<div id="discountedAmount">
			<!-- Display discounted amounts here -->
		</div>

		<button id="payNowBtn" onclick="payNow()">Create Order</button>
	</div>

	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script>
            let totalAmount = 0;
            let couponAmount = 0;
        $(document).ready(function () {
            fetchProducts();
            $("#applyCouponBtn").click(function () {
                let couponCode = $("#couponCode").val().toUpperCase(); // Convert to uppercase
                applyCoupon(couponCode);
            });
        });

        function fetchProducts() {
            $.ajax({
                url: "getProductsForPayment",
                type: "GET",
                dataType: "json",
                success: function (response) {
                    renderProducts(response,couponAmount);
                },
                error: function (xhr, status, error) {
                    console.error("Error fetching products:", error);
                }
            });
        }
        function renderProducts(products, couponAmount) {
            let productListDiv = $("#productsList");
            productListDiv.empty(); // Clear previous content
             totalAmount = 0;
            let totalShipping = 0;
            let totalShippingDiscount = 0;
            let totalPriceWithoutDiscount = 0;
            products.forEach(product => {
                let totalPriceBeforeGST = product.price * product.quantity;
                let totalPriceAfterGST = totalPriceBeforeGST * (1 + (product.productGST / 100));
                let discountAmount = product.discountAmount;
                let totalPriceAfterDiscount = totalPriceAfterGST - discountAmount;
                totalPriceWithoutDiscount += totalPriceAfterGST
                totalAmount += totalPriceAfterDiscount;
                totalShipping += product.shippingCharge;
                totalShippingDiscount += product.shippingDiscountAmount;

                let productDiv = $("<div>").addClass("cart-item");
                let productImage = $("<img>").attr("src", product.imageUrl).attr("alt", product.productName);
                let productDetails = $("<div>").addClass("cart-item-details");
                let productName = $("<h3>").text(product.productName);
                let priceBeforeGST = $("<p>").text("Price (Before GST): Rs. " + totalPriceBeforeGST.toFixed(2));
                let gstPercentage = $("<p>").text("GST Percentage: " + product.productGST + "%");
                let priceAfterGST = $("<p>").text("Price (After GST): Rs. " + totalPriceAfterGST.toFixed(2));

                productDetails.append(productName);
                productDetails.append(priceBeforeGST);
                productDetails.append(gstPercentage);
                productDetails.append(priceAfterGST);

                if (discountAmount > 0) {
                    let discountInfo = $("<p>").text("Discount: Rs. " + discountAmount.toFixed(2));
                    let priceAfterDiscount = $("<p>").text("Price (After Discount): Rs. " + totalPriceAfterDiscount.toFixed(2));
                    productDetails.append(discountInfo);
                    productDetails.append(priceAfterDiscount);
                } else {
                    let priceAfterDiscount = $("<p>").text("Price (After Discount): Rs. " + totalPriceAfterDiscount.toFixed(2));
                    productDetails.append(priceAfterDiscount);
                }

                let quantity = $("<p>").text("Quantity: " + product.quantity);
                let totalForQuantity = $("<p>").text("Total Price for Quantity: Rs. " + totalPriceAfterDiscount.toFixed(2));

                productDetails.append(quantity);
                productDetails.append(totalForQuantity);
                productDiv.append(productImage);
                productDiv.append(productDetails);

                productListDiv.append(productDiv);
            });

            // Display total amount and shipping charge
            let totalDiv = $("<div>").addClass("total-section");
            let totalAmountText = $("<p>").text("Total Amount: Rs. " + totalPriceWithoutDiscount.toFixed(2));
            let totalShippingText = $("<p>").text("Shipping Charge: Rs. " + totalShipping.toFixed(2));
            let totalShippingDiscountText = $("<p>").text("Shipping Discount: Rs. " + totalShippingDiscount.toFixed(2));
            let totalAfterShipping = $("<p>").text("Total After Shipping: Rs. " + (totalAmount + totalShipping - totalShippingDiscount).toFixed(2));

            totalDiv.append(totalAmountText);
            
            if (couponAmount > 0) {
            	
                let couponAppliedText = $("<p>").text("Coupon Applied: Rs " + couponAmount.toFixed(2));
                let totalAmountAfterCouponText = $("<p>").text("Total Amount After Coupon: Rs " + totalAmount.toFixed(2));

                totalDiv.append(couponAppliedText);
                totalDiv.append(totalAmountAfterCouponText);

                
            }
            
            
            
            
            totalDiv.append(totalShippingText);
            totalDiv.append(totalShippingDiscountText);
            totalDiv.append(totalAfterShipping);

            productListDiv.append(totalDiv);

            // Display coupon details if a coupon is applied
          
        }


        function applyCoupon(couponCode) {
            $.ajax({
                url: "getCoupon", // Update with your servlet URL
                method: "GET",
                data: { couponCode: couponCode },
                success: function (response) {
                    handleCouponResponse(response);
                },
                error: function (xhr, status, error) {
                    console.error("Error applying coupon:", error);
                }
            });
        }
        
        
        	function handleCouponResponse(response) {
        	    if (response === null) {
        	        // No coupon available with the given code
        	        displayMessageAndClear("No coupon available with the given code.");
        	    } else if (new Date(response.expireDate) < new Date()) {
        	        // Coupon is expired
        	        displayMessageAndClear("Coupon is expired.");
        	    } else if (totalAmount < response.minValue) {
        	        // Total amount is below minimum value
        	        displayMessageAndClear("Total amount is below the minimum value required for this coupon.");
        	    } else if (response.numOfCoupons <= 0) {
        	        // Number of coupons is exhausted
        	        displayMessageAndClear("Number of coupons available for this code is exhausted.");
        	    } else {
        	    	
        	    	couponAmount = response.value;
        	    	 $.ajax({
        	             url: "getProductsForPayment?action=applyCoupon&couponCode=" + response.code,
        	             type: "GET",
        	             dataType: "json",
        	             success: function (products) {
        	                 renderProducts(products,couponAmount);
        	                 
        	             },
        	             error: function (xhr, status, error) {
        	                 console.error("Error applying coupon:", error);
        	             }
        	         });
        	    }
        	}
        
        
        function displayMessageAndClear(message) {
            let messageDiv = $("<div>").text(message).css({
                "color": "red",
                "font-size": "14px",
                "margin-top": "10px"
            });
            $("#coupon-box").after(messageDiv);
            setTimeout(function () {
                messageDiv.fadeOut("slow", function () {
                    $(this).remove();
                });
            }, 3000); // Message disappears after 3 seconds
            $("#applyCoupon").val(""); // Clear the coupon code input
        }

        function payNow() {
            window.location.href = 'createOrder';
        }
    </script>
</body>
</html>
