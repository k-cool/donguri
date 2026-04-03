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

<c:if test="${empty sessionScope.user}">
    <div class="nav-menu">
        <button type="button" class="btn-signup" onclick="location.href='signup-do'">
            회원가입
        </button>

        <button type="button" class="btn-login" onclick="location.href='login'">
            로그인
        </button>
    </div>
</c:if>

</body>
</html>