<%@ page import="servlets.CSConnection" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>

<head>
    <title>Doctor Consult</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet">
   	<link rel="stylesheet" href="css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js"></script>
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
    
    <!--Pop up-->
    <div id="myPopup" class="popup">
        <div class="popup-content rounded-4" style="text-align: center;">
            <table class="table">
                <thead>
                  <tr class="table-primary">
                    <th scope="col">Symptom</th>
                    <th scope="col">Diagnosis</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>Symptom 1</td>
                    <td>Diagnosis 1</td>
                  </tr>
                  <tr>
                    <td>Symptom 2</td>
                    <td>Diagnosis 2</td>
                  </tr>
                </tbody>
              </table>
          <span class="d-flex justify-content-center">
            <a id="cancelBtn" class="btn px-3" style="font-size: 15px; background-color: lightgrey; color: black;">Cancel</a>
          </span>
        </div>
    </div>
    <!--Pop up end-->

    <div class="container-fluid mt-1 d-flex justify-content-center" style="background-color: white">
        <div class="chat" style="box-shadow: 0px 3px 2px rgba(0,0,0,0.100);">
            <div class="header-chat col">
                <span class="ms-3 text-center">Patient Name</span>
                <span>
                    <button type="button" class="btn fw-bold dropdown-toggle" style="color: black; font-size: 24px; border: none;" data-bs-toggle="dropdown" id="Patient name" name="Patient name">Pham Thai Duy</button>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><a id="Patient Diagnosis" class="text-center dropdown-item">Patient Diagnosis</a></li>                    
                    </ul>
                </span>
                <div class="d-flex justify-content-end container">
                    <button type="button" class="btn fw-bold" style="color: white; background-color: #FE5655; font-size: 18px;" id="consult" name="consult">End</button>
                </div>
            </div>
            <div class="messages-chat scroll">
                <p class="notify justify-content-center d-flex">6:33 PM 12/07/2022</p>
                <p class="notify justify-content-center d-flex">Patient has arrived!</p>
                <div class="message">
                    <p class="text"> Hi, how are you ? </p>
                </div>
                <div class="message">
                    <p class="text"> What are you doing tonight ? Want to go take a drink ?</p>
                </div>
                <div class="message">
                    <div class="response">
                        <p class="text"> Hey Megan ! It's been a while</p>
                    </div>
                </div>
                <div class="message">
                    <div class="response">
                        <p class="text"> When can we meet ?</p>
                    </div>
                </div>
                <div class="message">
                    <p class="text"> 9 pm at the bar if possible</p>
                </div>
                <div class="message">
                    <div class="response">
                        <p class="text"> Hey Megan ! It's been a while</p>
                    </div>
                </div>
                <div class="message">
                    <div class="response">
                        <p class="text"> When can we meet ?</p>
                    </div>
                </div>
                <div class="message">
                    <p class="text"> 9 pm at the bar if possible</p>
                </div>
                <p class="notify mt-4 justify-content-center d-flex">Consultion Ended. Waiting for the next patience ...</p>
            </div>
            <div class="footer-chat">
                <input type="text" class="write-message" placeholder="Type your message here"></input>
                <div class="d-flex justify-content-end mx-3">
                    <input type="submit" class="btn" style="color: white; color: #0083DB; font-size: 16px;" id="send" name="send" value="Send"></button>
                </div>
            </div>
        </div>
    </div>

    <script>
        var popup = document.getElementById("myPopup");
        var btn = document.getElementById("Patient Diagnosis");
        var cancel = document.getElementById("cancelBtn");
        btn.onclick = function() {
            popup.style.display = "block";
        }
        cancel.onclick = function() {
            popup.style.display = "none";
        }
    </script>
</body>