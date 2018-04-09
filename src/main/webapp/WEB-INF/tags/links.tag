<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ attribute name="target" required="true" %>
<%@ attribute name="append" required="false" %>
<%@ attribute name="displayBy" required="false" %>
<%@ attribute name="orderBy" required="false" %>
<%@ attribute name="orderDesc" required="false" %>
<%@ attribute name="pagenb" required="false" %>
<%@ attribute name="computerId" required="false" %>
<%@ attribute name="search" required="false" %>
<c:choose>
    <c:when test="${target == 'root'}"><c:url value="/"/>${append}</c:when>
    <c:when test="${target == 'css'}"><c:url value="/static/css/"/>${append}</c:when>
    <c:when test="${target == 'js'}"><c:url value="/static/js/"/>${append}</c:when>
    <c:when test="${target == 'dashboard'}"><c:url value="/dashboard"/></c:when>
    <c:when test="${target == 'dashboardPrev'}"><c:url value="/dashboard?prev&npage=${pagenb}&displayBy=${displayBy}&orderBy=${orderBy}&orderDesc=${orderDesc}&search=${search}"/></c:when>
    <c:when test="${target == 'dashboardNext'}"><c:url value="/dashboard?next&npage=${pagenb}&displayBy=${displayBy}&orderBy=${orderBy}&orderDesc=${orderDesc}&search=${search}"/></c:when>
    <c:when test="${target == 'dashboardOther'}"><c:url value="/dashboard?npage=${pagenb}&displayBy=${displayBy}&orderBy=${orderBy}&orderDesc=${orderDesc}&search=${search}"/></c:when>
    <c:when test="${target == 'dashboardSearch'}"><c:url value="/dashboard?npage=${pagenb}&displayBy=${displayBy}&orderBy=${orderBy}&orderDesc=${orderDesc}"/></c:when>
    <c:when test="${target == 'editComputer'}"><c:url value="/editComputer?computerId="/>${computerId}</c:when>
    <c:when test="${target == 'editComputerPost'}"><c:url value="/editComputer"/></c:when>
    <c:when test="${target == 'addComputer'}"><c:url value="/addComputer"/></c:when>
    <c:when test="${target == 'deleteComputer'}"><c:url value="/deleteComputer"/></c:when>
</c:choose>