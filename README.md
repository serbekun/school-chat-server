# School Chat Server

A small simple HTTP chat server.
made for use in school where all messengers are blocked.

## Summary

This project is a lightweight Java HTTP server that serves a chat UI and provides simple REST endpoints for creating users and sending/receiving messages. Source is under `src/main/java` and static UI files are under `src/main/resources/html`.

## Requirements

- Java 17+ (or the version configured in `build.gradle`)
- Git (optional)
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

## Networking

The server runs over plain HTTP only. There is no built-in TLS configuration, and no keystore setup is required.

## Notes

- Review `src/main/java/school/chat/Main.java` if you need to change the server port or add extra Javalin configuration.

## Quick start checklist

1. Install Java 17+
2. Build: `./gradlew clean ShadowJar`
3. Run: `java -jar build/libs/*.jar`
