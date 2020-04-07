package com.app.webapp.event;

import com.app.webapp.model.VerificationToken;
import com.app.webapp.service.VerificationTokenService;
import com.app.webapp.service.EmailService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ResendVerificationEmailListener implements ApplicationListener<OnResendVerificationEmail> {
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    public ResendVerificationEmailListener(VerificationTokenService verificationTokenService, EmailService emailService) {
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(OnResendVerificationEmail event) {
        VerificationToken verificationToken = event.getVerificationToken();
        String token = UUID.randomUUID().toString();
        verificationToken.setToken(token);
        verificationToken.setExpirationDate(LocalDateTime.now().plusDays(1));
        verificationToken.setNumberOfSent(verificationToken.getNumberOfSent() + 1);
        verificationTokenService.save(verificationToken);
        String to = verificationToken.getUser().getEmail();
        String subject = "Resend Registration Confirmation";
        String body = "Please activate your account by clicking on link.\n" +
                "http://localhost:12345/api/auth/confirm-registration?token=" + token;

        emailService.sendEmail(to, subject, body);
    }
}
