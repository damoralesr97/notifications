package com.morales.notifications.provider;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProviderResult {

    private boolean success;
    private String message;

    public static ProviderResult success() {
        return new ProviderResult(true, "Provider sent successfully");
    }

    public static ProviderResult failure(String error) {
        return new ProviderResult(false, error);
    }
}
