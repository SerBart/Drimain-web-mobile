# Zgloszenia SSE API Documentation

## Server-Sent Events (SSE) Endpoint

### Endpoint: `GET /api/zgloszenia/stream`

Real-time stream of zgloszenia (issues) events for web and mobile clients.

### Authentication
- **Required**: Bearer token in Authorization header
- **Example**: `Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...`

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `types` | String | No | All types | Comma-separated list of event types to filter |
| `dzialId` | Long | No | All departments | Filter events by department ID |
| `autorId` | Long | No | All authors | Filter events by author ID |
| `full` | Boolean | No | `false` | Include full zgloszenie data in events |

### Event Types

| Type | Description | Payload |
|------|-------------|---------|
| `CREATED` | New zgloszenie created | Full data or minimal |
| `UPDATED` | Zgloszenie updated | Full data + changed fields list |
| `DELETED` | Zgloszenie deleted | Minimal (ID only) |
| `STATUS_CHANGED` | Status changed | Full data |
| `ATTACHMENT_ADDED` | Attachment added | Minimal + attachment ID |
| `ATTACHMENT_REMOVED` | Attachment removed | Minimal + attachment ID |
| `PRIORITY_CHANGED` | Priority changed | Full data |

### Event Format

Each event is sent in SSE format:

```
event: zgloszenie-event
data: {
  "type": "CREATED",
  "zgloszenieId": 123,
  "timestamp": "2025-01-01T10:30:00",
  "data": { ... },  // Full ZgloszenieDTO if full=true
  "changedFields": ["status", "tytul"],  // Only for UPDATED events
  "attachmentId": 456  // Only for attachment events
}
```

### Heartbeat

Connection keepalive heartbeat is sent every 15 seconds:

```
event: heartbeat
data: {"timestamp": "2025-01-01T10:30:15"}
```

### Connection Limits

- **Max connections**: 100 (configurable via `app.sse.max-clients`)
- **Timeout**: 30 minutes (configurable via `app.sse.timeout-minutes`)
- **Heartbeat interval**: 15 seconds (configurable via `app.sse.heartbeat-seconds`)

### Usage Examples

#### 1. Listen to all events with full data
```bash
curl -X GET "http://localhost:8080/api/zgloszenia/stream?full=true" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Accept: text/event-stream"
```

#### 2. Filter only CREATE and UPDATE events
```bash
curl -X GET "http://localhost:8080/api/zgloszenia/stream?types=CREATED,UPDATED" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Accept: text/event-stream"
```

#### 3. Filter by department
```bash
curl -X GET "http://localhost:8080/api/zgloszenia/stream?dzialId=5" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Accept: text/event-stream"
```

#### 4. JavaScript client example
```javascript
const eventSource = new EventSource('/api/zgloszenia/stream?types=CREATED,UPDATED&full=true', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});

eventSource.addEventListener('zgloszenie-event', function(event) {
  const data = JSON.parse(event.data);
  console.log('Zgloszenie event:', data.type, data.zgloszenieId);
  
  if (data.type === 'UPDATED' && data.changedFields) {
    console.log('Changed fields:', data.changedFields);
  }
});

eventSource.addEventListener('heartbeat', function(event) {
  console.log('Heartbeat at:', JSON.parse(event.data).timestamp);
});
```

### Error Handling

- **401 Unauthorized**: Missing or invalid authentication token
- **503 Service Unavailable**: Maximum connections limit reached
- **Connection timeout**: After 30 minutes of inactivity

### Configuration

Add to `application.yml`:

```yaml
app:
  sse:
    max-clients: 100        # Maximum concurrent connections
    timeout-minutes: 30     # Connection timeout
    heartbeat-seconds: 15   # Heartbeat interval
```