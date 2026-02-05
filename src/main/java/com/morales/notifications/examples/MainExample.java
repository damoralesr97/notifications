package com.morales.notifications.examples;

import com.morales.notifications.channel.email.EmailChannel;
import com.morales.notifications.core.*;
import com.morales.notifications.provider.email.SendGridEmailProvider;
import com.morales.notifications.provider.email.MailgunEmailProvider;
import com.morales.notifications.channel.sms.SmsChannel;
import com.morales.notifications.provider.sms.TwilioSmsProvider;
import com.morales.notifications.channel.push.PushChannel;
import com.morales.notifications.provider.push.FcmPushProvider;
import com.morales.notifications.exception.NotificationSendException;
import com.morales.notifications.exception.ValidationException;

public class MainExample {

        public static void main(String[] args) {
                System.out.println("=== NOTIFICATION SERVICE EXAMPLES ===\n");

                runMailgunExample();
                runSendGridExample();
                runTwilioExample();
                runFcmExample();
        }

        // --- EMAIL: MAILGUN ---
        private static void runMailgunExample() {
                System.out.println("--- Mailgun Provider Examples ---");

                // 1. Success Case
                try {
                        System.out.println("1. Testing Successful Send...");
                        NotificationService service = NotificationServiceFactory.builder()
                                        .registerChannel(
                                                        EmailChannel.withProvider(
                                                                        MailgunEmailProvider.withApiKeyAndDomain(
                                                                                        "key-123", "example.com")))
                                        .build();

                        NotificationResult result = service.send(Notification.builder()
                                        .channel(ChannelType.EMAIL)
                                        .to("user@example.com")
                                        .subject("Welcome via Mailgun")
                                        .message("Hello from Mailgun!")
                                        .build());
                        System.out.println("   Result: " + result.getMessage());

                } catch (Exception e) {
                        System.out.println("   Error: " + e.getMessage());
                }

                // 2. Failure Case: Invalid Domain (Provider Validation)
                try {
                        System.out.println("2. Testing Provider Validation Failure (Invalid Domain)...");
                        NotificationService service = NotificationServiceFactory.builder()
                                        .registerChannel(
                                                        EmailChannel.withProvider(
                                                                        MailgunEmailProvider.withApiKeyAndDomain(
                                                                                        "key-valid",
                                                                                        "invalid-domain.net")))
                                        .build();

                        Notification notification = Notification.builder()
                                        .channel(ChannelType.EMAIL)
                                        .to("user@example.com")
                                        .subject("Fail")
                                        .message("This should fail")
                                        .build();

                        service.send(notification);

                } catch (NotificationSendException | IllegalArgumentException e) {
                        System.out.println("   Caught Expected Exception: " + e.getMessage());
                } catch (Exception e) {
                        System.out.println("   Caught Unexpected Exception: " + e.getMessage());
                }
                System.out.println();
        }

        // --- EMAIL: SENDGRID ---
        private static void runSendGridExample() {
                System.out.println("--- SendGrid Provider Examples ---");

                // 1. Success
                try {
                        System.out.println("1. Testing Successful Send...");
                        NotificationService service = NotificationServiceFactory.builder()
                                        .registerChannel(
                                                        EmailChannel.withProvider(
                                                                        SendGridEmailProvider.withApiKey(
                                                                                        "SG.valid_key_12345")))
                                        .build();

                        NotificationResult result = service.send(Notification.builder()
                                        .channel(ChannelType.EMAIL)
                                        .to("user@sendgrid.com")
                                        .subject("Welcome via SendGrid")
                                        .message("Hello from SendGrid!")
                                        .build());
                        System.out.println("   Result: " + result.getMessage());
                } catch (Exception e) {
                        System.out.println("   Error: " + e.getMessage());
                }

                // 2. Failure: Invalid Email (Channel Validation)
                try {
                        System.out.println("2. Testing Channel Validation Failure (Invalid/Missing Email)...");
                        NotificationService service = NotificationServiceFactory.builder()
                                        .registerChannel(
                                                        EmailChannel.withProvider(
                                                                        SendGridEmailProvider
                                                                                        .withApiKey("SG.valid_key")))
                                        .build();

                        // Sending without a valid 'to' email
                        service.send(Notification.builder()
                                        .channel(ChannelType.EMAIL)
                                        .to("invalid-email-format")
                                        .subject("Subject")
                                        .message("Body")
                                        .build());

                } catch (ValidationException e) {
                        System.out.println("   Caught Expected Exception: " + e.getMessage());
                } catch (Exception e) {
                        System.out.println("   Caught Unexpected Exception: " + e.getMessage());
                }
                System.out.println();
        }

