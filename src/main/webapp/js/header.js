$(function () {
    let menuVisible = false;
    $("#menuBtn").click(function () {
        if (menuVisible) {
            $("#slideMenu").css("left", "-250px");
        } else {
            $("#slideMenu").css("left", "0px");
        }
        menuVisible = !menuVisible;
    });

    // Click outside menu to close
    $(document).click(function (event) {
        if (menuVisible && !$(event.target).closest('#slideMenu').length && !$(event.target).closest('#menuBtn').length) {
            $("#slideMenu").css("left", "-250px");
            menuVisible = false;
        }
    });
});
$(document).ready(function () {
    // 1. 임시 UI 테스트용: 클릭하면 그 메뉴만 파란색으로 바꿈
    $('.nav-menu a').on('click', function () {
        $('.nav-menu a').removeClass('active'); // 다른 메뉴들의 active 끄기
        $(this).addClass('active');             // 방금 클릭한 메뉴만 active 켜기
    });

    // 2. 실전용 (페이지 이동 유지 로직)
    // JSP(MVC 패턴) 구조에서는 메뉴를 클릭하면 새로운 페이지로 새로고침 되기 때문에
    // 위 1번 코드만 있으면 색상이 다시 초기화되어 버립니다.
    // 그래서 현재 브라우저의 URL 주소를 읽어와서 일치하는 메뉴에 색상을 칠해주는 이 코드가 꼭 필요합니다!
    var currentPath = window.location.pathname;

    $('.nav-menu a').each(function () {
        var linkHref = $(this).attr('href');

        // 현재 URL에 href 주소(예: /reserve)가 포함되어 있다면 해당 메뉴 활성화
        if (linkHref !== "#" && currentPath.includes(linkHref)) {
            $('.nav-menu a').removeClass('active');
            $(this).addClass('active');
        }
    });
});