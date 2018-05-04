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
        
	    <div class="collapse navbar-collapse" id="navbarNav">
	        <ul class="navbar-nav">
	            <li class="nav-item">
		            <a class="navbar" href="?lang=en"> English </a>
	            </li>
	            <li class="nav-item">
		            <a class="navbar" href="?lang=fr"> Français </a>
	            </li>
	            <li>
	                <form:form action="${pageContext.request.contextPath}/logout" method="POST">
                        <input type="submit" value="<spring:message code="logout" />" />
                    </form:form>
                </li>
            </ul>
        </div>
        </div>
    </header>

    <section id="main">
        <div class="container">
			<c:if test = "${deletionSuccess}">
            <div class="alert alert-success">
                <spring:message code="computers_deletion_successful" />
            </div>
            </c:if>
            <h1 id="homeTitle">
                ${page.nbTotal} <spring:message code="computers_found" />
            </h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="<t:links target='dashboardSearch' pagenb='${page.currentPage}' displayBy='${page.nbPerPage}' orderBy='${page.orderBy}' orderDesc='${page.orderDesc}' />" method="GET" class="form-inline">
                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="<spring:message code="search_name" />" />
                        <input type="submit" id="searchsubmit" value="<spring:message code="filter" />"
                        class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="<t:links target='addComputer' />"><spring:message code="add_computer" /></a> 
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();"><spring:message code="delete" /></a>
                </div>
            </div>
        </div>

        <form:form id="deleteForm" action="#" method="POST">
            <input type="hidden" name="selection" value="">
        </form:form>

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
                            <a href="<t:links target='dashboardOther' displayBy='${page.nbPerPage}' orderBy='name' orderDesc='${page.orderBy == "name" && !page.orderDesc}' search='${page.search}' />"><spring:message code="computer_name" /></a>
                        </th>
                        <th>
                            <a href="<t:links target='dashboardOther' displayBy='${page.nbPerPage}' orderBy='introduced' orderDesc='${page.orderBy == "introduced" && !page.orderDesc}' search='${page.search}' />"><spring:message code="computer_introduced" /></a>
                        </th>
                        <th>
                            <a href="<t:links target='dashboardOther' displayBy='${page.nbPerPage}' orderBy='discontinued' orderDesc='${page.orderBy == "discontinued" && !page.orderDesc}' search='${page.search}' />"><spring:message code="computer_discontinued" /></a>
                        </th>
                        <th>
                            <a href="<t:links target='dashboardOther' displayBy='${page.nbPerPage}' orderBy='company' orderDesc='${page.orderBy == "company" && !page.orderDesc}' search='${page.search}' />"><spring:message code="company" /></a>
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                
                    <c:forEach var="computer" items="${page.content}">
                    <tr>
                        <td class="editMode">
                            <input type="checkbox" name="cb" class="cb" value="${computer.computerId}">
                        </td>
                        <td>
                            <a href="<t:links target='editComputer' computerId='${computer.computerId}' />" onclick="">${computer.computerName}</a>
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
            <t:pagination maxPage="${page.maxPage}" currentPage="${page.currentPage}" displayBy="${page.nbPerPage}" orderBy="${page.orderBy}" orderDesc="${page.orderDesc}" search="${page.search}" ></t:pagination>

            <div class="btn-group btn-group-sm pull-right" role="group" >
                <a href="<t:links target='dashboardOther' displayBy='10' pagenb='${page.currentPage}' orderBy='${page.orderBy}' orderDesc='${page.orderDesc}' search='${page.search}' />"><button type="button" class="btn btn-default">10</button></a>
                <a href="<t:links target='dashboardOther' displayBy='50' pagenb='${page.currentPage}' orderBy='${page.orderBy}' orderDesc='${page.orderDesc}' search='${page.search}' />"><button type="button" class="btn btn-default">50</button></a>
                <a href="<t:links target='dashboardOther' displayBy='100' pagenb='${page.currentPage}' orderBy='${page.orderBy}' orderDesc='${page.orderDesc}' search='${page.search}' />"><button type="button" class="btn btn-default">100</button></a>
            </div>
		</div>
    </footer>
<script src="<t:links target='js' append='jquery.min.js' />"></script>
<script src="<t:links target='js' append='bootstrap.min.js' />"></script>
<script src="<t:links target='js' append='dashboard.js' />"></script>

</body>
</html>