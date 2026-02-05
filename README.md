# Notification Service Library

Una librería Java modular y extensible para enviar notificaciones a través de múltiples canales (Email, SMS, Push) con soporte para diferentes proveedores.

## 📋 Tabla de Contenidos

- [Instalación](#instalación)
- [Quick Start](#quick-start)
- [Configuración](#configuración)
- [Proveedores Soportados](#proveedores-soportados)
- [API Reference](#api-reference)
- [Extensión](#extensión)
- [Seguridad](#seguridad)
- [Testing](#testing)
- [Docker](#docker)

## 📦 Instalación

### Maven

Agrega la dependencia a tu `pom.xml`:

```xml
<dependency>
    <groupId>com.morales</groupId>
    <artifactId>notifications</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Gradle

Agrega la dependencia a tu `build.gradle`:

```gradle
implementation 'com.morales:notifications:1.0-SNAPSHOT'
```

### Requisitos

- Java 21 o superior
- Maven 3.9+ (para compilar desde el código fuente)

## 🚀 Quick Start

### Ejemplo Básico - Email

```java
import com.morales.notifications.core.*;
import com.morales.notifications.channel.email.EmailChannel;
import com.morales.notifications.provider.email.MailgunEmailProvider;

public class Example {
    public static void main(String[] args) {
        // 1. Crear el servicio con un canal
        NotificationService service = NotificationServiceFactory.builder()
            .registerChannel(
                EmailChannel.withProvider(
                    MailgunEmailProvider.withApiKeyAndDomain("key-123", "example.com")
                )
            )
            .build();

        // 2. Crear la notificación
        Notification notification = Notification.builder()
            .channel(ChannelType.EMAIL)
            .to("user@example.com")
            .subject("Bienvenido")
            .message("Hola, bienvenido a nuestro servicio!")
            .build();

        // 3. Enviar
        NotificationResult result = service.send(notification);
        
        if (result.isSuccess()) {
            System.out.println("Enviado correctamente!");
        }
    }
}
```

### Ejemplo con Múltiples Canales

```java
NotificationService service = NotificationServiceFactory.builder()
    .registerChannel(
        EmailChannel.withProvider(
            SendGridEmailProvider.withApiKey("SG.your_key")
        )
    )
    .registerChannel(
        SmsChannel.withProvider(
            TwilioSmsProvider.withCredentials("AC_sid", "token", "+1234567890")
        )
    )
    .registerChannel(
        PushChannel.withProvider(
            FcmPushProvider.withServiceAccount("firebase-key.json")
        )
    )
    .build();

// Enviar email
service.send(Notification.builder()
    .channel(ChannelType.EMAIL)
    .to("user@example.com")
    .subject("Alerta")
    .message("Contenido del email")
    .build());

// Enviar SMS
service.send(Notification.builder()
    .channel(ChannelType.SMS)
    .to("+573001234567")
    .message("Tu código es: 1234")
    .build());

// Enviar Push
service.send(Notification.builder()
    .channel(ChannelType.PUSH)
    .to("device_token_abc")
    .subject("Nueva notificación")
    .message("Tienes un mensaje nuevo")
    .build());
```

## ⚙️ Configuración

### Email - Mailgun

```java
MailgunEmailProvider provider = MailgunEmailProvider.withApiKeyAndDomain(
    "key-your_api_key",  // API Key (debe empezar con "key-")
    "yourdomain.com"     // Dominio verificado (.com o .org)
);
```

**Validaciones:**
- API Key debe empezar con `key-`
- Dominio debe terminar en `.com` o `.org`

### Email - SendGrid

```java
SendGridEmailProvider provider = SendGridEmailProvider.withApiKey(
    "SG.your_api_key"  // API Key (debe empezar con "SG.")
);
```

**Validaciones:**
- API Key debe empezar con `SG.`

### SMS - Twilio

```java
TwilioSmsProvider provider = TwilioSmsProvider.withCredentials(
    "AC_account_sid",     // Account SID (debe empezar con "AC")
    "auth_token",         // Auth Token (mínimo 10 caracteres)
    "+1234567890"         // Número de origen
);
```

**Validaciones:**
- Account SID debe empezar con `AC`
- Auth Token debe tener al menos 10 caracteres
- Mensaje máximo: 160 caracteres

### Push - Firebase Cloud Messaging (FCM)

```java
FcmPushProvider provider = FcmPushProvider.withServiceAccount(
    "path/to/firebase-service-account.json"  // Ruta al archivo de credenciales
);
```

**Validaciones:**
- Service Account Key no puede estar vacío
- El título (subject) es obligatorio

## 🔌 Proveedores Soportados

| Canal | Proveedor | Clase | Estado |
|-------|-----------|-------|--------|
| Email | Mailgun | `MailgunEmailProvider` | ✅ Implementado |
| Email | SendGrid | `SendGridEmailProvider` | ✅ Implementado |
| SMS | Twilio | `TwilioSmsProvider` | ✅ Implementado |
| Push | FCM | `FcmPushProvider` | ✅ Implementado |

## 📚 API Reference

### Clases Principales

#### `NotificationService`

Servicio principal para enviar notificaciones.

```java
public interface NotificationService {
    NotificationResult send(Notification notification);
}
```

#### `NotificationServiceFactory`

Factory para construir el servicio.

```java
NotificationService service = NotificationServiceFactory.builder()
    .registerChannel(channel)
    .build();
```

#### `Notification`

Modelo de notificación.

```java
Notification notification = Notification.builder()
    .channel(ChannelType.EMAIL)  // EMAIL, SMS, PUSH
    .to("destinatario")           // Email, teléfono, o device token
    .subject("Asunto")            // Opcional para SMS
    .message("Mensaje")
    .build();
```

#### `NotificationResult`

Resultado de la operación de envío.

```java
public class NotificationResult {
    boolean isSuccess();
    String getMessage();
}
```

### Interfaces para Extensión

#### `NotificationProvider<T>`

Interfaz para implementar nuevos proveedores.

```java
public interface NotificationProvider<T> {
    void validate();
    ProviderResult send(T request);
}
```

#### `NotificationChannel`

Interfaz para implementar nuevos canales.

```java
public interface NotificationChannel {
    ChannelType supportedChannel();
    NotificationResult send(Notification notification);
}
```

## 🔧 Extensión

### Agregar un Nuevo Proveedor

1. **Crear la clase del proveedor:**

```java
package com.morales.notifications.provider.email;

import com.morales.notifications.provider.NotificationProvider;
import com.morales.notifications.provider.ProviderResult;
import com.morales.notifications.exception.NotificationSendException;

public class AmazonSesProvider implements NotificationProvider<EmailProviderRequest> {
    
    private final String accessKey;
    private final String secretKey;
    
    public static AmazonSesProvider withCredentials(String accessKey, String secretKey) {
        return new AmazonSesProvider(accessKey, secretKey);
    }
    
    @Override
    public void validate() {
        if (accessKey == null || accessKey.isEmpty()) {
            throw new NotificationSendException("Access Key is required");
        }
        // Más validaciones...
    }
    
    @Override
    public ProviderResult send(EmailProviderRequest request) {
        validate();
        
        // Lógica de envío con Amazon SES
        // ...
        
        return ProviderResult.success();
    }
}
```

2. **Usar el nuevo proveedor:**

```java
NotificationService service = NotificationServiceFactory.builder()
    .registerChannel(
        EmailChannel.withProvider(
            AmazonSesProvider.withCredentials("access_key", "secret_key")
        )
    )
    .build();
```

### Agregar un Nuevo Canal

1. **Crear el Request:**

```java
@Getter
@Builder
public class SlackProviderRequest {
    private String webhookUrl;
    private String message;
}
```

2. **Crear el Provider:**

```java
public class SlackProvider implements NotificationProvider<SlackProviderRequest> {
    // Implementación...
}
```

3. **Crear el Channel:**

```java
public class SlackChannel implements NotificationChannel {
    
    private final NotificationProvider<SlackProviderRequest> provider;
    
    @Override
    public ChannelType supportedChannel() {
        return ChannelType.SLACK;
    }
    
    @Override
    public NotificationResult send(Notification notification) {
        // Validar y delegar al provider
    }
}
```

4. **Agregar el tipo al enum:**

```java
public enum ChannelType {
    EMAIL,
    SMS,
    PUSH,
    SLACK  // Nuevo
}
```

## 🔒 Seguridad

### Mejores Prácticas para Credenciales

#### ❌ NO hagas esto:

```java
// NUNCA hardcodees credenciales en el código
MailgunEmailProvider.withApiKeyAndDomain("key-123456", "example.com");
```

#### ✅ Haz esto:

**1. Variables de Entorno**

```java
String apiKey = System.getenv("MAILGUN_API_KEY");
String domain = System.getenv("MAILGUN_DOMAIN");

MailgunEmailProvider.withApiKeyAndDomain(apiKey, domain);
```

**2. Archivos de Configuración (fuera del repositorio)**

```java
Properties props = new Properties();
props.load(new FileInputStream("config/credentials.properties"));

String apiKey = props.getProperty("mailgun.api.key");
```

Agrega a `.gitignore`:
```
config/credentials.properties
firebase-service-account.json
```

**3. Gestores de Secretos**

Para producción, usa servicios como:
- AWS Secrets Manager
- Azure Key Vault
- HashiCorp Vault
- Google Cloud Secret Manager

**4. Spring Boot (application.properties)**

```properties
# application.properties
mailgun.api.key=${MAILGUN_API_KEY}
mailgun.domain=${MAILGUN_DOMAIN}
```

```java
@Value("${mailgun.api.key}")
private String apiKey;

@Value("${mailgun.domain}")
private String domain;
```

### Validación de Entrada

La librería incluye validaciones automáticas:

- **Email**: Formato válido, subject obligatorio
- **SMS**: Número de teléfono, longitud máxima (160 caracteres)
- **Push**: Device token, título obligatorio
- **Proveedores**: Formato de credenciales

### Manejo de Errores

```java
try {
    NotificationResult result = service.send(notification);
    
    if (!result.isSuccess()) {
        logger.error("Failed to send: {}", result.getMessage());
    }
    
} catch (ValidationException e) {
    // Error de validación de entrada
    logger.error("Validation error: {}", e.getMessage());
    
} catch (NotificationSendException e) {
    // Error del proveedor
    logger.error("Provider error: {}", e.getMessage());
}
```

## 🧪 Testing

### Ejecutar Tests

```bash
mvn test
```

### Tests Incluidos

- **Provider Tests**: Validación de credenciales
- **Channel Tests**: Validación de entrada y delegación
- **Integration Tests**: Flujo completo

### Ejemplo de Test

```java
@Test
void shouldThrowExceptionWhenApiKeyIsInvalid() {
    MailgunEmailProvider provider = MailgunEmailProvider
        .withApiKeyAndDomain("invalid-key", "example.com");
    
    assertThrows(NotificationSendException.class, () -> {
        provider.validate();
    });
}
```

## 🐳 Docker

### Construir la Imagen

```bash
docker build -t notifications-demo .
```

### Ejecutar el Contenedor

```bash
docker run --rm notifications-demo
```

### Dockerfile Multi-Stage

El proyecto incluye un `Dockerfile` optimizado:
- **Build Stage**: Compila con Maven
- **Runtime Stage**: Ejecuta con JRE ligero (Eclipse Temurin 21)

## 📝 Ejemplos Completos

Revisa la clase `MainExample.java` para ver ejemplos de:
- ✅ Envíos exitosos
- ❌ Validaciones de canal (email inválido, mensaje largo, etc.)
- ❌ Validaciones de proveedor (credenciales inválidas)