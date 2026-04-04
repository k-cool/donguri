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
        this.envMap = EnvLoader.loadEnv(".env");
    }

    /*
        매개변수:
        - receiverEmail : 받는 사람 이메일
        - title         : 메일 제목
        - subject       : 엽서 제목
        - content       : 엽서 내용
    */
    public void send(String receiverEmail, String title, String content) {
        System.out.println("=== 실제 SMTP 발송 시도 ===" + receiverEmail);

        Properties props = new Properties();

        props.put("mail.smtp.host", envMap.get("SMTP_HOST"));
        props.put("mail.smtp.port", envMap.get("SMTP_PORT"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(envMap.get("SMTP_EMAIL"), envMap.get("SMTP_PASSWORD"));
            }
        });

        MimeMessage message = new MimeMessage(session);

        // 실제 발송자
        try {
            message.setFrom(new InternetAddress("noreply@donguri.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));

            message.setSubject(title, "UTF-8");

            message.setText(content, "UTF-8");

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        // 수신자
    }
}