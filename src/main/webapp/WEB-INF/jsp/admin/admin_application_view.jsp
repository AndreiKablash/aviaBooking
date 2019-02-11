<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Admin page</title></head>
    <body>
    <h3>Hello!</h3>
    <hr/>
    ${admin}, hello!
    <hr/>
    <a href="controller?command=logout">Logout</a><hr/>
    <a href="controller?command=ADMIN_VIEW_USERS">ViewAllUsers</a><hr/>
</body></html>