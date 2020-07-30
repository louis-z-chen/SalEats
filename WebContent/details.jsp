<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ page import="java.util.ArrayList"%>

<%@ page import="models.Coordinates"%>
<%@ page import="models.Categories"%>
<%@ page import="models.Location"%>
<%@ page import="models.Restaurant"%>
<%@ page import="models.RestaurantList"%>

<html>

<head>
	<title>Assignment 4</title>

	<!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
	<META HTTP-EQUIV="Expires" CONTENT="-1">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	<link href= "styles.css" rel="stylesheet" type="text/css">
	<link rel = "stylesheet" href='https://fonts.googleapis.com/css?family=Lobster'>
	<script src="https://kit.fontawesome.com/aef7737b1c.js" crossorigin="anonymous"></script>
	
	<meta name="google-signin-client_id" content="763628273790-0dlejje9ro9lr3s9383ks7opcd3a5um5.apps.googleusercontent.com">
	<script src="http://code.jquery.com/jquery-3.4.1.min.js" integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo=" crossorigin="anonymous"></script>
	    
    </script>
	
	<script>
		function addFavorite(rest_id){

			$.ajax({
	   			url: "AddFavoritesServlet",
	   			data: {
	     			rest_id: rest_id
	   			},
	   			success: function( result ) {
	   				$( "#reservation-btn").html(result);
	   			}
	 		}); 
		}
		
		function removeFavorite(rest_id){

			$.ajax({
	   			url: "RemoveFavoritesServlet",
	   			data: {
	     			rest_id: rest_id
	   			},
	   			success: function( result ) {
	   				$( "#reservation-btn").html(result);
	   			}
	 		}); 
		}
		
		function duplicateFinder(rest_id){

			$.ajax({
	   			url: "DuplicateServlet",
	   			data: {
	     			rest_id: rest_id
	   			},
	   			success: function( result ) {
	   				$( "#reservation-btn").html(result);
	   			}
	 		}); 
		}
		
		function noLogin(){
			/* alert("You Need to Login to Add to Favorites"); */
			$("#error").html("You Need to Login to Add to Favorites");
		}
		
		function noGoogleAccount(){
			$("#error2").html("You Need to Login with Google to Use this Feature");
			//alert("HIT");
		}
	</script>	
	
	<script>
	
/* 		gapi.load('auth2',function(){
			gapi.auth2.init();
		}); */
	
		function signOut() {

			var auth2 = gapi.auth2.getAuthInstance();
			auth2.signOut().then(function () {
			  console.log('User signed out.');
			});
		}
		
		function logOut(){
			$.ajax({
	   			url: "LogoutServlet",
	   			async: false,
	   			success: function( result ) {
	   				if(!result){
	   					window.location.replace("http://localhost:8082/chenloui_CSCI201_assignment4/home.jsp");
       				}
	   				signOut();
	   			}
	 		}); 
		}
	</script>

</head>

		<%
			boolean session_isloggedin = true;
			boolean session_isGoogleAccount = false;
			String session_loggedin;
			String session_userid;
			String session_email;
			String session_username;
			String session_isGoogle;
		
			if (session != null) {
				
				session_loggedin = (String) session.getAttribute("loggedin");
				session_userid = (String) session.getAttribute("userid");
				session_email = (String) session.getAttribute("email");
				session_username = (String) session.getAttribute("username");
				session_isGoogle = (String) session.getAttribute("isGoogleAccount");
			     
			} else {
				session_loggedin = "";
				session_userid = "";
				session_email = "";
				session_username = "";
				session_isGoogle = "";
			}
			
			if(session_loggedin == "true"){
				session_isloggedin = true;
			}
			else{
				session_isloggedin = false;
			}
			
			if(session_isGoogle == "true"){
				session_isGoogleAccount = true;
			}
			else{
				session_isGoogleAccount = false;
			}
		%>
		
		<% 
			String rest_name = request.getParameter("rest_name");
			if(rest_name == null){
				rest_name = "";
			}
			String name_error = (String)request.getAttribute("name_error");
			if(name_error == null){
				name_error = "";
			}
		
			String radio = request.getParameter("radio");
			if(radio == null){
				radio = "";
			}
			String button_error = (String)request.getAttribute("button_error");
			if(button_error == null){
				button_error = "";
			}
			
			String latitude = request.getParameter("latitude");
			if(latitude == null){
				latitude = "";
			}
			String latitude_error = (String)request.getAttribute("latitude_error");
			if(latitude_error == null){
				latitude_error = "";
			}
			
			String longitude = request.getParameter("longitude");
			if(longitude == null){
				longitude = "";
			}
			String longitude_error = (String)request.getAttribute("longitude_error");
			if(longitude_error == null){
				longitude_error = "";
			}
		%>
		
		<%
			String curr_id = (String)request.getParameter("id");
		%>
		

