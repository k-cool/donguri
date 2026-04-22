<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Title</title>
    <link rel="stylesheet" href="css/post.css">
    <script src="js/post.js" defer></script>
</head>


<div class="post">

    <div class="confirm-wrapper">

        <div class="confirm-container">


            <div class="post-container">

                <svg class="top-cover openable closed"
                     xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 0 333.98 148"
                     preserveAspectRatio="none"
                     style="display: block;">

                    <defs>
                        <clipPath id="clip-top">
                            <path d="M333.85,147.69
                                     l-6.61-61.85
                                     c-1.04-9.7-7.11-18.14-15.98-22.22
                                     L178.48,2.65
                                     c-7.33-3.37-15.77-3.37-23.1,0
                                     L22.59,63.62
                                     c-8.87,4.07-14.94,12.51-15.97,22.22
                                     L0,147.69
                                     H0
                                     l333.86.19
                                     v-.19
                                     h0Z"/>
                        </clipPath>
                    </defs>

                    <!-- 아래: 색상 -->
                    <path d="M333.85,147.69
                             l-6.61-61.85
                             c-1.04-9.7-7.11-18.14-15.98-22.22
                             L178.48,2.65
                             c-7.33-3.37-15.77-3.37-23.1,0
                             L22.59,63.62
                             c-8.87,4.07-14.94,12.51-15.97,22.22
                             L0,147.69
                             H0
                             l333.86.19
                             v-.19
                             h0Z"
                          fill="#${post.bgColor}"
                          clip-path="url(#clip-top)"
                    />

                    <!-- 위: 텍스처 -->
                    <image href="/image/template_texture.png"
                           x="0"
                           y="0"
                           width="333.98"
                           height="148"
                           preserveAspectRatio="xMidYMid slice"
                           clip-path="url(#clip-top)"
                    />

                </svg>

                <svg class="wing left openable closed"
                     viewBox="20 20 120 190"
                     preserveAspectRatio="none"
                     style="display: block;">

                    <defs>
                        <clipPath id="clip-left">
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
                        </clipPath>
                    </defs>

                    <!-- 1️⃣ 아래: 색상 -->
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
    "
                          fill="#${post.bgColor}"
                          clip-path="url(#clip-left)"
                    />

                    <!-- 2️⃣ 위: 텍스처 -->
                    <image href="/image/template_texture.png"
                           x="20" y="20"
                           width="120" height="190"
                           preserveAspectRatio="xMidYMid slice"
                           clip-path="url(#clip-left)"
                    />

                </svg>

                <svg class="wing right openable closed"
                     viewBox="10 20 120 190"
                     preserveAspectRatio="none"
                     style="display: block;">

                    <defs>
                        <clipPath id="clip-right">
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
                        </clipPath>
                    </defs>

                    <!-- 1️⃣ 아래: 색상 -->
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
    "
                          fill="#${post.bgColor}"
                          clip-path="url(#clip-right)"
                    />

                    <!-- 2️⃣ 위: 텍스처 -->
                    <image href="/image/template_texture.png"
                           x="10"
                           y="20"
                           width="120"
                           height="190"
                           preserveAspectRatio="xMidYMid slice"
                           clip-path="url(#clip-right)"
                    />

                </svg>

                <div class="post-square post-top" style="background-color: \#${post.bgColor}">
                    <div class="texture"></div>

                    <div class="img-wrapper">
                        <img src="${post.coverImgUrl}"
                             alt="cover-img"/>
                    </div>


                </div>

                <div class="post-bottom openable closed" style="background-color: \#${post.bgColor}">
                    <div class="pattern"></div>
                    <div class="texture"></div>

                    <div class="row">
                        <div class="label">받은 사람</div>
                        <div class="data">${post.recipientEmail}</div>
                    </div>

                    <div class="row">
                        <div class="label">제&nbsp&nbsp&nbsp&nbsp&nbsp목</div>
                        <div class="data">${post.subject}</div>
                    </div>

                    <div class="content">
                        <div class="content-text">${post.content}</div>

                        <%--                        <span>${post.content}</span>--%>
                    </div>

                    <div class="row">
                        <div class="date">${post.scheduledDate}</div>
                    </div>
                </div>

                <div class="clickable">
                    <div class="click-guide">
                        <div class="icon">♥️</div>
                        <div class="text">클릭해주세요!</div>
                    </div>


                </div>

                <div class="stamp">
                    <img src="image/template_post.svg" alt="stamp"/>
                </div>

                <div class="player">
                    <audio id="bgmPlayer" src="${post.bgmUrl}"></audio>

                    <button id="bgmToggle">
                        <img id="bgmTogglePlay" class="bgmToggleImg" src="/image/music_play_button.png" alt="bgmBtn"/>
                        <img id="bgmToggleStop" class="bgmToggleImg" src="/image/music_stop_button.png" alt="bgmBtn"/>
                    </button>
                </div>
            </div>
        </div>


    </div>

</div>


