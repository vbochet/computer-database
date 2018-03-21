<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<%@ attribute name="currentPage" required="true" %>
<%@ attribute name="maxPage" required="true" %>

<ul class="pagination">
    <li>
        <a href="?prev" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
        </a>
    </li>

    <c:if test="${currentPage <= 3}">
        <c:forEach var="i" begin="1" end="5" step="1">
            <c:if test="${i <= maxPage}">
                <li><a href="?npage=${i}">
                    <c:if test="${i == currentPage}"><u>${i}</u></c:if>
                    <c:if test="${i != currentPage}">${i}</c:if>
                </a></li>
            </c:if>
        </c:forEach>
    </c:if>
    <c:if test="${currentPage > 3}">
        <c:forEach var="i" begin="${currentPage-2}" end="${currentPage+2}" step="1">
            <c:if test="${i <= maxPage}">
                <li><a href="?npage=${i}">
                <c:if test="${i == currentPage}"><u>${i}</u></c:if>
                <c:if test="${i != currentPage}">${i}</c:if>
                </a></li>
            </c:if>
        </c:forEach>
    </c:if>
    
    <li>
        <a href="?next" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
        </a>
    </li>
</ul>