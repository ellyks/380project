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

    <h2>Ticket #${ticket.id}: <c:out value="${ticket.subject}" /></h2>
    <security:authorize access="hasRole('ADMIN') or principal.username=='${ticket.customerName}'">            
      [<a href="<c:url value="/ticket/edit/${ticket.id}" />">Edit</a>]
    </security:authorize>
    <security:authorize access="hasRole('ADMIN')">            
      [<a href="<c:url value="/ticket/delete/${ticket.id}" />">Delete</a>]
    </security:authorize>
    <br /><br />
    <i>Item Name - <c:out value="${ticket.customerName}" /></i><br /><br />
    <c:out value="${ticket.body}" /><br /><br />
    Price $ <c:out value="${ticket.price}" /><br /><br />
    <c:if test="${fn:length(ticket.attachments) > 0}">
      Attachments:
      <c:forEach items="${ticket.attachments}" var="attachment"
                 varStatus="status">
        <c:if test="${!status.first}">, </c:if>
        <a href="<c:url value="/ticket/${ticket.id}/attachment/${attachment.name}" />">
          <c:out value="${attachment.name}" /></a>
      </c:forEach><br /><br />
    </c:if>
    <a href="<c:url value="/ticket/addComment/${ticket.id}/" />">Add Comment</a><br /><br />
    <a href="<c:url value="/ticket/bid/${ticket.id}/" />">Bidding</a><br /><br />

    Bidding
    <br />
    <br />
    Current Number of Bids: ${fn:length(bid)}<br />
    <c:forEach items="${bid}" var="bid">
      Price: <c:out value="${bid.price}" />
      User Name: <c:out value="${bid.buyername}" /><br />
    </c:forEach><br /><br />



    Comment List:<br/>
    <ul>
      <c:forEach items="${comment}" var="comment">
        <li><c:out value="${comment.content}" /> (by:<c:out value="${comment.buyername}" />)
          <security:authorize access="hasRole('ADMIN')">            
            [<a href="<c:url value="/ticket/commentdelete/${comment.id}" />">Delete</a>]</li>
          </security:authorize>
        </c:forEach><br />
    </ul>
    <a href="<c:url value="/ticket" />">Return to list tickets</a>
  </body>
</html>