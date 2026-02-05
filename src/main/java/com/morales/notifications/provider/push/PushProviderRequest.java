package com.morales.notifications.provider.push;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PushProviderRequest {
    private String token;
    private String title;
    private String body;
}
