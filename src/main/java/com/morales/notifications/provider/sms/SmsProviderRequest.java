package com.morales.notifications.provider.sms;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmsProviderRequest {
    private String to;
    private String message;
}
