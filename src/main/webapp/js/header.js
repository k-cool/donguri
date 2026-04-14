$(function () {
    let menuVisible = false;

    $("#hd-menuBtn").click(function () {
        if (menuVisible) {
            // 🚨 메뉴 닫기: 오른쪽 화면 밖으로 숨김
            $("#hd-slideMenu").css("right", "-250px");
        } else {
            // 🚨 메뉴 열기: 오른쪽 화면 끝(0px)에 딱 붙임
            $("#hd-slideMenu").css("right", "0px");
        }
        menuVisible = !menuVisible;
    });

    // Click outside menu to close
    $(document).click(function (event) {
        if (menuVisible && !$(event.target).closest('#hd-slideMenu').length && !$(event.target).closest('#hd-menuBtn').length) {
            // 🚨 바깥 클릭 시 메뉴 닫기: 오른쪽 화면 밖으로 숨김
            $("#hd-slideMenu").css("right", "-250px");
            menuVisible = false;
        }
    });
});

$(document).ready(function () {
    var currentPath = window.location.pathname;

    // 1. 현재 접속한 주소가 메인 페이지('/main')가 아닐 경우 2층 메뉴 전체 숨기기
    if (currentPath !== '/main' && currentPath !== '/') {
        $('.hd-header-bottom').hide();
        $('header').addClass('hd-sub-header');
    }

    // 2. 임시 UI 테스트용: 클릭하면 그 메뉴만 파란색으로 바꿈
    $('.hd-nav-menu a').on('click', function () {
        $('.hd-nav-menu a').removeClass('hd-active');
        $(this).addClass('hd-active');
    });

    // 3. 실전용 (페이지 이동 유지 로직 - 활성화된 메뉴 파란색 표시)
    $('.hd-nav-menu a').each(function () {
        var linkHref = $(this).attr('href');

        if (linkHref !== "#" && currentPath.includes(linkHref)) {
            $('.hd-nav-menu a').removeClass('hd-active');
            $(this).addClass('hd-active');
        }
    });
});