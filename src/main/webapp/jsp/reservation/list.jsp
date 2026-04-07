<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>너가 남겨준 추억들</title>
    <style>
        /* 1. 레이아웃 초기화: 칸이 절대 밖으로 나가지 않게 함 */
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            background-color: #e0c9a6;
            background-image: url('https://www.transparenttextures.com/patterns/wood-pattern.png');
            color: #3e2723;
            font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', sans-serif;
            padding: 50px 15px;
        }

        .container {
            max-width: 900px;
            margin: 0 auto;
            text-align: center;
        }

        /* 2. 필터 바구니 (검색창) 디자인 */
        .filter-basket {
            background: rgba(255, 255, 255, 0.5);
            padding: 20px;
            border-radius: 15px;
            margin-bottom: 30px;
            border: 2px dashed #795548;
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 10px;
        }

        .filter-basket select,
        .filter-basket input[type="text"] {
            height: 45px;
            border: 1px solid #d7ccc8;
            border-radius: 8px;
            padding: 0 15px;
            font-size: 1rem;
            outline: none;
        }

        /* 입력창 너비 고정 */
        .filter-basket input[type="text"] {
            width: 250px;
        }

        .btn-search {
            height: 45px;
            padding: 0 25px;
            background: #795548;
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: bold;
            cursor: pointer;
            transition: 0.2s;
        }

        .btn-search:hover {
            background: #5d4037;
        }

        /* 3. 테이블 디자인: 절대 깨지지 않는 고정 레이아웃 */
        .table-wrapper {
            width: 100%;
            background: rgba(255, 255, 255, 0.2);
            border-radius: 10px;
            padding: 10px;
        }

        table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0 10px;
            table-layout: fixed; /* 칸 너비를 강제로 고정 */
        }

        th {
            padding: 10px;
            color: #5d4037;
            font-size: 0.9rem;
            border-bottom: 2px solid #5d4037;
        }

        /* 각 컬럼 너비 비율 고정 (총 100%) */
        th:nth-child(1) {
            width: 22%;
        }

        /* 이메일 */
        th:nth-child(2) {
            width: 18%;
        }

        /* 시간 */
        th:nth-child(3) {
            width: 10%;
        }

        /* 상태 */
        th:nth-child(4) {
            width: 25%;
        }

        /* 제목 */
        th:nth-child(5) {
            width: 13%;
        }

        /* 상세보기 */
        th:nth-child(6) {
            width: 12%;
        }

        /* 삭제 */

        tr td {
            background: #fff9c4;
            padding: 15px 10px;
            box-shadow: 3px 3px 8px rgba(0, 0, 0, 0.05);
            font-size: 0.9rem;
            vertical-align: middle;
            /* 글자가 길면 줄바꿈 대신 '...' 처리하여 정렬 유지 */
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        tr td:first-child {
            border-radius: 10px 0 0 10px;
            border-left: 5px solid #795548;
        }

        tr td:last-child {
            border-radius: 0 10px 10px 0;
        }

        /* 상태 배지 */
        .status {
            display: inline-block;
            padding: 2px 8px;
            background: #d7ccc8;
            border-radius: 5px;
            font-size: 0.75rem;
            color: #5d4037;
        }

        /* 버튼류 */
        .btn-view {
            background: #795548;
            color: #fff !important;
            padding: 6px 12px;
            border-radius: 20px;
            text-decoration: none;
            font-size: 0.8rem;
        }

        .btn-delete {
            color: #d32f2f !important;
            text-decoration: none;
            font-size: 0.8rem;
        }

        .btn-write {
            display: inline-block;
            margin-top: 40px;
            padding: 18px 50px;
            background: #5d4037;
            color: #fff !important;
            border-radius: 50px;
            font-weight: bold;
            text-decoration: none;
            box-shadow: 0 5px 0 #2e1a12;
        }

        tr.row-highlight td {
            background-color: #fff176 !important; /* 전체적으로 밝은 노란 조명 */
            box-shadow: inset 0 0 10px rgba(121, 85, 72, 0.1);
            transform: scale(1.02); /* 살짝 앞으로 튀어나오는 효과 */
            transition: all 0.3s ease;
            z-index: 10;
            border-top: 2px solid #fbc02d;
            border-bottom: 2px solid #fbc02d;
        }

        /* 2. 행 왼쪽 끝에 도토리 포인트 주기 */
        tr.row-highlight td:first-child {
            border-left: 10px solid #fbc02d !important;
        }

        /* 3. 검색어 단어 자체도 색연필 느낌으로 강조 */
        .text-highlight {
            background: linear-gradient(to top, #ffb300 50%, transparent 50%);
            font-weight: bold;
            padding: 0 2px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>🌳 숲속 우체통: 추억 찾기</h2>

    <div class="filter-basket">
        <form action="reservation" method="get" style="display: flex; gap: 10px; margin: 0;">
            <input type="hidden" name="action" value="list">

            <select name="searchType">
                <option value="all" ${param.searchType == 'all' ? 'selected' : ''}>전체</option>
                <option value="recipientEmail" ${param.searchType == 'recipientEmail' ? 'selected' : ''}>이메일</option>
                <option value="subject" ${param.searchType == 'subject' ? 'selected' : ''}>제목</option>

            </select>

            <input type="text" name="keyword" placeholder="찾고싶은 도토리.." value="${param.keyword}">

            <button type="submit" class="btn-search">🔍 검색</button>

            <c:if test="${not empty param.keyword}">
                <a href="reservation?action=list" style="color: #8d6e63; font-size: 0.8rem; align-self: center;">초기화</a>
            </c:if>
        </form>
    </div>

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

    <a href="reservation?action=write" class="btn-write">도토리 심으러 가기 🌰</a>
</div>
<script src="js/filter.js"></script>
</body>
</html>