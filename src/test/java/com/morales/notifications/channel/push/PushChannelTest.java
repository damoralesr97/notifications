package com.morales.notifications.channel.push;

import com.morales.notifications.core.Notification;
import com.morales.notifications.exception.ValidationException;
import com.morales.notifications.provider.NotificationProvider;
import com.morales.notifications.provider.ProviderResult;
import com.morales.notifications.provider.push.PushProviderRequest;
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
class PushChannelTest {

    @Mock
    private NotificationProvider<PushProviderRequest> provider;

    @InjectMocks
    private PushChannel channel;

    @Test
    void shouldThrowExceptionWhenTokenIsMissing() {
        Notification notification = Notification.builder()
                .subject("Title")
                .message("Body")
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> channel.send(notification));
        assertEquals("Push requires 'to' token", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTitleIsMissing() {
        Notification notification = Notification.builder()
                .to("token-123")
                .message("Body")
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> channel.send(notification));
        assertEquals("Push requires title (subject)", exception.getMessage());
    }

    @Test
    void shouldCallProviderSendWhenInputIsValid() {
        Notification notification = Notification.builder()
                .to("token-123")
                .subject("Title")
                .message("Body")
                .build();

        when(provider.send(any(PushProviderRequest.class))).thenReturn(ProviderResult.success());

        channel.send(notification);

        verify(provider).send(any(PushProviderRequest.class));
    }
}
