<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Donguri - Main</title>
    <link href="css/reset.css" rel="stylesheet">
    <link href="css/main.css" rel="stylesheet">
    <script src="js/main.js"></script>

</head>

<body>
<header class="mn-header">
    <div class="logo">
        <h1>header</h1>
    </div>

    <div class="user-auth-area">
        <c:if test="${not empty loginPage}">
            <jsp:include page="${loginPage}"/>
        </c:if>
        <c:if test="${empty loginPage and not empty sessionScope.user}">
            <jsp:include page="jsp/user/login_ok.jsp"/>
        </c:if>
    </div>
</header>

<div class="mn-content">
    <jsp:include page="${content}"/>
</div>

</body>
</html>