<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <form:form method="POST" enctype="multipart/form-data"
                   modelAttribute="UserForm">   
            <form:label path="username">User Name</form:label><br/>
            <form:input type="text" path="username" /><br/><br/>
            <form:label path="password">Password</form:label><br/>
            <form:input type="text" path="password" /><br/><br/>

            <form:label path="roles">Roles</form:label><br/>
            <form:checkbox path="roles" value="ROLE_USER" />ROLE_USER
            <form:checkbox path="roles" value="ROLE_ADMIN" />ROLE_ADMIN

            <input type="submit" value="Save"/><br/><br/>
        </form:form>
    </body>
</html>