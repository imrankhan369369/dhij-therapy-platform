package com.dhij.app.com.dhij.app.events;

import com.dhij.app.com.dhij.app.service.MailService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserRegisteredListener {

    private final MailService mailService;

    public UserRegisteredListener(MailService mailService) {
        this.mailService = mailService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserRegistered(UserRegisteredEvent event) {
        mailService.sendWelcomeEmail(event.email(), event.username());
    }
}
``