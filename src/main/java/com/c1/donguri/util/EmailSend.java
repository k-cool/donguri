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

            message.setText(content, "UTF-8");

            Transport.send(message);

            System.out.println("이메일 전송 성공: " + receiverEmail);
        } catch (MessagingException e) {
            System.out.println("이메일 전송 실패: " + receiverEmail);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // 인증 이메일 발송 메소드
    public boolean sendVerificationEmail(String email, String verificationCode) {
        try {
            String title = "도토리 이메일 인증 코드";
            String content = "안녕하세요.\n\n" +
                    "도토리 서비스 이메일 인증 코드입니다.\n\n" +
                    "인증 코드: " + verificationCode + "\n\n" +
                    "해당 코드를 입력하여 이메일 인증을 완료해주세요.\n\n" +
                    "감사합니다.\n" +
                    "도토리 팀 드림";

            send(email, title, content);
            return true;
        } catch (Exception e) {
            System.out.println("인증 이메일 발송 실패: " + email);
            e.printStackTrace();
            return false;
        }
    }

}