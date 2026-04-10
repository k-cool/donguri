<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>문의사항</title>
    <link type="text/css" rel="stylesheet" href="css/inquiry.css"/>
</head>

<body>

<%
    String msg = (String) request.getAttribute("msg");
    if (msg != null) {
%>

<script>
    alert("<%= msg %>");
    window.location.href = "<%= request.getContextPath() %>/inquiry";
</script>

<%
    }
%>
<div class="hero">
    <div>
        <h1>Get in touch</h1>
        <p>Let's work together on your next project.</p>
    </div>
</div>

<div class="container">
    <div class="flex">

        <div class="left">
            <h3>Contact</h3>
            <p>For commissions and inquiries, please email.</p>
            <p><b>leesepatrick@gmail.com</b></p>
        </div>

        <div class="right">


            <form action="inquiry" method="post">
                이름 <input type="text" name="name"><br><br>
                연락처 <input type="text" name="phone"><br><br>
                이메일 <input type="email" name="email"><br><br>
                문의내용 <textarea name="message"></textarea><br><br>
                <br>


                <button type="submit">전송</button>
            </form>
        </div>

    </div>
</div>


</body>
</html>