        // --- SMS: TWILIO ---
        private static void runTwilioExample() {
                System.out.println("--- Twilio SMS Examples ---");

                // 1. Success
                try {
                        System.out.println("1. Testing Successful Send...");
                        NotificationService service = NotificationServiceFactory.builder()
                                        .registerChannel(
                                                        SmsChannel.withProvider(
                                                                        TwilioSmsProvider.withCredentials(
                                                                                        "AC_ACCOUNT_SID_123",
                                                                                        "AUTH_TOKEN_TOKEN", "+12345")))
                                        .build();

                        NotificationResult result = service.send(Notification.builder()
                                        .channel(ChannelType.SMS)
                                        .to("+15550001")
                                        .message("Hello via SMS")
                                        .build());
                        System.out.println("   Result: " + result.getMessage());
                } catch (Exception e) {
                        System.out.println("   Error: " + e.getMessage());
                }

                // 2. Failure: Message Too Long (Channel Validation)
                try {
                        System.out.println("2. Testing Channel Validation Failure (Message too long)...");
                        NotificationService service = NotificationServiceFactory.builder()
                                        .registerChannel(
                                                        SmsChannel.withProvider(
                                                                        TwilioSmsProvider.withCredentials(
                                                                                        "AC_VALID_SID", "AUTH_TOKEN",
                                                                                        "+123")))
                                        .build();

                        // Create a message longer than 160 chars
                        String longMessage = "A".repeat(161);
                        service.send(Notification.builder().channel(ChannelType.SMS).to("+123456").message(longMessage)
                                        .build());

                } catch (ValidationException e) {
                        System.out.println("   Caught Expected Exception: " + e.getMessage());
                } catch (Exception e) {
                        System.out.println("   Caught Exception: " + e.getMessage());
                }
                System.out.println();
        }

        // --- PUSH: FCM ---
        private static void runFcmExample() {
                System.out.println("--- FCM Push Examples ---");

                // 1. Success
                try {
                        System.out.println("1. Testing Successful Send...");
                        NotificationService service = NotificationServiceFactory.builder()
                                        .registerChannel(
                                                        PushChannel.withProvider(
                                                                        FcmPushProvider.withServiceAccount(
                                                                                        "valid-service-account.json")))
                                        .build();

                        NotificationResult result = service.send(Notification.builder()
                                        .channel(ChannelType.PUSH)
                                        .to("device_token_abc")
                                        .subject("Push Title")
                                        .message("Push Body")
                                        .build());
                        System.out.println("   Result: " + result.getMessage());

                } catch (Exception e) {
                        System.out.println("   Error: " + e.getMessage());
                }

                // 2. Failure: Missing Title (Channel Validation)
                try {
                        System.out.println("2. Testing Channel Validation Failure (Missing Title/Subject)...");
                        NotificationService service = NotificationServiceFactory.builder()
                                        .registerChannel(
                                                        PushChannel.withProvider(
                                                                        FcmPushProvider.withServiceAccount(
                                                                                        "valid-key.json")))
                                        .build();

                        // Missing subject
                        service.send(Notification.builder()
                                        .channel(ChannelType.PUSH)
                                        .to("device_token")
                                        // .subject("Missing Subject")
                                        .message("Body")
                                        .build());

                } catch (ValidationException e) {
                        System.out.println("   Caught Expected Exception: " + e.getMessage());
                } catch (Exception e) {
                        System.out.println("   Caught Exception: " + e.getMessage());
                }
                System.out.println();
        }
}
