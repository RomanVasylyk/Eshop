package sk;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Shop")
public class Shop extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String JDBC_URL = "jdbc:mysql://localhost/Eshop";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";
    private boolean isAdmin;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(true);

        Connection connection = (Connection) session.getAttribute("dbConnection");

        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                session.setAttribute("dbConnection", connection);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error initializing the database connection");
            }
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
            String username = (String) session.getAttribute("login");
            

            out.println("<html><head><title>Main Page</title>");
            out.println("<style>");
            out.println("  body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f5f5f5; }");
            out.println("  h2 { color: #333; text-align: center; padding: 20px 0; }");
            out.println("  ul { list-style-type: none; margin: 0; padding: 0; overflow: hidden; background-color: #333; }");
            out.println("  li { float: left; width: 25%; }");
            out.println("  li a { display: block; color: white; text-align: center; padding: 14px 16px; text-decoration: none; }");
            out.println("  li a:hover { background-color: #555; }");
            out.println("  .logout-btn { background-color: #f44336; color: white; border: none; padding: 14px 16px; text-decoration: none; cursor: pointer; }");
            out.println("  .logout-btn:hover { background-color: #d32f2f; }");
            out.println("  .product-list { display: flex; flex-wrap: wrap; justify-content: space-around; }");
            out.println("  .product-item { width: 30%; margin: 10px; border: 1px solid #ddd; padding: 10px; text-align: center; cursor: pointer; }");
            out.println("  .product-modal { display: none; position: fixed; z-index: 1; left: 0; top: 0; width: 100%; height: 100%; overflow: auto; background-color: rgba(0, 0, 0, 0.7); }");
            out.println("  .modal-content { background-color: #fefefe; margin: 10% auto; padding: 20px; border: 1px solid #888; width: 50%; }");
            out.println("  span { color: #888; float: right; font-size: 28px; font-weight: bold; cursor: pointer; }");
            out.println("</style>");
            out.println("<script>");
            out.println("  function openModal(productId) {");
            out.println("    var modal = document.getElementById('productModal_' + productId);");
            out.println("    modal.style.display = 'block';");
            out.println("  }");
            out.println("  function closeModal(productId) {");
            out.println("    var modal = document.getElementById('productModal_' + productId);");
            out.println("    modal.style.display = 'none';");
            out.println("  }");
            out.println("function addToCart(productId) {");
            out.println("    var quantityInput = document.getElementById('quantity_' + productId);");
            out.println("    var quantity = quantityInput.value;");
            out.println("");
            out.println("    // Check if quantity is empty or not a valid number");
            out.println("    if (!quantity || isNaN(parseInt(quantity))) {");
            out.println("        alert('Please enter a valid quantity.');");
            out.println("        return;");
            out.println("    }");
            out.println("");
            out.println("    var xhr = new XMLHttpRequest();");
            out.println("    xhr.onreadystatechange = function() {");
            out.println("        if (xhr.readyState == 4) {");
            out.println("            if (xhr.status == 200) {");
            out.println("                alert('Added ' + quantity + ' items of Product ' + productId + ' to the cart.');");
            out.println("            } else {");
            out.println("                alert('Failed to add items to the cart. Please try again.');");
            out.println("            }");
            out.println("        }");
            out.println("    };");
            out.println("");
            out.println("    xhr.open('POST', 'Shop', true);");
            out.println("    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');");
            out.println("    xhr.send('action=AddToCart&productId=' + productId + '&quantity=' + quantity);");
            out.println("}");

            out.println("</script>");
            out.println("</head><body>");
            
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



            // Product List
            out.println("<h2>Product List</h2>");
            out.println("<div class='product-list'>");

            try {
                String query = "SELECT * FROM sklad";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        int productId = resultSet.getInt("ID");
                        String productName = resultSet.getString("nazov");
                        int productQuantity = resultSet.getInt("ks");
                        int productPrice = resultSet.getInt("cena");
                        String productDescription = resultSet.getString("popis");
                        String productPhotoUrl = resultSet.getString("photo_url");
                        int quantity = resultSet.getInt("ks");

                        int cartItemId = resultSet.getInt("ID");
                        // Product
                        out.println("<div class='product-item' onclick=\"openModal(" + productId + ")\">");
                        out.println("  <img src='" + productPhotoUrl + "' alt='" + productName + "' style='width: 50%; height: 300px;'>");
                        out.println("  <h3>" + productName + "</h3>");
                        out.println("  <p>Quantity: " + productQuantity + "</p>");
                        out.println("  <p>Price: $" + productPrice + "</p>");
                        
                        out.println("</div>");

                     // Product Modal
                        out.println("<div class='product-modal' id='productModal_" + productId + "'>");
                        out.println("  <div class='modal-content'>");
                        out.println("    <span onclick=\"closeModal(" + productId + ")\" style='cursor: pointer;'>&times;</span>");
                        out.println("    <h2>" + productName + " Details</h2>");
                        out.println("    <img src='" + productPhotoUrl + "' alt='" + productName + "' style='width: 50%; height: 400px;'>");
                        out.println("    <p>Quantity: " + productQuantity + "</p>");
                        out.println("    <p>Price: $" + productPrice + "</p>");
                        out.println("    <input type='number' id='quantity_" + productId + "' placeholder='Quantity' min='1' max='" + productQuantity + "' oninput='validateQuantity(" + productId + ")' required>");

                        if (quantity > 0) {
                            out.println("<td><button type='button' onclick='addToCart(" + cartItemId + ")'>Add to Cart</button></td>");
                        } else {
                            out.println("<td>Out of Stock</td>");
                        }
                        
                        out.println("    <p>Description: " + productDescription + "</p>");
                        out.println("  </div>");
                        out.println("</div>");
                        out.println("<script>");
                        out.println("function validateQuantity(productId) {");
                        out.println("  var quantityInput = document.getElementById('quantity_' + productId);");
                        out.println("  var maxQuantity = parseInt(quantityInput.max);");
                        out.println("  var minQuantity = 1;");
                        out.println("  var enteredQuantity = parseInt(quantityInput.value);");
                        out.println("  if (isNaN(enteredQuantity) || enteredQuantity < minQuantity) {");
                        out.println("    quantityInput.value = minQuantity;");
                        out.println("  } else if (enteredQuantity > maxQuantity) {");
                        out.println("    quantityInput.value = maxQuantity;");
                        out.println("  }");
                        out.println("}");
                        out.println("</script>");


                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            out.println("</div>");
            out.println("</body></html>");
        } else {
            out.println("<html><head><title>Registration and Login Form</title>");
            out.println("<style>");
            out.println("  body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f5f5f5; }");
            out.println("  h2 { color: #333; text-align: center; padding: 20px; }");
            out.println("  .container { display: flex; justify-content: space-around; }");
            out.println("  .form-container { flex: 1; max-width: 400px; background-color: #fff; padding: 20px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
            out.println("  label { display: block; margin-bottom: 8px; }");
            out.println("  input[type='text'], input[type='password'], input[type='email'], textarea { width: 100%; padding: 10px; margin-bottom: 15px; box-sizing: border-box; }");
            out.println("  input[type='submit'] { background-color: #4caf50; color: white; padding: 10px 15px; border: none; cursor: pointer; }");
            out.println("  input[type='submit']:hover { background-color: #45a049; }");
            out.println("</style></head><body>");
            out.println("<h2>Registration and Login Form</h2>");

            out.println("<div class='container'>");

            // Registration Form
            out.println("<div class='form-container'>");
            out.println("<h3>Registration</h3>");
            out.println("<form method='post' action='Shop'>");
            out.println("  <label for='regLogin'>Login:</label>");
            out.println("  <input type='email' id='regLogin' name='login' required>");
            out.println("  <label for='regPassword'>Password:</label>");
            out.println("  <input type='password' id='regPassword' name='passwd' required>");
            out.println("  <label for='regAddress'>Address:</label>");
            out.println("  <input type='text' id='regAddress' name='adresa' required>");
            out.println("  <label for='regFirstName'>First Name:</label>");
            out.println("  <input type='text' id='regFirstName' name='meno' required>");
            out.println("  <label for='regLastName'>Last Name:</label>");
            out.println("  <input type='text' id='regLastName' name='priezvisko' required>");
            out.println("  <label for='regNotes'>Notes:</label>");
            out.println("  <textarea id='regNotes' name='poznamky' required></textarea>");
            out.println("  <input type='submit' name='action' value='Register'>");
            out.println("</form>");
            out.println("</div>");

            // Login Form
            out.println("<div class='form-container'>");
            out.println("<h3>Login</h3>");
            out.println("<form method='post' action='Shop'>");
            out.println("  <label for='login'>Login:</label>");
            out.println("  <input type='text' id='login' name='login' required>");
            out.println("  <label for='password'>Password:</label>");
            out.println("  <input type='password' id='password' name='passwd' required>");
            out.println("  <input type='submit' name='action' value='Login'>");
            out.println("</form>");
            out.println("</div>");

            out.println("</div>");

            out.println("</body></html>");
        }
    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    String action = request.getParameter("action");
    HttpSession session = request.getSession();
    Connection connection = (Connection) session.getAttribute("dbConnection");

    if (connection == null && !"Logout".equals(action)) {
        out.println("Error: Database connection is null.");
        return;
    }
    else if ("Register".equals(action)) {
        String login = request.getParameter("login");
        String passwd = request.getParameter("passwd");
        String adresa = request.getParameter("adresa");
        int zlava = 0; 
        String meno = request.getParameter("meno");
        String priezvisko = request.getParameter("priezvisko");
        String poznamky = request.getParameter("poznamky");

        if (!isValidEmail(login)) {
            out.println("<html><head><title>Registration Failed</title></head><body>");
            out.println("<h2>Registration Failed</h2>");
            out.println("<p>Login should be in the form of an email address.</p>");
            out.println("<a href='Shop'>Go back</a>");
            out.println("</body></html>");
            return;
        }

        try {
            if (isLoginExists(connection, login)) {
                out.println("<html><head><title>Registration Failed</title></head><body>");
                out.println("<h2>Registration Failed</h2>");
                out.println("<p>Login already exists. Choose a different login.</p>");
                out.println("<a href='Shop'>Go back</a>");
                out.println("</body></html>");
                return;
            }

            String query = "INSERT INTO users (login, passwd, adresa, zlava, meno, priezvisko, poznamky, je_admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, passwd);
                preparedStatement.setString(3, adresa);
                preparedStatement.setInt(4, zlava);
                preparedStatement.setString(5, meno);
                preparedStatement.setString(6, priezvisko);
                preparedStatement.setString(7, poznamky);
                preparedStatement.setBoolean(8, false);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    out.println("<html><head><title>Registration Successful</title></head><body>");
                    out.println("<h2>Registration Successful</h2>");
                    out.println("<a href='Shop'>Go back</a>");
                    out.println("</body></html>");
                } else {
                    out.println("<html><head><title>Registration Failed</title></head><body>");
                    out.println("<h2>Registration Failed</h2>");
                    out.println("<a href='Shop'>Go back</a>");
                    out.println("</body></html>");
                    
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 else if ("Login".equals(action)) {
            String login = request.getParameter("login");
            String passwd = request.getParameter("passwd");

            try {
                String query = "SELECT * FROM users WHERE login = ? AND passwd = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, login);
                    preparedStatement.setString(2, passwd);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        HttpSession newSession = request.getSession();
                        newSession.setAttribute("login", login);
                        response.sendRedirect("Shop");
                       
                    } else {
                        out.println("<html><head><title>Login Failed</title></head><body>");
                        out.println("<h2>Login Failed</h2>");
                        out.println("<p>Incorrect login credentials. Please try again.</p>");
                        out.println("<a href='Shop'>Go back to login</a>");
                        out.println("</body></html>");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if ("AddToCart".equals(action)) {
            String productIdString = request.getParameter("productId");
            String quantityString = request.getParameter("quantity");

            if (productIdString != null && !productIdString.isEmpty() && quantityString != null && !quantityString.isEmpty()) {
                try {
                    int productId = Integer.parseInt(productIdString);
                    int quantity = Integer.parseInt(quantityString);

                    int userId = getUserId(session, connection);

                    // Begin transaction
                    connection.setAutoCommit(false);

                    String productQuery = "SELECT ks, cena FROM sklad WHERE ID = ?";
                    String cartInsertQuery = "INSERT INTO kosik (ID_pouzivatela, ID_tovaru, cena, ks) VALUES (?, ?, ?, ?)";

                    try (PreparedStatement productStatement = connection.prepareStatement(productQuery)) {
                        productStatement.setInt(1, productId);

                        try (ResultSet productResultSet = productStatement.executeQuery()) {
                            if (productResultSet.next()) {
                                int availableQuantity = productResultSet.getInt("ks");
                                int productPrice = productResultSet.getInt("cena");

                                if (quantity <= availableQuantity) {
                                    try (PreparedStatement cartInsertStatement = connection.prepareStatement(cartInsertQuery)) {
                                        cartInsertStatement.setInt(1, userId);
                                        cartInsertStatement.setInt(2, productId);
                                        cartInsertStatement.setInt(3, productPrice);
                                        cartInsertStatement.setInt(4, quantity);

                                        int rowsAffected = cartInsertStatement.executeUpdate();

                                        if (rowsAffected > 0) {
                                            out.println("Added " + quantity + " items of Product " + productId + " to the cart.");
                                            // Commit the transaction
                                            connection.commit();
                                        } else {
                                            out.println("Failed to add items to the cart.");
                                        }
                                    }
                                } else {
                                    out.println("Error: Insufficient stock for Product " + productId + ".");
                                }
                            } else {
                                out.println("Product with ID " + productId + " not found.");
                            }
                        }
                    }
                } catch (NumberFormatException | SQLException e) {
                    // Rollback the transaction in case of an exception
                    try {
                        connection.rollback();
                    } catch (SQLException rollbackException) {
                        rollbackException.printStackTrace();
                    }
                    e.printStackTrace();
                    out.println("Error processing the request.");
                } finally {
                    // Reset auto-commit to true
                    try {
                        connection.setAutoCommit(true);
                    } catch (SQLException autoCommitException) {
                        autoCommitException.printStackTrace();
                    }
                }
            } else {
                System.out.print("tru");
                out.println("Error: Missing parameters.");
            }
        }

        else if ("Logout".equals(action)) {
            HttpSession session1 = request.getSession(false);
            if (session1 != null) {
                session1.invalidate();
            }

            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Database connection closed.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            response.sendRedirect("Shop");
            
            return;
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isLoginExists(Connection connection, String login) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            return count > 0;
        }
    }

    
    @Override
    public void destroy() {
        super.destroy();
        
        ServletContext servletContext = getServletContext();
        Connection connection = (Connection) servletContext.getAttribute("dbConnection");

        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
