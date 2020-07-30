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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import com.google.gson.Gson;
import com.mysql.jdbc.Driver;

import models.Coordinates;
import models.Location;
import models.Categories;
import models.Restaurant;
import models.RestaurantList;
import models.Schedule;
import models.Schedule.Task;
import parser.ScheduleParser;
import util.ScheduleFormatException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoginServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean valid = true; 
		
		String username = request.getParameter("login_username").trim();
		if(username == null || username =="") {
			request.setAttribute("login_username_error", "Username needs to be filled out.");
			valid = false;
		}
		
		String password = request.getParameter("login_password").trim();
		if(password == null || password =="") {
			request.setAttribute("login_password_error", "Password needs to be filled out.");
			valid = false;
		}
		
		//username doesn't exist
		boolean exists = true;
		if(valid) {
			exists = usernameExists(username);
			if(exists == false) {
				request.setAttribute("username_exist_error", "Username does not exist.");
			}
		}
		
		//username and password arent correct
		boolean correctLogin = true;
		if(valid) {
			correctLogin = correctLogin(username, password);
			if(correctLogin == false) {
				request.setAttribute("login_error", "Username and Password are incorrect.");
			}
		}
		
		//log in
		boolean loginHappened = false;
		if(valid == true && exists == true && correctLogin == true) {
			loginHappened = login(username, password, request);
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
		String google="";
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=louis9104&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
			ps = conn.prepareStatement("SELECT UserID, Email, Username, isGoogleAccount\n" + 
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
				google = rs.getString("isGoogleAccount");
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
			session.setAttribute("isGoogleAccount", google);
			loginHappened = true;
		}
		return loginHappened;
	}

}
