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

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/FavoritesServlet")
public class FavoritesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public FavoritesServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		String input_sort_type = request.getParameter("sort_type");
		String sql_sort = "RestaurantID DESC;";
		
		if(input_sort_type.equals("A to Z")) {
			sql_sort = "RestaurantName ASC;";
		}
		else if(input_sort_type.equals("Z to A")) {
			sql_sort = "RestaurantName DESC;";
		}
		else if(input_sort_type.equals("Highest Rating")) {
			sql_sort = "Rating DESC;";
		}
		else if(input_sort_type.equals("Lowest Rating")) {
			sql_sort = "Rating ASC;";
		}
		else if(input_sort_type.equals("Most Recent")) {
			sql_sort = "RestaurantID DESC;";
		}
		else if(input_sort_type.equals("Least Recent")) {
			sql_sort = "RestaurantID ASC;";
		}
		
		HttpSession session = request.getSession();
		String session_userid = "";
		String loggedin = "false";
		
		boolean session_isloggedin = true;
		String session_loggedin = "";
		
		if (session != null) {
			session_loggedin = (String) session.getAttribute("loggedin");
			session_userid = (String) session.getAttribute("userid");
			loggedin = (String) session.getAttribute("loggedin");
		}
		
		if(session_loggedin == "true"){
			session_isloggedin = true;
		}
		else{
			session_isloggedin = false;
		}
		
		if(session_isloggedin == false) {
			out.println("<hr>");
			out.println("<h4>No Favorites</h4>");
			return;
		}
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql_statement = "SELECT Rating, Price, PhoneNumber, YelpID, Cuisine, ReviewCount, RestaurantName, URL, Latitude, Longitude, ImageURL, City, Country, State, Address1, ZipCode, Distance" +
				" FROM UserFavorites" +
			    " WHERE UserID = ?" + 
				" ORDER BY " + 
			    sql_sort;
		
		ArrayList<Restaurant> display_results = new ArrayList<Restaurant>();
		String rating = "";
		String price = "";
		String phonenumber = "";
		String yelpid = "";
		String cuisine = "";
		String reviewcount = "";
		String restaurantname = "";
		String url = "";
		Double latitude = 0.0;
		Double longitude = 0.0;
		String imageurl = "";
		String city = "";
		String country = "";
		String state = "";
		String address1 = "";
		String zipcode = "";
		String distance = "";
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=louis9104&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
			ps = conn.prepareStatement(sql_statement);
			ps.setString(1,session_userid);
			rs = ps.executeQuery();
			while (rs.next()) {
				rating = rs.getString("Rating");
				price = rs.getString("Price");
				phonenumber = rs.getString("PhoneNumber");
				yelpid = rs.getString("YelpID");
				//ArrayList<Categories>
				cuisine = rs.getString("Cuisine");
				ArrayList<Categories>  categories = new ArrayList<Categories>();
				String alias = "";
				Categories single_category = new Categories(alias,cuisine);
				categories.add(single_category);
				
				reviewcount = rs.getString("ReviewCount");
				restaurantname = rs.getString("RestaurantName");
				url = rs.getString("URL");
				//Coordinates
				latitude = rs.getDouble("Latitude");
				longitude = rs.getDouble("Longitude");
				Coordinates coordinates = new Coordinates(latitude, longitude);
				
				imageurl = rs.getString("ImageURL");
				//Location
				city = rs.getString("City");
				country = rs.getString("Country");
				state = rs.getString("State");
				address1 = rs.getString("Address1");
				zipcode = rs.getString("ZipCode");
				String address2 = "";
				String address3 = "";
				Location location = new Location(city, country, address2, 
				        address3, state, address1, zipcode);
				
				distance = rs.getString("Distance");
				
				Restaurant curr_fav = new Restaurant(rating, price, phonenumber, yelpid, 
						categories, reviewcount, restaurantname, url,
						coordinates, imageurl, location, distance);
				display_results.add(curr_fav);
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
		
		if(display_results.size() > 0) {
			session.setAttribute("rest_list", display_results);
			for(int i = 0; i < display_results.size(); i++) {

				out.println("<hr>");
				out.println("<div class = \"rest-list center\">");
				out.println("<div class = \"rest-item\">");
				out.println("<div class = \"rest-img\">");
				out.println("<a href = \"details.jsp?id=" + i + "&yelpid=" + display_results.get(i).getID() + "\">");
				out.println("<img src= \" " + display_results.get(i).getImage_URL() + "\">");
				out.println("</a>");
				out.println("</div>");
				out.println("<div class = \"rest-details\">");
				out.println("<div class = \"name detail-item\">");
				out.println("<h2>");
				out.println(display_results.get(i).getName());
				out.println("</h2>");
				out.println("</div>");
				out.println("<div class = \"address detail-item\">");
				out.println(display_results.get(i).getAddress());
				out.println("</div>");
				out.println("<div class = \"url detail-item\"> ");
				out.println("<a href = \"" + display_results.get(i).getURL() + "\" >");
				out.println(display_results.get(i).getURL().split("\\?")[0]);
				out.println("</a>");
				out.println("</div>");
				out.println("</div>");
				out.println("</div>");
				out.println("</div>");
			}
		}
		else {
			out.println("<hr>");
			out.println("<h4>No Favorites</h4>");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
