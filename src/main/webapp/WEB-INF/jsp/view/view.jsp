<!DOCTYPE html>
<html>
    <head>
        <title>Online Bidding</title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>Bidding #${ticketId}: <c:out value="${ticket.subject}" /></h2>
        <security:authorize access="hasRole('ADMIN') or principal.username=='${ticket.customerName}'">
            [<a href="<c:url value="/ticket/edit/${ticketId}" />">Edit</a>]
        </security:authorize>
        <security:authorize access="hasRole('ADMIN')">
            [<a href="<c:url value="/ticket/delete/${ticketId}" />">Delete</a>]
        </security:authorize>
        <br /><br />

        Description: <c:out value="${ticket.body}" /><br /><br />

        Expected Price: $<c:out value="${ticket.price}" /><br /><br />

        <c:if test="${ticket.numberOfAttachments > 0}">
            Attachments:
            <c:forEach items="${ticket.attachments}" var="attachment" varStatus="status">
                <c:if test="${!status.first}">, </c:if>
                <a href="<c:url value="/ticket/${ticketId}/attachment/${attachment.name}" />">
                    <c:out value="${attachment.name}" /></a>
            </c:forEach><br /><br />
        </c:if>

        <i>Item Owner Name - <c:out value="${ticket.customerName}" /></i><br /><br />
        <a href="<c:url value="/ticket" />">Return to list of Bidding</a>
    </body>
</html>
