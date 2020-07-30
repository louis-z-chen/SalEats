<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.io.InputStreamReader"%>
<%@ page import="java.io.ObjectInputStream"%>
<%@ page import="java.io.ObjectOutputStream"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.net.HttpURLConnection"%>
<%@ page import="java.net.ServerSocket"%>
<%@ page import="java.net.Socket"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Scanner"%>
<%@ page import="java.util.Vector"%>

<%@ page import="com.google.gson.Gson"%>

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
    <meta http-equiv="pragma" content="no-cache" />
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	<link href= "styles.css" rel="stylesheet" type="text/css">
	<link rel = "stylesheet" href='https://fonts.googleapis.com/css?family=Lobster'>
	<script src="https://kit.fontawesome.com/aef7737b1c.js" crossorigin="anonymous"></script>
	
	<meta name="google-signin-client_id" content="763628273790-0dlejje9ro9lr3s9383ks7opcd3a5um5.apps.googleusercontent.com">
	<script src="http://code.jquery.com/jquery-3.4.1.min.js" integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo=" crossorigin="anonymous"></script>
	<script src="https://apis.google.com/js/platform.js?" async defer></script>
	
	<script>
		function sort(sortType){
			$.ajax({
       			url: "FavoritesServlet",
       			data: {
         			sort_type: sortType
       			},
       			success: function( result ) {
         			$( "#favorite-list").html(result);
       			}
     		});
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
			String session_loggedin;
			String session_userid;
			String session_email;
			String session_username;
		
			if (session != null) {
				
				session_loggedin = (String) session.getAttribute("loggedin");
				session_userid = (String) session.getAttribute("userid");
				session_email = (String) session.getAttribute("email");
				session_username = (String) session.getAttribute("username");
			     
			} else {
				session_loggedin = "";
				session_userid = "";
				session_email = "";
				session_username = "";
			}
			
			if(session_loggedin == "true"){
				session_isloggedin = true;
			}
			else{
				session_isloggedin = false;
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
			String name = "";
			if(session_isloggedin){
				name = session_username + "'s Favorites:";
			}
			else{
				name = "No User Logged In";
			}
		%>

<body onload="sort('Most Recent')">

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
	
		<div id="main">
			<div class = "favorite_bar">
				<h2 id="result"><%= name %></h2>
			
				<div class="dropdown">
					<button class="btn btn-danger dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				   	 	<i class="fas fa-sort-amount-down"></i> Sort by
				  	</button>
				  	<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
				   		<a class="dropdown-item" onClick = "return sort('A to Z')" href="#">A to Z</a>
				    	<a class="dropdown-item" onClick = "return sort('Z to A')" href="#">Z to A</a>
				    	<a class="dropdown-item" onClick = "return sort('Highest Rating')" href="#">Highest Rating</a>
				    	<a class="dropdown-item" onClick = "return sort('Lowest Rating')" href="#">Lowest Rating</a>
				    	<a class="dropdown-item" onClick = "return sort('Most Recent')" href="#">Most Recent</a>
				    	<a class="dropdown-item" onClick = "return sort('Least Recent')" href="#">Least Recent</a>
				  	</div>
				</div>
			
			</div>

			<div id="favorite-list"></div>

		</div>


	<footer id = "footer" class = "center">
		<h3>
			Made by Louis Chen
		</h3>
		
		<h3>
			Designed for Google Chrome (2880 x 1800)
		</h3>
	</footer>
	
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
	
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

	<script src=details.js></script>

</body>
</html>