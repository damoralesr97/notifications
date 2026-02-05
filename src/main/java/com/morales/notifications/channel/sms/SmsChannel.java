package com.morales.notifications.channel.sms;

import com.morales.notifications.channel.NotificationChannel;
import com.morales.notifications.core.ChannelType;
import com.morales.notifications.core.Notification;
import com.morales.notifications.core.NotificationResult;
import com.morales.notifications.exception.ValidationException;
import com.morales.notifications.provider.NotificationProvider;
import com.morales.notifications.provider.ProviderResult;
import com.morales.notifications.provider.sms.SmsProviderRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SmsChannel implements NotificationChannel {

    private final NotificationProvider<SmsProviderRequest> provider;

    public static SmsChannel withProvider(NotificationProvider<SmsProviderRequest> provider) {
        return new SmsChannel(provider);
    }

    @Override
    public ChannelType supportedChannel() {
        return ChannelType.SMS;
    }

    @Override
    public NotificationResult send(Notification notification) {
        validate(notification);

        SmsProviderRequest request = SmsProviderRequest.builder()
                .to(notification.getTo())
                .message(notification.getMessage())
                .build();

        ProviderResult result = provider.send(request);

        return result.isSuccess()
                ? NotificationResult.success()
                : NotificationResult.failure(result.getMessage());
    }

    private void validate(Notification notification) {
        if (notification.getTo() == null || notification.getTo().isEmpty()) {
            throw new ValidationException("SMS requires 'to' phone number");
        }
        if (notification.getMessage() == null) {
            throw new ValidationException("SMS requires message body");
        }
        if (notification.getMessage().length() > 160) {
            throw  new ValidationException("SMS message is too long. 160 characters are allowed.");
        }
    }
}
