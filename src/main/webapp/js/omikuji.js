$(() => {
    console.log("omikuji.js loaded");

    $("#draw-btn").click(() => {
        console.log("draw-btn");

        const $luck = $("#luck")
        const $message = $("#message")
        $message.empty();

        $.ajax({
            url: "omikuji",
            type: "POST",
            data: {},
            success: (resData) => {
                console.log(resData);

                const omikuji = resData.omikuji;

                $luck.append(`${omikuji.luck}`);
                $message.append(`${omikuji.message}`);

            },
            error: (error) => {
                console.log(
                    error.status
                );

                if (error.status === 409) {
                    $message.append("오늘은 이미 뽑았어요 내일 다시 시도해주세요!");

                }
            }
        })
    })

})

