package com.morales.notifications.channel.push;

import com.morales.notifications.channel.NotificationChannel;
import com.morales.notifications.core.ChannelType;
import com.morales.notifications.core.Notification;
import com.morales.notifications.core.NotificationResult;
import com.morales.notifications.exception.ValidationException;
import com.morales.notifications.provider.NotificationProvider;
import com.morales.notifications.provider.ProviderResult;
import com.morales.notifications.provider.push.PushProviderRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PushChannel implements NotificationChannel {

    private final NotificationProvider<PushProviderRequest> provider;

    public static PushChannel withProvider(NotificationProvider<PushProviderRequest> provider) {
        return new PushChannel(provider);
    }

    @Override
    public ChannelType supportedChannel() {
        return ChannelType.PUSH;
    }

    @Override
    public NotificationResult send(Notification notification) {
        validate(notification);

        PushProviderRequest request = PushProviderRequest.builder()
                .token(notification.getTo())
                .title(notification.getSubject())
                .body(notification.getMessage())
                .build();

        ProviderResult result = provider.send(request);

        return result.isSuccess()
                ? NotificationResult.success()
                : NotificationResult.failure(result.getMessage());
    }

    private void validate(Notification notification) {
        if (notification.getTo() == null || notification.getTo().isEmpty()) {
            throw new ValidationException("Push Notification requires 'to' token");
        }
        if (notification.getSubject() == null) {
            throw new ValidationException("Push Notification requires title (subject)");
        }
    }
}
