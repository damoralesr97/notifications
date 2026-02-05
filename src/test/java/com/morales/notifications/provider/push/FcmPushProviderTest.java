package com.morales.notifications.provider.push;

import com.morales.notifications.exception.NotificationSendException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FcmPushProviderTest {

    @Test
    void shouldValidateSuccessfullyWithValidKey() {
        FcmPushProvider provider = FcmPushProvider.withServiceAccount("key.json");
        assertDoesNotThrow(provider::validate);
    }

    @Test
    void shouldThrowExceptionWhenKeyIsNull() {
        FcmPushProvider provider = FcmPushProvider.withServiceAccount(null);
        NotificationSendException exception = assertThrows(NotificationSendException.class, provider::validate);
        assertEquals("FCM Service Account Key is required", exception.getMessage());
    }
}
