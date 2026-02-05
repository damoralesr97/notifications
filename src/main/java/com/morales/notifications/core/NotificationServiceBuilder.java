package com.morales.notifications.core;

import com.morales.notifications.channel.NotificationChannel;

import java.util.HashMap;
import java.util.Map;

public class NotificationServiceBuilder {

    private final Map<ChannelType, NotificationChannel> channels = new HashMap<>();

    public NotificationServiceBuilder registerChannel(NotificationChannel channel) {
        channels.put(channel.supportedChannel(), channel);
        return this;
    }

    public NotificationService build() {
        return new NotificationService(channels);
    }

}
