API documentation for the school‑chat server

# School‑Chat Server API Reference

All endpoints are prefixed with `/v0/api`.
The server is a Java Javalin application that serves JSON only for the `/api` routes.
Responses contain:

| Field     | Type   | Description       |
|-----------|---------|------------------|
| `success` | `boolean` | Indicates operation success (where applicable). |
| `message`  | `string` | Human‑readable status or error message. |
| `exitCode`| `integer` | Numeric code for the error condition (0 = success). |

---

## 1. Health Check

| Method | Path              | Description                                  |
|--------|-------------------|---------------------------------------------|
| `GET`  | `/v0/api/health`  | Simple health probe.                         |

**Response**

```json
{ "healthy": true }
```

———

## 2. Generate a new ID (Token)

| Method | Path              | Description                                    |
|--------|-------------------|-----------------------------------------------|
| GET  | /v0/api/get_id | Generates a new unique token. Optionally adds it to the whitelist
(AddFreeWhitelist). |

Query Parameters (optional)

- addFreeWhitelist – if true, the new ID is automatically whitelisted.

Response

```json
{ "id": "a1b2c3d4e5f6g7h8i9j0" }
```

———

## 3. Register a new user

| Method | Path                     | Description                         |
|--------|---------------------------|--------------------------------------|
| POST | /v0/api/create_user     | Creates a new user in the system.    |

Request Body

```json
{
"id":       "xyz987token",
"login":    "new-user",
"password": "qwerty123"
}
```

> Note
> If the server is running in whitelist mode and the supplied id is not in the whitelist, the operation
> fails.

Successful Response

```json
{ "success": true, "message": "User created successfully", "exitCode": 0 }
```

Error Responses

- Whitelist

```json
{ "success": false, "message": "Not in whitelist.", "exitCode": 1 }
```
- Prohibited login

```json
{ "success": false, "message": "This login is prohibited", "exitCode": 5 }
```
- Login already taken / other error

```json
{ "success": false, "message": "Error creating User", "exitCode": 3 }
```

———

## 4. Send a chat message

| Method | Path                     | Description                                    |
|--------|---------------------------|-------------------------------------------------|
| POST | /v0/api/send_chat_message | Broadcasts a message to the chat stream.       |

Request Body

```json
{
"login":    "user",
"password": "123",
"id":       "123",
"text":     "hello"
}
```

Possible Responses

| ExitCode | Message                      | Status |
|----------|------------------------------|--------|
| 0        | Message sent successfully | Success |
| 1        | Access denied: not in whitelist | Error |
| 2        | Incorrect login or password | Error |
| 3        | Invalid request format / Missing required fields | Error |
| 4        | Account is banned | Error |

```json
{ "success": true, "message": "Message sent successfully", "exitCode": 0 }
```

———

## 5. Retrieve chat messages

| Method | Path                     | Description                              |
|--------|---------------------------|-------------------------------------------|
| POST | /v0/api/get_chat_messages | Returns the current chat history.           |

Request Body

```json
{ "id": "a1b2c3d4e5f6g7h8i9j0" }
```

Responses

- Whitelist check failed

```json
{ "success": false, "message": "Not in whitelist.", "exitCode": 1 }
```

- Success – the body is a JSON array of chat messages (the format is defined by
Chat.GetChatMessageJsonString). No wrapper object is returned.

———

## 6. Send a verification text

| Method | Path                     | Description                          |
|--------|---------------------------|-------------------------------------|
| POST | /v0/api/send_verification_text | Stores a verification sentence for a user. |

Request Body

```json
{
"login":    "user123",
"password": "secret321",
"id":       "abc123token",
"text":     "I confirm this is my account"
}
```

| ExitCode | Message                            | Status |
|----------|------------------------------------|--------|
| 0        | null (no message)                | Success |
| 1        | Access denied: not in whitelist | Error |
| 2        | Incorrect login or password      | Error |
| 3        | Invalid json / Missing required fields | Error |
| 4        | Account is banned               | Error |

```json
{ "success": true, "message": null, "exitCode": 0 }
```

———

## General Notes

- Whitelist Mode – When enabled (WhitelistMode = true), any request that uses an id not present in the
whitelist fails with exit code 1.
- AddFreeWhitelist – A configuration flag (AddFreeWhitelist = true) automatically adds newly generated
IDs to the whitelist.
- Error Codes –
    - 0 – Success
    - 1 – Not in whitelist
    - 2 – Authentication failed
    - 3 – Input validation error
    - 4 – Account banned
    - 5 – Login prohibited
- All endpoints are /v0/api/*. Prefixing