<body onload="duplicateFinder(<%= curr_id %>)">

	<!--  Navbar -->
	<nav class="navbar navbar-expand-md navbar-light">

		<a class="navbar-brand nav-company" href="home.jsp"><h1>SalEats!</h1></a>

		<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="collapsibleNavbar">
			<%
				if(session_isloggedin){
			%>
				<ul class="navbar-nav ml-auto">
					<li class="nav-item"><a class="nav-link" href="home.jsp">Home</a></li>
					<li class="nav-item"><a class="nav-link" href="favorites.jsp">Favorites</a></li>
					<li class="nav-item"><a class="nav-link" onClick = "return logOut()" href="#">Logout</a></li>
				</ul>
			<%
				}
			%>
			
			<%
				if(!session_isloggedin){
			%>
				<ul class="navbar-nav ml-auto">
					<li class="nav-item"><a class="nav-link" href="home.jsp">Home</a></li>
					<li class="nav-item"><a class="nav-link" href="login.jsp">Login / Sign Up</a></li>
				</ul>
			<%
				}
			%>
		</div>
	</nav>
	
	<hr>
	
	<div class = "main-body center">
	
		<form method="GET" action="HomeFormServlet" name="homeform">
			<div class = "container1">
				<div class="form-group box1">
			    	<input type="text" class="form-control" name="rest_name" placeholder="Restaurant Name" required>
			  	</div>
			  	
			  	<div class="form-group box2">
				  	<button type="submit" class="btn btn-danger" id="red-button">
				  		<i class="fas fa-search"></i>
				  	</button>
				</div>
				
				<div class = "radio-container box3">
				  	<div class="form-check-inline radio">
					  	<input class="form-check-input radio-item" type="radio" name="radio" value="best_match" checked>
					  	<label class="form-check-label" for="exampleRadios1">Best Match</label>
					</div>
					<div class="form-check-inline radio">
					  	<input class="form-check-input radio-item" type="radio" name="radio" value="review_count">
					  	<label class="form-check-label" for="exampleRadios2">Review Count</label>
					</div>
					<div class="form-check-inline radio">
					  	<input class="form-check-input radio-item" type="radio" name="radio" value="rating">
					  	<label class="form-check-label" for="exampleRadios3">Rating</label>
					</div>
					<div class="form-check-inline radio">
					  	<input class="form-check-input radio-item" type="radio" name="radio" value="distance">
					  	<label class="form-check-label" for="exampleRadios3">Distance</label>
					</div>
				</div>
			</div>
			
			<div class = "container2">
				<div class="form-group box4">
			    	<input type="number" step="any" class="form-control" name="latitude" id="latitude" placeholder="Latitude" min="-90" max="90" value="" required>
			  	</div>
			  	
			  	<div class="form-group box5">
			    	<input type="number" step="any" class="form-control" name="longitude" id="longitude" placeholder="Longitude" min="-180" max="180" value="" required>
			  	</div>
			  	
			  	<div class="form-group box6">
			  		<button type="button" class="btn btn-primary" id = "blue-btn" name="map_btn" data-toggle="modal" data-target="#myModal"> 
			  			<i class="fas fa-map-marker-alt"></i>
			  			Google Maps (Drop a pin!)
			  		</button>
			  	</div>
			</div>
			
			<div class="form-group hidden">
		    	<input type="text" class="form-control" name="page_id" value="search_results">
		  	</div>

		</form>
		
		<div class = "error-div center">
			<font color="red"><%= name_error %></font>
			<font color="red"><%= button_error %></font>
			<font color="red"><%= latitude_error %></font>
			<font color="red"><%= longitude_error %></font>
		</div>
	
		<%
			/* String curr_id = (String)request.getParameter("id"); */
			ArrayList<Restaurant> display_results = new ArrayList<Restaurant>();
			display_results = (ArrayList<Restaurant>)session.getAttribute("rest_list");
					  			
			if(curr_id != null && session != null && display_results != null){
				int id = Integer.parseInt(curr_id);
				Restaurant curr = display_results.get(id);
				
				//Name
				String name = curr.getName();
				
				//image url
				String image_url = curr.getImage_URL();
				
				//url
				String url = curr.getURL();
				
				//address
				Location temp = curr.getLocation();
			    
			    String city = temp.getCity();
			    String country = temp.getCountry();
			    String address2 = temp.getAddress2();
			    String address3 = temp.getAddress3();
			    String state = temp.getState();
			    String address1 = temp.getAddress1();
			    String zip_code = temp.getZipCode();
			    
			    String address = address1 + " " + city + " " + state + " " + zip_code;
			    
			    //phone number
			    String phone = curr.getPhone();
			    phone = phone.substring(2);
			    String final_phone = phone.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
			    
			    //cuisine
			    ArrayList<Categories> categories = new ArrayList<Categories>();
			    categories = curr.getCategories();
			    
			    int category_count = categories.size();
			    String cuisine = "";
			    for(int i = 0; i < category_count; i++){
			    	if(i != 0){
			    		cuisine = cuisine.concat(", ");
			    	}
			    	String temp_cuisine = categories.get(i).getTitle();
			    	cuisine = cuisine.concat(temp_cuisine);
			    }
			    
			    //Price
			    String price = curr.getPrice();
			    
			    //Rating
				String rating = curr.getRating();
			    double rating_double = Double.parseDouble(rating);
			    rating_double = Math.round(rating_double * 2) / 2.0;
		%>
	
	
		<div id="main">
			<h2 id="result"><%= name %></h2>
			<hr>
			<div class = "rest-list center">
				<div class = "rest-item">
					<a class = "rest-img" href = "<%= url %>">
						<img src="<%= image_url %>">
					</a>
					<div class = "rest-details">
						<div id = "address-div" value = "<%= address %>" class = "address detail-item">
							Address: <%= address %>
						</div>
						<div class = "phone detail-item">
							Phone No: <%= final_phone %>
						</div>
						<div class = "cuisine detail-item">
							Cuisine: <%= cuisine %>
						</div>
						<div class = "price detail-item">
							Price: <%= price %>
						</div>
						<div class = "rating detail-item">
							Rating: 
							<%
								int total_star = 5;
								int star_count = 0;

								while(rating_double > 0){
									if(rating_double - 1.0 >= 0){
										%>
										<i class="fas fa-star"></i>
										<% 
										star_count++;
										rating_double = rating_double - 1;
									}
									else if(rating_double - 1.0 == -0.5){
										%>
										<i class="fas fa-star-half-alt"></i>
										<%
										star_count++;
										rating_double = rating_double - 1;
									}
								}

								for(int i = 0; i < (total_star - star_count); i++){
									%>
									<i class="far fa-star"></i>
									<%
								}
							%>
						</div>
					</div>
				</div>
			</div>
		</div>
	
		<%
			}
		%>


		
		<font color="red">
			<div id = "error" class = "center"></div>
			<div id = "error2" class = "center"></div>
		</font>
		
		<div id="reservation-btn">

		</div>

			<%
				if(session_isGoogleAccount){
			%>
				<button type="button" class="btn btn-danger" id="toggle-button">
			  		<i class="far fa-calendar-plus"></i>
			  		Add Reservation
			  	</button>
			<%
				}
			%>
			
			<%
				if(!session_isGoogleAccount){
			%>
				<button type="button" class="btn btn-danger" id="toggle-button2" onClick = "noGoogleAccount()">
			  		<i class="far fa-calendar-plus"></i>
			  		Add Reservation
			  	</button>
			<%
				}
			%>
	  	
	  	<br></br>
	  	
	  	<div id = "toggle-section" class = "hidden">
			<form method="GET" name="reservationform" onsubmit="return reservation()">
				<div class = "container1">
					<div class="form-group login1">
						<input type="date" class="form-control" id="date" placeholder="Date" required>
					</div>
					<div class="form-group login2">
						<input type="time" class="form-control" id="time" placeholder="Time" required>
					</div>
				</div>
				
				<div class="form-group">
					<textarea class="form-control" id="notes" rows = "5" placeholder="Reservation Notes"></textarea>
				</div>
				<div class="form-group">
				  	<button type="submit" class="btn btn-danger" id="submit-reservation">
				  		<i class="far fa-calendar-plus"></i>
	  					Submit Reservation
				  	</button>
				</div>
			</form>
		</div>
	</div>
	
	<font color = "green">
		<div id ="end"></div>
	</font>
	
	<script>
	//javascript to handle calendar reservations
		function reservation() {
			console.log(document.reservationform.date.value);
			console.log(document.reservationform.time.value);
			console.log(document.reservationform.notes.value);
			console.log(document.getElementById("result").innerHTML.trim());    
			console.log(document.getElementById("address-div").innerHTML.trim()); 

			handleAuthClick(event);
			//alert("Hi3");
			createEvent()
      		return false;
   		}
	
 		// Client ID and API key from the Developer Console
		var CLIENT_ID = '763628273790-0dlejje9ro9lr3s9383ks7opcd3a5um5.apps.googleusercontent.com';
		var API_KEY = 'AIzaSyD7DICMiO-x_teN4t-MUhbE3RRJPuGKeeY';
		
		// Array of API discovery doc URLs for APIs used by the quickstart
		var DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest"];
		
		// Authorization scopes required by the API; multiple scopes can be
		// included, separated by spaces.
		var SCOPES = "https://www.googleapis.com/auth/calendar";
		
		var addReservationButton = document.getElementById('toggle-button');
		var submitReservationButton = document.getElementById('submit-reservation');

		function handleClientLoad() {
			gapi.load('client:auth2', initClient);
		}
		
		function initClient() {
			gapi.client.init({
				apiKey: API_KEY,
				clientId: CLIENT_ID,
				discoveryDocs: DISCOVERY_DOCS,
				scope: SCOPES
			}).then(function () {
				// Listen for sign-in state changes.
				gapi.auth2.getAuthInstance().isSignedIn.listen(updateSigninStatus);
			
				// Handle the initial sign-in state.
				//updateSigninStatus(gapi.auth2.getAuthInstance().isSignedIn.get());
				//addReservationButton.onclick = handleAuthClick;
			}, function(error) {
				appendPre(JSON.stringify(error, null, 2));
			}); 
		}
	      
		function updateSigninStatus(isSignedIn) {
			if (isSignedIn) {
				//submitReservationButton.style.display = 'block'; 
				
				//button function
				//alert("Hi");
				//createEvent(); 
			} else {
				//submitReservationButton.style.display = 'none';
			}
		}
		
	    function handleAuthClick(event) {
			gapi.auth2.getAuthInstance().signIn();
		}
	    
	    function createEvent() {
	    	
			var rest_name = document.getElementById("result").innerHTML.trim();
			var temp = document.getElementById("address-div").innerHTML.trim();  
			var address = temp.slice(9);
			
			var date = document.reservationform.date.value;
			var time = document.reservationform.time.value;
			var start_datetime = date + "T" + time + ":00-07:00";
			
			
			var hour = time.substring(0,2);
			var minutes = time.substring(3);
			hour_int = parseInt(hour);
			var end_hour;
			if(hour_int + 2 <= 24){
				end_hour = hour_int + 2;
			}
			else{
				end_hour = 23;
				minutes = 59;
			}
			
			var string_end_hour = end_hour.toString();
			var string_minutes = minutes.toString();
			var end_datetime = date + "T" + string_end_hour + ":" + string_minutes + ":00-07:00";
				
			var notes = document.reservationform.notes.value;
			
			var summary = "Reservation at " + rest_name;
			
			var event = {
				'summary': summary,
				'location': address,
				'description': notes,
				'start': {
				  'dateTime': start_datetime,
				  'timeZone': 'America/Los_Angeles'
				},
				'end': {
				  'dateTime': end_datetime,
				  'timeZone': 'America/Los_Angeles'
				},
			};
	    	
	    	var request = gapi.client.calendar.events.insert({
    		  'calendarId': 'primary',
    		  'resource': event
    		});

    		request.execute(function(event) {
    			//alert("hi");
				//appendPre('Event created: ' + event.htmlLink);
    			$("#end").html("Reservation was Made");
    		});
    		
		}
	    
	
	</script>
	

	<script>
		function initMap() {
			var usc = {lat: 34.02556283731423, lng: -118.2851130343933};
			var map = new google.maps.Map(
				document.getElementById('map'), {zoom: 15, center: usc});

	        var infoWindow = new google.maps.InfoWindow(
	            {content: 'Click the map to get the Latitude/Longitude!', position: usc});
	        infoWindow.open(map);

	        var markersArray = [];
	        map.addListener('click', function(mapsMouseEvent) {
	        	
	        	$("#myModal").modal("toggle");
	        	
	        	//$("#blue-btn").toggleClass('active');
	        	
				infoWindow.close();
	        	
				for (var i = 0; i < markersArray.length; i++) {
		        	markersArray[i].setMap(null);
		        }
				markers = [];
	     
	        	var temp = mapsMouseEvent.latLng.toString();
	        	
	        	var temp2= temp.split(",")[0];
	        	var lat = temp2.split("(")[1];
	        	
	        	var temp3 = temp.split(", ")[1];
	        	var lng = temp3.split(")")[0];
	        	
	        	var x = document.getElementById("latitude");   
	        	x.value = lat; 

	        	var y = document.getElementById("longitude");   
	        	y.value = lng; 
	        	
				setTimeout(function () {
					var marker = new google.maps.Marker({position: mapsMouseEvent.latLng, map: map});
					markersArray.push(marker);
					map.panTo(mapsMouseEvent.latLng);
			    }, 100);
				
			});
		}
    </script>
    
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyD7DICMiO-x_teN4t-MUhbE3RRJPuGKeeY&callback=initMap"
    async defer></script>
	 
	<!--Modal: Name-->
	<div class="modal fade" id="myModal" role="dialog">
		<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		    <!--Content-->
		    <div class="modal-content">
		
				<!--Body-->
				<div class="modal-body mb-0 p-0">
		
					<!--Google map-->
		        	<div>
		          		<div style="width: 1140pxpx; height: 650px;" id="map"></div>
		        	</div>
		
				</div>
		    </div>
		</div>
	</div>
    


	<footer id = "footer" class = "center">
		<h3>
			Made by Louis Chen
		</h3>
		
		<h3>
			Designed for Google Chrome (2880 x 1800)
		</h3>
	</footer>
	
	<script src="https://apis.google.com/js/platform.js?" onload="this.onload=function(){};handleClientLoad()" async defer></script>

	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

	<script src=details.js></script>

</body>
</html>