package com.morales.notifications.provider.sms;

import com.morales.notifications.exception.NotificationSendException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwilioSmsProviderTest {

    @Test
    void shouldValidateSuccessfullyWithValidCredentials() {
        TwilioSmsProvider provider = TwilioSmsProvider.withCredentials("AC12345", "1234567890", "+15555555");
        assertDoesNotThrow(provider::validate);
    }

    @Test
    void shouldThrowExceptionWhenAccountSidIsInvalid() {
        TwilioSmsProvider provider = TwilioSmsProvider.withCredentials("INVALID_SID", "1234567890", "+15555555");
        NotificationSendException exception = assertThrows(NotificationSendException.class, provider::validate);
        assertEquals("Invalid Account SID", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAuthTokenIsInvalid() {
        TwilioSmsProvider provider = TwilioSmsProvider.withCredentials("AC12345", "short", "+15555555");
        NotificationSendException exception = assertThrows(NotificationSendException.class, provider::validate);
        assertEquals("Invalid Auth Token", exception.getMessage());
    }
}
