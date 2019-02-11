<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Admin view users page</title></head>
    <body>
    <h3>Users list</h3>
    <hr/>
    ${admin}, hello!
    <hr/>
    <a href="adminPageViewt">Go to admin page</a><hr/>

    <c:forEach items="${request.getAttributes(\"userList\")}" var="element">
        <tr>
            <td>
                <p>${element.name}</p>
            </td>
            <td>
                <p>${element.surname}</p>
            </td>
            <td>
                <p>${element.documentId}</p>
            </td>
            <td>
                <p>${element.email}</p>
            </td>
            <td>
                <p>${element.login}</p>
            </td>
            <td>
                <p>${element.phone}</p>
            </td>
        </tr>
    </c:forEach>
</body></html>