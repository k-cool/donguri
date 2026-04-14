<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="css/confirm.css"/>

<div class="confirm">

    <div class="confirm-wrapper">
        <h2 class="title">📬 예약 내용 확인 📬</h2>

        <div class="confirm-container">


            <div class="post-container">

                <svg class="top-cover"
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
                          fill="#f4cfd2"
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


                <svg class="wing left"
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
                          fill="#f4cfd2"
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


                <svg class="wing right"
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
                          fill="#f4cfd2"
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


                <div class=" post-square post-top">
                    <div class="texture"></div>

                    <div class="img-wrapper">
                        <img src="https://donguri-dev.s3.ap-northeast-2.amazonaws.com/ui_asset/template/template_sakura_img.png"
                             alt="cover-img"/>
                    </div>

                    <div class="stamp">
                        <img src="image/template_post.svg" alt="stamp"/>
                    </div>

                </div>

                <div class="post-square post-bottom">
                    <div class="texture"></div>

                    <div class="row">
                        <div class="label">받은 사람</div>
                        <div class="data">user1@naver.com</div>
                    </div>

                    <div class="row">
                        <div class="label">제&nbsp&nbsp&nbsp&nbsp&nbsp목</div>
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

    <!-- 모달 -->

    <form action="reservation" method="post">
        <input type="hidden" name="action" value="insert">
        <div id="modal" class="modal-overlay">
            <div class="modal-box">
                <p class="modal-text">
                    📬 이 추억은 <strong>2026-04-09 12:00:00</strong>에 배달될 예정이야.
                </p>

                <div class="modal-actions">
                    <a class="btn edit-btn" href="reservation?action=write">
                        <div>✏️ 수정하고 싶어요!</div>
                    </a>
                    <button class="btn plant-btn" type="submit">🍀 도토리 심기</button>
                </div>

            </div>
        </div>
    </form>

</div>
