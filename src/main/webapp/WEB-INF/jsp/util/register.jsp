<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
              pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Registration Form</title>
</head>
<body>
<h1>Register Form</h1>
<form name="registrationForm"action="controller" method="post">
    <input type="hidden" name="command" value="registration"/>
    <h3> Go to
        <a href="index">
            Main page
        </a>
    </h3>
    <table style="with: 30%">
        <tr>
            <td>Name</td>
            <td><input type="text" name="name" /></td>
        </tr>
        <tr>
            <td>Surname</td>
            <td><input type="text" name="surname" /></td>
        </tr>
        <tr>
            <td>Document ID</td>
            <td><input type="text" name="documentId" /></td>
        </tr>
        <tr>
            <td>Email</td>
            <td><input type="text" name="email" /></td>
        </tr>
        <tr>
            <td>Login</td>
            <td><input type="text" name="login" /></td>
        </tr>
        <tr>
            <td>Password</td>
            <td><input type="text" name="password" /></td>
        </tr>
        <tr>
            <td>Phone number</td>
            <td><input type="text" name="phone" /></td>
        </tr>
    </table><hr/>
    <br/>
    ${errorRegistrationPassMessage}
    <br/>
    ${wrongAction}
    <br/>
    ${nullPage}
    <br/>
    <input type="submit" value="registration" />
</form>
</body>
</html>