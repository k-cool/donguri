<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

    <link rel="stylesheet" href="css/omikuji.css">

    <script src="https://code.jquery.com/jquery-3.7.1.js"
            integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>

    <script src="js/omikuji.js" defer></script>

</head>
<body>

<div>

    <div id="tree">🌳</div>

    <c:if test="${isOmikujiAvailable}">
        <div id="acorn">🌰</div>
        <div id="squirrel">🐿️</div>
    </c:if>

    <c:if test="${!isOmikujiAvailable}">
        <div id="guide-text" style="display: <%=(boolean)request.getAttribute("isOmikujiAvailable") ?"none" :"block"%>">
            <h2>오늘은 다람쥐가 쉬러갔어요. 내일 다시 만나요!</h2>
        </div>
    </c:if>


    <div id="modal-overlay">
        <div id="memo-modal">
            <h2>🐿️ 다람쥐의 쪽지</h2>

            <div id="luck"></div>
            <div id="message"></div>
            <div class="close-btn" onclick="closeModal()">닫기</div>
        </div>
    </div>


</div>
</body>
</html>
