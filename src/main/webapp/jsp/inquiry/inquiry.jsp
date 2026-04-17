<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>문의사항</title>
    <link type="text/css" rel="stylesheet" href="css/inquiry.css"/>
</head>

<script>
    function checkDomain(select) {
        const direct = document.getElementById("directDomain");

        if (select.value === "direct") {
            direct.style.display = "inline";
            direct.name = "emailDomain";
        } else {
            direct.style.display = "none";
            direct.name = "";
        }
    }
</script>


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


<div class="inquiry-wrapper">


    <div class="wrapper">
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
                <p><b> Business hour from 9:00 to 18:00(Mon-fri)</b></p>
            </div>

            <div class="right">


                <form action="inquiry" method="post">
                    Name <input type="text" name="name"><br><br>
                    Contact number <input type="text" name="phone"><br><br>
                    E-mail <input type="email" name="email"><br><br>
                    Message <textarea name="message"></textarea><br><br>
                    <br>


                    <button id="submit-btn" type="submit">Send a message</button>
                </form>
            </div>

        </div>
    </div>


</div>
