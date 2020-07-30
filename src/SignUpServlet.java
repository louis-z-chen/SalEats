import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import com.google.gson.Gson;

import models.Coordinates;
import models.Location;
import models.Categories;
import models.Restaurant;
import models.RestaurantList;
import models.Schedule;
import models.Schedule.Task;
import parser.ScheduleParser;
import util.ScheduleFormatException;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SignUpServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean valid = true; 
		
		String email = request.getParameter("input_email").trim();
		if(email == null || email =="") {
			request.setAttribute("email_error", "Email needs to be filled out.");
			valid = false;
		}
		
		String username = request.getParameter("input_username").trim();
		if(username == null || username =="") {
			request.setAttribute("username_error", "Username needs to be filled out.");
			valid = false;
		}
		
		String password = request.getParameter("input_password").trim();
		if(password == null || password =="") {
			request.setAttribute("password_error", "Password needs to be filled out.");
			valid = false;
		}
		
		String password2 = request.getParameter("input_password2").trim();
		if(password2 == null || password2 =="") {
			request.setAttribute("password2_error", "Confirm Password needs to be filled out.");
			valid = false;
		}
		
		//if email is incorrectly formatted
		boolean email_valid = false;
		if(email.endsWith(".com") || email.endsWith(".net") || email.endsWith(".edu") || email.endsWith(".org") || email.endsWith(".co") || email.endsWith(".us")) {
			email_valid = true;
		}
		
		if(email_valid == false) {
			request.setAttribute("emaildata_error", "Email is not formatted properly. It needs to end in .com, .net, .edu, etc.");
		}
		
		//if email already exists
		boolean email_exists = false;
		if(valid) {
			email_exists = emailExists(email);
			if(email_exists == true) {
				request.setAttribute("email_exist_error", "There is already an account associated with this email.");
			}
		}
		
		//if username exists already 
		boolean exists = false;
		if(valid) {
			exists = usernameExists(username);
			if(exists == true) {
				request.setAttribute("username_exist_error2", "Username exists already.");
			}
		}
		
		//if password and password2 don't match
		boolean passwordMatch = true;
		if(password.length() > 0 && password2.length() > 0) {
			if(!password.equals(password2)) {
				request.setAttribute("password_mismatch_error", "Password and Confirm Password need to match.");
				passwordMatch = false;
			}
		}
		
		//create account
		if(valid && email_valid && !email_exists && !exists && passwordMatch) {
			createAccount(email, username, password);
		}
		
		//check newly created account
		boolean account_exists = correctLogin(username, password);
		
		//sign into account
		boolean loginHappened = false;
		if(account_exists) {
			loginHappened = login(username, password, request);
		}
		else {
			request.setAttribute("signup_error", "Account was not created successfully.");
		}
		
		
		String next = "/login.jsp";
		if(loginHappened) {
			next = "/home.jsp";
		}
		
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(next);
		dispatch.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public boolean emailExists(String input_email) {
		int result_count = 0;
		String search_email = input_email;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=louis9104&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
			ps = conn.prepareStatement("SELECT UserID, Email, Username\n" + 
					"	FROM UserAccounts\n" + 
					"    WHERE Email = ?;");
			ps.setString(1,search_email);
			rs = ps.executeQuery();
			while (rs.next()) {
				result_count++;
				String userid = rs.getString("UserID");
				String email = rs.getString("Email");
				String username = rs.getString("Username");
			}
		} 
		catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} 
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		
		boolean exists = false;
		if (result_count > 0) {
			exists = true;
		}
		return exists;	
	}
	
	public boolean usernameExists(String input_username) {
		int result_count = 0;
		String search_username = input_username;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=louis9104&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
			ps = conn.prepareStatement("SELECT UserID, Email, Username\n" + 
					"	FROM UserAccounts\n" + 
					"    WHERE Username = ?;");
			ps.setString(1,search_username);
			rs = ps.executeQuery();
			while (rs.next()) {
				result_count++;
			}
		} 
		catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} 
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		
		boolean exists = false;
		if (result_count > 0) {
			exists = true;
		}
		return exists;	
	}
	
	public void createAccount(String input_email, String input_username, String input_password) {
		String insert_email = input_email;
		String insert_username = input_username;
		String insert_password = input_password;
		String insert_boolean = "false";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
		Connection conn = null;
		PreparedStatement ps = null;
		int rows_affected = 0;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=louis9104&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
			ps = conn.prepareStatement("INSERT INTO UserAccounts (Email, Username, Password, isGoogleAccount) "
					+ "VALUES (?,?,?,?);");
			ps.setString(1,insert_email);
			ps.setString(2,insert_username);
			ps.setString(3,insert_password);
			ps.setString(4,insert_boolean);
			rows_affected = ps.executeUpdate();
		} 
		catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} 
		finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
	}
	
	public boolean correctLogin(String input_username, String input_password) {
		
		int result_count = 0;
		String search_username = input_username;
		String search_password = input_password;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=louis9104&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
			ps = conn.prepareStatement("SELECT UserID, Email, Username, Password\n" + 
					"	FROM UserAccounts\n" + 
					"    WHERE Username = ?" + 
					"    AND Password = ?;");
			ps.setString(1,search_username);
			ps.setString(2,search_password);
			rs = ps.executeQuery();
			while (rs.next()) {
				result_count++;
			}
		} 
		catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} 
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		
		boolean exists = false;
		if (result_count > 0) {
			exists = true;
		}
		return exists;	
	}
	
	public boolean login(String input_username, String input_password, HttpServletRequest request) {
		
		boolean loginHappened = false;
		int result_count = 0;
		String search_username = input_username;
		String search_password = input_password;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String userid="";
		String email="";
		String username="";
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=louis9104&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
			ps = conn.prepareStatement("SELECT UserID, Email, Username\n" + 
					"	FROM UserAccounts\n" + 
					"    WHERE Username = ?" + 
					"    AND Password = ?;");
			ps.setString(1,search_username);
			ps.setString(2,search_password);
			rs = ps.executeQuery();
			while (rs.next()) {
				result_count++;
				userid = rs.getString("UserID");
				email = rs.getString("Email");
				username = rs.getString("Username");
			}
		} 
		catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} 
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		
		if(result_count > 0) {
			HttpSession session = request.getSession();
			session.setAttribute("loggedin", "true");
			session.setAttribute("userid", userid);
			session.setAttribute("email", email);
			session.setAttribute("username", username);
			loginHappened = true;
		}
		return loginHappened;
	}

}
