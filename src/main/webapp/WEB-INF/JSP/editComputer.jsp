<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib tagdir = "/WEB-INF/tags" prefix = "t" %>

<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
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
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <div class="label label-default pull-right">
                        id: ${computer.computerId}
                    </div>
                    <h1><spring:message code="edit_computer" /></h1>

                    <form action="<t:links target='editComputerPost' />" method="POST">
                        <input type="hidden" value="${computer.computerId}" id="id" name="computerId" />
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName"><spring:message code="computer_name" /></label>
                                <input type="text" class="form-control" id="computerName" name="computerName" placeholder="<spring:message code="computer_name" />" 
		                                value="${computer.computerName}"
		                                data-validation="custom"
		                                data-validation-regexp="^([^\s<>]+(\s)*)+$"
		                                data-validation-error-msg="<spring:message code="computer_name_not_allowed" />">
                            </div>
                            <div class="form-group">
                                <label for="introduced"><spring:message code="computer_introduced" /></label>
                                <input type="date" class="form-control" id="introduced" name="introduced" 
                                   		value="${computer.computerIntroduced}"
                                        data-validation="date" 
                                        data-validation-format="yyyy-mm-dd"
                                        data-validation-depends-on="discontinued" >
                            </div>
                            <div class="form-group">
                                <label for="discontinued"><spring:message code="computer_discontinued" /></label>
                                <input type="date" class="form-control" id="discontinued" name="discontinued" 
                                		value="${computer.computerDiscontinued}"
                                        data-validation="date"
                                        data-validation-format="yyyy-mm-dd"
                                        data-validation-optional="true" >
                            </div>
                            <div class="form-group">
                                <label for="companyId"><spring:message code="company" /></label>
                                <select class="form-control" id="companyId" name="companyId">
                                    <option value="0">--</option>
                                    <c:forEach items="${companyList}" var="companyl">
	                                    <c:if test="${companyl.companyId != company.companyId}">
	                                    <option value="${companyl.companyId}">${companyl.companyName}</option>
	                                    </c:if>
	                                    <c:if test="${companyl.companyId == company.companyId}">
	                                    <option value="${companyl.companyId}" selected>${companyl.companyName}</option>
	                                    </c:if>
                                    </c:forEach>
                                </select>
                            </div>            
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="<spring:message code="edit" />" class="btn btn-primary">
                            <spring:message code="or" />
                            <a href="<c:url value="/"/>dashboard" class="btn btn-default"><spring:message code="cancel" /></a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
<script src="<t:links target='js' append='jquery.min.js' />"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery-form-validator/2.3.26/jquery.form-validator.min.js"></script>
<script>
 $.validate({
 	  modules : 'date, logic'
 	});
</script>
    
</body>
</html>