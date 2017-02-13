<!-- Editor:Luming Wu, but there is a 406 error, so I decided to do it pure text/html. -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:forEach var="todo" items="${todos}" varStatus="loop">
    <tr>
        <td>${todo.category}</td>
        <td>${todo.description}</td>
        <td>${todo.startDate}</td>
        <td>${todo.endDate}</td>
        <td>${todo.complete}</td>
        <input type="hidden" value="${loop.index}">
    </tr>
</c:forEach>