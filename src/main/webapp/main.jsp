<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

        <div class="nav-menu">
            <button type="button" class="btn-signup" onclick="location.href='signup-do'">
                회원가입
            </button>
        </div>
    </header>

    <div class="content">
        <jsp:include page="${content}"/>
    </div>
</div>
</body>
</html>