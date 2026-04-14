<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Title</title>
    <link rel="stylesheet" href="css/_post.css">
</head>
<body>
<div>
    <div class="post-wrap">

        <div class="img-envelope" id="img-card">
            <%-- img src 가데이터. 수정 필요 --%>
            <%-- ${post.coverImgUrl} --%>
            <div class="image-area">
                <img
                        src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSlRqzE_9fEQS3P6LZf4rMGe2jIeHQ2ifbXdg&s"
                        style="width: 100%;">
            </div>
        </div>

        <div class="cover">
            <div class="cover-front">편지를 열어보세요</div>
            <div class="cover-back">
                <p class="cover-subject">“ ${post.subject} ”</p>
                <p class="cover-content">${post.content}</p>
                <p class="cover-date">${post.scheduledDate}</p>
            </div>
        </div>

        <div class="post-envelope" id="postcard">
            <div class="top-flap"></div>

            <svg class="part left-flap" viewBox="0 0 150 230" preserveAspectRatio="none">
                <path d="
                    M 140 20
                    L 70 40
                    L 26 56
                    Q 20 60 20 68
                    L 20 162
                    Q 20 170 26 174
                    L 70 190
                    L 140 210
                    L 140 20
                    Z
                "/>
            </svg>

            <svg class="part right-flap" viewBox="0 0 150 230" preserveAspectRatio="none">
                <path d="
                    M 10 20
                    L 10 210
                    L 80 190
                    L 124 174
                    Q 130 170 130 162
                    L 130 68
                    Q 130 60 124 56
                    L 80 40
                    L 10 20
                    Z
                "/>
            </svg>
        </div>

    </div>
</div>

<script>
    const postWrap = document.querySelector(".post-wrap");

    postWrap.addEventListener("click", function () {
        postWrap.classList.toggle("open");
    });
</script>
</body>
</html>