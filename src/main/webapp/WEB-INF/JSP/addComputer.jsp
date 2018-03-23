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
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1>Add Computer</h1>
                    <form action="<t:links target='addComputer' />" method="POST" id="add-form">
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                                <input type="text" class="form-control" id="computerName" name="computerName" placeholder="Computer name"
                                        data-validation="custom"
                                        data-validation-regexp="^([^\s-]+)$"
                                        data-validation-error-msg="Computer name cannot be empty">
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <input type="date" class="form-control" id="introduced" name="introduced" placeholder="Introduced date"
                                        data-validation="date" 
                                        data-validation-format="yyyy-mm-dd"
                                        data-validation-depends-on="discontinued" >
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <input type="date" class="form-control" id="discontinued" name="discontinued" placeholder="Discontinued date"
                                        data-validation="date"
                                        data-validation-format="yyyy-mm-dd"
                                        data-validation-optional="true" >
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <select class="form-control" id="companyId" name="companyId" >
                                    <option value="0">--</option>
                                    <c:forEach items="${companyList}" var="company">
                                    <option value="${company.companyId}">${company.companyName}</option>
                                    </c:forEach>
                                </select>
                            </div>                  
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="Add" class="btn btn-primary">
                            or
                            <a href="<t:links target='dashboard' />" class="btn btn-default">Cancel</a>
                        </div>
                    </form>
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