# Authentication Guide

This guide covers all authentication methods, token management, and security best practices for the API.

## Table of Contents

- [Overview](#overview)
- [Authentication Methods](#authentication-methods)
- [Token Management](#token-management)
- [Security Best Practices](#security-best-practices)
- [Implementation Examples](#implementation-examples)
- [Troubleshooting](#troubleshooting)

## Overview

The API supports multiple authentication methods to accommodate different use cases:

- **API Key Authentication**: For server-to-server communication
- **OAuth 2.0**: For user authorization with third-party applications
- **JWT Tokens**: For stateless authentication
- **Session-based**: For traditional web applications

## Authentication Methods

### API Key Authentication

API keys are suitable for server-to-server communication and do not expire automatically.

**Request Format:**
```http
Authorization: Bearer YOUR_API_KEY
```

**Example:**
```bash
curl -X GET \
  https://api.example.com/v1/users \
  -H 'Authorization: Bearer sk_live_1234567890abcdef'
```

### OAuth 2.0 Flow

OAuth 2.0 is recommended for applications that need to access user data on behalf of users.

#### Authorization Code Flow

1. **Redirect to Authorization Server**
   ```http
   GET https://auth.example.com/oauth/authorize?
     response_type=code&
     client_id=YOUR_CLIENT_ID&
     redirect_uri=YOUR_REDIRECT_URI&
     scope=read write&
     state=random_state_string
   ```

2. **Exchange Code for Token**
   ```http
   POST https://auth.example.com/oauth/token
   Content-Type: application/x-www-form-urlencoded

   grant_type=authorization_code&
   code=AUTHORIZATION_CODE&
   client_id=YOUR_CLIENT_ID&
   client_secret=YOUR_CLIENT_SECRET&
   redirect_uri=YOUR_REDIRECT_URI
   ```

3. **Token Response**
   ```json
   {
     "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "token_type": "Bearer",
     "expires_in": 3600,
     "refresh_token": "def50200abc123...",
     "scope": "read write"
   }
   ```

### JWT Token Authentication

JWT tokens provide stateless authentication and include user information in the token payload.

**Token Structure:**
```
Header.Payload.Signature
```

**Example Token:**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

**Decoded Payload:**
```json
{
  "sub": "1234567890",
  "name": "John Doe",
  "iat": 1516239022,
  "exp": 1516242622,
  "scope": ["read", "write"]
}
```

## Token Management

### Token Lifecycle

1. **Obtain Token**: Through authentication flow
2. **Use Token**: Include in API requests
3. **Refresh Token**: Before expiration
4. **Revoke Token**: When no longer needed

### Automatic Token Refresh

```javascript
class TokenManager {
  constructor(clientId, clientSecret) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.accessToken = null;
    this.refreshToken = null;
    this.expiresAt = null;
  }

  async getValidToken() {
    if (this.isTokenExpired()) {
      await this.refreshAccessToken();
    }
    return this.accessToken;
  }

  isTokenExpired() {
    if (!this.expiresAt) return true;
    return Date.now() >= this.expiresAt - 60000; // Refresh 1 minute before expiry
  }

  async refreshAccessToken() {
    const response = await fetch('https://auth.example.com/oauth/token', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: new URLSearchParams({
        grant_type: 'refresh_token',
        refresh_token: this.refreshToken,
        client_id: this.clientId,
        client_secret: this.clientSecret,
      }),
    });

    const data = await response.json();
    
    if (response.ok) {
      this.accessToken = data.access_token;
      this.refreshToken = data.refresh_token;
      this.expiresAt = Date.now() + (data.expires_in * 1000);
    } else {
      throw new Error('Failed to refresh token');
    }
  }
}
```

### Token Storage

**Browser Environment:**
```javascript
// Secure storage using httpOnly cookies (recommended)
function setAuthCookie(token) {
  document.cookie = `auth_token=${token}; Secure; HttpOnly; SameSite=Strict; Max-Age=3600`;
}

// Alternative: localStorage (less secure)
function storeToken(token) {
  localStorage.setItem('auth_token', token);
}

function getStoredToken() {
  return localStorage.getItem('auth_token');
}

function clearToken() {
  localStorage.removeItem('auth_token');
}
```

**Node.js Environment:**
```javascript
// Environment variables
process.env.API_KEY = 'your_api_key';

// Secure file storage
const fs = require('fs');
const path = require('path');

function storeToken(token) {
  const tokenPath = path.join(process.env.HOME, '.app_tokens');
  fs.writeFileSync(tokenPath, JSON.stringify({ token }), { mode: 0o600 });
}

function loadToken() {
  try {
    const tokenPath = path.join(process.env.HOME, '.app_tokens');
    const data = fs.readFileSync(tokenPath, 'utf8');
    return JSON.parse(data).token;
  } catch (error) {
    return null;
  }
}
```

## Security Best Practices

### Token Security

1. **Never expose tokens in client-side code**
2. **Use HTTPS for all authentication requests**
3. **Implement token rotation**
4. **Set appropriate token expiration times**
5. **Revoke tokens when no longer needed**

### API Key Management

```javascript
// ❌ Don't do this
const apiKey = 'sk_live_1234567890abcdef'; // Hardcoded

// ✅ Do this instead
const apiKey = process.env.API_KEY;

// ✅ Or use a secure configuration service
const config = await getSecureConfig();
const apiKey = config.apiKey;
```

### Request Validation

```javascript
function validateAuthHeader(req) {
  const authHeader = req.headers.authorization;
  
  if (!authHeader) {
    throw new Error('Authorization header required');
  }
  
  if (!authHeader.startsWith('Bearer ')) {
    throw new Error('Invalid authorization format');
  }
  
  const token = authHeader.substring(7);
  
  if (!token) {
    throw new Error('Token required');
  }
  
  return token;
}
```

### Rate Limiting by Authentication

Different rate limits based on authentication method:

```javascript
const rateLimits = {
  anonymous: { requests: 100, window: 3600 }, // 100/hour
  apiKey: { requests: 1000, window: 3600 },   // 1000/hour
  oauth: { requests: 5000, window: 3600 },    // 5000/hour
};

function getRateLimit(authType) {
  return rateLimits[authType] || rateLimits.anonymous;
}
```

## Implementation Examples

### React Application

```jsx
import { createContext, useContext, useEffect, useState } from 'react';

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [token, setToken] = useState(null);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check for existing token on mount
    const storedToken = localStorage.getItem('auth_token');
    if (storedToken) {
      validateAndSetToken(storedToken);
    } else {
      setLoading(false);
    }
  }, []);

  const validateAndSetToken = async (token) => {
    try {
      const response = await fetch('/api/auth/validate', {
        headers: { Authorization: `Bearer ${token}` }
      });

      if (response.ok) {
        const userData = await response.json();
        setToken(token);
        setUser(userData);
      } else {
        localStorage.removeItem('auth_token');
      }
    } catch (error) {
      console.error('Token validation failed:', error);
    } finally {
      setLoading(false);
    }
  };

  const login = async (credentials) => {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(credentials)
    });

    if (response.ok) {
      const { token, user } = await response.json();
      localStorage.setItem('auth_token', token);
      setToken(token);
      setUser(user);
      return { success: true };
    } else {
      const error = await response.json();
      return { success: false, error: error.message };
    }
  };

  const logout = () => {
    localStorage.removeItem('auth_token');
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{
      token,
      user,
      loading,
      login,
      logout,
      isAuthenticated: !!token
    }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
```

### Express.js Middleware

```javascript
const jwt = require('jsonwebtoken');

function authenticateToken(req, res, next) {
  try {
    const token = validateAuthHeader(req);
    
    // Verify JWT token
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    req.user = decoded;
    next();
  } catch (error) {
    if (error.name === 'TokenExpiredError') {
      return res.status(401).json({ error: 'Token expired' });
    } else if (error.name === 'JsonWebTokenError') {
      return res.status(401).json({ error: 'Invalid token' });
    } else {
      return res.status(401).json({ error: error.message });
    }
  }
}

// Optional authentication (doesn't fail if no token)
function optionalAuth(req, res, next) {
  try {
    const authHeader = req.headers.authorization;
    if (authHeader && authHeader.startsWith('Bearer ')) {
      const token = authHeader.substring(7);
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      req.user = decoded;
    }
  } catch (error) {
    // Ignore authentication errors for optional auth
  }
  next();
}

// Usage
app.get('/api/protected', authenticateToken, (req, res) => {
  res.json({ message: 'Protected resource', user: req.user });
});

app.get('/api/public', optionalAuth, (req, res) => {
  res.json({ 
    message: 'Public resource',
    authenticated: !!req.user
  });
});
```

## Troubleshooting

### Common Issues

#### Invalid Token Error

**Problem:** Receiving "Invalid token" errors

**Solutions:**
1. Check token format (should be Bearer token)
2. Verify token hasn't expired
3. Ensure token is valid and not corrupted
4. Check for extra whitespace or characters

```javascript
// Debug token issues
function debugToken(token) {
  console.log('Token length:', token.length);
  console.log('Token prefix:', token.substring(0, 10));
  
  if (token.includes('.')) {
    // JWT token
    const [header, payload, signature] = token.split('.');
    try {
      const decodedPayload = JSON.parse(atob(payload));
      console.log('Token expires at:', new Date(decodedPayload.exp * 1000));
      console.log('Token issued at:', new Date(decodedPayload.iat * 1000));
    } catch (error) {
      console.log('Could not decode JWT payload');
    }
  }
}
```

#### Token Refresh Failures

**Problem:** Refresh token requests failing

**Solutions:**
1. Check refresh token validity
2. Verify client credentials
3. Ensure proper content type headers
4. Check for expired refresh tokens

#### CORS Issues with Authentication

**Problem:** CORS errors during authentication

**Solutions:**
```javascript
// Server-side CORS configuration
app.use(cors({
  origin: ['https://yourapp.com'],
  credentials: true,
  allowedHeaders: ['Content-Type', 'Authorization']
}));

// Client-side request configuration
fetch('/api/data', {
  credentials: 'include',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
});
```

### Testing Authentication

```javascript
// Test authentication flow
describe('Authentication', () => {
  it('should authenticate with valid credentials', async () => {
    const response = await request(app)
      .post('/api/auth/login')
      .send({
        username: 'testuser',
        password: 'password123'
      });

    expect(response.status).toBe(200);
    expect(response.body).toHaveProperty('token');
    expect(response.body).toHaveProperty('user');
  });

  it('should reject invalid credentials', async () => {
    const response = await request(app)
      .post('/api/auth/login')
      .send({
        username: 'testuser',
        password: 'wrongpassword'
      });

    expect(response.status).toBe(401);
    expect(response.body).toHaveProperty('error');
  });

  it('should access protected routes with valid token', async () => {
    const token = 'valid_jwt_token';
    const response = await request(app)
      .get('/api/protected')
      .set('Authorization', `Bearer ${token}`);

    expect(response.status).toBe(200);
  });
});
```

### Monitoring Authentication

```javascript
// Log authentication events
function logAuthEvent(event, details) {
  console.log({
    timestamp: new Date().toISOString(),
    event,
    ...details
  });
}

// Usage examples
logAuthEvent('login_success', { userId: user.id, ip: req.ip });
logAuthEvent('login_failure', { username, reason: 'invalid_password', ip: req.ip });
logAuthEvent('token_refresh', { userId: user.id });
logAuthEvent('logout', { userId: user.id });
```

For additional help with authentication, see:
- [API Reference](endpoints.md)
- [Security Best Practices](../guides/security.md)
- [Error Handling Guide](../guides/error-handling.md)