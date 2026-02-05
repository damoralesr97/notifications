package com.morales.notifications.provider.email;

import com.morales.notifications.exception.NotificationSendException;
import com.morales.notifications.provider.NotificationProvider;
import com.morales.notifications.provider.ProviderResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MailgunEmailProvider implements NotificationProvider<EmailProviderRequest> {

    private final String apiKey;
    private final String domain;

    public static MailgunEmailProvider withApiKeyAndDomain(String apiKey, String domain) {
        return new MailgunEmailProvider(apiKey, domain);
    }

    @Override
    public void validate() {
        if (!apiKey.startsWith("key-")) {
            throw new NotificationSendException("Invalid API Key");
        }

        if (!domain.endsWith(".com") && !domain.endsWith(".org")) {
            throw new NotificationSendException("Invalid Domain");
        }
    }

    @Override
    public ProviderResult send(EmailProviderRequest request) {
        validate();

        System.out.printf("[Mailgun] Sending email to %s with subject '%s' using domain %s%n",
                request.getTo(), request.getSubject(), domain);

        return ProviderResult.success();
    }

}
