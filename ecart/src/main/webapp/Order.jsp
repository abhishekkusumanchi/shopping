<%@ page import="com.ecart.models.*" language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.util.stream.Collectors" %>



<%
    Order order = (Order) request.getAttribute("order");
    List<Product> products = (List<Product>) request.getAttribute("products");

    // Convert products list to a map for efficient lookup
    Map<Integer, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getProductId, product -> product));
%>

<html>
<head>
    <title>Order Confirmation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 90%;
            margin: auto;
            overflow: hidden;
        }

        .order-details {
            width: 100%;
            margin: 30px auto;
            background: #fff;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .order-details h2 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }

        .order-details table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        .order-details table, th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }

        .order-details th {
            background-color: #f8f8f8;
            color: #333;
        }

        .order-details td {
            background-color: #fff;
            color: #666;
        }

        .order-summary {
            margin-top: 30px;
            text-align: left;
        }

        .order-summary p {
            font-size: 18px;
            color: #333;
            margin: 5px 0;
        }

        @media (max-width: 768px) {
            .order-details table, th, td {
                font-size: 14px;
                padding: 8px;
            }

            .order-summary p {
                font-size: 16px;
            }
        }

        @media (max-width: 480px) {
            .order-details {
                padding: 10px;
            }

            .order-details table, th, td {
                font-size: 12px;
                padding: 6px;
            }

            .order-summary p {
                font-size: 14px;
            }
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
    <div class="order-details">
        <h2>Order Confirmation</h2>
        <p>Order ID: <%= order.getOrderId() %></p>
        <p>User Name: <%= order.getUserName() %></p>
        <p>Order Date: <%= order.getOrderDate() %></p>
        <p>Address ID: <%= order.getAddressId() %></p>

        <h3>Products</h3>
        <table>
            <tr>
                <th>Product ID</th>
                <th>Product Name</th>
                <th>Quantity</th>
                <th>Price (Before GST)</th>
                <th>GST (%)</th>
                <th>Total Price (After GST)</th>
                <th>Shipping Charge</th>
                <th>Shipping Discount</th>
            </tr>
            <%
                double totalAmount = 0;
                double totalShipping = 0;
                double totalShippingDiscount = 0;
                
                for (OrderProduct orderProduct : order.getOrderProducts()) {
                    Product product = productMap.get(orderProduct.getProductId());
                    double totalPriceBeforeGST = orderProduct.getPrice() * orderProduct.getQuantity();
                    double totalPriceAfterGST = totalPriceBeforeGST * (1 + (orderProduct.getTotalGst() / 100.0));
                    double shippingCharge = product.getShippingCharge() * orderProduct.getQuantity();
                    double shippingDiscount = product.getShippingDiscountAmount() * orderProduct.getQuantity();
                    
                    totalAmount += totalPriceAfterGST;
                    totalShipping += shippingCharge;
                    totalShippingDiscount += shippingDiscount;
            %>
                <tr>
                    <td><%= orderProduct.getProductId() %></td>
                    <td><%= product.getProductName() %></td>
                    <td><%= orderProduct.getQuantity() %></td>
                    <td>Rs. <%= String.format("%.2f", orderProduct.getPrice()) %></td>
                    <td><%= orderProduct.getTotalGst() %>%</td>
                    <td>Rs. <%= String.format("%.2f", totalPriceAfterGST) %></td>
                    <td>Rs. <%= String.format("%.2f", shippingCharge) %></td>
                    <td>Rs. <%= String.format("%.2f", shippingDiscount) %></td>
                </tr>
            <%
                }
            %>
        </table>

        <div class="order-summary">
            <p>Total Amount: Rs. <%= String.format("%.2f", order.getOrderTotal()) %></p>
            <%
                if (order.getDiscountCode() != null) {
                    double discountAmount = order.getOrderTotal() - totalAmount;
            %>
                <p>Coupon Code: <%= order.getDiscountCode() %></p>
                <p>Discount Applied: Rs. <%= String.format("%.2f", discountAmount) %></p>
                <p>Total Amount After Discount: Rs. <%= String.format("%.2f", totalAmount) %></p>
            <%
                }
            %>
            <p>Shipping Charge: Rs. <%= String.format("%.2f", totalShipping) %></p>
            <p>Shipping Discount: Rs. <%= String.format("%.2f", totalShippingDiscount) %></p>
            <p>Total Amount Including Shipping: Rs. <%= String.format("%.2f", (totalAmount + totalShipping - totalShippingDiscount)) %></p>
        </div>
    </div>
</body>
</html>

