<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/home.css">
    <script src="js/home.js" defer></script>

</head>
<body>

<%--<div class="test-btn">--%>
<%--    <h1>여기 이동 버튼 모아서 사용해주세요!</h1>--%>

<%--    <button onclick="location.href='omikuji'">omikuji</button>--%>
<%--    <button type="button" onclick="location.href='template-list-admin'">template list admin</button>--%>
<%--    <button type="button" onclick="location.href='template-create-admin'">template create admin</button>--%>
<%--    <button type="button" onclick="location.href='template-list'">template list</button>--%>
<%--    <button type="button" onclick="location.href='qr-decode'">PC QR Upload!</button>--%>
<%--    <button onclick="location.href='sent-post'">sent-post</button>--%>
<%--    <button onclick="location.href='reservation'">전송서비스</button>--%>
<%--    <button onclick="location.href='inquiry'">inquiry</button>--%>
<%--</div>--%>

<div class="donguri-ani" id="scene">
    <img id="acorn" class="dong" src="image/acon.png" alt="main donguri"/>

    <div id="bottomGroup" class="bottom-group">
        <img id="postbox" class="postbox" src="image/postbox.png" alt="postbox"/>
        <img class="falling-item cone-1" src="image/cone_1.png" alt="cone 1"/>
        <img class="falling-item cone-2" src="image/cone_2.png" alt="cone 2"/>
        <img class="falling-item cone-3" src="image/cone_3.png" alt="cone 3"/>
        <img class="falling-item cone-4" src="image/cone_4.png" alt="cone 4"/>
        <img class="falling-item cone-5" src="image/cone_5.png" alt="cone 5"/>
    </div>

    <div id="cloudLayer">
        <img class="cloud" src="image/cloud_1.png"/>
        <img class="cloud" src="image/cloud_2.png"/>
        <img class="cloud" src="image/cloud_3.png"/>
        <img class="cloud" src="image/cloud_4.png"/>
        <img class="cloud" src="image/cloud_5.png"/>
        <img class="cloud" src="image/cloud_6.png"/>
        <img class="cloud" src="image/cloud_7.png"/>
        <img class="cloud" src="image/cloud_8.png"/>
        <img class="cloud" src="image/cloud_9.png"/>
    </div>

    <img id="textImage" src="image/text.png" alt="DONGURI YU BIN"/>
</div>
<%--<div class="scene" id="scene">--%>
<%--    <div class="acorn" id="acorn">🌰</div>--%>
<%--    <div class="bottom-group" id="bottomGroup">--%>
<%--        <div class="mailbox">📮</div>--%>
<%--        <div class="ground"></div>--%>
<%--    </div>--%>
<%--</div>--%>

</body>
</html>