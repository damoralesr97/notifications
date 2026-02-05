package com.morales.notifications.provider.push;

import com.morales.notifications.exception.NotificationSendException;
import com.morales.notifications.provider.NotificationProvider;
import com.morales.notifications.provider.ProviderResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FcmPushProvider implements NotificationProvider<PushProviderRequest> {

    private final String serviceAccountKey;

    public static FcmPushProvider withServiceAccount(String serviceAccountKey) {
        return new FcmPushProvider(serviceAccountKey);
    }

    @Override
    public void validate() {
        if (serviceAccountKey == null || serviceAccountKey.isEmpty()) {
            throw new NotificationSendException("FCM Service Account Key is required");
        }
    }

    @Override
    public ProviderResult send(PushProviderRequest request) {
        validate();

        System.out.printf("[FCM] Sending Push to %s: '%s' - '%s'%n",
                request.getToken(), request.getTitle(), request.getBody());

        return ProviderResult.success();
    }
}
