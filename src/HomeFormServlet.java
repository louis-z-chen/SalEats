import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import models.Coordinates;
import models.Location;
import models.Categories;
import models.Restaurant;
import models.RestaurantList;
import models.Schedule;
import models.Schedule.Task;
import parser.ScheduleParser;
import util.ScheduleFormatException;

@WebServlet("/HomeFormServlet")
public class HomeFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public HomeFormServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean valid = true; 
		
		String rest_name = request.getParameter("rest_name").trim();
		//System.out.println(rest_name);
		if(rest_name == null || rest_name =="") {
			request.setAttribute("name_error", "Restaurant name needs to be filled out.");
			valid = false;
		}
		
		String radio = request.getParameter("radio").trim();
		//System.out.println(radio);
		if(radio == null || radio =="") {
			request.setAttribute("button_error", "Please pick one of the buttons.");
			valid = false;
		}
		
		String latitude = request.getParameter("latitude").trim();
		//System.out.println(latitude);
		if(latitude == null || latitude =="") {
			request.setAttribute("latitude_error", "Latitude needs to be filled out.");
			valid = false;
		}
		
		String longitude = request.getParameter("longitude").trim();
		//System.out.println(longitude);
		if(longitude == null || longitude =="") {
			request.setAttribute("longitude_error", "Longitude needs to be filled out.");
			valid = false;
		}
		
		String page = request.getParameter("page_id").trim();
	
		
		String next = "/searchresults.jsp";
		if(page.equals("home")) {
			next = "/home.jsp";
		}
		else if (page.equals("favorites")) {
			next = "/favorites.jsp";
		}
		
		if(valid) {
			next = "/searchresults.jsp";
		}
		
		//yelp
		if(valid) {
			
			request.setAttribute("search_term", rest_name);
			
			Gson gson = new Gson();
			HttpURLConnection connection = null;
			
			try{
				rest_name = rest_name.replaceAll(" ", "-");
				rest_name = rest_name.replaceAll("’", "'");
				
				URL url = new URL("https://api.yelp.com/v3/businesses/search?latitude="
						+ latitude + "&longitude=" + longitude + "&term=" + rest_name + "&sort_by=" + radio + "&limit=10");
				
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Authorization", "Bearer 9VbQ8d1lXz6JkI6C_HSBS03tKKFRz4vU_22bc6Wsvnkoy6fBzyFdW6WnVuhSX9ReVdV3F1pTtyIE4oPnd04HK3W9hsyQwDwv20PwwjscIaJJ8tDmQJumf3gqMOB5XnYx");
				
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				RestaurantList businesses = gson.fromJson(in, RestaurantList.class);
				request.setAttribute("yelp_results", businesses);
			}
			catch(IOException ioe) {
				System.out.println("ioe in API requester: " + ioe.getMessage());
			}
		}
		
		String yelpCalled = "false";
		if (valid) {
			yelpCalled = "true";
		}
		request.setAttribute("yelpCalled", yelpCalled);
		
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(next);
		dispatch.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
