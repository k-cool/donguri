<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/index.css">
</head>
<body>
<div>
    ${post.renderedHtml}

    <div class="wrapper">

        <div class="envelope" id="envelope">

            <div class="envelope-body">

                <!-- 닫힌 상태에서 보이는 앞면 -->
                <div class="cover">
                    📩 편지를 열어보세요
                </div>


                <div class="wing left"></div>
                <div class="wing right"></div>
                <div class="top-flap"></div>

                <div class="letter">
                    <div class="stamp">どんぐり</div>

                    <div class="image-area"></div>

                    <div class="content">
                        <div class="field">
                            <label>받는 사람</label>
                            <input type="text" value="user1@naver.com"/>
                        </div>

                        <div class="field">
                            <label>제목</label>
                            <input type="text" value="올해도 같이 벚꽃 볼래?"/>
                        </div>

                        <div class="message">
                            <p>벌써 봄은 왔는데도 아직은 쌀쌀하더라.</p>
                            <p>네 생각이 나서 그냥 편지를 끄적여봤어.</p>
                            <p>꽃 보니까 같이 걷던 길도 생각나고, 그래서</p>
                            <p>바쁘더라도 가끔은 봄도 좀 느끼고 살자.</p>
                            <p>조만간 꼭 다 같이 모여 얼굴 한번 보자.</p>
                        </div>

                        <div class="date">2026.04.07</div>
                    </div>
                </div>

            </div>

        </div>
    </div>


    <script>
        const envelope = document.getElementById('envelope');

        envelope.addEventListener('click', () => {
            envelope.classList.toggle('open');
        });
    </script>

</div>
</body>
</html>
