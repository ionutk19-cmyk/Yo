# API Endpoints Documentation

This document provides comprehensive documentation for all public API endpoints.

## Table of Contents

- [Authentication](#authentication)
- [Base URL](#base-url)
- [Error Handling](#error-handling)
- [Rate Limiting](#rate-limiting)
- [Endpoints](#endpoints)

## Authentication

All API requests require authentication using one of the following methods:

### API Key Authentication
```http
Authorization: Bearer YOUR_API_KEY
```

### OAuth 2.0
```http
Authorization: Bearer YOUR_ACCESS_TOKEN
```

## Base URL

```
https://api.example.com/v1
```

## Error Handling

The API uses conventional HTTP response codes to indicate success or failure:

- `200` - OK: Everything worked as expected
- `400` - Bad Request: The request was unacceptable
- `401` - Unauthorized: No valid API key provided
- `403` - Forbidden: The API key doesn't have permissions
- `404` - Not Found: The requested resource doesn't exist
- `429` - Too Many Requests: Too many requests hit the API
- `500` - Server Error: Something went wrong on our end

### Error Response Format

```json
{
  "error": {
    "code": "invalid_request",
    "message": "The request was invalid or cannot be served",
    "details": "Additional error details"
  }
}
```

## Rate Limiting

API requests are limited to:
- **Authenticated requests**: 1000 requests per hour
- **Unauthenticated requests**: 100 requests per hour

Rate limit information is included in response headers:
```http
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1609459200
```

## Endpoints

### Users

#### Get User Profile
```http
GET /users/{user_id}
```

**Parameters:**
- `user_id` (string, required): The unique identifier for the user

**Example Request:**
```bash
curl -X GET \
  https://api.example.com/v1/users/123 \
  -H 'Authorization: Bearer YOUR_API_KEY'
```

**Example Response:**
```json
{
  "id": "123",
  "username": "johndoe",
  "email": "john@example.com",
  "created_at": "2023-01-15T10:30:00Z",
  "updated_at": "2023-01-20T14:45:00Z",
  "profile": {
    "first_name": "John",
    "last_name": "Doe",
    "avatar_url": "https://example.com/avatars/123.jpg"
  }
}
```

#### Create User
```http
POST /users
```

**Request Body:**
```json
{
  "username": "string (required)",
  "email": "string (required)",
  "password": "string (required)",
  "profile": {
    "first_name": "string (optional)",
    "last_name": "string (optional)"
  }
}
```

**Example Request:**
```bash
curl -X POST \
  https://api.example.com/v1/users \
  -H 'Authorization: Bearer YOUR_API_KEY' \
  -H 'Content-Type: application/json' \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "password": "securepassword123",
    "profile": {
      "first_name": "New",
      "last_name": "User"
    }
  }'
```

**Example Response:**
```json
{
  "id": "456",
  "username": "newuser",
  "email": "newuser@example.com",
  "created_at": "2023-01-21T09:15:00Z",
  "profile": {
    "first_name": "New",
    "last_name": "User",
    "avatar_url": null
  }
}
```

#### Update User
```http
PUT /users/{user_id}
```

**Parameters:**
- `user_id` (string, required): The unique identifier for the user

**Request Body:**
```json
{
  "username": "string (optional)",
  "email": "string (optional)",
  "profile": {
    "first_name": "string (optional)",
    "last_name": "string (optional)"
  }
}
```

#### Delete User
```http
DELETE /users/{user_id}
```

**Parameters:**
- `user_id` (string, required): The unique identifier for the user

**Example Response:**
```json
{
  "message": "User deleted successfully"
}
```

### Posts

#### List Posts
```http
GET /posts
```

**Query Parameters:**
- `page` (integer, optional): Page number (default: 1)
- `limit` (integer, optional): Number of items per page (default: 10, max: 100)
- `author_id` (string, optional): Filter by author ID
- `status` (string, optional): Filter by status (`draft`, `published`, `archived`)

**Example Request:**
```bash
curl -X GET \
  'https://api.example.com/v1/posts?page=1&limit=10&status=published' \
  -H 'Authorization: Bearer YOUR_API_KEY'
```

**Example Response:**
```json
{
  "data": [
    {
      "id": "post_123",
      "title": "Getting Started with Our API",
      "content": "This post explains how to use our API...",
      "author_id": "user_456",
      "status": "published",
      "created_at": "2023-01-15T10:30:00Z",
      "updated_at": "2023-01-20T14:45:00Z"
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 10,
    "total": 25,
    "total_pages": 3,
    "has_next": true,
    "has_prev": false
  }
}
```

#### Get Post
```http
GET /posts/{post_id}
```

#### Create Post
```http
POST /posts
```

**Request Body:**
```json
{
  "title": "string (required)",
  "content": "string (required)",
  "status": "string (optional, default: 'draft')",
  "tags": ["string"] (optional)
}
```

#### Update Post
```http
PUT /posts/{post_id}
```

#### Delete Post
```http
DELETE /posts/{post_id}
```

## Webhooks

### Setting Up Webhooks

You can configure webhooks to receive real-time notifications when certain events occur:

```http
POST /webhooks
```

**Request Body:**
```json
{
  "url": "https://your-site.com/webhook",
  "events": ["user.created", "post.published"],
  "secret": "optional_webhook_secret"
}
```

### Webhook Events

Available webhook events:
- `user.created` - Triggered when a new user is created
- `user.updated` - Triggered when a user is updated
- `user.deleted` - Triggered when a user is deleted
- `post.created` - Triggered when a new post is created
- `post.published` - Triggered when a post is published
- `post.deleted` - Triggered when a post is deleted

### Webhook Payload Example

```json
{
  "event": "user.created",
  "timestamp": "2023-01-21T09:15:00Z",
  "data": {
    "id": "456",
    "username": "newuser",
    "email": "newuser@example.com"
  }
}
```

## SDKs and Libraries

Official SDKs are available for:
- [JavaScript/Node.js](https://github.com/example/api-js)
- [Python](https://github.com/example/api-python)
- [PHP](https://github.com/example/api-php)
- [Ruby](https://github.com/example/api-ruby)

## Testing

You can test the API using tools like:
- [Postman Collection](https://example.com/postman)
- [Swagger UI](https://api.example.com/docs)
- [curl examples](examples/curl-examples.md)