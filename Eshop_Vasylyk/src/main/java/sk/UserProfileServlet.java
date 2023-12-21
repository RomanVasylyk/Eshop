package sk;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/UserProfile")
public class UserProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        Connection connection = (Connection) session.getAttribute("dbConnection");

        String login = (String) session.getAttribute("login");
        session.setAttribute("je_admin", true);
        Boolean isAdmin = (Boolean) session.getAttribute("je_admin");
        try {
			isAdmin = isAdmin(connection, login);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if(isAdmin && session.getAttribute("login") != null) {
            response.sendRedirect("AdminPanel");
        }
        if (session.getAttribute("login") != null) {

            if (connection == null) {
                out.println("Error: Database connection is null.");
                return;
            }

            try {
                String query = "SELECT * FROM users WHERE login = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, login);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        int userID = resultSet.getInt("ID"); 
                        String password = resultSet.getString("passwd");
                        String address = resultSet.getString("adresa");
                        String firstName = resultSet.getString("meno");
                        String lastName = resultSet.getString("priezvisko");
                        String notes = resultSet.getString("poznamky");
                        
                        
                        out.println("<html><head><title>User Profile</title></head><body>");
                        out.println("<style>");
                        out.println("  body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f5f5f5; }");
                        out.println("  h2 { color: #333; text-align: center; padding: 20px 0; }");
                        out.println("  ul { list-style-type: none; margin: 0; padding: 0; overflow: hidden; background-color: #333; }");
                        out.println("  li { float: left; width: 25%; }");
                        out.println("  li a { display: block; color: white; text-align: center; padding: 14px 16px; text-decoration: none; }");
                        out.println("  li a:hover { background-color: #555; }");
                        out.println("  .logout-btn { background-color: #f44336; color: white; border: none; padding: 14px 16px; text-decoration: none; cursor: pointer; }");
                        out.println("  .logout-btn:hover { background-color: #d32f2f; }");
                        
                        out.println("</style>");
                        out.println("<script>");
                        out.println("  function openOrderDetails(orderID) {");
                        out.println("    var modal = document.getElementById('orderDetailsModal_' + orderID);");
                        out.println("    modal.style.display = 'block';");
                        out.println("  }");
                        out.println("  function closeOrderDetails(orderID) {");
                        out.println("    var modal = document.getElementById('orderDetailsModal_' + orderID);");
                        out.println("    modal.style.display = 'none';");
                        out.println("  }");
                        out.println("</script>");

                        out.println("<body>");
                        out.println("<ul>");
                        
                        if(isAdmin != null && isAdmin) {
                        out.println("  <li><a href='AdminPanel'>Admin Panel</a></li>"); 
                        }else {
                        	out.println("  <li><a href='Shop'>Shop</a></li>");
                            out.println("  <li><a href='Cart'>Cart</a></li>");
                            out.println("  <li><a href='UserProfile'>" + login + "</a></li>");
                        }
                        out.println("  <li><form method='post' action='Shop'><input class='logout-btn' type='submit' name='action' value='Logout'></form></li>");
                        out.println("</ul>");

                        out.println("<h2>User Profile</h2>");
                        out.println("<style>");
                        out.println("  body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f5f5f5; }");
                        out.println("  h2 { color: #333; text-align: center; padding: 20px; }");
                        out.println("  .container { display: flex; justify-content: space-around; }");
                        out.println("  .form-container { flex: 1; max-width: 400px; background-color: #fff; padding: 20px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
                        out.println("  label { display: block; margin-bottom: 8px; }");
                        out.println("  input[type='text'], input[type='password'], input[type='email'], textarea { width: 100%; padding: 10px; margin-bottom: 15px; box-sizing: border-box; }");
                        out.println("  .submit-button { background-color: #4caf50; color: white; padding: 10px 15px; border: none; cursor: pointer; }");
                        out.println("  .submit-button:hover { background-color: #45a049; }");
                        out.println("</style>");

                        out.println("<div class='container'>");
                        out.println("  <div class='form-container'>");
                        out.println("    <form method='post' action='UserProfile'>");
                        out.println("      <label for='rLogin'>Login:</label>");
                        out.println("      <input type='email' id='Login' name='login' value='" + login + "'><br>");
                        out.println("      <label for='Password'>Password:</label>");
                        out.println("      <input type='password' id='regPassword' name='passwd' value='" + password + "' required><br>");
                        out.println("      <label for='Address'>Address:</label>");
                        out.println("      <input type='text' id='Address' name='adresa' value='" + address + "' required><br>");
                        out.println("      <label for='FirstName'>First Name:</label>");
                        out.println("      <input type='text' id='FirstName' name='meno' value='" + firstName + "' required><br>");
                        out.println("      <label for='LastName'>Last Name:</label>");
                        out.println("      <input type='text' id='LastName' name='priezvisko' value='" + lastName + "' required><br>");
                        out.println("      <label for='Notes'>Notes:</label>");
                        out.println("      <textarea id='Notes' name='poznamky' required>" + notes + "</textarea><br>");
                        out.println("      <input type='submit' name='action' class='submit-button' value='Update'>");
                        out.println("    </form>");
                        out.println("  </div>");
                        out.println("</div>");

                        String orderQuery = "SELECT * FROM obj_zoznam WHERE ID_pouzivatela = ?";
                        try (PreparedStatement orderStatement = connection.prepareStatement(orderQuery)) {
                            orderStatement.setInt(1, userID);
                            ResultSet orderResultSet = orderStatement.executeQuery();
                            out.println("<h2>Order List</h2>");

                            while (orderResultSet.next()) {
                                int orderID = orderResultSet.getInt("ID");
                                String orderNumber = orderResultSet.getString("obj_cislo");
                                Date orderDate = orderResultSet.getDate("datum_objednavky");
                                int orderTotal = orderResultSet.getInt("suma");
                                String orderStatus = orderResultSet.getString("stav");

                                out.println("<div>");
                                out.println("<p>Order Number: " + orderNumber + "</p>");
                                out.println("<p>Order Date: " + orderDate + "</p>");
                                out.println("<p>Total: " + orderTotal + "</p>");
                                out.println("<p>Status: " + orderStatus + "</p>");

                                String orderItemsQuery = "SELECT * FROM obj_polozky WHERE ID_objednavky = ?";
                                try (PreparedStatement orderItemsStatement = connection.prepareStatement(orderItemsQuery)) {
                                    orderItemsStatement.setInt(1, orderID);
                                    ResultSet orderItemsResultSet = orderItemsStatement.executeQuery();

                                    out.println("<ul>");
                                    while (orderItemsResultSet.next()) {
                                        int productID = orderItemsResultSet.getInt("ID_tovaru");
                                        int itemPrice = orderItemsResultSet.getInt("cena");
                                        int quantity = orderItemsResultSet.getInt("ks");

                                        String productQuery = "SELECT * FROM sklad WHERE ID = ?";
                                        try (PreparedStatement productStatement = connection.prepareStatement(productQuery)) {
                                            productStatement.setInt(1, productID);
                                            ResultSet productResultSet = productStatement.executeQuery();

                                            if (productResultSet.next()) {
                                                String productName = productResultSet.getString("nazov");
                                                String productPhotoUrl = productResultSet.getString("photo_url");

                                                out.println("<li>");
                                                out.println("  <p>Product Name: " + productName + "</p>");
                                                out.println("  <p>Price: " + itemPrice + "</p>");
                                                out.println("  <p>Quantity: " + quantity + "</p>");
                                                int photoWidth = 100; 
                                                int photoHeight = 150; 
                                                out.println("  <img src='" + productPhotoUrl + "' alt='Product Photo' width='" + photoWidth + "' height='" + photoHeight + "'>");
                                                out.println("</li>");
                                            }
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                            out.println("Error fetching product details.");
                                        }
                                    }
                                    out.println("</ul>");

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    out.println("Error fetching order items.");
                                }

                                out.println("</div>");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            out.println("Error fetching orders.");
                        }
                        } else {
                            out.println("<p>User not found.</p>");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    out.println("Error processing the request.");
                }
            } else {
                response.sendRedirect("UserProfile");
            }
        }
    

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String currentLogin = (String) session.getAttribute("login");

        if (currentLogin != null) {
            Connection connection = (Connection) session.getAttribute("dbConnection");

            if (connection == null) {
                response.sendRedirect("ErrorPage");
                return;
            }

            response.setContentType("text/html"); 
            PrintWriter out = response.getWriter(); 

            try {
                String newLogin = request.getParameter("login");
                String newPassword = request.getParameter("passwd");
                String newAddress = request.getParameter("adresa");
                String newFirstName = request.getParameter("meno");
                String newLastName = request.getParameter("priezvisko");
                String newNotes = request.getParameter("poznamky");

                if (!isValidLogin(newLogin)) {
                    out.println("<html><head></head><body>");
                    out.println("<h2>Update Failed</h2>");
                    out.println("<p>Login should be in the form of an email address.</p>");
                    out.println("<a href='UserProfile'>Go back</a>");
                    out.println("</body></html>");
                    return;
                }

                if (!currentLogin.equals(newLogin) && isLoginExists(newLogin, connection)) {
                    out.println("<html><head></head><body>");
                    out.println("<h2>Update Failed</h2>");
                    out.println("<p>Login already exists. Choose a different login.</p>");
                    out.println("<a href='UserProfile'>Go back</a>");
                    out.println("</body></html>");
                    return;
                }

                String updateQuery = "UPDATE users SET login=?, passwd=?, adresa=?, meno=?, priezvisko=?, poznamky=? WHERE login=?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setString(1, newLogin);
                    updateStatement.setString(2, newPassword);
                    updateStatement.setString(3, newAddress);
                    updateStatement.setString(4, newFirstName);
                    updateStatement.setString(5, newLastName);
                    updateStatement.setString(6, newNotes);
                    updateStatement.setString(7, currentLogin);

                    int rowsUpdated = updateStatement.executeUpdate();

                    if (rowsUpdated > 0) {
                        session.setAttribute("login", newLogin);
                        response.sendRedirect("UserProfile");
                    } else {
                        response.sendRedirect("ErrorPage");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("ErrorPage");
            }
        } else {
            response.sendRedirect("UserProfile");
        }
    }


    private boolean isValidLogin(String login) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return login.matches(emailRegex);
    }


    private boolean isLoginExists(String login, Connection connection) throws SQLException {
        String query = "SELECT * FROM users WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); 
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

}
