API documentation for the school-chat server

# School-Chat Server API Reference

All endpoints are prefixed with `/v0/api`.

Responses for mutation endpoints contain:

| Field | Type | Description |
|---|---|---|
| `success` | `boolean` | Indicates operation success. |
| `message` | `string` | Human-readable status or error message. |
| `exitCode` | `integer` | Numeric code for the error condition (`0` = success). |

---

## 1. Health check

| Method | Path | Description |
|---|---|---|
| `GET` | `/v0/api/health` | Simple health probe. |

Response:

```json
{ "healthy": true }
```

---

## 2. Generate new ID (token)

| Method | Path | Description |
|---|---|---|
| `POST` | `/v0/api/ids` | Generates a new unique token. Optionally adds it to whitelist (`AddFreeWhitelist`). |

Response:

```json
{ "id": "a1b2c3d4e5f6g7h8i9j0" }
```

---

## 3. Register a new user

| Method | Path | Description |
|---|---|---|
| `POST` | `/v0/api/users` | Creates a new user in the system. |

Request body:

```json
{
  "id": "xyz987token",
  "login": "new-user",
  "password": "qwerty123"
}
```

Success:

```json
{ "success": true, "message": "User created successfully", "exitCode": 0 }
```

Common errors:

```json
{ "success": false, "message": "Not in whitelist.", "exitCode": 1 }
```

```json
{ "success": false, "message": "This login is prohibited", "exitCode": 5 }
```

```json
{ "success": false, "message": "Error creating User", "exitCode": 3 }
```

---

## 4. Send a chat message

| Method | Path | Description |
|---|---|---|
| `POST` | `/v0/api/messages` | Sends a message to the chat stream. |

Request body:

```json
{
  "login": "user",
  "password": "123",
  "id": "123",
  "text": "hello"
}
```

Example success:

```json
{ "success": true, "message": "Message sent successfully", "exitCode": 0 }
```

---

## 5. Get chat messages

| Method | Path | Description |
|---|---|---|
| `GET` | `/v0/api/messages?id=<token>` | Returns current chat history. |

Whitelist failure:

```json
{ "success": false, "message": "Not in whitelist.", "exitCode": 1 }
```

Success response is a JSON array of messages.

---

## 6. Send verification text

| Method | Path | Description |
|---|---|---|
| `POST` | `/v0/api/verification-texts` | Stores a verification sentence for a user. |

Request body:

```json
{
  "login": "user123",
  "password": "secret321",
  "id": "abc123token",
  "text": "I confirm this is my account"
}
```

Example success:

```json
{ "success": true, "message": null, "exitCode": 0 }
```

---

## Notes

- If `WhitelistMode = true`, requests that use unknown `id` fail with `exitCode = 1`.
- Common error codes:
  - `0` success
  - `1` not in whitelist
  - `2` authentication failed
  - `3` input validation error
  - `4` account banned
  - `5` login prohibited
