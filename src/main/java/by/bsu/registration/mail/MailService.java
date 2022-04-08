package by.bsu.registration.mail;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService implements MailSender{

    private final static Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String email) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirmation token");
            helper.setFrom("vasia.drobyshevsky@tut.by");
            mailSender.send(mimeMessage);
        } catch (MessagingException e){
            LOGGER.error("failed to send message", e);
            throw new IllegalStateException("failed to send message");
        }
    }
}
