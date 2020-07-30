<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
	
	
	<!-- <script src="https://apis.google.com/js/platform.js" async defer></script> -->
	<script src="https://apis.google.com/js/platform.js?onload=renderButton" async defer></script>
	<meta name="google-signin-client_id" content="763628273790-0dlejje9ro9lr3s9383ks7opcd3a5um5.apps.googleusercontent.com">
	<script src="http://code.jquery.com/jquery-3.4.1.min.js" integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo=" crossorigin="anonymous"></script>

	<script>
		/* gapi.load('auth2',function()){
			gapi.auth2.init();
		}); */
		
		function onSignIn(googleUser) {
			var profile = googleUser.getBasicProfile();
			console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
			console.log('Name: ' + profile.getName());
			console.log('Image URL: ' + profile.getImageUrl());
			console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
			
			var auth2 = gapi.auth2.getAuthInstance();
			auth2.disconnect();
			
			googleLogin(profile.getName(), profile.getEmail());
		}
		
		function googleLogin(name, email){
			$.ajax({
	   			url: "GoogleLoginServlet",
	   			async: false,
	   			data: {
	     			name: name,
	     			email: email
	   			},
	   			success: function( result ) {
	   				if(!result){
	   					window.location.replace("http://localhost:8082/chenloui_CSCI201_assignment4/home.jsp");
       				}
       				$("#errormessage").html(result);
	   			}
	 		}); 
		}
		
		function onSuccess(googleUser) {
		  console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
		}
		function onFailure(error) {
		  console.log(error);
		}
		function renderButton() {
		  gapi.signin2.render('my-signin2', {
		    'scope': 'profile email',
		    'width': 700,
		    'height': 45,
		    'longtitle': true,
		    'theme': 'dark',
		    'onsuccess': onSignIn,
		    'onfailure': onFailure
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

	<body onload="document.loginform.reset()">
		<% 
			String login_username = request.getParameter("login_username");
			if(login_username == null){
				login_username = "";
			}
			String login_username_error = (String)request.getAttribute("login_username_error");
			if(login_username_error == null){
				login_username_error = "";
			}
			
			String login_password_error = (String)request.getAttribute("login_password_error");
			if(login_password_error == null){
				login_password_error = "";
			}
			
			String username_exist_error = (String)request.getAttribute("username_exist_error");
			if(username_exist_error == null){
				username_exist_error = "";
			}
			
			String login_error = (String)request.getAttribute("login_error");
			if(login_error == null){
				login_error = "";
			}
		
		%>

	<body onload="document.signupform.reset()">
		<% 
			String email = request.getParameter("input_email");
			if(email == null){
				email = "";
			}
			String email_error = (String)request.getAttribute("email_error");
			if(email_error == null){
				email_error = "";
			}
			String emaildata_error = (String)request.getAttribute("emaildata_error");
			if(emaildata_error == null){
				emaildata_error = "";
			}
			String email_exist_error = (String)request.getAttribute("email_exist_error");
			if(email_exist_error == null){
				email_exist_error = "";
			}
			
			String username = request.getParameter("input_username");
			if(username == null){
				username = "";
			}
			String username_error = (String)request.getAttribute("username_error");
			if(username_error == null){
				username_error = "";
			}
			String username_exist_error2 = (String)request.getAttribute("username_exist_error2");
			if(username_exist_error2 == null){
				username_exist_error2 = "";
			}
			
			String password_error = (String)request.getAttribute("password_error");
			if(password_error == null){
				password_error = "";
			}
			
			String password2_error = (String)request.getAttribute("password2_error");
			if(password2_error == null){
				password2_error = "";
			}
			
			String password_mismatch_error = (String)request.getAttribute("password_mismatch_error");
			if(password_mismatch_error == null){
				password_mismatch_error = "";
			}
		
		%>

<body>

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
	<br>
	<br>
	
	<div class = "main-body center">
		<div class="login">
			<div class="login1">
				<form method="GET" action="LoginServlet" name="loginform">
					<h2>Login</h2>
					<br>
					<div class="form-group">
						<label for="login_username">Username <font color="red"><i><%= login_username_error %></i></font>
						<font color="red"><i><%= username_exist_error %></i></font></label>
						<input type="text" class="form-control" name="login_username" value="<%=login_username%>">
					</div>
					<div class="form-group">
						<label for="login_password">Password <font color="red"><i><%= login_password_error %></i></font></label>
						<input type="password" class="form-control" name="login_password">
					</div>
					<div>
						<font color="red"><i><%= login_error %></i></font>
					</div>
					<br>
					<div class="form-group">
					  	<button type="submit" class="btn btn-danger" id="red-button">
					  		<i class="fas fa-sign-in-alt"></i>
					  		Sign In
					  	</button>
					</div>
					<hr width=50%>
					<div class="form-group">
						<div id=errormessage></div>
						<div id="my-signin2"></div>
<!-- 				  		<button type="button" class="btn btn-primary" id = "blue-btn" name="map_btn"> 
				  			<i class="fab fa-google"></i>
				  			Sign In with Google
				  		</button> -->
				  	</div>
				</form>
			</div>
			
			<div class="login2">
				<form method="GET" action="SignUpServlet" name="signupform">
					<h2>Sign up</h2>
					<br>
					<div class="form-group">
						<label for="input_email">Email <font color="red"><i><%= email_error %></i></font>
						<font color="red"><i><%= emaildata_error %></i></font>
						<font color="red"><i><%= email_exist_error %></i></font></label>
						<input type="email" class="form-control" name="input_email" value="<%=email%>" required>
					</div>
					<div class="form-group">
						<label for="input_username">Username <font color="red"><i><%= username_error %></i></font>
						<font color="red"><i><%= username_exist_error2 %></i></font></label>
						<input type="text" class="form-control" name="input_username" value="<%=username%>" required>
					</div>
					<div class="form-group">
						<label for="input_password">Password <font color="red"><i><%= password_error %></i></font><font color="red"><i><%=  password_mismatch_error %></i></font></label>
						<input type="password" class="form-control" name="input_password" required>
					</div>
					<div class="form-group">
						<label for="input_password2">Confirm Password <font color="red"><i><%= password2_error %></i></font><font color="red"><i><%= password_mismatch_error %></i></font></label>
						<input type="password" class="form-control" name="input_password2" required>
					</div>
					<div class="form-check">
						<input class="form-check-input" type="checkbox" value="checked" id="terms" required>
						<label for="terms" class=form-check-label">
							I have read and agreed to all terms and conditions of SalEats
						</label>
					</div>
					<div class="form-group">
					  	<button type="submit" class="btn btn-danger" id="red-button">
					  		<i class="fas fa-user-plus"></i>
					  		Create Account
					  	</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	
<!-- 	<div class="g-signin2" data-onsuccess="onSignIn"></div> -->
	
	
  
  
  
  

	<footer id = "footer" class = "center">
		<h3>
			Made by Louis Chen
		</h3>
		
		<h3>
			Designed for Google Chrome (2880 x 1800)
		</h3>
	</footer>

	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</body>
</html>