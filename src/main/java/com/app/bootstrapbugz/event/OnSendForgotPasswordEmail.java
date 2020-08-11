package com.app.bootstrapbugz.event;

import com.app.bootstrapbugz.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnSendForgotPasswordEmail extends ApplicationEvent {
    private User user;
    private String token;

    public OnSendForgotPasswordEmail(User user, String token) {
        super(user);
        this.user = user;
        this.token = token;
    }
}