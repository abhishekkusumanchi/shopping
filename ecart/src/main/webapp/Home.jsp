<%@ page language="java"
	import="java.util.List,java.util.ArrayList,com.ecart.models.*,java.util.*"
	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Product Page</title>
<style type="text/css">
body {
	font-family: Arial, sans-serif;
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

#product-container {
	display: flex;
	flex-wrap: wrap;
	justify-content: space-around;
	padding: 20px;
}

.product-card {
	border: 1px solid #ddd;
	margin: 10px;
	padding: 10px;
	width: 200px;
}

.product-card img {
	width: 100%;
	height: 200px;
}

.product-card button {
	background-color: #4CAF50;
	border: none;
	color: white;
	padding: 10px 20px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 16px;
	margin: 4px 2px;
	cursor: pointer;
}

.pagination {
	display: flex;
	justify-content: center;
	margin-top: 20px;
}

.pagination button {
	background-color: #4CAF50;
	color: #fff;
	border: none;
	padding: 10px 15px;
	border-radius: 5px;
	margin: 0 5px;
	cursor: pointer;
	transition: background-color 0.3s ease;
}

.pagination button:hover {
	background-color: #0056b3;
}

.quantity-control {
	margin-top: 10px;
	display: flex;
	align-items: center;
}

.quantity-btn {
	background-color: #4CAF50;
	color: white;
	border: none;
	cursor: pointer;
	padding: 5px 10px;
}

.quantity-input {
	width: 50px;
	text-align: center;
}

.add-to-cart-btn {
	background-color: #4CAF50;
	border: none;
	color: white;
	padding: 10px 20px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 16px;
	margin-top: 10px;
	cursor: pointer;
}

