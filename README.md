
Test Input

```json
{
  "jsonrpc": "2.0",
  "id": "6e7bf51a-f364-42d7-a060-343da3cc4f43",
  "method": "SendMessage",
  "params": {
    "message": {
      "messageId": "messageId-1",
      "role": "ROLE_USER",
      "parts": [
        {
          "text": "Can you check systems?"
        }
      ]
    }
  }
}
```

Response `200 OK`

```json
{
  "error": null,
  "id": "6e7bf51a-f364-42d7-a060-343da3cc4f43",
  "jsonrpc": "2.0",
  "result": {
    "role": "ROLE_AGENT",
    "parts": [
      {
        "text": "Hello World",
        "metadata": null
      }
    ],
    "messageId": "d062faba-39e6-4ac6-b891-9e0ba1363a69",
    "contextId": "c2c16e07-dfad-40f1-958c-41d336d4363e",
    "taskId": "d38c2bc5-7491-4920-a290-93582d21a9e0",
    "referenceTaskIds": null,
    "metadata": null,
    "extensions": null
  }
}
```