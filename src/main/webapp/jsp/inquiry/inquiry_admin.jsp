<%@ page import="java.util.ArrayList" %>
<%@ page import="com.c1.donguri.inquiry.InquiryDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>admin page</title>
    <link rel="stylesheet" href="css/inquiry_admin.css">
</head>

<script>
    function openModal(no, name, email, Phone, message) {

        document.getElementById("modal").style.display = "flex";

        document.getElementById("mName").innerText = "이름: " + name;
        document.getElementById("mEmail").innerText = "이메일: " + email;
        document.getElementById("mPhone").innerText = "연락처: " + Phone;
        document.getElementById("mMessage").innerText = "내용: " + message;
        document.getElementById("emailInput").value = email;
        document.getElementById("noInput").value = no;
    }

    function sendReply() {

        const email = document.getElementById("emailInput").value;
        const no = document.getElementById("noInput").value;
        const reply = document.getElementById("replyText").value;

        fetch("<%= request.getContextPath() %>/replyInquiry", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "email=" + encodeURIComponent(email) +
                "&reply=" + encodeURIComponent(reply) +
                "&no=" + encodeURIComponent(no)
        })
            .then(res => res.text())
            .then(data => {
                alert("전송이 완료되었습니다 👍");

                // 모달 닫기
                document.getElementById("modal").style.display = "none";

                // 리스트 새로고침
                location.reload();
            })
            .catch(err => {
                alert("전송 실패");
            });
    }

    function deleteInquiry(event, no) {

        event.stopPropagation();

        if (confirm("정말 삭제하시겠습니까?")) {
            location.href = "<%= request.getContextPath() %>/deleteInquiry?no=" + no;
        }
    }

    function closeModal() {
        document.getElementById("modal").style.display = "none";
    }

    <%
    String msg = (String) request.getParameter("msg");
    if ("success".equals(msg)) {
%>

    alert("답변이 전송되었습니다 👍");

    <%
        }
    %>
</script>


<div class="inquiry-admin-wrapper">


    <h2>문의사항 List</h2>

    <%
        ArrayList<InquiryDTO> list =
                (ArrayList<InquiryDTO>) request.getAttribute("list");

        for (InquiryDTO dto : list) {
    %>

    <div class="card" onclick="openModal(
            '<%= dto.getInquiryId() %>','<%= dto.getName() %>','<%= dto.getEmail() %>','<%= dto.getPhone()%>','<%= dto.getMessage().replace("'", "\\'") %>')">


        <div class="card-title">
            문의번호 <%= dto.getInquiryId() %>

            <span class="delete-btn"
                  onclick="deleteInquiry(event, '<%= dto.getInquiryId() %>')">✕</span>
        </div>

        <div class="card-body">
            <p><b>이름:</b> <%= dto.getName() %>
            </p>
            <p><b>이메일:</b> <%= dto.getEmail() %>
            </p>
            <p><b>연락처:</b> <%= dto.getPhone()%>
            </p>
            <p><b>내용:</b> <%= dto.getMessage() %>
            </p>
        </div>

    </div>

    <%
        }
    %>


    <div id="modal" class="modal">

        <div class="modal-content">

            <span class="close" onclick="closeModal()">&times;</span>

            <h3>문의 상세내용</h3>

            <p id="mName"></p>
            <p id="mEmail"></p>
            <p id="mPhone"></p>
            <p id="mMessage"></p>

            <form action="<%= request.getContextPath() %>/replyInquiry" method="post"
                  onsubmit="this.querySelector('button').disabled=true;">
                <input type="hidden" id="emailInput" name="email">
                <input type="hidden" id="noInput" name="no">

                <textarea id="replyText" placeholder="답변 입력"></textarea>

                <button type="button" class="reply-btn" onclick="sendReply()">답변 보내기</button>

            </form>

        </div>
    </div>

</div>
