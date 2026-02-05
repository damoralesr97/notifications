package com.morales.notifications.channel.email;

import com.morales.notifications.channel.NotificationChannel;
import com.morales.notifications.core.ChannelType;
import com.morales.notifications.core.Notification;
import com.morales.notifications.core.NotificationResult;
import com.morales.notifications.exception.ValidationException;
import com.morales.notifications.provider.NotificationProvider;
import com.morales.notifications.provider.ProviderResult;
import com.morales.notifications.provider.email.EmailProviderRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailChannel implements NotificationChannel {

    private final NotificationProvider<EmailProviderRequest> provider;

    public static EmailChannel withProvider(NotificationProvider<EmailProviderRequest> provider) {
        return new EmailChannel(provider);
    }

    @Override
    public ChannelType supportedChannel() {
        return ChannelType.EMAIL;
    }

    @Override
    public NotificationResult send(Notification notification) {
        validate(notification);

        EmailProviderRequest request = EmailProviderRequest.builder()
                .to(notification.getTo())
                .subject(notification.getSubject())
                .body(notification.getMessage())
                .build();

        ProviderResult result = provider.send(request);

        return result.isSuccess()
                ? NotificationResult.success()
                : NotificationResult.failure(result.getMessage());
    }

    private void validate(Notification notification) {
        if (notification.getTo() == null || !notification.getTo().contains("@")) {
            throw new ValidationException("Invalid email address");
        }
        if (notification.getSubject() == null) {
            throw new ValidationException("Email requires subject");
        }
        if (notification.getMessage() == null) {
            throw new ValidationException("Email requires message body");
        }
    }
}
