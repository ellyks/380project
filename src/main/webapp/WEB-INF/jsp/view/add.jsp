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

        <h2>Create a Bidding</h2>
        <form:form method="POST" enctype="multipart/form-data" modelAttribute="ticketForm">

            <form:label path="subject">Item Name</form:label><br/>
            <form:input type="text" path="subject" /><br/><br/>
            <form:label path="body">Description</form:label><br/>
            <form:textarea path="body" rows="5" cols="30" /><br/><br/>
            <form:label path="price">Price</form:label><br/>
            <form:input type="number" path="Price" min="0"/><br/><br/>
            <b>Attachments</b><br/>
            <input type="file" name="attachments" multiple="multiple"/><br/><br/>
            <input type="submit" value="Submit"/>
        </form:form>
    </body>
</html>
