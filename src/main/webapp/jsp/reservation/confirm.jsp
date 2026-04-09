<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>예약 내용 확인</title>
    <link rel="stylesheet" href="css/reservation_confirm.css">

  
</head>
<body>
<div class="confirm-container">
    <h2>📬 예약 내용 확인</h2>


    <div class="confirm-row"><label>받는 이메일:</label> <span>${insertReservation.recipientEmail}</span></div>
    <div class="confirm-row"><label>제목:</label> <span>${insertReservation.subject}</span></div>
    <div class="confirm-row"><label>내용:</label> <span>${insertReservation.content}</span></div>
    <div class="confirm-row"><label>예약 시간:</label> <span>${insertReservation.scheduledDate}</span></div>

    <div class="confirm-row">
        <label>템플릿:</label>
        <span>
                <strong>${selectedTemplate.name}</strong> (${selectedTemplate.type})
                <div style="margin-top: 10px;">
                    <img src="${selectedTemplate.coverImgUrl}" width="120"
                         style="border-radius: 5px; border: 1px solid #ddd;">
                </div>
<%--        <c:forEach var="t" items="${templateList}">--%>
<%--            <c:if test="${insertReservation.templateId == t.templateId}">--%>
<%--            </c:if>--%>
<%--        </c:forEach>--%>
    </span>
    </div>

    <%-- BGM 표시 --%>
    <div class="confirm-row">
        <label>BGM:</label>
        <span>
            ${insertReservation.bgmUrl == 'none' ? '없음' :
                    insertReservation.bgmUrl == 'piano' ? '피아노' : 'Lo-fi'}
        </span>
    </div>


    <form action="reservation" method="post">
        <input type="hidden" name="action" value="insert">

        <div class="button-group">
            <button type="submit">💌 최종 전송</button>
            <a href="reservation?action=write" class="link-btn">✏️ 수정하러 돌아가기</a>
        </div>
    </form>

</div>
</body>
</html>