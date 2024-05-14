<%@ page language="java" import="com.ecart.models.*,java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Choose Address</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .address-box {
            border: 1px solid #ccc;
            padding: 10px;
            margin-bottom: 10px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        .address-box.disabled {
            opacity: 0.5;
            cursor: not-allowed;
        }
        .address-box:hover {
            background-color: #f0f0f0;
        }
        .address-box input[type="radio"] {
            display: none;
        }
        .address-box label {
            display: block;
            padding-left: 30px;
            position: relative;
        }
        .address-box label::before {
            content: "";
            display: block;
            position: absolute;
            left: 0;
            top: 50%;
            transform: translateY(-50%);
            width: 20px;
            height: 20px;
            border: 2px solid #007bff;
            border-radius: 50%;
            transition: background-color 0.3s ease;
        }
        .address-box input[type="radio"]:checked + label::before {
            background-color: #007bff;
        }
        .address-details {
            display: inline-block;
            vertical-align: middle;
        }
        .address-details h3 {
            margin-top: 0;
            margin-bottom: 5px;
        }
        .address-details p {
            margin: 0;
        }
        #submitBtn {
            margin-top: 10px;
            padding: 5px 15px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        #submitBtn:disabled {
            background-color: #ccc;
            cursor: not-allowed;
        }
        #submitBtn:hover:not(:disabled) {
            background-color: #0056b3;
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
    <h1>Select an Address</h1>
    <form action="PaymentServlet" method="post">
        <div class="address-list">
            <% 
            List<Address> addresses = (List<Address>) request.getAttribute("addresses");
            if (addresses != null && !addresses.isEmpty()) {
                for (Address address : addresses) {
            %>
            <div class="address-box<% if (!address.isDeliverable()) { %> disabled<% } %>"
                 onclick="selectAddress('<%= address.getAddressId() %>')">
                <input type="radio" id="address<%= address.getAddressId() %>" name="selectedAddress"
                       value="<%= address.getAddressId() %>" <% if (!address.isDeliverable()) { %>disabled<% } %>>
                <label for="address<%= address.getAddressId() %>">
                    <div class="address-details">
                        <h3><%= address.getCustomerName() %></h3>
                        <p><%= address.getMobile() %></p>
                        <p><%= address.getAddress() +" - Pin Code:" + address.getPincode() %></p>
                    </div>
                </label>
            </div>
            <% 
                }
            } else {
            %>
            <p>There are no addresses available.</p>
            <% 
            }
            %>
             <a href="AddressForm.jsp">Do you want to add address?</a>
        </div>
        <input type="submit" id="submitBtn" value="Next" disabled>
    </form>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
var selectedAddressId = 0;
    $(document).ready(function() {
        $('#submitBtn').click(function(e) {
            e.preventDefault(); // Prevent the default form submission

            var selectedAddress = $('input[name="selectedAddress"]:checked').val();
            if (!selectedAddress) {
                alert('Please select an address.');
                return;
            }

          
                    window.location.href = 'payment?selectedAddressId='+selectedAddressId;
               
        });
    });
    function selectAddress(id){
    	selectedAddressId = id;
    	document.getElementById("submitBtn").disabled = false;
    }
</script>
</body>
</html>
