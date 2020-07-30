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

@WebServlet("/AddFavoritesServlet")
public class AddFavoritesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AddFavoritesServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession();
		String session_userid = "";
		String loggedin = "false";
		
		if (session != null) {
			session_userid = (String) session.getAttribute("userid");
			loggedin = (String) session.getAttribute("loggedin");
			if(loggedin == null || loggedin == "") {
				loggedin = "false";
			}
		}
		if(loggedin == "false") {
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
		
//		RestaurantName:
		String RestaurantName = curr_rest.getName();
		if(RestaurantName == null) {
			RestaurantName = "";
		}
		
//		City:
		String City = curr_rest.getLocation().getCity();
		if(City == null) {
			City = "";
		}
		
//		Country:
		String Country = curr_rest.getLocation().getCountry();
		if(Country == null) {
			Country = "";
		}
		
//		State: 
		String State = curr_rest.getLocation().getState();
		if(State == null) {
			State = "";
		}
		
//		Address1:
		String Address1 = curr_rest.getLocation().getAddress1();
		if(Address1 == null) {
			Address1 = "";
		}
		
//		ZipCode:
		String ZipCode = curr_rest.getLocation().getZipCode();
		if(ZipCode == null) {
			ZipCode = "";
		}
		
//		URL:
		String URL = curr_rest.getURL();
		if(URL == null) {
			URL = "";
		}
		
//		ImageURL:
		String ImageURL = curr_rest.getImage_URL();
		if(ImageURL == null) {
			ImageURL = "";
		}
		
//		PhoneNumber:
		String PhoneNumber = curr_rest.getPhone();
		if(PhoneNumber == null) {
			PhoneNumber = "";
		}
		
//		Rating:
		String Rating = curr_rest.getRating();
		if(Rating == null) {
			Rating = "";
		}
		
//		ReviewCount:
		String ReviewCount = curr_rest.getReviewCount();
		if(ReviewCount == null) {
			ReviewCount = "";
		}
		
//		Distance:
		String Distance = curr_rest.getDistance();
		if(Distance == null) {
			Distance = "";
		}
		
//		Cuisine:
		String Cuisine = "";
		for(int i = 0; i < curr_rest.getCategories().size(); i++) {
			if(i != 0){
	    		Cuisine = Cuisine.concat(", ");
	    	}
			Cuisine = Cuisine.concat(curr_rest.getCategories().get(i).getTitle());
		}
		
//		Price:
		String Price = curr_rest.getPrice();
		if(Price == null) {
			Price = "";
		}
		
//		Latitude:
		Double Latitude = curr_rest.getCoordinates().getLatitude();
		if(Latitude == null) {
			Latitude = 0.0;
		}
		
//		Longitude:
		Double Longitude = curr_rest.getCoordinates().getLongitude();
		if(Longitude == null) {
			Longitude = 0.0;
		}
		
		
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
			ps = conn.prepareStatement("INSERT INTO UserFavorites (UserID, YelpID, RestaurantName, City, Country, State, Address1, ZipCode, URL, ImageURL, PhoneNumber, Rating, ReviewCount, Distance, Cuisine, Price, Latitude, Longitude) \n" + 
					"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
			ps.setString(1,UserID);
			ps.setString(2,YelpID);
			ps.setString(3,RestaurantName);
			ps.setString(4,City);
			ps.setString(5,Country);
			ps.setString(6,State);
			ps.setString(7,Address1);
			ps.setString(8,ZipCode);
			ps.setString(9,URL);
			ps.setString(10,ImageURL);
			ps.setString(11,PhoneNumber);
			ps.setString(12,Rating);
			ps.setString(13,ReviewCount);
			ps.setString(14,Distance);
			ps.setString(15,Cuisine);
			ps.setString(16,Price);
			ps.setDouble(17,Latitude);
			ps.setDouble(18,Longitude);
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
		if(rows_affected == 1) {
			out.println("<button type=\"button\" class=\"btn btn-warning\" id=\"yel-button\" onClick = \"removeFavorite(" + rest_id + ")\">");
			out.println("<i class=\"fas fa-star\"></i>");
			out.println("Remove from Favorites");
			out.println("</button>");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
