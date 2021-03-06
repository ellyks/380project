<!DOCTYPE html>
<html>
    <head>
        <title>Online Bidding</title>
    </head>
    <body>
        <security:authorize access="isAuthenticated()">
            <c:url var="logoutUrl" value="/logout"/>
            <form action="${logoutUrl}" method="post">
                <input type="submit" value="Log out" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </security:authorize>

        <security:authorize access="isAnonymous()">
            <i>You are not login yet. Login or register.</i>
            <c:url var="loginUrl" value="/login"/>
            <form action="${loginUrl}" method="post">
                <input type="submit" value="Log in" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
            <c:url var="registerUrl" value="/ticket/register"/>
            <a href="/380project/ticket/register">register</a>
        </security:authorize>

        <h2>Item #${ticket.id}: <c:out value="${ticket.subject}" /></h2>
        <security:authorize access="hasRole('ADMIN') or 
                            isAuthenticated() and principal.username=='${ticket.customerName}'">
            [<a href="<c:url value="/ticket/edit/${ticket.id}" />">Edit</a>]
        </security:authorize>
        <security:authorize access="hasRole('ADMIN')">            
            [<a href="<c:url value="/ticket/delete/${ticket.id}" />">Delete</a>]
        </security:authorize>

        <br /><br />
        <b>Owner Name: <c:out value="${ticket.customerName}" /></b><br /><br />
        Item Description: <c:out value="${ticket.body}" /><br /><br />
        Expected Price: $ <c:out value="${ticket.price}" /><br /><br />
        <c:if test="${fn:length(ticket.attachments) > 0}">
            Photos: <br />
            <c:forEach items="${ticket.attachments}" var="attachment"
                       varStatus="status">
                <c:if test="${!status.first}"></c:if>
                <img src="<c:url value="/ticket/${ticket.id}/attachment/${attachment.name}" />" height="100" width="100">
                <c:out value="${attachment.name}" /></a><br />
        </c:forEach><br /><br />
    </c:if>

    <security:authorize access="isAuthenticated()&&principal.username!='${ticket.customerName}'">
        <a href="<c:url value="/ticket/bid/${ticket.id}/" />">Bid this item</a><br />
    </security:authorize>    
    
    Current Number of Bids: ${fn:length(bid)}<br />
        Current Status of Bid: 
        <c:choose>
            <c:when test="${ticket.status}">
                Open<br>
                <security:authorize access="hasRole('ADMIN') or 
                            isAuthenticated() and principal.username=='${ticket.customerName}'">
                    <a href="<c:url value="/ticket/endbid/${ticket.id}/" />">End the Bidding with no winner</a>
                    /<br>
                    <a href="<c:url value="/ticket/endbidw/${ticket.id}/" />">End the Bidding</a><br>
                </security:authorize>  

            </c:when>
            <c:otherwise>
                Close
                <br>
                <c:choose>
                    <c:when test="${not empty ticket.winnername}">
                        Bid ended. The winner is  <c:out value="${ticket.winnername}" />
                        <br>
                    </c:when>
                    <c:otherwise>
                        Bid ended with no winner. 
                        <br>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
      
    Bidding List:<br />
    <ul>
        <c:forEach items="${bid}" var="bid">
            <li>Price: $<c:out value="${bid.price}" />, User Name: <c:out value="${bid.buyername}" /></li>
        </c:forEach>
    </ul>
    <br>
    
    <security:authorize access="isAuthenticated()">
        <a href="<c:url value="/ticket/addComment/${ticket.id}/" />">Add Comment</a><br />
    </security:authorize>    

    Comment List:<br/>
    <ul>
        <c:forEach items="${comment}" var="comment">
            <li><c:out value="${comment.content}" /> (by:<c:out value="${comment.buyername}" />)
                <security:authorize access="hasRole('ADMIN')">            
                    [<a href="<c:url value="/ticket/commentdelete/${comment.id}" />">Delete</a>]</li>
                </security:authorize>
            </c:forEach>
    </ul>
    <a href="<c:url value="/ticket" />">Return to list item(s)</a>
</body>
</html>