package sk;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private int zl;
    private boolean isAdmin;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        Connection connection = (Connection) session.getAttribute("dbConnection");

        if (connection == null) {
            out.println("Error: Database connection is null.");
            return;
        }

        String login = (String) session.getAttribute("login");

		try {
			isAdmin = isAdmin(connection, login);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if(isAdmin && session.getAttribute("login") != null) {
            response.sendRedirect("AdminPanel");
        }
        if (session.getAttribute("login") != null) {
        
            int totalSum = 0;
            int userId = getUserId(session, connection);
            String username = (String) session.getAttribute("login");
            
            out.println("<html><head><title>Shopping Cart</title></head><body>");

            out.println("<style>");
            out.println("  body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f5f5f5; }");
            out.println("  h2 { color: #333; text-align: center; padding: 20px 0; }");
            out.println("  ul { list-style-type: none; margin: 0; padding: 0; overflow: hidden; background-color: #333; }");
            out.println("  li { float: left; width: 25%; }");
            out.println("  li a { display: block; color: white; text-align: center; padding: 14px 16px; text-decoration: none; }");
            out.println("  li a:hover { background-color: #555; }");
            out.println("  .logout-btn { background-color: #f44336; color: white; border: none; padding: 14px 16px; text-decoration: none; cursor: pointer; }");
            out.println("  .logout-btn:hover { background-color: #d32f2f; }");
            out.println("  table { width: 80%; margin: 20px auto; border-collapse: collapse; }");
            out.println("  th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            out.println("  th { background-color: #4CAF50; color: white; }");
            out.println("  .total-row { background-color: #ddd; font-weight: bold; }");
            out.println("  .buy-btn { background-color: #4CAF50; color: white; border: none; padding: 10px 20px; text-decoration: none; cursor: pointer; }");
            out.println("  .buy-btn:hover { background-color: #45a049; }");
            out.println("</style>");

            out.println("<body>");
            out.println("<ul>");
            
            if(isAdmin) {
            out.println("  <li><a href='AdminPanel'>Admin Panel</a></li>"); 
            }else {
            	out.println("  <li><a href='Shop'>Shop</a></li>");
                out.println("  <li><a href='Cart'>Cart</a></li>");
                out.println("  <li><a href='UserProfile'>" + username + "</a></li>");
            }
            out.println("  <li><form method='post' action='Shop'><input class='logout-btn' type='submit' name='action' value='Logout'></form></li>");
            out.println("</ul>");

            out.println("<h2>Shopping Cart for " + login + "</h2>");

            try {
            	String query = "SELECT kosik.*, sklad.nazov, users.zlava FROM kosik "
                        + "INNER JOIN sklad ON kosik.ID_tovaru = sklad.ID "
                        + "INNER JOIN users ON kosik.ID_pouzivatela = users.ID "
                        + "WHERE ID_pouzivatela = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, userId);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        out.println("<form method='post' action='Cart'>");
                        out.println("<table>");
                        out.println("<tr><th>Product Name</th><th>Price</th><th>Quantity</th><th>Action</th></tr>");

                        do {
                            String productName = resultSet.getString("nazov");
                            int productPrice = resultSet.getInt("cena");
                            int quantity = resultSet.getInt("ks");
                            int userDiscount = resultSet.getInt("zlava");
                            this.zl = userDiscount;
                            int cartItemId = resultSet.getInt("ID");

                            int discountedPrice = calculateDiscountedPrice(productPrice, userDiscount);

                            out.println("<tr>");
                            out.println("<td>" + productName + "</td>");
                            out.println("<td>$" + productPrice + "</td>");
                            out.println("<td>" + quantity + "</td>");
                            out.println("<td><button type='submit' name='action' value='Remove' formaction='Cart?productId=" + cartItemId + "'>Remove</button></td>");
                            out.println("</tr>");

                            totalSum += discountedPrice * quantity;

                        } while (resultSet.next());

                        out.println("<tr class='total-row'><td>Total Sum:</td><td colspan='1'>$" + totalSum + "</td><td colspan='1'>Zlava: " + zl + "%</td>"+ " <td colspan='1'><input class='buy-btn' type='submit' name='action' value='Buy'></td></tr>");
                        out.println("</table>");
                        out.println("</form>");


                    } else {
                        out.println("<p>Your shopping cart is empty.</p>");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            out.println("</body></html>");
            } else {
                out.println("<p>Please log in to view your shopping cart.</p>");
            }
        }


    private int getUserId(HttpSession session, Connection connection) {
        Object loginObj = session.getAttribute("login");

        if (loginObj != null) {
            String login = (String) loginObj;

            try {
                String query = "SELECT ID FROM users WHERE login = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, login);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        return resultSet.getInt("ID");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Connection connection = (Connection) session.getAttribute("dbConnection");

        if (connection == null) {
            out.println("Error: Database connection is null.");
            return;
        }

        if ("Remove".equals(action)) {
            String productIdString = request.getParameter("productId");

            if (productIdString != null) {
                try {
                    int productId = Integer.parseInt(productIdString);
                    String deleteQuery = "DELETE FROM kosik WHERE ID = ?";
                    try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                        deleteStatement.setInt(1, productId);
                        int rowsAffected = deleteStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            out.println("Product with ID " + productId + " removed from the cart.");
                        } else {
                            out.println("Failed to remove product from the cart.");
                        }
                    }

                    response.sendRedirect("Cart");
                } catch (NumberFormatException | SQLException e) {
                    e.printStackTrace();
                    out.println("Error processing the request.");
                }
            } else {
                out.println("Error: Missing parameters.");
            }
        }  else if ("Buy".equals(action)) {
            int userId = getUserId(session, connection);

            try {
                if (isProductAvailableInStock(userId, connection) ) {
                    String insertOrderQuery = "INSERT INTO obj_zoznam (obj_cislo, datum_objednavky, ID_pouzivatela, suma, stav) VALUES (?, NOW(), ?, ?, 'Processed')";
                    
                    try (PreparedStatement insertOrderStatement = connection.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        String orderNumber = generateOrderNumber();
                        int totalSum = getTotalSum(userId, connection);

                        insertOrderStatement.setString(1, orderNumber);
                        insertOrderStatement.setInt(2, userId);
                        
                        if (zl != 0) {
                            insertOrderStatement.setInt(3, (int) ((1 - zl / 100.0) * totalSum));
                        } else {
                            insertOrderStatement.setInt(3, totalSum);
                        }

                        int rowsInserted = insertOrderStatement.executeUpdate();

                        if (rowsInserted > 0) {
                            ResultSet generatedKeys = insertOrderStatement.getGeneratedKeys();
                            if (generatedKeys.next()) {
                                int orderId = generatedKeys.getInt(1);

                                moveItemsToOrder(userId, orderId, connection);

                                clearCart(userId, connection);

                                out.println("<script>");
                                out.println("  alert('Order placed successfully. Order ID: " + orderId + "');");
                                out.println("  window.location.href = 'Shop';");
                                out.println("</script>");
                            } else {
                                out.println("Failed to retrieve order ID.");
                            }
                        } else {
                            out.println("Failed to place the order.");
                        }
                    }
                } else {
                	out.println("<script>");
                	out.println("  alert('Error: Some products in your cart are not available in sufficient quantity. Please update your cart and try again.');");
                	out.println("  window.location.href = 'Cart';");
                	out.println("</script>");

                }
            } catch (SQLException e) {
                e.printStackTrace();
                out.println("Error processing the request.");
            }
        }
    }
    private String generateOrderNumber() {
        return "ORDER" + System.currentTimeMillis();
    }
    private int getTotalSum(int userId, Connection connection) throws SQLException {
        int totalSum = 0;

        String query = "SELECT SUM(cena * ks) AS totalSum FROM kosik WHERE ID_pouzivatela = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                totalSum = resultSet.getInt("totalSum");
            }
        }

        return totalSum;
    }

    private void moveItemsToOrder(int userId, int orderId, Connection connection) throws SQLException {
        String insertOrderItemQuery = "INSERT INTO obj_polozky (ID_objednavky, ID_tovaru, cena, ks) VALUES (?, ?, ?, ?)";
        String updateStockQuery = "UPDATE sklad SET ks = ks - ? WHERE ID = ?";

        String selectCartItemsQuery = "SELECT ID_tovaru, cena, ks FROM kosik WHERE ID_pouzivatela = ?";
        
        try (PreparedStatement selectCartItemsStatement = connection.prepareStatement(selectCartItemsQuery)) {
            selectCartItemsStatement.setInt(1, userId);
            
            ResultSet resultSet = selectCartItemsStatement.executeQuery();

            try (PreparedStatement insertOrderItemStatement = connection.prepareStatement(insertOrderItemQuery);
                 PreparedStatement updateStockStatement = connection.prepareStatement(updateStockQuery)) {

                while (resultSet.next()) {
                    int productId = resultSet.getInt("ID_tovaru");
                    int price = resultSet.getInt("cena");
                    int quantity = resultSet.getInt("ks");

                    insertOrderItemStatement.setInt(1, orderId);
                    insertOrderItemStatement.setInt(2, productId);
                    insertOrderItemStatement.setInt(3, (int) ((1 - zl / 100.0) * price));
                    insertOrderItemStatement.setInt(4, quantity);

                    insertOrderItemStatement.executeUpdate();

                    updateStockStatement.setInt(1, quantity);
                    updateStockStatement.setInt(2, productId);
                    updateStockStatement.executeUpdate();
                }
            }
        }
    }


    private void clearCart(int userId, Connection connection) throws SQLException {
        String deleteCartItemsQuery = "DELETE FROM kosik WHERE ID_pouzivatela = ?";
        try (PreparedStatement deleteCartItemsStatement = connection.prepareStatement(deleteCartItemsQuery)) {
            deleteCartItemsStatement.setInt(1, userId);
            deleteCartItemsStatement.executeUpdate();
        }
    }
    private int calculateDiscountedPrice(int originalPrice, int userDiscount) {
        if (userDiscount > 0) {
            double discountFactor = userDiscount / 100.0;
            return (int) Math.round(originalPrice * (1 - discountFactor));
        } else {
            return originalPrice;
        }
    }
    public static boolean isAdmin(Connection connection, String username) throws SQLException {
        String query = "SELECT je_admin FROM users WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int isAdminValue = resultSet.getInt("je_admin");
                    return isAdminValue == 1; 
                }
            }
        }
        return false; 
    }
    private boolean isProductAvailableInStock(int userId, Connection connection) throws SQLException {
        String cartQuery = "SELECT kosik.ID_tovaru, kosik.ks, sklad.ks AS availableQuantity " +
                           "FROM kosik " +
                           "JOIN sklad ON kosik.ID_tovaru = sklad.ID " +
                           "WHERE kosik.ID_pouzivatela = ?";

        try (PreparedStatement cartStatement = connection.prepareStatement(cartQuery)) {
            cartStatement.setInt(1, userId);

            try (ResultSet cartResultSet = cartStatement.executeQuery()) {
                while (cartResultSet.next()) {
                    int cartProductId = cartResultSet.getInt("ID_tovaru");
                    int cartProductQuantity = cartResultSet.getInt("ks");
                    int availableQuantity = cartResultSet.getInt("availableQuantity");

                    if (cartProductQuantity > availableQuantity) {
                        return false;
                    }
                }
            }
        }

        return true;
    }


}