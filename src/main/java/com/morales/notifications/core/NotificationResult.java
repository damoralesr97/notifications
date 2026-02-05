package com.morales.notifications.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationResult {

    private boolean success;
    private String message;

    public static NotificationResult success() {
        return new NotificationResult(true, "Sent successfully");
    }

    public static NotificationResult failure(String error) {
        return new NotificationResult(false, error);
    }
}
