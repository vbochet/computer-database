<%@ page isErrorPage="true" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib tagdir = "/WEB-INF/tags" prefix = "t" %>

<!DOCTYPE html>
<html>
<head>
	<title>Computer Database</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Bootstrap -->
	<link href="<t:links target='css' append='bootstrap.min.css' />" rel="stylesheet" media="screen">
	<link href="<t:links target='css' append='font-awesome.css' />" rel="stylesheet" media="screen">
	<link href="<t:links target='css' append='main.css' />" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="<t:links target='dashboard' />"> Application - Computer Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<div class="alert alert-danger">
				Error 403: Access denied!
				<br/>
				<br/>
				<!-- stacktrace -->
				<c:forEach var = "trace" 
                  items = "${pageContext.exception.stackTrace}">
                  ${trace}<br/>
               </c:forEach>
			</div>
		</div>
	</section>

	<script src="<t:links target='js' append='jquery.min.js' />"></script>
	<script src="<t:links target='js' append='bootstrap.min.js' />"></script>
	<script src="<t:links target='js' append='dashboard.js' />"></script>

</body>
</html>