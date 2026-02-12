# School Chat Server

A small simple HTTP chat server.
made for use in school where all messengers are blocked.

## Summary

This project is a lightweight Java HTTP server that serves a chat UI and provides simple REST endpoints for creating users and sending/receiving messages. Source is under `src/main/java` and static UI files are under `src/main/resources/html`.

## Requirements

- Java 17+ (or the version configured in `build.gradle`)
- Git (optional)
- OpenSSL and `keytool` for creating TLS materials
- Gradle wrapper included (`./gradlew`)

## Build

From the repository root run:

```bash
./gradlew clean ShadowJar
```

This produces artifacts under `build/libs/`.

## Run (development)

```bash
./gradlew ShadowJar 
java -jar build/libs/school_chat_server_java-0.1-all.jar 
```

If the project produces a fat/shadow jar.

## Configuration & Data

- Server config stored in `school_chat_server_java/src/main/java/school/chat/core/Config.java`
- Stored runtime data (users, messages, verification text): the `server_data/` directory contains JSON files such as `users.json`, `chat_messages.json`, and `verification_text.json`.

Modify those files or the server source if you need different default data.

## Enabling TLS / SSL

There is a `keys/` directory in the repository intended to hold TLS keys and keystores. Below are example ways to create a self-signed certificate and a Java keystore (PKCS#12) usable by the server.

Important: these are examples for development and testing. For production use a CA-signed certificate (e.g., Let's Encrypt).

### 1) Create a self-signed certificate (OpenSSL)

```bash
# create a new private key and self-signed certificate (valid 1 year)
openssl req -x509 -newkey rsa:4096 -nodes \
  -keyout server.key -out server.crt -days 365 \
  -subj "/CN=localhost"

# create a PKCS#12 keystore from key+cert (password: changeit)
openssl pkcs12 -export -out keys/keystore.p12 \
  -inkey server.key -in server.crt -name schoolchat -passout pass:changeit

# secure the key files (optional)
chmod 600 keys/keystore.p12
```

Place `keystore.p12` inside the `keys/` directory (create it if missing).

### 2) Use Java `keytool` to convert/import

If you need a JKS keystore instead of PKCS#12:

```bash
keytool -importkeystore \
  -srckeystore keys/keystore.p12 -srcstoretype PKCS12 -srcstorepass changeit \
  -destkeystore keys/keystore.jks -deststoretype JKS -deststorepass changeit
```

### 3) Run the server with the keystore

If the server reads standard JVM SSL system properties, start it like this:

```bash
java \
  -Djavax.net.ssl.keyStore=keys/keystore.p12 \
  -Djavax.net.ssl.keyStorePassword=changeit \
  -jar build/libs/*.jar
```

If the server requires custom properties or a different keystore path, adjust accordingly in `src/main/java/school/chat/http_server/Server.java`.

### 4) Obtaining a real certificate (Let's Encrypt)

For a publicly reachable server, use Certbot or another ACME client to obtain certs. Example (on a host serving `example.com`):

```bash
# install certbot and run
sudo certbot certonly --standalone -d example.com

# Certbot will store keys at /etc/letsencrypt/live/example.com/
# Combine the fullchain and privkey into a PKCS12 keystore:
openssl pkcs12 -export -out keys/keystore.p12 \
  -in /etc/letsencrypt/live/example.com/fullchain.pem \
  -inkey /etc/letsencrypt/live/example.com/privkey.pem \
  -name schoolchat -passout pass:changeit
```

Remember to renew certificates and reload/restart the server after renewal.

## Notes

- The repository contains a `keys/` directory placeholder; do NOT commit private keys to public repositories. Add `keys/` to `.gitignore` if you store private materials there.
- If you change keystore passwords, update any scripts or service unit files accordingly.
- Review `src/main/java/school/chat/http_server/Server.java` for how the server reads TLS configuration and adapt the above commands if the project expects a different format or property names.

## Quick start checklist

1. Install Java 17+
2. Build: `./gradlew clean ShadowJar`
3. Create keystore (optional): generate `keys/keystore.p12`
4. Run: `java -jar build/libs/*.jar` (add `-Djavax.net.ssl.*` props to enable TLS)