package com.morales.notifications.channel.email;

import com.morales.notifications.core.Notification;
import com.morales.notifications.exception.ValidationException;
import com.morales.notifications.provider.NotificationProvider;
import com.morales.notifications.provider.ProviderResult;
import com.morales.notifications.provider.email.EmailProviderRequest;
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
class EmailChannelTest {

    @Mock
    private NotificationProvider<EmailProviderRequest> provider;

    @InjectMocks
    private EmailChannel channel;

    @Test
    void shouldThrowExceptionWhenToIsMissing() {
        Notification notification = Notification.builder()
                .subject("Subject")
                .message("Body")
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> channel.send(notification));
        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenToIsInvalid() {
        Notification notification = Notification.builder()
                .to("invalid-email")
                .subject("Subject")
                .message("Body")
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> channel.send(notification));
        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSubjectIsMissing() {
        Notification notification = Notification.builder()
                .to("user@example.com")
                .message("Body")
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> channel.send(notification));
        assertEquals("Email requires subject", exception.getMessage());
    }

    @Test
    void shouldCallProviderSendWhenInputIsValid() {
        Notification notification = Notification.builder()
                .to("user@example.com")
                .subject("Subject")
                .message("Body")
                .build();

        when(provider.send(any(EmailProviderRequest.class))).thenReturn(ProviderResult.success());

        channel.send(notification);

        verify(provider).send(any(EmailProviderRequest.class));
    }
}
