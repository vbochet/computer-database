<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib tagdir = "/WEB-INF/tags" prefix = "t" %>

<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="<c:url value="/"/>static/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="<c:url value="/"/>static/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="<c:url value="/"/>static/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="<c:url value="/"/>dashboard"> Application - Computer Database </a>
        </div>
    </header>

    <section id="main">
        <div class="container">
            <h1 id="homeTitle">
                ${nbComputersFound} Computers found
            </h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="#" method="GET" class="form-inline">

                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" />
                        <input type="submit" id="searchsubmit" value="Filter by name"
                        class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="<c:url value="/"/>addComputer">Add Computer</a> 
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">Edit</a>
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
                            Computer name
                        </th>
                        <th>
                            Introduced date
                        </th>
                        <th>
                            Discontinued date
                        </th>
                        <th>
                            Company
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                
                    <c:forEach var="computer" items="${computersList}">
                    <tr>
                        <td class="editMode">
                            <input type="checkbox" name="cb" class="cb" value="${computer.computerId}">
                        </td>
                        <td>
                            <a href="<c:url value="/"/>editComputer" onclick="">${computer.computerName}</a>
                        </td>
                        <td>${computer.computerIntroduced}</td>
                        <td>${computer.computerDiscontinued}</td>
                        <td>${computer.computerCompanyName}</td>

                    </tr>
                    </c:forEach>
                    
                </tbody>
            </table>
        </div>
    </section>

    <footer class="navbar-fixed-bottom">
        <div class="container text-center">
            <t:pagination maxPage="${maxPage}" currentPage="${currentPage}"></t:pagination>

            <div class="btn-group btn-group-sm pull-right" role="group" >
                <a href="?displayBy=10"><button type="button" class="btn btn-default">10</button></a>
                <a href="?displayBy=50"><button type="button" class="btn btn-default">50</button></a>
                <a href="?displayBy=100"><button type="button" class="btn btn-default">100</button></a>
            </div>
		</div>
    </footer>
<script src="<c:url value="/"/>static/js/jquery.min.js"></script>
<script src="<c:url value="/"/>static/js/bootstrap.min.js"></script>
<script src="<c:url value="/"/>static/js/dashboard.js"></script>

</body>
</html>