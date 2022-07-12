<%@ page language="java" contentType="text/html"%>
<!DOCTYPE html>

<head>
<title>Test Info</title>
</head>

<body>
<h1>LOGIN INFO</h1>
	<%
		out.print("username = " + request.getParameter("Username"));
	%>
</body>