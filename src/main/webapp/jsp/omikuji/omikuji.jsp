<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

    <link rel="stylesheet" href="css/omikuji.css">

    <script src="https://code.jquery.com/jquery-3.7.1.js"
            integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>

    <script src="/js/omikuji.js" defer></script>

</head>
<body>
<div class="omikuji-container">
    <div id="tree"><img src="../../image/tree.png"></div>

    <c:if test="${isOmikujiAvailable}">
        <div id="squirrel">
            <img src="../../image/squirrel_go.png" id="squirrel_go">
            <img src="../../image/squirrel_come.png" id="squirrel_come">
        </div>
    </c:if>

    <c:if test="${!isOmikujiAvailable}">
        <div id="guide-text">
            <img src="../../image/memo.png" class="memo-bg">
            <h2>{ 오늘은 🐿️가 쉬러갔어요. <br> 내일 다시 만나요!🍀 }</h2>
        </div>
    </c:if>

    <div id="modal-overlay">
        <div id="memo-modal">
            <img src="../../image/memo.png" class="memo-bg">
            <div class="memo-content">
                <h2>{ 오늘의 운세🍀 }</h2>
                <div id="luck"></div>
                <div id="message"></div>
                <div class="close-btn" onclick="closeModal()">닫기</div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
