<%@ page language="java" contentType="text/html"%>
<!DOCTYPE html>

<head>
<title>Log In</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
</head>

<body style="background-color: #0083db">
	<div style="height: 50px; background-color: #EEEEEE"></div>
	<div class="container-fluid mt-3" style="text-align: center;">
		<h1 class="text-white">Symptom Checker</h1>
	</div>
	<div class="col-5" style="margin: auto; margin-top: 200px;">
		<h2 class="text-white mb-5 text-center">Log In</h2>
		<form action="login" method="post">
			<div class="my-3">
				<label for="username">Username:</label> <input type="text"
					class="form-control" id="username"
					placeholder="Enter your username" name="Username">
			</div>
			<div class="my-3">
				<label for="password">Password:</label> <input type="password"
					class="form-control" id="password"
					placeholder="Enter your password" name="Password">
			</div>
			<div class="LogInBtn container text-center my-4">
				<input type="submit" name="Submit" value="Log In" class="rounded-3"
					style="background-color: #F7B942; text-decoration: none; border: none; outline: none; height: 40px; width: 80px">
			</div>
			<% 
			if (request.getAttribute("result") != null) {
				if (request.getAttribute("result").toString() == "false") { %>
					<p>Wrong identity, please enter again</p>
			<% }} %>
		</form>
	</div>
</body>