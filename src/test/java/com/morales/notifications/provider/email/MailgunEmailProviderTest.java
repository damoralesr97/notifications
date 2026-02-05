package com.morales.notifications.provider.email;

import com.morales.notifications.exception.NotificationSendException;
import com.morales.notifications.provider.ProviderResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MailgunEmailProviderTest {

    @Test
    void shouldValidateSuccessfullyWithValidCredentials() {
        MailgunEmailProvider provider = MailgunEmailProvider.withApiKeyAndDomain("key-123456", "example.com");
        assertDoesNotThrow(provider::validate);
    }

    @Test
    void shouldThrowExceptionWhenApiKeyIsInvalid() {
        MailgunEmailProvider provider = MailgunEmailProvider.withApiKeyAndDomain("invalid-key", "example.com");
        NotificationSendException exception = assertThrows(NotificationSendException.class, provider::validate);
        assertEquals("Invalid API Key", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDomainIsInvalid() {
        MailgunEmailProvider provider = MailgunEmailProvider.withApiKeyAndDomain("key-123456", "example.net");
        NotificationSendException exception = assertThrows(NotificationSendException.class, provider::validate);
        assertEquals("Invalid Domain", exception.getMessage());
    }
}
