<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html><head><title>User page</title></head>

<body>
    <%
        String name = session.getAttribute("sessionName").toString();
    %>
    <h3>Welcome</h3>
    <hr/>
         ${requestScope.user}, hello!
    <hr/>

    <a href="controller?command=logout">Logout</a>
</body></html>
