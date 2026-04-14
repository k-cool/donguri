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

$(document).ready(function () {
    var currentPath = window.location.pathname;

    // 로그인 또는 회원가입 페이지일 때 헤더 하단과 우측 버튼 숨기기
    if (currentPath.includes('login') || currentPath.includes('signup')) {
        $('.hd-header-bottom').hide();
        $('.hd-header-right').hide();
        $('header').addClass('hd-sub-header');
    } else if (currentPath !== '/main' && currentPath !== '/') {
        $('.hd-header-bottom').hide();
        $('header').addClass('hd-sub-header');
    }

    $('.hd-nav-menu a').on('click', function () {
        $('.hd-nav-menu a').removeClass('hd-active');
        $(this).addClass('hd-active');
    });

    $('.hd-nav-menu a').each(function () {
        var linkHref = $(this).attr('href');
        if (linkHref !== "#" && currentPath.includes(linkHref)) {
            $('.hd-nav-menu a').removeClass('hd-active');
            $(this).addClass('hd-active');
        }
    });
});
