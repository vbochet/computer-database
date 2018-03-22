<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ attribute name="target" required="true" %>
<%@ attribute name="append" required="false" %>
<%@ attribute name="amount" required="false" %>
<%@ attribute name="npage" required="false" %>
<%@ attribute name="computerId" required="false" %>
<c:choose>
    <c:when test="${target == 'root'}"><c:url value="/"/>${append}</c:when>
    <c:when test="${target == 'css'}"><c:url value="/static/css/"/>${append}</c:when>
    <c:when test="${target == 'js'}"><c:url value="/static/js/"/>${append}</c:when>
    <c:when test="${target == 'dashboard'}"><c:url value="/dashboard"/></c:when>
    <c:when test="${target == 'dashboardPrev'}"><c:url value="/dashboard?prev"/></c:when>
    <c:when test="${target == 'dashboardNext'}"><c:url value="/dashboard?next"/></c:when>
    <c:when test="${target == 'dashboardGoTo'}"><c:url value="/dashboard?npage="/>${npage}</c:when>
    <c:when test="${target == 'displayBy'}"><c:url value="/dashboard?displayBy="/>${amount}</c:when>
    <c:when test="${target == 'editComputer'}"><c:url value="/editComputer?computerId="/>${computerId}</c:when>
    <c:when test="${target == 'addComputer'}"><c:url value="/addComputer"/></c:when>
</c:choose>