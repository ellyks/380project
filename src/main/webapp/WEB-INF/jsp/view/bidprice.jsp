<!DOCTYPE html>
<html>
    <head>
        <title>Bidding Price</title>
    </head>
    <body>
        

        <h2>Bidding Price</h2>
        <form:form method="POST" enctype="multipart/form-data" modelAttribute="bidForm">

         
            <form:label path="price">Price</form:label><br/>
            <form:input type="number" path="Price" min="0"/><br/><br/>
         
            <input type="submit" value="Submit"/>
        </form:form>
    </body>
</html>
