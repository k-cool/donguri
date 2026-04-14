<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Reservation Confirmation</title>
    <link rel="stylesheet" href="css/reservation_confirm.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


</head>
<body>
<div class="confirm-container">
    <h2>도토리 확인페이지</h2>


    <div class="confirm-row"><label>받는사람:</label> <span>${insertReservation.recipientEmail}</span></div>
    <div class="confirm-row"><label>제목:</label> <span>${insertReservation.subject}</span></div>
    <div class="confirm-row"><label>내용:</label> <span>${insertReservation.content}</span></div>
    <div class="confirm-row"><label>예약일시:</label> <span>${insertReservation.scheduledDate}</span></div>

    <div class="confirm-row">
        <label>템플릿:</label>
        <span>
                <strong>${selectedTemplate.name}</strong> (${selectedTemplate.type})
                <div style="margin-top: 10px;">
                    <img src="${selectedTemplate.coverImgUrl}" width="120"
                         style="border-radius: 5px; border: 1px solid #ddd;">
                </div>

    </span>
    </div>

    <%-- BGM Display --%>
    <div class="confirm-row">
        <label>BGM:</label>
        <span>
            <c:choose>
                <c:when test="${empty insertReservation.bgmUrl or insertReservation.bgmUrl eq ''}">
                    BGM
                </c:when>
                <c:otherwise>
                    <c:set var="bgmFileName" value="${fn:substringAfter(insertReservation.bgmUrl, 'bgm/')}"/>
                    <c:set var="bgmName"
                           value="${fn:replace(fn:replace(bgmFileName, '.mp3', ''), '(chosic.com)', '')}"/>
                    ${bgmName}
                </c:otherwise>
            </c:choose>
        </span>
    </div>

    <audio controls style="width:300px;">
        <source src="${param.bgmUrl}" type="audio/mpeg">
    </audio>


    <form action="reservation" method="post">
        <input type="hidden" name="action" value="insert">

        <div class="button-group">
            <button type="submit">Send</button>
            <a href="reservation?action=write" class="link-btn">Edit</a>
        </div>
    </form>

</div>
</body>
</html>
