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

@WebServlet("/AdminPanel")
public class AdminPanelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private boolean isAdmin;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            String login = (String) session.getAttribute("login");
            Connection connection = (Connection) session.getAttribute("dbConnection");
            if (connection == null) {
                out.println("Error: Database connection is null.");
                return;
            }


    		try {
    			isAdmin = isAdmin(connection, login);
    		} catch (SQLException e1) {
    			e1.printStackTrace();
    		}
    		if(!(isAdmin && session.getAttribute("login") != null)) {
                response.sendRedirect("Shop");
            }
    		else if(isAdmin && session.getAttribute("login") != null) {
              
            out.println("<html><head><title>Admin Panel</title></head><body>");
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
            out.println("<ul>");
            if (isAdmin) {
                out.println("  <li><a href='AdminPanel'>Admin Panel</a></li>");  
            }else {
            	out.println("  <li><a href='Shop'>Shop</a></li>");
                out.println("  <li><a href='Cart'>Cart</a></li>");
                out.println("  <li><a href='UserProfile'>" + login + "</a></li>");
            }
            out.println("<style>");
            out.println("  body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f5f5f5; }");
            out.println("  h2 { color: #333; text-align: center; padding: 20px; }");
            out.println("  .container { display: flex; justify-content: space-around; }");
            out.println("  .form-container { flex: 1; max-width: 400px; background-color: #fff; padding: 20px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
            out.println("  label { display: block; margin-bottom: 8px; }");
            out.println("  input[type='text'], input[type='password'], input[type='email'], textarea { width: 100%; padding: 10px; margin-bottom: 15px; box-sizing: border-box; }");
            out.println("  select { width: 100%; padding: 10px; margin-bottom: 15px; box-sizing: border-box; }");
            out.println("  .submit-button { background-color: #4caf50; color: white; padding: 10px 15px; border: none; cursor: pointer; }");
            out.println("  .submit-button:hover { background-color: #45a049; }");
            out.println("  table { border-collapse: collapse; width: 100%; }");
            out.println("  th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            out.println("  th { background-color: #4CAF50; color: white; }");
            out.println("</style>");
            
            out.println("  <li><form method='post' action='Shop'><input class='logout-btn' type='submit' name='action' value='Logout'></form></li>");
            out.println("</ul>");
            out.println("<h3>Order List</h3>");
            out.println("<table border='1'>");
            out.println("<tr><th>Order Number</th><th>Order Date</th><th>User ID</th><th>Total</th><th>Status</th><th>Action</th></tr>");

            try {
                String query = "SELECT * FROM obj_zoznam";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int orderId = resultSet.getInt("ID");
                        String orderNumber = resultSet.getString("obj_cislo");
                        Date orderDate = resultSet.getDate("datum_objednavky");
                        int userId = resultSet.getInt("ID_pouzivatela");
                        int total = resultSet.getInt("suma");
                        String status = resultSet.getString("stav");

                        out.println("<tr>");
                        out.println("<td>" + orderNumber + "</td>");
                        out.println("<td>" + orderDate + "</td>");
                        out.println("<td>" + userId + "</td>");
                        out.println("<td>" + total + "</td>");
                        out.println("<td>" + status + "</td>");
                        out.println("<td>");

                        out.println("<form method='post' action='AdminPanel' class='form-container'>");
                        out.println("  <input type='hidden' name='orderId' value='" + orderId + "'>");
                        out.println("  <label for='newStatus'>Select Status:</label>");
                        out.println("  <select name='newStatus' id='newStatus'>");
                        out.println("    <option value='Processed'>Processed</option>");
                        out.println("    <option value='Shipped'>Shipped</option>");
                        out.println("    <option value='Paid'>Paid</option>");
                        out.println("  </select>");
                        out.println("  <input type='submit' name='action' value='ChangeStatus' class='submit-button'>");
                        out.println("</form>");

                        out.println("<form method='post' action='AdminPanel' class='form-container'>");
                        out.println("  <input type='hidden' name='orderId' value='" + orderId + "'>");
                        out.println("  <input type='submit' name='action' value='DeleteOrder' class='submit-button' style='background-color: #f44336;'>");
                        out.println("</form>");
                        
                        out.println("</td>");
                        out.println("</tr>");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            out.println("</table>");

            out.println("</body></html>");
            out.println("<h3>User List</h3>");
            out.println("<table border='1'>");
            out.println("<tr><th>User ID</th><th>Login</th><th>Admin Rights</th><th>Action</th></tr>");

            try {
                String queryUsers = "SELECT * FROM users";
                try (PreparedStatement preparedStatementUsers = connection.prepareStatement(queryUsers)) {
                    ResultSet resultSetUsers = preparedStatementUsers.executeQuery();
                    while (resultSetUsers.next()) {
                        int userId = resultSetUsers.getInt("ID");
                        String userLogin = resultSetUsers.getString("login");
                        boolean userIsAdmin = resultSetUsers.getBoolean("je_admin");

                        out.println("<tr>");
                        out.println("<td>" + userId + "</td>");
                        out.println("<td>" + userLogin + "</td>");
                        out.println("<td>" + (userIsAdmin ? "Admin" : "User") + "</td>");
                        out.println("<td>");

                        out.println("<form method='post' action='AdminPanel' class='form-container'>");
                        out.println("  <input type='hidden' name='userId' value='" + userId + "'>");
                        out.println("  <label for='isAdmin'>Admin Rights:</label>");
                        out.println("  <select name='isAdmin' id='isAdmin'>");
                        out.println("    <option value='1'" + (userIsAdmin ? " selected" : "") + ">Admin</option>");
                        out.println("    <option value='0'" + (userIsAdmin ? "" : " selected") + ">User</option>");
                        out.println("  </select>");
                        out.println("  <input type='submit' name='action' value='ChangeRights' class='submit-button'>");
                        out.println("</form>");

                        out.println("</td>");
                        out.println("</tr>");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            out.println("</table>");
        }
             
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String login = (String) session.getAttribute("login");
        Connection connection = (Connection) session.getAttribute("dbConnection");

        if (connection == null) {
            response.getWriter().println("Error: Database connection is null.");
            return;
        }

        
        try {
			isAdmin = isAdmin(connection, login);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if(!isAdmin && session.getAttribute("login") != null) {
            response.getWriter().println("Error: Not admin.");
            return;
        }

        String action = request.getParameter("action");

        if ("ChangeStatus".equals(action)) {
            String orderIdStr = request.getParameter("orderId");
            String newStatus = request.getParameter("newStatus");

            if (orderIdStr != null) {
                int orderId = Integer.parseInt(orderIdStr);

                try {
                    String updateQuery = "UPDATE obj_zoznam SET stav=? WHERE ID=?";

                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, newStatus);
                        updateStatement.setInt(2, orderId);

                        int rowsAffected = updateStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            response.sendRedirect(request.getContextPath() + "/AdminPanel");
                        } else {
                            response.getWriter().println("Error: Unable to update order status.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.getWriter().println("Error: Database error occurred.");
                }
            } else {
                response.getWriter().println("Error: Invalid order ID.");
            }
        } else if ("DeleteOrder".equals(action)) {
            String orderIdStr = request.getParameter("orderId");

            if (orderIdStr != null) {
                int orderId = Integer.parseInt(orderIdStr);

                try {
                    String checkStatusQuery = "SELECT stav FROM obj_zoznam WHERE ID=?";
                    try (PreparedStatement checkStatusStatement = connection.prepareStatement(checkStatusQuery)) {
                        checkStatusStatement.setInt(1, orderId);
                        ResultSet statusResultSet = checkStatusStatement.executeQuery();

                        if (statusResultSet.next()) {
                            String orderStatus = statusResultSet.getString("stav");

                            if ("Processed".equals(orderStatus)) {
                                String returnItemsToStockQuery = "UPDATE sklad SET ks = ks + (SELECT ks FROM obj_polozky WHERE ID_objednavky=?) WHERE ID = (SELECT ID_tovaru FROM obj_polozky WHERE ID_objednavky=?)";
                                try (PreparedStatement returnItemsToStockStatement = connection.prepareStatement(returnItemsToStockQuery)) {
                                    returnItemsToStockStatement.setInt(1, orderId);
                                    returnItemsToStockStatement.setInt(2, orderId);
                                    returnItemsToStockStatement.executeUpdate();
                                }

                                String deleteItemsQuery = "DELETE FROM obj_polozky WHERE ID_objednavky=?";
                                try (PreparedStatement deleteItemsStatement = connection.prepareStatement(deleteItemsQuery)) {
                                    deleteItemsStatement.setInt(1, orderId);
                                    deleteItemsStatement.executeUpdate();
                                }

                                String deleteOrderQuery = "DELETE FROM obj_zoznam WHERE ID=?";
                                try (PreparedStatement deleteOrderStatement = connection.prepareStatement(deleteOrderQuery)) {
                                    deleteOrderStatement.setInt(1, orderId);

                                    int rowsAffected = deleteOrderStatement.executeUpdate();

                                    if (rowsAffected > 0) {
                                        response.sendRedirect(request.getContextPath() + "/AdminPanel");
                                    } else {
                                        response.getWriter().println("Error: Unable to delete order.");
                                    }
                                }
                            } else {
                                out.println("<script>");
                                out.println("alert('Unable to delete order. Order is not in \"Processed\" status.');");
                                out.println("  window.location.href = 'AdminPanel';");
                                out.println("</script>");
                            }
                        } else {
                            response.getWriter().println("Error: Unable to retrieve order status.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.getWriter().println("Error: Database error occurred.");
                }
            } else {
                response.getWriter().println("Error: Invalid order ID.");
            }
        }

else if ("ChangeRights".equals(action)) {
            String userIdStr = request.getParameter("userId");
            String isAdminStr = request.getParameter("isAdmin");

            if (userIdStr != null && isAdminStr != null) {
                int userId = Integer.parseInt(userIdStr);
                boolean isAdminRights = "1".equals(isAdminStr);

                try {
                    String updateRightsQuery = "UPDATE users SET je_admin=? WHERE ID=?";

                    try (PreparedStatement updateRightsStatement = connection.prepareStatement(updateRightsQuery)) {
                        updateRightsStatement.setBoolean(1, isAdminRights);
                        updateRightsStatement.setInt(2, userId);

                        int rowsAffected = updateRightsStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            response.sendRedirect("AdminPanel");
                        } else {
                            response.getWriter().println("Error: Unable to update user rights.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.getWriter().println("Error: Database error occurred.");
                }
            } else {
                response.getWriter().println("Error: Invalid user ID or admin rights value.");
            }
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
