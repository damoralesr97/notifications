package com.morales.notifications.channel;

import com.morales.notifications.core.ChannelType;
import com.morales.notifications.core.Notification;
import com.morales.notifications.core.NotificationResult;

public interface NotificationChannel {
    ChannelType supportedChannel();
    NotificationResult send(Notification notification);
}
