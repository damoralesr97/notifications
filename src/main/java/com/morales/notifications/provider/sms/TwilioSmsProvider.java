package com.morales.notifications.provider.sms;

import com.morales.notifications.exception.NotificationSendException;
import com.morales.notifications.provider.NotificationProvider;
import com.morales.notifications.provider.ProviderResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TwilioSmsProvider implements NotificationProvider<SmsProviderRequest> {

    private final String accountSid;
    private final String authToken;
    private final String fromPhoneNumber;

    public static TwilioSmsProvider withCredentials(String accountSid, String authToken, String fromPhoneNumber) {
        return new TwilioSmsProvider(accountSid, authToken, fromPhoneNumber);
    }

    @Override
    public void validate() {
        if (!accountSid.startsWith("AC")) {
            throw new NotificationSendException("Invalid Account SID");
        }
        if (authToken == null || authToken.length() < 10) {
            throw new NotificationSendException("Invalid Auth Token");
        }
    }

    @Override
    public ProviderResult send(SmsProviderRequest request) {
        validate();

        System.out.printf("[Twilio] Sending SMS to %s from %s: '%s'%n",
                request.getTo(), fromPhoneNumber, request.getMessage());

        return ProviderResult.success();
    }
}
