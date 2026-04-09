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

<%
    System.out.println("main.jsp: " + request.getAttribute("cb"));
%>

<%--<c:if test="${cb != null}">--%>

<%--    <div>${cb}</div>--%>

<%--    <script>--%>
<%--        location.href = "${cb}";--%>
<%--    </script>--%>
<%--    --%>
<%--</c:if>--%>

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

</div>
</body>
</html>