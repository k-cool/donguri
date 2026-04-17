package com.c1.donguri.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

/*
    실제 SMTP 발송 담당 클래스
    Gmail SMTP
*/
public class EmailSend {
    public static final EmailSend EMAIL_SEND = new EmailSend();
    public static Map<String, String> envMap;

    private EmailSend() {
        envMap = EnvLoader.loadEnv(".env");
    }

    /*
        매개변수:
        - receiverEmail : 받는 사람 이메일
        - title         : 메일 제목
        - subject       : 엽서 제목
        - content       : 엽서 내용
    */
    public void send(String receiverEmail, String title, String content) {
        Properties props = new Properties();

        props.put("mail.smtp.host", envMap.get("SMTP_HOST"));
        props.put("mail.smtp.port", envMap.get("SMTP_PORT"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(envMap.get("SMTP_EMAIL"), envMap.get("SMTP_PASSWORD"));
                }
            });

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress("noreply@donguri.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));

            message.setSubject(title, "UTF-8");

            message.setContent(content, "text/html; charset=UTF-8");

            Transport.send(message);

            System.out.println("이메일 전송 성공: " + receiverEmail);
        } catch (MessagingException e) {
            System.out.println("이메일 전송 실패: " + receiverEmail);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

//    public String createEmailContent(String reservationId) {
//        return "    <header>\n" +
//                "                <div>동구리 로고</div>\n" +
//                "                </header>\n" +
//                "\n" +
//                "                <body>\n" +
//                "                    <h2>동구리 우편이 도착했어요. 아래 링크에서 확인해주세요!</h2>\n" +
//                "                    <a href=\"%s\">아이콘</a>\n".replace("%s", reservationId) +
//                "                </body>\n" +
//                "\n" +
//                "                <footer>\n" +
//                "                </footer>";
//    }

    public String createEmailContent(String reservationId) {
        String url = envMap.get("BASE_URL") + "/post?id=" + reservationId;

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <title>동구리 우편</title>" +
                "</head>" +
                "<body style='font-family: Arial, sans-serif; background-color:#f9f9f9; padding:20px;'>" +
                "    <div style='max-width:600px; margin:0 auto; background:#ffffff; padding:30px; border-radius:12px; box-shadow:0 2px 8px rgba(0,0,0,0.1);'>" +
                "        <header style='text-align:center; margin-bottom:20px;'>" +
                "            <h1 style='color:#6b4f3b;'>동구리</h1>" +
                "        </header>" +
                "        <main style='text-align:center;'>" +
                "            <h2 style='color:#333;'>동구리 우편이 도착했어요!</h2>" +
                "            <p style='color:#555;'>아래 버튼을 눌러 엽서를 확인해주세요.</p>" +
                "            <a href='" + url + "' " +
                "               style='display:inline-block; padding:12px 24px; background-color:#8b5e3c; color:white; text-decoration:none; border-radius:8px; margin-top:20px;'>" +
                "               엽서 확인하기" +
                "            </a>" +
                "        </main>" +
                "        <footer style='margin-top:30px; text-align:center; font-size:12px; color:#999;'>" +
                "            Donguri Team" +
                "        </footer>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    // 인증 이메일 발송 메소드
    public boolean sendVerificationEmail(String email, String verificationCode) {
        try {
            String title = "[동구리] 이메일 인증 코드";
//            String content = "안녕하세요.\n\n" +
//                    "도토리 서비스 이메일 인증 코드입니다.\n\n" +
//                    "인증 코드: " + verificationCode + "\n\n" +
//                    "해당 코드를 입력하여 이메일 인증을 완료해주세요.\n\n" +
//                    "감사합니다.\n" +
//                    "도토리 팀 드림";
//
            String content = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>도토리 이메일 인증</title>\n" +
                    "</head>\n" +
                    "<body style=\"margin: 0; padding: 0; background-color: #fdfaf7; font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', sans-serif;\">\n" +
                    "    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color: #fdfaf7; padding: 40px 20px;\">\n" +
                    "        <tr>\n" +
                    "            <td align=\"center\">\n" +
                    "                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"max-width: 500px; background-color: #ffffff; border-top: 6px solid #7b3f00; border-radius: 12px; box-shadow: 0 4px 10px rgba(0,0,0,0.05); overflow: hidden;\">\n" +
                    "                    <tr>\n" +
                    "                        <td style=\"padding: 40px 30px;\">\n" +
                    "                            <h1 style=\"margin: 0; color: #7b3f00; font-size: 24px; font-weight: bold; text-align: center;\">\n" +
                    "                                \uD83C\uDF30 도토리\n" +
                    "                            </h1>\n" +
                    "                            \n" +
                    "                            <div style=\"margin-top: 30px; height: 1px; background-color: #eee;\"></div>\n" +
                    "                            \n" +
                    "                            <p style=\"margin-top: 30px; font-size: 16px; color: #4a4238; line-height: 1.6;\">\n" +
                    "                                안녕하세요.<br>\n" +
                    "                                도토리 서비스 이메일 인증 코드입니다.\n" +
                    "                            </p>\n" +
                    "                            <p style=\"font-size: 15px; color: #7d6e5e; margin-bottom: 30px;\">\n" +
                    "                                아래의 인증 코드를 입력하여 이메일 인증을 완료해주세요.\n" +
                    "                            </p>\n" +
                    "                            \n" +
                    "                            <div style=\"background-color: #f9f3ed; border-radius: 8px; padding: 25px; text-align: center; margin-bottom: 30px;\">\n" +
                    "                                <span style=\"display: block; font-size: 13px; color: #a68b6d; margin-bottom: 8px; letter-spacing: 1px;\">VERIFICATION CODE</span>\n" +
                    "                                <strong style=\"font-size: 32px; color: #7b3f00; letter-spacing: 5px;\">" + verificationCode + "</strong>\n" +
                    "                            </div>\n" +
                    "                            \n" +
                    "                            <p style=\"font-size: 14px; color: #8d8379; line-height: 1.6; margin-top: 40px;\">\n" +
                    "                                감사합니다.<br>\n" +
                    "                                <strong>도토리 팀 드림</strong>\n" +
                    "                            </p>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td style=\"background-color: #f7f3ef; padding: 20px; text-align: center;\">\n" +
                    "                            <p style=\"margin: 0; font-size: 12px; color: #b5ada3;\">\n" +
                    "                                본 메일은 발신 전용입니다. 문의사항은 고객센터를 이용해주세요.\n" +
                    "                            </p>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </table>\n" +
                    "</body>\n" +
                    "</html>";


            send(email, title, content);

            return true;
        } catch (Exception e) {
            System.out.println("인증 이메일 발송 실패: " + email);
            e.printStackTrace();
            return false;
        }
    }

}