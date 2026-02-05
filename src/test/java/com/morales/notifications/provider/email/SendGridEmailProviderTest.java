package com.morales.notifications.provider.email;

import com.morales.notifications.exception.NotificationSendException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SendGridEmailProviderTest {

    @Test
    void shouldValidateSuccessfullyWithValidKey() {
        SendGridEmailProvider provider = SendGridEmailProvider.withApiKey("SG.valid-key");
        assertDoesNotThrow(provider::validate);
    }

    @Test
    void shouldThrowExceptionWhenApiKeyIsInvalid() {
        SendGridEmailProvider provider = SendGridEmailProvider.withApiKey("invalid-key");
        NotificationSendException exception = assertThrows(NotificationSendException.class, provider::validate);
        assertEquals("Invalid API Key", exception.getMessage());
    }
}
