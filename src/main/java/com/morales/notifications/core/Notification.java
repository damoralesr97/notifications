package com.morales.notifications.core;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Notification {

    private ChannelType channel;
    private String to;
    private String subject;
    private String message;
}
