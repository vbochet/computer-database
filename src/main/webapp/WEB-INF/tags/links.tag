<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ attribute name="target" required="true" %>
<%@ attribute name="append" required="false" %>
<%@ attribute name="displayBy" required="false" %>
<%@ attribute name="page" required="false" %>
<%@ attribute name="computerId" required="false" %>
<c:choose>
    <c:when test="${target == 'root'}"><c:url value="/"/>${append}</c:when>
    <c:when test="${target == 'css'}"><c:url value="/static/css/"/>${append}</c:when>
    <c:when test="${target == 'js'}"><c:url value="/static/js/"/>${append}</c:when>
    <c:when test="${target == 'dashboard'}"><c:url value="/dashboard"/></c:when>
    <c:when test="${target == 'dashboardPrev'}"><c:url value="/dashboard?prev&npage=${page}&displayBy=${displayBy}"/></c:when>
    <c:when test="${target == 'dashboardNext'}"><c:url value="/dashboard?next&npage=${page}&displayBy=${displayBy}"/></c:when>
    <c:when test="${target == 'dashboardGoTo'}"><c:url value="/dashboard?npage=${page}&displayBy=${displayBy}"/></c:when>
    <c:when test="${target == 'displayBy'}"><c:url value="/dashboard?npage=${page}&displayBy=${displayBy}"/></c:when>
    <c:when test="${target == 'editComputer'}"><c:url value="/editComputer?computerId="/>${computerId}</c:when>
    <c:when test="${target == 'editComputerPost'}"><c:url value="/editComputer"/></c:when>
    <c:when test="${target == 'addComputer'}"><c:url value="/addComputer"/></c:when>
</c:choose>