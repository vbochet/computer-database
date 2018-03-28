<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib tagdir = "/WEB-INF/tags" prefix = "t" %>

<%@ attribute name="currentPage" required="true" %>
<%@ attribute name="maxPage" required="true" %>
<%@ attribute name="displayBy" required="true" %>
<%@ attribute name="orderBy" required="true" %>
<%@ attribute name="orderDesc" required="true" %>
<%@ attribute name="search" required="true" %>

<ul class="pagination">
    <li>
    <c:if test="${currentPage > 1}">
        <a href="<t:links target='dashboardPrev' pagenb='${currentPage}' displayBy='${displayBy}' orderBy='${orderBy}' orderDesc='${orderDesc}' search='${search}' />" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
        </a>
    </c:if>
    <c:if test="${currentPage <= 1}">
        <a aria-label="Previous" class="disabled" style="color:grey;">
            <span aria-hidden="true">&laquo;</span>
        </a>
    </c:if>
    </li>

    <c:if test="${currentPage <= 3}">
        <c:forEach var="i" begin="1" end="5" step="1">
            <c:if test="${i <= maxPage}">
                <li>
                <c:if test="${i == currentPage}"><a class="disabled" style="text-decoration:underline;color:grey;">${i}</a></c:if>
                <c:if test="${i != currentPage}"><a href="<t:links target='dashboardOther' pagenb='${i}' displayBy='${displayBy}' orderBy='${orderBy}' orderDesc='${orderDesc}' search='${search}' />">${i}</a></c:if>
                </li>
            </c:if>
        </c:forEach>
    </c:if>
    <c:if test="${currentPage > 3}">
        <c:forEach var="i" begin="${currentPage-2}" end="${currentPage+2}" step="1">
            <c:if test="${i <= maxPage}">
                <li>
                <c:if test="${i == currentPage}"><a class="disabled" style="text-decoration:underline;color:grey;">${i}</a></c:if>
                <c:if test="${i != currentPage}"><a href="<t:links target='dashboardOther' pagenb='${i}' displayBy='${displayBy}' orderBy='${orderBy}' orderDesc='${orderDesc}' search='${search}' />">${i}</a></c:if>
                </li>
            </c:if>
        </c:forEach>
    </c:if>
    
    <li>
        
    <a href="<t:links target='dashboardNext' pagenb='${currentPage}' displayBy='${displayBy}' orderBy='${orderBy}' orderDesc='${orderDesc}' search='${search}' />" aria-label="Next">
        <span aria-hidden="true">&raquo;</span>
    </a>
    </li>
</ul>