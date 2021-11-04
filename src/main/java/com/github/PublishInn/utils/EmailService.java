package com.github.PublishInn.utils;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final static String ERROR_WHILE_SENDING_EMAIL_MSG = "Failed to send email";

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String emailContent, String emailSubject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(emailContent, true);
            helper.setTo(to);
            helper.setSubject(emailSubject);
            helper.setFrom("publishinn2021@gmail.com");
            mailSender.send(mimeMessage);
        }
        catch (MessagingException e) {
            LOGGER.error(ERROR_WHILE_SENDING_EMAIL_MSG, e);
            throw new IllegalStateException(ERROR_WHILE_SENDING_EMAIL_MSG);
        }
    }
}
