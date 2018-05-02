<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@page session="true"%>
<%@ taglib tagdir = "/WEB-INF/tags" prefix = "t" %>

<!DOCTYPE html>
<html>
<head>
<title><spring:message code="login" /> | Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
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
            <h1 id="homeTitle">
                <spring:message code="login" />
            </h1>
        </div>

        <div class="container" style="margin-top: 10px;">
            <h2>Please login to access the platform</h2>

        	<c:if test="${error != null}">
        	<div class="row">
        		<div class="alert alert-danger" role="alert">
				    <spring:message code="error_msg" />
				</div>
        	</div>
        	</c:if>
        	<c:if test="${logout != null}">
        	<div class="row">
        		<div class="alert alert-info" role="alert">
				    <spring:message code="logout_msg" />
				</div>
        	</div>
        	</c:if>

			<form name='loginForm' action="<c:url value='/j_spring_security_check' />" method='POST'>
				<table>
					<tr>
						<td><label for="username"><spring:message code="username" /> :</label></td>
						<td><input type='text' id="username" name='username'></td>
					</tr>
					<tr>
						<td><label for="password"><spring:message code="password" /> :</label></td>
						<td><input type='password' id="password" name='password' /></td>
					</tr>
					<tr>
						<td colspan='2'><input name="submit" type="submit" value="<spring:message code="login" />" /></td>
					</tr>
				  </table>
				  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			</form>
        </div>
    </section>

    <footer class="navbar-fixed-bottom">
    </footer>
<script src="<t:links target='js' append='jquery.min.js' />"></script>
<script src="<t:links target='js' append='bootstrap.min.js' />"></script>

</body>
</html>