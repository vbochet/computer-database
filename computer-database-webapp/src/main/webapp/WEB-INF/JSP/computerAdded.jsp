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
            <h1 id="homeTitle">
                <spring:message code="computer_saved" />
            </h1>
        </div>

        <div class="container" style="margin-top: 10px;">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <!-- Variable declarations for passing labels as parameters -->

                        <th>
                            <spring:message code="computer_name" />
                        </th>
                        <th>
                            <spring:message code="computer_introduced" />
                        </th>
                        <th>
                            <spring:message code="computer_discontinued" />
                        </th>
                        <th>
                            <spring:message code="company" />
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                    <tr>
                        <td>${computer.computerName}</td>
                        <td>${computer.computerIntroduced}</td>
                        <td>${computer.computerDiscontinued}</td>
                        <td>${computer.computerCompanyName}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </section>

    <footer class="navbar-fixed-bottom">
    </footer>
<script src="<t:links target='js' append='jquery.min.js' />"></script>
<script src="<t:links target='js' append='bootstrap.min.js' />"></script>
<script src="<t:links target='js' append='dashboard.js' />"></script>

</body>
</html>