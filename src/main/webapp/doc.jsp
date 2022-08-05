<%@page import="servlets.CSConnection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<head>
    <title>Welcome Doctor</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css"
        rel="stylesheet">
    <script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
</head>

<body class="container-fluid h-100" style="padding: 0; background-color: white;">
    <!--Navigation bar -->
    <nav class="navbar container-fluid" style="background-color: #0083DB;">
        <a class="navbar-brand fw-bold mx-3" style="font-size: 32px; color: white">Symptom Checker</a>
        <div>
            <img src="img/doctor-ava.png" alt="User Avatar" class="rounded-circle" style="width: 50px">
            <span>
            	<button type="button" class="btn dropdown-toggle" style="color: white; font-size: 18px; border: none;" data-bs-toggle="dropdown" id="username" name="Username"><%= CSConnection.getActiveUsername() %></button>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li><form class="text-center dropdown-item" action="doc" method="POST" id="DocSignOutForm"><input type="submit" name="SignOut" value="Sign Out" style="background-color: transparent; border: none;"></form></li>                    
                </ul>
            </span>
        </div>
    </nav>
    <!--Navigation bar end-->
    <div class="container-fluid row h-100">
        <div class="mt-5 col" style="padding: 0;">
            <img src="img/doctor.jpg" style="width: 1000px">        
        </div>
        <div class="mt-5 col" style="padding: 0;">
            <div class="text-center pt-5">
                <div class="mb-5">
                    <p class="fw-bolder" style="color: #0083DB; font-size: 84px;">WELCOME</p>
                    <p class="fw-bolder" style="color: #0083DB; font-size: 36px;">TO SYMPTOM CHECKER<br>DOCTOR SITE</p>
                </div>
                <form class="mt-5" method="POST" action="doc" id="DocWelcomeForm">
                    <!-- <button type="button" class="btn fw-bolder mt-5 px-5 py-2" style="color: white; background-color: #FE5655; font-size: 24px;" id="consult" name="consult">Start Consulting</button> -->
                    <input type="submit" class="btn fw-bolder mt-5 px-5 py-2" style="color: white; background-color: #FE5655; font-size: 24px;" id="consult" name="consult" value="Start Consulting"></button>
                </form>
            </div>   
        </div>    
    </div>
</body>