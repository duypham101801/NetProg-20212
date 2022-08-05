<%@page import="servlets.CSConnection"%>
<%@ page language="java" contentType="text/html"%>
<% int i; %>
<% int j; %>
<!DOCTYPE html>
<head>
    <title>Symptom Checker</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css"
        rel="stylesheet">
    <script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
    <link rel="stylesheet" href="css/style.css">
</head>

<body class="container-fluid h-100" style="padding: 0; background-color: #0083DB;">
    <!--Navigation bar -->
    <nav class="navbar container-fluid sticky-top" style="background-color: white;">
        <a class="navbar-brand fw-bold mx-3" style="font-size: 32px; color: #0083DB">Symptom Checker</a>
        <div>
            <img src="img/user-ava.png" alt="User Avatar" class="rounded-circle" style="width: 50px">
            <span>
            	<button type="button" class="btn dropdown-toggle" style="color: black; font-size: 18px; border: none;" data-bs-toggle="dropdown" id="username" name="Username"><%= CSConnection.getActiveUsername() %></button>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li><form class="text-center dropdown-item" action="index" method="POST" id="IndexSignOutForm"><input type="submit" name="SignOut" value="Sign Out" style="background-color: transparent; border: none;"></form></li>                    
                </ul>
            </span>
        </div>
    </nav>
    <!--Navigation bar end-->

    <div class="container-fluid h-100 mt-3">
        <div class="col" style="padding: 0;">
            <div class="text-center">
                <div >
                    <p class="fw-bold" style="color: white; font-size: 40px;">SYMPTOM CHECKER</p>
                    <p style="color: white; font-size: 20px;">Choose a symptom and answer simple questions using our physician-reviewed<br>Symptom Checker to find a possible diagnosis for your health issue.<br>Consult with your doctor if you feel you have a serious medical problem.</p>
                </div>
                <!--Step 1-->
                <div class="container text-center rounded mt-4 py-3" style="background-color: white;" id="step1">
                	<form action="index" method="POST" id="Step1">
	                    <div>
	                        <p class="fw-bold" style="color: grey; font-size: 30px;">Step 1: Selecting A Symptom</p>
	                    </div>
	                    <div class="pt-3">
	                    	<% for (i = 1; i <= 5; i++) { %>
	                        	<input type="checkbox" class="btn-check" id="btncheck<%= i %>" autocomplete="off" name="btncheck" value="<%= i %>" />
	                        	<label class="btn btn-outline-primary mx-3 px-5 py-3 text-capitalize" for="btncheck<%= i %>"><%= CSConnection.getPartsAt(i).substring(4, CSConnection.getPartsAt(i).length()-1) %></label>
	                        <% } %>
	                    </div>
	                    <div>
	                        <input type="submit" class="btn fw-bolder mt-4 px-5 py-2" style="color: white; background-color: #FE5655;" id="submit1" name="submit1" value="Submit">
	                    </div>
	            	</form>
                </div>
                <!--Step 1 end-->

                <!--Step 2-->
                <div class="container text-center rounded mt-4 py-3" style="background-color: white;" id="step2">
                    <div>
                        <p class="fw-bold" style="color: grey; font-size: 30px;">Step 2: Answering Questions</p>
                    </div>
                    <% if (CSConnection.keyAnsReq) { %>
                    <form action="index" method="post" name="step2">
	                    <div class="pt-3">
	                    <% for (i = 0; i < CSConnection.getNumSymp(); i++) { %>
	                        <div class="mb-5" id="question-for-symptom-<%= i+1 %>">
	                            <div>
	                                <p class="fw-bold" style="color: black; font-size: 24px;" id="symp1">Symptom <%= i+1 %></p>
	                            </div>
	                        <% for (j = 1; j <= 5; j++) {%>
	                            <div class="row gx-1">
	                                <div class="col rounded mx-2 p-2" style="border: 2px solid #0d6efd;" id="question<%= i*5+j %>">
	                                    <div>
	                                        <p class="text-wrap"><%= CSConnection.ansparts[i*5+j] %></p>
	                                    </div>
	                                    <div class="btn-group" role="group">
	                                        <input type="checkbox" class="btn-check" name="btnYes" id="btnradio1<%= i*5+j %>" autocomplete="off" value="<%= CSConnection.anspartsID[i*5+j] %>">
	                                        <label class="btn btn-outline-primary" for="btnradio1<%= i*5+j %>">Yes</label>
	                                    
	                                        <input type="checkbox" class="btn-check" name="btnradio" id="btnradio2<%= i*5+j %>" autocomplete="off">
	                                        <label class="btn btn-outline-primary" for="btnradio2<%= i*5+j %>">No</label>
	                                    </div>
	                                </div>
	                            </div>
	                        <% } %>
	                        </div>
						<% } %>
	                    </div>
	                    <div>
	                        <input type="submit" class="btn fw-bolder mt-4 px-5 py-2" style="color: white; background-color: #FE5655;" id="submit2" name="submit2" value="Submit">
	                    </div>
                    </form>
                    <% } %>
                </div>
                <!--Step 2 end-->

                <!--Step 3-->
                <div class="container text-center rounded my-4 py-3" style="background-color: white;" id="step3">
                    <div>
                        <p class="fw-bold" style="color: grey; font-size: 30px;">Step 3: Receive Diagnosis</p>
                    </div>
                    <% if (CSConnection.keyDiag == true) { %>
                    <div class="pt-3">
                        <p>Your problem maybe a</p>
                    	<% for (i = 1; i < CSConnection.diagString.length; i++) { %>
                        	<p class="fw-bold text-capitalize" id="diagnosis" style="color: #0d6efd;"><%= CSConnection.diagString[i] %></p>
                   		<% } %>
                    </div>
                    <form action="index" method="POST" name="step3">
                        <input type="submit" class="btn fw-bolder mt-4 px-5 py-2" style="color: white; background-color: #FE5655;" id="request" name="requestCons" value="Request Consult">
                    </form>
                    <% } %>
                </div>
                <!--Step 3 end-->
            </div>   
        </div>
    </div>

    <script>
        var step2 = document.getElementById("step2");
        var step3 = document.getElementById("step3");
        var submit1 = document.getElementById("submit1");
        var submit2 = document.getElementById("submit2");
        submit1.onclick = function() {
            step2.style.display = "block";
        }
        submit2.onclick = function() {
            step3.style.display = "block";
        }
    </script>
</body>