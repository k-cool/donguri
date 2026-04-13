<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>예약 내용 확인</title>
    <link rel="stylesheet" href="css/confirm.css"/>
</head>
<body>

<div class="confirm-wrapper">

    <div class="confirm-container">
        <h2>📬 예약 내용 확인 📬</h2>


        <div class="post-container">

            <div class="top-cover"></div>

            <div class="wing left"></div>
            <div class="wing right"></div>

            <div class="post-square post-top">

                <%--                <div class="texture"></div>--%>


                <div class="img-wrapper">
                    <img src="https://cdn.assets.lomography.com/86/93d57e0bd8e88f6890c1687803700ab45f3007/576x576x2.jpg?auth=fb4474f73f10307f800cfe75a5a7052702f6d316"
                         alt="cover-img"/>
                </div>

                <div class="stamp">
                    <img src="image/template_post.svg" alt="stamp"/>
                </div>

            </div>

            <div class="post-square post-bottom">
                <div class="row">
                    <div class="label">받은 사람</div>
                    <div class="data">user1@naver.com</div>
                </div>
                <div class="row">
                    <div class="label">제목</div>
                    <div class="data">"둘레로 같이 벚꽃 볼래?"</div>
                </div>

                <div class="content">
                    <span>테스트 텍스트</span>

                    <span>와우와우</span>
                </div>

                <div class="row">
                    <div class="date">2026.04.07</div>
                </div>
            </div>
        </div>

    </div>
</div>

</body>
</html>