<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib tagdir = "/WEB-INF/tags" prefix = "t" %>

<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
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
			<c:if test = "${deletionSuccess}">
            <div class="alert alert-success">
                User(s) deleted successfully
            </div>
            </c:if>
            <h1 id="homeTitle">
                User list
            </h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-right">
                    <a class="btn btn-success" id="addUser" href="add">Add a new user account</a>
                    <a class="btn btn-default" id="deleteUser" href="#" onclick="$.fn.toggleEditMode();">Delete users</a>
                </div>
            </div>
        </div>

        <form id="deleteForm" action="#" method="POST">
            <input type="hidden" name="selection" value="">
        </form>

        <div class="container" style="margin-top: 10px;">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <!-- Variable declarations for passing labels as parameters -->

                        <th class="editMode" style="width: 60px; height: 22px;">
                            <input type="checkbox" id="selectall" /> 
                            <span style="vertical-align: top;">
                                 -  <a href="#" id="deleteSelected" onclick="$.fn.deleteSelected();">
                                        <i class="fa fa-trash-o fa-lg"></i>
                                    </a>
                            </span>
                        </th>
                        <th>
                            Username
                        </th>
                        <th>
                            Password
                        </th>
                        <th>
                            Enabled
                        </th>
                        <th>
                            Role
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                
                    <c:forEach var="user" items="${usersList}">
                    <tr>
                        <td class="editMode">
                            <input type="checkbox" name="cb" class="cb" value="${user.username}">
                        </td>
                        <td>
                            <a href="edit?username=${user.username}">${user.username}</a>
                        </td>
                        <td>${user.password}</td>
                        <td>${user.enabled}</td>
                        <td>${user.role}</td>

                    </tr>
                    </c:forEach>
                    
                </tbody>
            </table>
        </div>
    </section>
<script src="<t:links target='js' append='jquery.min.js' />"></script>
<script src="<t:links target='js' append='bootstrap.min.js' />"></script>
<script src="<t:links target='js' append='dashboard.js' />"></script>

</body>
</html>