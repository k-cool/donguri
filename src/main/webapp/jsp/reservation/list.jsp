<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>너가 남겨준 추억들</title>
    <style>
        /* 1. 배경: 따뜻한 원목 테이블 느낌 */
        body {
            background-color: #e0c9a6;
            background-image: url('https://www.transparenttextures.com/patterns/wood-pattern.png'); /* 나무 질감 패턴 */
            color: #3e2723;
            font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', sans-serif;
            margin: 0;
            padding: 50px 20px;
        }

        /* 전체 컨테이너 */
        .container {
            max-width: 1000px;
            margin: 0 auto;
            text-align: center;
        }

        h2 {
            font-size: 2.2rem;
            color: #2e1a12;
            margin-bottom: 40px;
            text-shadow: 2px 2px 0px rgba(255, 255, 255, 0.3);
        }

        /* 2. 테이블을 게시판 형태로 변형 */
        table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0 15px; /* 행 사이 간격 */
            margin-bottom: 40px;
        }

        /* 헤더는 투명하게 해서 깔끔하게 */
        th {
            padding: 10px;
            color: #5d4037;
            font-weight: bold;
            border-bottom: 2px solid #5d4037;
        }

        /* 3. 각 행을 '종이 쪽지' 느낌으로 */
        tr td {
            background: #fff9c4; /* 연한 노란색 포스트잇 느낌 */
            padding: 20px;
            box-shadow: 3px 3px 10px rgba(0, 0, 0, 0.1);
            position: relative;
            border: none;
        }

        /* 왼쪽 끝과 오른쪽 끝 둥글게 */
        tr td:first-child {
            border-radius: 10px 0 0 10px;
            border-left: 5px solid #795548; /* 왼쪽에 진한 갈색 포인트 */
        }

        tr td:last-child {
            border-radius: 0 10px 10px 0;
        }

        /* 마우스를 올리면 종이가 살짝 들리는 효과 */
        tr:hover td {
            background: #fffde7;
            transform: translateY(-3px);
            transition: all 0.2s ease-in-out;
            box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.2);
        }

        /* 4. 상세보기 버튼 커스텀 */
        .btn-view {
            display: inline-block;
            background-color: #795548;
            color: white !important;
            padding: 8px 20px;
            border-radius: 20px;
            text-decoration: none;
            font-size: 0.9rem;
            font-weight: bold;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
        }

        .btn-view:hover {
            background-color: #5d4037;
        }

        /* 5. 삭제 버튼 커스텀 (글자 대신 배지 느낌) */
        .btn-delete {
            display: inline-block;
            color: #d32f2f !important;
            text-decoration: none;
            font-size: 0.85rem;
            padding: 5px 10px;
            border: 1px solid #ffcdd2;
            border-radius: 5px;
            background: #fff;
            transition: 0.3s;
        }

        .btn-delete:hover {
            background: #d32f2f;
            color: white !important;
            border-color: #d32f2f;
        }

        /* 6. 하단 '도토리 보내기' 버튼을 도토리처럼 둥글게 */
        .btn-write {
            display: inline-block;
            background: #5d4037;
            color: white !important;
            padding: 20px 50px;
            border-radius: 50px 50px 80px 80px; /* 도토리 아래가 뾰족한 느낌 */
            text-decoration: none;
            font-size: 1.3rem;
            font-weight: bold;
            border: 4px solid #3e2723;
            box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
            transition: 0.3s;
        }

        .btn-write:hover {
            transform: scale(1.05);
            background: #4e342e;
        }

        /* 상태값 강조 (예: 완료된 것) */
        .status {
            font-size: 0.8rem;
            padding: 3px 8px;
            border-radius: 10px;
            background: #e0e0e0;
            color: #616161;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>🌳 숲속 우체통: 너가 남겨준 추억들</h2>

    <table>
        <thead>
        <tr>
            <th>이메일</th>
            <th>예약시간</th>
            <th>상태</th>
            <th>제목</th>
            <th>상세보기</th>
            <th>관리</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty list}">
                <c:forEach var="r" items="${list}">
                    <tr>
                        <td>${r.recipientEmail}</td>
                        <td><small>${r.scheduledDate}</small></td>
                        <td><span class="status">${r.isDone}</span></td>
                        <td style="max-width:300px; word-wrap:break-word;">
                            <strong>${r.subject}</strong>
                        </td>
                        <td>
                            <a href="reservation?action=detail&id=${r.reservationId}" class="btn-view">
                                추억 열기
                            </a>
                        </td>
                        <td>
                            <a href="reservation?action=delete&id=${r.reservationId}"
                               class="btn-delete"
                               onclick="return confirm('소중한 추억의 도토리가 사라지는데 진짜 괜찮아요?');">
                                비우기
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="6" style="padding: 100px; color: #8d6e63; font-style: italic;">
                        아직 보관된 도토리가 없어요. 🐿️
                    </td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>

    <div style="margin-top:50px;">
        <a href="reservation?action=write" class="btn-write">
            도토리 심으러 가기 🌰
        </a>
    </div>
</div>
</body>
</html>