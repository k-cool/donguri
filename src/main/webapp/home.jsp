<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>Home page~!!~!</h2>

<c:forEach items="${userList}" var="user">
    <div>
        <div>${user.userId}</div>
        <div>${user.name}</div>
        <div>${user.email}</div>
        <div>${user.password}</div>
    </div>
</c:forEach>

<div>
    <button type="button" onclick="location.href='template-list'">template list</button>
</div>
<div>
    <button type="button" onclick="location.href='template-user'">User's template list</button>
</div>


</body>
</html>
