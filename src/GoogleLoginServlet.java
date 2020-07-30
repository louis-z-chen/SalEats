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


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GoogleLoginServlet")
public class GoogleLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GoogleLoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("name").trim();
		if(username == null) {
			username = "";
		}
		
		String email = request.getParameter("email").trim();
		if(email == null) {
			email = "";
		}
		
		//if email already exists
		boolean email_exists = false;
		email_exists = emailExists(email);
		
		//if email does not exist, create an account then login
		if(email_exists == false) {
			createAccount(email, username, "");
		}
		
		//if email does exist login
		//sign into account
		boolean loginHappened = false;
		if(email_exists) {
			loginHappened = Googlelogin(email, request);
		}

		String next = "/login.jsp";
		if(loginHappened) {
			next = "/home.jsp";
		}
		
//		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(next);
//		dispatch.forward(request, response);
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
	
	public void createAccount(String input_email, String input_username, String input_password) {
		String insert_email = input_email;
		String insert_username = input_username;
		String insert_password = input_password;
		String insert_boolean = "true";
		
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
	
	public boolean Googlelogin(String input_email, HttpServletRequest request) {
		
		boolean loginHappened = false;
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
		String userid="";
		String email="";
		String username="";
		String google="";
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=louis9104&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
			ps = conn.prepareStatement("SELECT UserID, Email, Username, isGoogleAccount\n" + 
					"	FROM UserAccounts\n" + 
					"    WHERE Email = ?;");
			ps.setString(1,search_email);
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