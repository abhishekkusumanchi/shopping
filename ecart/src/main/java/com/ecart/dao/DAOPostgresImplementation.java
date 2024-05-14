package com.ecart.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import com.ecart.bll.BusinessLogic;
import com.ecart.models.Address;
import com.ecart.models.Coupon;
import com.ecart.models.Order;
import com.ecart.models.OrderProduct;
import com.ecart.models.Product;
import com.ecart.models.ProductCategory;
import com.ecart.models.User;
import com.ecart.utility.HashGenerator;

public class DAOPostgresImplementation implements DAO {
	private Connection connection;

	public DAOPostgresImplementation() {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("db");
		String driver = resourceBundle.getString("driver");
		String url = resourceBundle.getString("url");
		String uname = resourceBundle.getString("userName");
		String pass = resourceBundle.getString("password");
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, uname, pass);
			System.out.println("Connected to PostgreSQL database!");
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Product> getProducts(int pageNo) {
		return getProductsByCategoryOrAll(pageNo, 0); // Passing 0 as the category ID for all products
	}

	public List<Product> getProductsByCategory(int pageNo, int categoryId) {
		return getProductsByCategoryOrAll(pageNo, categoryId);
	}

	private List<Product> getProductsByCategoryOrAll(int pageNo, int categoryId) {
		List<Product> products = new ArrayList<>();
		try {
			String query = "SELECT p.prod_id, p.prod_prct_id, p.prod_title, p.prod_base_price, h.hsnc_gstc_percentage, p.prod_image, ps.prod_stock "
					+ "FROM Products p " + "JOIN HSNCodes h ON p.prod_hsnc_id = h.hsnc_hsncode "
					+ "JOIN ProductStock ps ON p.prod_id = ps.prod_id ";
			if (categoryId != 0) {
				query += "WHERE p.prod_prct_id = ? ";
			}
			query += "LIMIT ? OFFSET ? ";

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			int parameterIndex = 1;
			if (categoryId != 0) {
				preparedStatement.setInt(parameterIndex++, categoryId);
			}
			preparedStatement.setInt(parameterIndex++, 12);
			preparedStatement.setInt(parameterIndex, (pageNo - 1) * 12);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int productId = rs.getInt("prod_id");
				int productCategoryId = rs.getInt("prod_prct_id");
				String productName = rs.getString("prod_title");
				double price = rs.getDouble("prod_base_price");
				double hsnGST = rs.getDouble("hsnc_gstc_percentage");
				String imageUrl = rs.getString("prod_image");
				int productStock = rs.getInt("prod_stock");

				Product product = new Product();
				product.setProductId(productId);
				product.setProductCategoryId(productCategoryId);
				product.setProductName(productName);
				product.setPrice(price);
				product.setProductGST(hsnGST);
				product.setImageUrl(imageUrl);
				product.setProductStock(productStock);

				products.add(product);
			}
			System.out.println(query + products);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public List<Product> getProducts(List<Integer> pids, int pincode) {
		List<Product> products = new ArrayList<>();
		try {

			List<String> placeholders = Collections.nCopies(pids.size(), "?");
			String prodIdPlaceholders = String.join(", ", placeholders);

			String query = "SELECT p.prod_id, p.prod_prct_id, p.prod_title, p.prod_base_price, "
					+ "p.prod_image, p.prod_shipping_charge, ps.prod_stock, " + "h.hsnc_gstc_percentage AS gst, "
					+ "CASE WHEN u.pincode IS NULL THEN true ELSE false END AS deliverable " + "FROM Products p "
					+ "JOIN ProductStock ps ON p.prod_id = ps.prod_id "
					+ "LEFT JOIN UnserviceableRegions u ON p.prod_id = u.prod_id AND u.pincode = ? "
					+ "JOIN HSNCodes h ON p.prod_hsnc_id = h.hsnc_hsncode " + "WHERE p.prod_id IN ("
					+ prodIdPlaceholders + ")";

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, pincode);

			for (int i = 0; i < pids.size(); i++) {
				preparedStatement.setInt(i + 2, pids.get(i));
			}

			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int productId = rs.getInt("prod_id");
				int productCategoryId = rs.getInt("prod_prct_id");
				String productName = rs.getString("prod_title");
				double price = rs.getDouble("prod_base_price");
				String imageUrl = rs.getString("prod_image");
				double shippingCharge = rs.getDouble("prod_shipping_charge");
				int productStock = rs.getInt("prod_stock");
				double gst = rs.getDouble("gst");
				boolean deliverable = rs.getBoolean("deliverable");

				Product product = new Product();
				product.setProductId(productId);
				product.setProductCategoryId(productCategoryId);
				product.setProductName(productName);
				product.setPrice(price);
				product.setImageUrl(imageUrl);
				product.setShippingCharge(shippingCharge);
				product.setProductStock(productStock);
				product.setProductGST(gst);
				product.setDeliverable(deliverable);

				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public int getNoOfProducts() {
		try {

			String query = "SELECT count(*) FROM Products ";

			PreparedStatement preparedStatement = connection.prepareStatement(query);

			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 1;
	}

	@Override
	public int getNoOfProductsByCategory(int categoryId) {
		try {

			String query = "SELECT count(*) FROM Products WHERE prod_prct_id = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, categoryId);

			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 1;
	}

	@Override
	public List<ProductCategory> getAllCategories() {
		List<ProductCategory> categories = new ArrayList<>();

		String query = "SELECT prct_id,  prct_title FROM ProductCategories";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int categoryId = rs.getInt("prct_id");
				String categoryName = rs.getString("prct_title");
				ProductCategory category = new ProductCategory(categoryId, categoryName);
				categories.add(category);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return categories;
	}

	@Override
	public boolean isValidUser(String uname, String password) {
		String givenHash = new HashGenerator().generateHash(password);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			String query = "SELECT password_hash FROM User_Credentials WHERE user_name = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, uname);
			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				String dbHash = rs.getString("password_hash");
				if (dbHash.equals(givenHash)) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	@Override
	public boolean createUser(User user) {

		try {
			String queryString = "insert into User_Credentials (user_name,email,password_hash) values(?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setString(1, user.getUserName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, new HashGenerator().generateHash(user.getPassword()));
			int rows = preparedStatement.executeUpdate();
			return rows == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	@Override
	public List<Address> getAddresses(String userName) {
		System.out.println(userName);
		List<Address> addressList = new ArrayList<>();
		String query = "SELECT * FROM Address_info WHERE user_name=?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, userName);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Address address = new Address();
					address.setAddressId(rs.getInt("address_id"));
					address.setUserName(rs.getString("user_name"));
					address.setCustomerName(rs.getString("customer_name"));
					address.setMobile(rs.getString("mobile"));
					address.setEmail(rs.getString("email"));
					address.setPincode(rs.getInt("pincode"));
					address.setAddress(rs.getString("address"));
					addressList.add(address);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addressList;
	}

	@Override
	public boolean addAddress(Address address) {
		String query = "INSERT INTO Address_info (user_name, customer_name, mobile, email, pincode, address)"
				+ "VALUES (?, ?, ?, ?, ?, ?);";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, address.getUserName());
			ps.setString(2, address.getCustomerName());
			ps.setString(3, address.getMobile());
			ps.setString(4, address.getEmail());
			ps.setInt(5, address.getPincode());
			ps.setString(6, address.getAddress());

			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Coupon getCouponByCode(String code) {
		Coupon coupon = null;
		String query = "SELECT * FROM DiscountCoupon WHERE dcpn_code = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, code);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				coupon = new Coupon();
				coupon.setCode(resultSet.getString("dcpn_code"));
				coupon.setValue(resultSet.getDouble("dcpn_value"));
				coupon.setNumOfCoupons(resultSet.getInt("no_of_cpn"));
				coupon.setMinValue(resultSet.getDouble("dcpn_min_val"));
				coupon.setExpireDate(resultSet.getString("expire_date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return coupon;
	}

	@Override
	public double getShippingPercentage(double totalAmount) {
		double shippingDiscountPercentage = 0;

		try {
			PreparedStatement statement = connection.prepareStatement(
					"SELECT discount FROM ShippingDiscount WHERE ? BETWEEN total_price_from AND total_price_to");

			statement.setDouble(1, totalAmount);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				shippingDiscountPercentage = resultSet.getDouble("discount");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return shippingDiscountPercentage;
	}

	@Override
	public Order createOrder(String userName, List<Product> products, int selectedAddress, String couponCode) {
		System.out.println("in create order,copuonm" + couponCode);
		Order order = new Order();
		try {
			connection.setAutoCommit(false);
			String insertOrderSQL;
			if (couponCode != null)
				insertOrderSQL = "INSERT INTO Orders (user_name, order_date, order_total, address_id, dcpn_code) VALUES (?, NOW(), ?, ?, ?)";
			else
				insertOrderSQL = "INSERT INTO Orders (user_name, order_date, order_total, address_id) VALUES (?, NOW(), ?, ?)";

			PreparedStatement pstmtOrder = connection.prepareStatement(insertOrderSQL,
					PreparedStatement.RETURN_GENERATED_KEYS);
			double total = new BusinessLogic().getTotalAmountWithDiscount(products);
			pstmtOrder.setString(1, userName);
			pstmtOrder.setDouble(2, total);
			pstmtOrder.setInt(3, selectedAddress);
			if (couponCode != null)
				pstmtOrder.setString(4, couponCode);
			pstmtOrder.executeUpdate();

			ResultSet rs = pstmtOrder.getGeneratedKeys();
			int orderId = -1;
			if (rs.next()) {
				orderId = rs.getInt(1);
			}
			order.setOrderId(orderId);

			List<OrderProduct> orderProducts = new ArrayList<>();
			String insertOrderProductSQL = "INSERT INTO Order_Product (order_ID, product_id, quantity, price,total_gst) VALUES (?, ?, ?, ?,?)";
			PreparedStatement pstmtOrderProduct = connection.prepareStatement(insertOrderProductSQL);
			for (Product product : products) {
				pstmtOrderProduct.setInt(1, orderId);
				pstmtOrderProduct.setInt(2, product.getProductId());
				pstmtOrderProduct.setInt(3, product.getQuantity());
				pstmtOrderProduct.setDouble(4, product.getPrice());
				pstmtOrderProduct.setDouble(5, product.getProductGST());

				orderProducts.add(new OrderProduct(orderId, product.getProductId(), product.getQuantity(),
						product.getPrice(), product.getProductGST()));
				pstmtOrderProduct.addBatch();
			}

			pstmtOrderProduct.executeBatch();
			connection.commit();

			order.setOrderId(orderId);
			order.setUserName(userName);
			order.setOrderDate(LocalDate.now().toString());
			order.setOrderTotal(total);
			order.setOrderProducts(orderProducts);

		} catch (Exception e) {
			order = null;
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		return order;
	}

}
