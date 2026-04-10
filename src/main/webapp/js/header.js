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
