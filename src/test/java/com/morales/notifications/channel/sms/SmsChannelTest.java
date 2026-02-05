package com.morales.notifications.channel.sms;

import com.morales.notifications.core.Notification;
import com.morales.notifications.exception.ValidationException;
import com.morales.notifications.provider.NotificationProvider;
import com.morales.notifications.provider.ProviderResult;
import com.morales.notifications.provider.sms.SmsProviderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsChannelTest {

    @Mock
    private NotificationProvider<SmsProviderRequest> provider;

    @InjectMocks
    private SmsChannel channel;

    @Test
    void shouldThrowExceptionWhenToIsMissing() {
        Notification notification = Notification.builder()
                .message("Hello")
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> channel.send(notification));
        assertEquals("SMS requires 'to' phone number", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMessageIsMissing() {
        Notification notification = Notification.builder()
                .to("+123456789")
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> channel.send(notification));
        assertEquals("SMS requires message body", exception.getMessage());
    }

    @Test
    void shouldCallProviderSendWhenInputIsValid() {
        Notification notification = Notification.builder()
                .to("+123456789")
                .message("Hello")
                .build();

        when(provider.send(any(SmsProviderRequest.class))).thenReturn(ProviderResult.success());

        channel.send(notification);

        verify(provider).send(any(SmsProviderRequest.class));
    }
}
