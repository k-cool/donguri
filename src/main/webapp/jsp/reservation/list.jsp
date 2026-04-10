<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>너가 남겨준 추억들</title>
    <link rel="stylesheet" href="css/reservation_list.css">

</head>
<body>
<div class="container">
    <h2> {숲속 우체통: 추억 찾기}</h2>


    <div class="filter-basket">
        <form action="reservation" method="get" style="display: flex; gap: 10px; margin: 0;">
            <input type="hidden" name="action" value="list">

            <select name="searchType">
                <option value="all" ${param.searchType == 'all' ? 'selected' : ''}>전체</option>
                <option value="recipientEmail" ${param.searchType == 'recipientEmail' ? 'selected' : ''}>이메일</option>
                <option value="subject" ${param.searchType == 'subject' ? 'selected' : ''}>제목</option>

            </select>

            <input type="text" name="keyword" placeholder="찾고싶은 도토리.." value="${param.keyword}">

            <button type="submit" class="btn-search">검색</button>

            <c:if test="${not empty param.keyword}">
                <a href="reservation?action=list" style="color: #8d6e63; font-size: 0.8rem; align-self: center;">초기화</a>
            </c:if>
        </form>
    </div>

    <a href="reservation?action=write" class="btn-write">도토리 심으러 가기</a>

    <div class="table-wrapper">
        <table>
            <thead>
            <tr>
                <th>받는사람 이메일</th>
                <th>예약시간</th>
                <th>상태</th>
                <th>제목</th>
                <th>보기</th>
                <th>관리</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty list}">
                    <c:forEach var="r" items="${list}">
                        <tr>
                            <td title="${r.recipientEmail}">${r.recipientEmail}</td>
                            <td>${r.scheduledDate}</td>
                            <td><span class="status">${r.isDone}</span></td>
                            <td title="${r.subject}"><strong>${r.subject}</strong></td>
                            <td><a href="reservation?action=detail&id=${r.reservationId}" class="btn-view">열기</a></td>
                            <td><a href="reservation?action=delete&id=${r.reservationId}" class="btn-delete"
                                   onclick="return confirm('지울까요?');">비우기</a></td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="6" style="padding: 80px; color: #8d6e63;">
                            숲속에 해당하는 도토리가 없어요. 🐿️
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>


</div>
<script src="js/filter.js"></script>
</body>
</html>