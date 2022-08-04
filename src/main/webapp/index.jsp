<%@page import="servlets.CSConnection"%>
<%@ page language="java" contentType="text/html"%>
<% int i; %>
<!DOCTYPE html>

<head>
    <title>Homepage</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="style.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
</head>

<body style="background-color: #0083db">
    <div style="height: 50px; background-color: #EEEEEE">
		<div style="float: right; margin-right: 20px; padding-top: 10px;">
			<form action="index" method="post">
				<input type="submit" name="SignOut" value="User: <%= CSConnection.getActiveUsername() %>">
			</form>
		</div>
    </div>
    <div class="container mt-3" style="text-align: center;">
        <h1 class="text-white">Symptom Checker</h1>
        <p class="text-white col-6" style="margin-left: auto; margin-right: auto;">Choose a symptom and answer simple questions using our Symptom Checker to find a possible diagnosis for your health issue. Consult with your doctor if you feel you have a serious medical problem.</p>
    </div>
    <div class="container-fluid row justify-content-evenly" style="margin: auto; margin-top: 30px;">
        <div class="col-3 rounded-3 checkerBox">
        	<form action="index" method="POST" class="indexList">        	
	            <h5 class="stepTxt">Step 1</h5>
	            <h2 class="checkerTitle">Select A Symptom</h2>
	            <% for (i = 0; i < 5; i++) { %>
	            	<input type="checkbox" name="sympItem" value="<%= i %>" id="symp<%= i %>"/>
	            	<label for="symp<%= i %>">Travel with jump</label>
	            	<br>
	            <% } %>
        	</form>
        </div>
        <div class="col-3 rounded-3 checkerBox">
            <h5 class="stepTxt">Step 2</h5>
            <h2 class="checkerTitle">Answering Questions</h2>
            <form action="index" method="POST">
            	
            </form>
        </div>
        <div class="col-3 rounded-3 checkerBox">
            <h5 class="stepTxt">Step 3</h5>
            <h2 class="checkerTitle">Diagnosis<h2>
        </div>
    </div>
</body>