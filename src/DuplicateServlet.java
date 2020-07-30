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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DuplicateServlet")
public class DuplicateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DuplicateServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		boolean session_isloggedin = true;
		String session_loggedin = "";
		
		HttpSession session = request.getSession();
		String session_userid = "";
		String loggedin = "false";
		
		if (session != null) {
			session_userid = (String) session.getAttribute("userid");
			session_loggedin = (String) session.getAttribute("loggedin");
		}
		
		if(session_loggedin == "true"){
			session_isloggedin = true;
		}
		else{
			session_isloggedin = false;
		}
		
		if(session_isloggedin == false) {
			out.println("<button type=\"button\" class=\"btn btn-warning\" id=\"yel-button\" onClick = \"noLogin()\">");
			out.println("<i class=\"fas fa-star\"></i>");
			out.println("Add to Favorites");
			out.println("</button>");
			return;
		}
		
		String rest_id = (String)request.getParameter("rest_id");
		int id = Integer.parseInt(rest_id);
		ArrayList<Restaurant> display_results = new ArrayList<Restaurant>();
		display_results = (ArrayList<Restaurant>)session.getAttribute("rest_list");
		
		if(display_results == null) {
			return;
		}
		Restaurant curr_rest = display_results.get(id);
		
//		UserID: 
		String UserID = session_userid;
		if(UserID == null) {
			UserID = "";
		}
		
//		YelpID:
		String YelpID = curr_rest.getID();
		if(YelpID == null) {
			YelpID = "";
		}
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result_count = 0;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=louis9104&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
			ps = conn.prepareStatement("SELECT RestaurantName\n" + 
					"	FROM UserFavorites\n" + 
					"	WHERE UserID = ?\n" + 
					"		AND YelpID = ?;");
			ps.setString(1,UserID);
			ps.setString(2,YelpID);
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
			out.println("<button type=\"button\" class=\"btn btn-warning\" id=\"yel-button\" onClick = \"removeFavorite(" + rest_id + ")\">");
			out.println("<i class=\"fas fa-star\"></i>");
			out.println("Remove from Favorites");
			out.println("</button>");
		}
		else {
			out.println("<button type=\"button\" class=\"btn btn-warning\" id=\"yel-button\" onClick = \"addFavorite(" + rest_id + ")\">");
			out.println("<i class=\"fas fa-star\"></i>");
			out.println("Add to Favorites");
			out.println("</button>");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