.current-page {
	font-size: 14px;
	float: right;
	margin-right: 10px;
	padding: 8px 12px;
	border-radius: 5px;
}
</style>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	 <%
	List<Product> cartproducts = null;
	
	%> 
	<nav>
		<a href="#"><img alt="logo" src="./assets/images/logo.png"
			width="120px" height="41px"></a>
		<div>
			<select id="categoryDropdown" onchange="filterByCategory()">
				<option value="all">All Categories</option>
				<%
				List<ProductCategory> allCategories = (List<ProductCategory>) request.getAttribute("allCategories");
				for (ProductCategory category : allCategories) {
				%>
				<option value="<%=category.getCategoryId()%>"><%=category.getTitle()%></option>
				<%
				}
				%>

			</select>
			<%
			if (session.getAttribute("LOGGEDIN") != null && session.getAttribute("LOGGEDIN").equals("yes")) {
			    String username = (String) session.getAttribute("USERNAME");
			%>
			<span>Welcome, <%= username %></span>
			<!-- Add a logout link here if needed -->
			<%
			} else {
				%>
			<a href="Login.jsp">Login</a>
			<%
				}
			%>
			<a href="#" onclick="promptAndOpenCart();"><img src="./assets/images/cart-icon.png"
				height="40px" width="40px" alt="Cart"></a>
		</div>
	</nav>

	<div id="product-container">
		<!-- Placeholder for product data -->
	</div>
	<div class="pagination">
		<button id="prevButton">Previous</button>
		<button id="nextButton">Next</button>
		<div class="current-page">Page 1</div>
	</div>

	<script>
	var currentPage = 1;
    var totalPages = 1; 
    var itemCounter = 0;
	
    $(document).ready(function () {
    	
    	 
        loadData();
        $('#categoryDropdown').change(function () {
        	console.log("hello");
            loadData(); 
        });
       
    });
    function promptAndOpenCart() {
        // Prompt the user to enter the pincode
        
    	 $.ajax({
    	        url: 'checkCart',
    	        method: 'GET',
    	        dataType: 'json',
    	        success: function(data) {
    	            if (data.status === 'success' && data.cartSize > 0) {
    	                // Cart is not empty, prompt for pincode
    	                let pincode = prompt("Enter your pincode:");
    	                if (pincode !== null && pincode.trim() !== "") {
    	                    window.location.href = "cart?pincode=" + encodeURIComponent(pincode);
    	                } else {
    	                    alert("Please enter a valid pincode to proceed to the cart.");
    	                }
    	            } else {
    	            	/* let pincode = prompt("Enter your pincode:");
    	                if (pincode !== null && pincode.trim() !== "") { */
    	                    window.location.href = "cart?pincode=0" 
    	                    
    	               /*  } */
    	            }
    	        },
    	        error: function(xhr, status, error) {
    	            console.error('Error checking cart:', error);
    	            window.location.href = "cart?pincode=0"; // Redirect with pincode 0 on error
    	        }
    	    });
    }

    
    function loadData() {
        var category = $('#categoryDropdown').val();
        var pageNumber = 1; // You can track the current page number

        // Make an AJAX call to fetch the number of pages
        $.ajax({
            url: '/ecart/getNoOfpages',
            type: 'GET',
            data: { categoryId: category },
            success: function (response) {
                 totalPages = response.pages;
               
                
                loadProductData(pageNumber);
                
            },
            error: function (xhr, status, error) {
                console.error('Error fetching number of pages:', status, error);
            }
        });
    }

    function loadProductData(pageNumber) {
        var category = $('#categoryDropdown').val();
        var url = '/ecart/getProducts/' + pageNumber;
        if (category !== 'all') {
            url += '?categoryId=' + category;
        }
        // Make an AJAX call to fetch product data based on category and page number
        $.ajax({
            url: url,
            type: 'GET',
            success: function (response) {
                renderProducts(response);
            },
            error: function (xhr, status, error) {
                console.error('Error fetching data:', status, error);
            }
        });
    }

    function renderProducts(products) {
        var productContainer = $('#product-container');
        productContainer.empty();

        const arr = Array.from(products);
        arr.forEach(function (product) {
        	
        	var productHtml = '<div class="product-card" data-product-id="' + product.productId + '">' +
            '<img src="' + product.imageUrl + '" alt="' + product.productName + '">' +
            '<h2>' + product.productName + '</h2>' +
            '<p>Price: Rs.' + Math.ceil(product.price * (1 + product.productGST / 100)) + '</p>';

        if (product.productStock > 0) {
            productHtml += '<div class="quantity-control" style="display:none;" id="quantity-control-'+product.productId+'">'+
                '<button class="quantity-btn minus" id="minus-btn-' + product.productId + '" onclick="removeFromCart(' + product.productId + ',' + product.productStock + ')">-</button>' +
                '<input type="number" class="quantity-input" id="quantity-' + product.productId + '" value="0">' +
                '<button class="quantity-btn plus" id="plus-btn-' + product.productId + '" onclick="addToCart(' + product.productId + ',' + product.productStock + ')">+</button>' +
                '</div>' +
                '<button class="add-to-cart-btn" id="add-to-cart-' + product.productId + '" onclick="addToCart(' + product.productId + ',' + product.productStock + ')">Add to Cart</button>';
        } else {
            productHtml += '<img src="https://static.thenounproject.com/png/213498-200.png"  style="width: 100px; height: 70px;" alt="Out of Stock" class="out-of-stock-image">';
        }

        productHtml += '</div>';



            productContainer.append(productHtml);
        });
        updatePaginationButtons();
    }

    function filterByCategory() {
        loadData(); // Call the function to load data when the category is changed
    }

    $('#prevButton').click(function () {
    	if (currentPage > 1) {
            currentPage--; 
            loadProductData(currentPage); // Load data for the previous page
            updatePaginationButtons(); // Update pagination buttons
        }
    });

    $('#nextButton').click(function () {
    	if (currentPage < totalPages) {
            currentPage++; // Increment current page
            loadProductData(currentPage); // Load data for the next page
            updatePaginationButtons(); // Update pagination buttons
        }
    });

    function addToCart(productId,productStock) {
    	itemCounter++;
    	
    	var quantityInput = $('#quantity-' + productId);
        var quantity = parseInt(quantityInput.val());
		if(quantity < productStock){
        	 var quantityControl = $('#quantity-control-' + productId);
	    	    quantityControl.show(); 
	    	
	    	
	            quantityInput.val(quantity + 1); 
	    	
	    		var url = '/ecart/cartAction?action=addProduct&pid=' + productId;
	 			$.ajax({
	  	             url: url,
	  	             type: 'GET',
	  	             success: function (response) {
	  	             },
	  	             error: function (xhr, status, error) {
	  	                 console.error('Error fetching data:', status, error);
	  	             }
	         });
	 		
       
		}else{
			$('#plus-btn-' + productId).prop('disabled', true);
			$('#add-to-cart-' + productId).prop('disabled', true);
			$('#add-to-cart-' + productId).css('background-color', '#2f9052');
			$('#plus-btn-' + productId).css('background-color', '#2f9052');
			 
		}
		
    	
    }
	function removeFromCart(productId,productStock){
		itemCounter--;
		
		var quantityInput = $('#quantity-' + productId);
	    var quantity = parseInt(quantityInput.val()); // Get the current quantity
	    
	    if (quantity > 1) {
	        quantityInput.val(quantity - 1); // Decrement the quantity
	    } else {
	        quantityInput.val(0); // Set the quantity to 1 if it's less than 1
	        $('#minus-btn-' + productId).prop('disabled', true); // Disable the minus button
	        $('#quantity-control-' + productId).hide();
	    }
		 var url = '/ecart/cartAction?action=removeProduct&pid=' + productId;
		$.ajax({
            url: url,
            type: 'GET',
            success: function (response) {
            },
            error: function (xhr, status, error) {
                console.error('Error fetching data:', status, error);
            }
        });
		if(quantity <= productStock){
			$('#plus-btn-' + productId).prop('disabled', false);
			$('#add-to-cart-' + productId).prop('disabled', false);
			$('#add-to-cart-' + productId).css('background-color', '#4CAF50');
			$('#plus-btn-' + productId).css('background-color', '#4CAF50');
			 
		}
	}
	function updateCurrentPage() {
        $('.current-page').text('Page ' + currentPage); // Update text content
    }
	function updatePaginationButtons() {
		updateCurrentPage();
        $('#prevButton').prop('disabled', currentPage === 1);
       
        $('#nextButton').prop('disabled', currentPage === totalPages); 
        if(currentPage===1)
        	 $('#prevButton').hide();
        else
        	 $('#prevButton').show();
        if(currentPage === totalPages)
        	$('#nextButton').hide();
        else
        	$('#nextButton').show();
    }
</script>
</body>