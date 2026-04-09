<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>예약 내용 확인</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #fff8f0;
            display: flex;
            justify-content: center;
            align-items: flex-start;
            padding-top: 40px;
        }

        .confirm-container {
            max-width: 500px;
            width: 90%;
            background: rgba(255, 255, 255, 0.95);
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .confirm-container h2 {
            text-align: center;
            margin-bottom: 20px;
            font-size: 24px;
        }

        .confirm-row {
            margin-bottom: 15px;
        }

        .confirm-row label {
            font-weight: bold;
        }

        .confirm-row span {
            margin-left: 5px;
            color: #333;
        }

        button, .link-btn {
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            background: #8e6546;
            color: white;
            cursor: pointer;
            font-size: 16px;
            margin-top: 10px;
            display: inline-block;
            text-decoration: none;
        }

        .button-group {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }
    </style>
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

        <c:forEach var="t" items="${templateList}">
            <c:if test="${insertReservation.templateId == t.templateId}">
                <strong>${t.templateName}</strong> (${t.type})


                <div style="margin-top: 10px;">
                    <img src="${t.coverImgUrl}" width="120" style="border-radius: 5px; border: 1px solid #ddd;">
                </div>
            </c:if>
        </c:forEach>
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