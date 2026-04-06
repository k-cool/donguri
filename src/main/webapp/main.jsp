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
  <header>
      <h1>header</h1>
  </header>

    <div class="content">
        <jsp:include page="${content}"/>
    </div>

    <button onclick="location.href='reservation?action=main'">
        전송서비스
    </button>
</div>
</body>
</html>