package com.morales.notifications.core;

import com.morales.notifications.channel.NotificationChannel;
import com.morales.notifications.exception.NotificationSendException;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class NotificationService {

    private final Map<ChannelType, NotificationChannel> channels;

    public NotificationResult send(Notification notification) {
        NotificationChannel channel = channels.get(notification.getChannel());

        if (channel == null)
            throw new NotificationSendException("Channel not configured: " + notification.getChannel());

        return channel.send(notification);
    }

}
