<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<%@ attribute name="currentPage" required="true" %>
<%@ attribute name="maxPage" required="true" %>

<ul class="pagination">
    <li>
    <c:if test="${currentPage > 1}">
        <a href="?prev" aria-label="Previous">
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
                <c:if test="${i != currentPage}"><a href="?npage=${i}">${i}</a></c:if>
                </li>
            </c:if>
        </c:forEach>
    </c:if>
    <c:if test="${currentPage > 3}">
        <c:forEach var="i" begin="${currentPage-2}" end="${currentPage+2}" step="1">
            <c:if test="${i <= maxPage}">
                <li>
                <c:if test="${i == currentPage}"><a class="disabled" style="text-decoration:underline;color:grey;">${i}</a></c:if>
                <c:if test="${i != currentPage}"><a href="?npage=${i}">${i}</a></c:if>
                </li>
            </c:if>
        </c:forEach>
    </c:if>
    
    <li>
        
    <c:if test="${currentPage < maxPage}">
        <a href="?next" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
        </a>
    </c:if>
    <c:if test="${currentPage >= maxPage}">
        <a aria-label="Next" class="disabled" style="color:grey;">
            <span aria-hidden="true">&raquo;</span>
        </a>
    </c:if>
    </li>
</ul>