<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Donguri - Main</title>
    <link href="css/reset.css" rel="stylesheet">
    <link href="css/main.css" rel="stylesheet">
    <script src="js/main.js"></script>
</head>

<body>
<div class="container">
    <header>
        <div class="logo">
            <h1>header</h1>
        </div>

        <div class="user-auth-area">
            <c:if test="${not empty sessionScope.user}">
                <jsp:include page="jsp/user/login_ok.jsp"/>
            </c:if>
        </div>
    </header>

    <div class="content">
        <jsp:include page="${not empty content ? content : 'home.jsp'}"/>
    </div>

    <button onclick="location.href='reservation?action=main'">
        전송서비스
    </button>
</div>
</body>
</html>