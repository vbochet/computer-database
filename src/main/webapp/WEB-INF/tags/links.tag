<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ attribute name="target" required="true" %>
<%@ attribute name="append" required="true" %>
<c:choose>
    <c:when test="${target == 'root'}"><c:url value="/"/>${append}</c:when>
    <c:when test="${target == 'css'}"><c:url value="/static/css/"/>${append}</c:when>
    <c:when test="${target == 'js'}"><c:url value="/static/js/"/>${append}</c:when>
</c:choose>