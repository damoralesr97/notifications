package com.morales.notifications.provider.email;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailProviderRequest {
    private String to;
    private String subject;
    private String body;
}