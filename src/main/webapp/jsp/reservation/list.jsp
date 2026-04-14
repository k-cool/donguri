<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>너가 남겨준 추억들</title>
    <link rel="stylesheet" href="css/reservation_list.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<div class="container">
    <h2> {숲속 우체통: 추억 찾기}</h2>

    <div class="filter-basket">
        <form action="reservation" method="get" style="display: flex; gap: 10px; margin: 0; align-items: center;">
            <input type="hidden" name="action" value="list">

            <select name="status">
                <option value="all" ${param.status == 'all' || empty param.status ? 'selected' : ''}>${param.status == 'all' || empty param.status ? '✓ ' : ''}전체 상태</option>
                <option value="완료" ${param.status == '완료' ? 'selected' : ''}>${param.status == '완료' ? '✓ ' : ''}발송 완료</option>
                <option value="대기" ${param.status == '대기' ? 'selected' : ''}>${param.status == '대기' ? '✓ ' : ''}발송 대기</option>
            </select>

            <select name="searchType">
                <option value="all" ${param.searchType == 'all' ? 'selected' : ''}>${param.searchType == 'all' ? '✓ ' : ''}전체 항목</option>
                <option value="recipientEmail" ${param.searchType == 'recipientEmail' ? 'selected' : ''}>${param.searchType == 'recipientEmail' ? '✓ ' : ''}이메일</option>
                <option value="subject" ${param.searchType == 'subject' ? 'selected' : ''}>${param.searchType == 'subject' ? '✓ ' : ''}제목</option>
            </select>

            <input type="text" name="keyword" placeholder="찾고싶은 도토리.." value="${param.keyword}">
            <button type="submit" class="btn-search">검색</button>

            <c:if test="${not empty param.keyword or (not empty param.status and param.status != 'all')}">
                <a href="reservation?action=list"
                   style="color: #8d6e63; font-size: 0.8rem; align-self: center; text-decoration: none;">초기화</a>
            </c:if>
        </form>
    </div>

    <a href="reservation?action=write" class="btn-write">도토리 심으러 가기</a>

    <div class="acorn-box">
        <h3>{ 보낸 도토리 보관함 }</h3>

        <div class="acorn-grid">
            <c:choose>
                <c:when test="${not empty list}">
                    <c:forEach var="r" items="${list}">
                        <a href="reservation?action=detail&id=${r.reservationId}" class="acorn-card">
                            <c:if test="${r.isDone == '완료'}">
                                <img src="${pageContext.request.contextPath}/image/stamp_red.svg"
                                     class="stamp-img">
                            </c:if>
                            <div class="acorn-info">
                                <p class="subject">${r.subject}</p>
                                <p class="date">
                                    <c:choose>
                                        <c:when test="${r.scheduledDate.length() >= 10}">
                                            ${r.scheduledDate.substring(0, 10)}
                                        </c:when>
                                        <c:otherwise>
                                            ${r.scheduledDate}
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                        </a>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="empty">찾으시는 도토리가 아직 숲에 없나봐요.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <script src="js/filter.js"></script>
</body>
</html>