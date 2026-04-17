/*
 * 메뉴 클릭시 active 클래스 토글 이벤트
 */
// const menuList = document.querySelectorAll(".hd-main-nav-btn");
//
// menuList.forEach(menu => {
//
//     menu.addEventListener("click", () => {
//         menuList.forEach(m => m.classList.remove("active"));
//         menu.classList.add("active");
//     });
//
// });

window.addEventListener("DOMContentLoaded", () => {
    const menuList = document.querySelectorAll(".hd-main-nav-btn");
    const currentPath = window.location.pathname;

    menuList.forEach(menu => {
        const menuLink = menu.getAttribute("href");

        if (currentPath.includes(menuLink)) {
            menu.classList.add("active");
        }
    });
});
$(function () {
    let menuVisible = false;

    $("#hd-menuBtn").click(function () {
        if (menuVisible) {
            // 🚨 메뉴 숨길 때 완전히 화면 밖으로 밀어냄 (-350px)
            $("#hd-slideMenu").css("right", "-350px");
        } else {
            $("#hd-slideMenu").css("right", "0px");
        }
        menuVisible = !menuVisible;
    });

    $(document).click(function (event) {
        if (menuVisible && !$(event.target).closest('#hd-slideMenu').length && !$(event.target).closest('#hd-menuBtn').length) {
            // 🚨 바깥 클릭해서 숨길 때도 완전히 밀어냄 (-350px)
            $("#hd-slideMenu").css("right", "-350px");
            menuVisible = false;
        }
    });
});

// $(document).ready(function () {
//     var currentPath = window.location.pathname;
//
//     // 🚨 1. 로그인 또는 회원가입 페이지: 2층 바 숨김 + 우측 햄버거/버튼 모두 숨김
//     if (currentPath.includes('login') || currentPath.includes('signup')) {
//         $('.hd-header-bottom').hide();
//         $('.hd-header-right').hide(); // 우측 메뉴 숨김
//         $('header').addClass('hd-sub-header');
//     }
//     // 🚨 2. 메인 페이지('/main')가 아닌 다른 서비스 페이지: 2층 바만 숨김! (햄버거 메뉴는 살아있음)
//     else if (currentPath !== '/main' && currentPath !== '/') {
//         $('.hd-header-bottom').hide(); // 2층 민트색 바 숨김
//         $('header').addClass('hd-sub-header');
//         // 주의: 여기에 $('.hd-header-right').hide(); 를 넣지 않았으므로 햄버거 메뉴는 잘 보입니다.
//     }
//
//     // --- 탭 활성화 로직 ---
//     $('.hd-nav-menu a').on('click', function () {
//         $('.hd-nav-menu a').removeClass('hd-active');
//         $(this).addClass('hd-active');
//     });
//
//     $('.hd-nav-menu a').each(function () {
//         var linkHref = $(this).attr('href');
//         if (linkHref !== "#" && currentPath.includes(linkHref)) {
//             $('.hd-nav-menu a').removeClass('hd-active');
//             $(this).addClass('hd-active');
//         }
//     });
// });
