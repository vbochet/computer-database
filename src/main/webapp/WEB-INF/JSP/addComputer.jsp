<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
        	<c:if test="${error}">
        	<div class="row">
        		<div class="alert alert-danger" role="alert">
				    <spring:message code="add_computer_error_message" />
				</div>
        	</div>
        	</c:if>
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1><spring:message code="add_computer" /></h1>
                    <c:set var="formAction"><t:links target='addComputer' /></c:set>
                    <form:form action="${formAction}" method="POST" id="add-form" modelAttribute="computerDto">
                        <fieldset>
                            <div class="form-group">
                            	<c:set var="cptName"><c:if test="${error}">${computer.computerName}</c:if></c:set>
                            	<c:set var="cptNamePlaceholder"><spring:message code="computer_name" /></c:set>
                                <form:label path="computerName" for="computerName"><spring:message code="computer_name" /></form:label>
                                <form:input path="computerName" type="text" class="form-control" id="computerName" name="computerName" placeholder="${cptNamePlaceholder }"
                                        value="${cptName}"
                                        data-validation="custom"
                                        data-validation-regexp="^([^\s<>]+(\s)*)+$"
                                        data-validation-error-msg="<spring:message code='computer_name_not_allowed' />" />
                            </div>
                            <div class="form-group">
                                <form:label path="computerIntroduced" for="introduced"><spring:message code="computer_introduced" /></form:label>
                                <form:input path="computerIntroduced" type="date" class="form-control" id="introduced" name="introduced" placeholder="Introduced date"
                                        value='<c:if test="${error}">${computer.computerIntroduced}</c:if>'
                                        data-validation="date" 
                                        data-validation-format="yyyy-mm-dd"
                                        data-validation-depends-on="discontinued" />
                            </div>
                            <div class="form-group">
                                <form:label path="computerDiscontinued" for="discontinued"><spring:message code="computer_discontinued" /></form:label>
                                <form:input path="computerDiscontinued" type="date" class="form-control" id="discontinued" name="discontinued" placeholder="Discontinued date"
                                        value='<c:if test="${error}">${computer.computerDiscontinued}</c:if>'
                                        data-validation="date"
                                        data-validation-format="yyyy-mm-dd"
                                        data-validation-optional="true" />
                            </div>
                            <div class="form-group">
                                <form:label path="computerCompanyId" for="companyId"><spring:message code="company" /></form:label>
                                <form:select path="computerCompanyId" class="form-control" id="companyId" name="companyId" >
                                    <option value="0">--</option>
                                    <c:forEach items="${companyList}" var="company">
                                    <option value="${company.companyId}">${company.companyName}</option>
                                    </c:forEach>
                                </form:select>
                            </div>                  
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="<spring:message code="add" />" class="btn btn-primary">
                            <spring:message code="or" />
                            <a href="<t:links target='dashboard' />" class="btn btn-default"><spring:message code="cancel" /></a>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </section>
<script src="<t:links target='js' append='jquery.min.js' />"></script>
<script src="<t:links target='js' append='bootstrap.min.js' />"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery-form-validator/2.3.26/jquery.form-validator.min.js"></script>
<script>
 $.validate({
 	  modules : 'date, logic'
 	});
</script>

</body>
</html>