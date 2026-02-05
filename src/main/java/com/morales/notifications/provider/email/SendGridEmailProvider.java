package com.morales.notifications.provider.email;

import com.morales.notifications.exception.NotificationSendException;
import com.morales.notifications.provider.NotificationProvider;
import com.morales.notifications.provider.ProviderResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SendGridEmailProvider implements NotificationProvider<EmailProviderRequest> {

    private final String apiKey;

    public static SendGridEmailProvider withApiKey(String apiKey) {
        return new SendGridEmailProvider(apiKey);
    }

    @Override
    public void validate() {
        if (!apiKey.startsWith("SG.")) {
            throw new NotificationSendException("Invalid API Key");
        }
    }

    @Override
    public ProviderResult send(EmailProviderRequest request) {
        validate();

        System.out.printf("[SendGrid] Sending email to %s with subject '%s'%n", request.getTo(), request.getSubject());

        return ProviderResult.success();
    }

}
