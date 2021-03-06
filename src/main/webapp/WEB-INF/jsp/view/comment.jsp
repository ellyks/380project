<!DOCTYPE html>
<html>
    <head>
        <title>Comment</title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>Create a Comment</h2>
        <form:form method="POST" enctype="multipart/form-data" modelAttribute="commentForm">

            <form:label path="content">Comment</form:label><br/>
            <form:textarea path="content" rows="5" cols="30" /><br/><br/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="submit" value="Submit"/>
        </form:form>
    </body>
</html>
