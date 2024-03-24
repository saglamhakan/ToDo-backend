package com.example.todo.util;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Service
public class TestNotificationService {

    @Autowired
    private GreenMail greenMail; // Test mail sunucusu

    public void sendTestEmail(String to, String subject, String message) {
        GreenMailUtil.sendTextEmailTest(to, "no-reply@example.com", subject, message);
    }

}
