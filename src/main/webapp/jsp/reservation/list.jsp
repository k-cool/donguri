<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>너가 남겨준 추억들</title>
</head>
<body>
<div align="center">
    <h2>너가 남겨준 그동안의 추억들이야</h2>

    <table border="1" cellpadding="10">
        <tr>
            <th>이메일</th>
            <th>예약시간</th>
            <th>상태</th>
            <th>제목</th>
            <th>상세보기</th>
            <th>삭제</th>
        </tr>

        <c:choose>
            <c:when test="${not empty list}">
                <c:forEach var="r" items="${list}">
                    <tr>
                        <td>${r.recipientEmail}</td>
                        <td>${r.scheduledDate}</td>
                        <td>${r.isDone}</td>
                        <td style="max-width:300px; word-wrap:break-word;">${r.subject}</td>
                        <td>
                            <a href="reservation?action=detail&id=${r.reservationId}">
                                보기
                            </a>
                        </td>
                        <td>
                            <a href="reservation?action=delete&id=${r.reservationId}"
                               onclick="return confirm('소중한 추억의 도토리가 사라지는데 진짜 괜찮아요?');">삭제</a>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="5">기록된 도토리가 없어!ㅜㅜ</td>
                </tr>
            </c:otherwise>
        </c:choose>
    </table>

    <div style="margin-top:20px;">
        <a href="reservation?action=write"
           style="display:inline-block; padding:20px 40px; text-decoration:none; color:black; border:1px solid black; border-radius:10px;">
            편지 써보자<br>
        </a>
    </div>


</div>
</body>
</html>