package com.esticharalegal.backendServer.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    // Your Twilio Account SID and Auth Token
    public static final String ACCOUNT_SID = "AC942a8a7cb62e795d7c0d0008386964e7";
    public static final String AUTH_TOKEN = "ed7d512b0dc60ec98ebb3036f8336c36";

    public static final String TWILIO_PHONE_NUMBER = "+12406522228";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendSms(String to, String messageBody) {
        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                messageBody
        ).create();

        System.out.println("Message SID: " + message.getSid());
    }
}

