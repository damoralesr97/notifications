package com.morales.notifications.provider;

public interface NotificationProvider<T> {
    ProviderResult send(T request);
    void validate();
}
