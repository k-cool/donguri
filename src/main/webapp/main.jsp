<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <link href="css/reset.css" rel="stylesheet">
    <link href="css/main.css" rel="stylesheet">
    <script src="js/main.js"></script>
</head>

<body>


<div class="container">

    <div class="content">
        <jsp:include page="${content}"/>
    </div>


</div>
</body>
</html>