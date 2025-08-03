# Quick Start Guide

This guide will help you get up and running with the project quickly. Follow these steps to integrate and start using the APIs, functions, and components.

## Table of Contents

- [Installation](#installation)
- [Basic Setup](#basic-setup)
- [Authentication](#authentication)
- [Making Your First API Call](#making-your-first-api-call)
- [Using Core Functions](#using-core-functions)
- [Building Your First Component](#building-your-first-component)
- [Common Patterns](#common-patterns)
- [Next Steps](#next-steps)

## Installation

### Package Manager Installation

```bash
# Using npm
npm install your-package-name

# Using yarn
yarn add your-package-name

# Using pnpm
pnpm add your-package-name
```

### CDN Installation

```html
<!-- For browser environments -->
<script src="https://cdn.example.com/your-package@latest/dist/index.min.js"></script>
```

### Manual Installation

```bash
# Clone the repository
git clone https://github.com/example/your-project.git
cd your-project

# Install dependencies
npm install

# Build the project
npm run build
```

## Basic Setup

### Environment Configuration

Create a `.env` file in your project root:

```env
# API Configuration
API_BASE_URL=https://api.example.com/v1
API_KEY=your_api_key_here

# Optional: Debug mode
DEBUG=true

# Optional: Cache settings
CACHE_TTL=3600
```

### Initialize the SDK

```javascript
import { initializeApp, configure } from 'your-package-name';

// Initialize with configuration
const app = initializeApp({
  apiKey: process.env.API_KEY,
  baseURL: process.env.API_BASE_URL,
  debug: process.env.DEBUG === 'true'
});

// Alternative: Configure globally
configure({
  apiKey: 'your_api_key',
  baseURL: 'https://api.example.com/v1',
  timeout: 5000,
  retries: 3
});
```

### TypeScript Setup

```typescript
// types.ts
export interface User {
  id: string;
  username: string;
  email: string;
  profile: {
    firstName: string;
    lastName: string;
    avatarUrl?: string;
  };
}

export interface ApiResponse<T> {
  data: T;
  success: boolean;
  message?: string;
  errors?: string[];
}
```

## Authentication

### Basic Authentication

```javascript
import { authenticate, setAuthToken } from 'your-package-name';

async function loginUser(credentials) {
  try {
    const result = await authenticate({
      username: credentials.username,
      password: credentials.password,
      rememberMe: true
    });

    if (result.success) {
      // Store the token for subsequent requests
      setAuthToken(result.token);
      localStorage.setItem('authToken', result.token);
      
      console.log('User logged in:', result.user);
      return result.user;
    } else {
      throw new Error(result.error);
    }
  } catch (error) {
    console.error('Login failed:', error.message);
    throw error;
  }
}

// Usage
const user = await loginUser({
  username: 'john@example.com',
  password: 'securePassword123'
});
```

### Token Management

```javascript
import { refreshToken, logout, isTokenValid } from 'your-package-name';

// Check if token is still valid
function checkAuthStatus() {
  const token = localStorage.getItem('authToken');
  if (!token || !isTokenValid(token)) {
    redirectToLogin();
    return false;
  }
  return true;
}

// Automatic token refresh
async function handleTokenRefresh() {
  const refreshTokenValue = localStorage.getItem('refreshToken');
  
  try {
    const newTokens = await refreshToken(refreshTokenValue);
    localStorage.setItem('authToken', newTokens.accessToken);
    localStorage.setItem('refreshToken', newTokens.refreshToken);
    setAuthToken(newTokens.accessToken);
  } catch (error) {
    // Refresh failed, redirect to login
    await logout();
    localStorage.clear();
    redirectToLogin();
  }
}

// Logout functionality
async function logoutUser() {
  const token = localStorage.getItem('authToken');
  await logout(token);
  localStorage.clear();
  redirectToLogin();
}
```

## Making Your First API Call

### GET Request

```javascript
import { apiClient } from 'your-package-name';

async function fetchUsers() {
  try {
    const response = await apiClient.get('/users', {
      params: {
        page: 1,
        limit: 10,
        status: 'active'
      }
    });

    console.log('Users:', response.data);
    return response.data;
  } catch (error) {
    console.error('Failed to fetch users:', error.message);
    throw error;
  }
}

// Usage
const users = await fetchUsers();
```

### POST Request

```javascript
async function createUser(userData) {
  try {
    const response = await apiClient.post('/users', {
      username: userData.username,
      email: userData.email,
      password: userData.password,
      profile: {
        firstName: userData.firstName,
        lastName: userData.lastName
      }
    });

    console.log('User created:', response.data);
    return response.data;
  } catch (error) {
    if (error.response?.status === 400) {
      console.error('Validation errors:', error.response.data.errors);
    }
    throw error;
  }
}

// Usage
const newUser = await createUser({
  username: 'newuser',
  email: 'newuser@example.com',
  password: 'securePassword123',
  firstName: 'New',
  lastName: 'User'
});
```

### PUT/PATCH Request

```javascript
async function updateUser(userId, updates) {
  try {
    const response = await apiClient.put(`/users/${userId}`, updates);
    console.log('User updated:', response.data);
    return response.data;
  } catch (error) {
    console.error('Failed to update user:', error.message);
    throw error;
  }
}

// Usage
const updatedUser = await updateUser('123', {
  profile: {
    firstName: 'Updated',
    lastName: 'Name'
  }
});
```

## Using Core Functions

### Data Processing

```javascript
import { processData, validateInput } from 'your-package-name';

async function handleUserData(rawUserData) {
  // Validate input first
  const userSchema = {
    type: 'object',
    required: ['username', 'email'],
    rules: {
      username: { minLength: 3, maxLength: 50 },
      email: { pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/ },
      age: { type: 'number', min: 0, max: 120 }
    }
  };

  const validation = validateInput(rawUserData, userSchema);
  
  if (!validation.valid) {
    console.error('Validation errors:', validation.errors);
    return { success: false, errors: validation.errors };
  }

  // Process the validated data
  const result = await processData(validation.sanitized, {
    format: 'json',
    validate: true,
    transform: (user) => ({
      ...user,
      fullName: `${user.firstName} ${user.lastName}`,
      isAdult: user.age >= 18
    })
  });

  return { success: true, data: result.data };
}

// Usage
const userResult = await handleUserData({
  username: 'john',
  email: 'john@example.com',
  firstName: 'John',
  lastName: 'Doe',
  age: 30
});
```

### Utility Functions

```javascript
import { formatDate, debounce, throttle } from 'your-package-name';

// Date formatting
const formattedDate = formatDate(new Date(), 'MMM DD, YYYY');
console.log(formattedDate); // "Dec 25, 2023"

// Debounced search function
const debouncedSearch = debounce(async (query) => {
  const results = await apiClient.get('/search', { params: { q: query } });
  updateSearchResults(results.data);
}, 300);

// Throttled scroll handler
const throttledScroll = throttle(() => {
  console.log('Scroll position:', window.scrollY);
}, 100);

window.addEventListener('scroll', throttledScroll);
```

## Building Your First Component

### React Component Example

```jsx
import React, { useState, useEffect } from 'react';
import { Button, Input, Card, Modal } from 'your-package-name/components';
import { fetchUsers, createUser } from 'your-package-name';

function UserManagement() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [newUser, setNewUser] = useState({
    username: '',
    email: '',
    firstName: '',
    lastName: ''
  });

  // Load users on component mount
  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    setLoading(true);
    try {
      const userData = await fetchUsers();
      setUsers(userData.data);
    } catch (error) {
      console.error('Failed to load users:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateUser = async () => {
    try {
      const createdUser = await createUser(newUser);
      setUsers([...users, createdUser]);
      setNewUser({ username: '', email: '', firstName: '', lastName: '' });
      setShowCreateModal(false);
    } catch (error) {
      console.error('Failed to create user:', error);
    }
  };

  return (
    <div className="user-management">
      <div className="header">
        <h1>User Management</h1>
        <Button 
          variant="primary" 
          onClick={() => setShowCreateModal(true)}
        >
          Add User
        </Button>
      </div>

      {loading ? (
        <div>Loading users...</div>
      ) : (
        <div className="user-grid">
          {users.map(user => (
            <Card key={user.id} title={user.username}>
              <p>Email: {user.email}</p>
              <p>Name: {user.profile.firstName} {user.profile.lastName}</p>
            </Card>
          ))}
        </div>
      )}

      <Modal
        isOpen={showCreateModal}
        onClose={() => setShowCreateModal(false)}
        title="Create New User"
      >
        <div className="create-user-form">
          <Input
            label="Username"
            value={newUser.username}
            onChange={(e) => setNewUser({...newUser, username: e.target.value})}
            required
          />
          <Input
            label="Email"
            type="email"
            value={newUser.email}
            onChange={(e) => setNewUser({...newUser, email: e.target.value})}
            required
          />
          <Input
            label="First Name"
            value={newUser.firstName}
            onChange={(e) => setNewUser({...newUser, firstName: e.target.value})}
          />
          <Input
            label="Last Name"
            value={newUser.lastName}
            onChange={(e) => setNewUser({...newUser, lastName: e.target.value})}
          />
          <div className="modal-actions">
            <Button 
              variant="outline" 
              onClick={() => setShowCreateModal(false)}
            >
              Cancel
            </Button>
            <Button 
              variant="primary" 
              onClick={handleCreateUser}
            >
              Create User
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}

export default UserManagement;
```

### Vanilla JavaScript Component

```javascript
class UserCard {
  constructor(container, userData) {
    this.container = container;
    this.userData = userData;
    this.render();
  }

  render() {
    this.container.innerHTML = `
      <div class="user-card">
        <div class="user-avatar">
          <img src="${this.userData.profile.avatarUrl || '/default-avatar.png'}" 
               alt="${this.userData.username}">
        </div>
        <div class="user-info">
          <h3>${this.userData.username}</h3>
          <p>${this.userData.email}</p>
          <p>${this.userData.profile.firstName} ${this.userData.profile.lastName}</p>
        </div>
        <div class="user-actions">
          <button class="btn btn-primary" data-action="edit">Edit</button>
          <button class="btn btn-danger" data-action="delete">Delete</button>
        </div>
      </div>
    `;

    this.attachEventListeners();
  }

  attachEventListeners() {
    this.container.addEventListener('click', (e) => {
      const action = e.target.dataset.action;
      if (action === 'edit') {
        this.editUser();
      } else if (action === 'delete') {
        this.deleteUser();
      }
    });
  }

  async editUser() {
    // Implementation for editing user
    console.log('Editing user:', this.userData.id);
  }

  async deleteUser() {
    if (confirm('Are you sure you want to delete this user?')) {
      try {
        await apiClient.delete(`/users/${this.userData.id}`);
        this.container.remove();
      } catch (error) {
        console.error('Failed to delete user:', error);
      }
    }
  }
}

// Usage
const userContainer = document.getElementById('user-container');
const userCard = new UserCard(userContainer, userData);
```

## Common Patterns

### Error Handling

```javascript
import { createError, handleError } from 'your-package-name';

// Centralized error handling
function setupGlobalErrorHandling() {
  // Handle unhandled promise rejections
  window.addEventListener('unhandledrejection', (event) => {
    handleError(event.reason, {
      type: 'unhandled_promise_rejection',
      timestamp: new Date().toISOString()
    });
  });

  // Handle general errors
  window.addEventListener('error', (event) => {
    handleError(event.error, {
      type: 'javascript_error',
      filename: event.filename,
      lineNumber: event.lineno,
      timestamp: new Date().toISOString()
    });
  });
}

// Custom error creation
function validateUserInput(input) {
  if (!input.email) {
    throw createError(
      'VALIDATION_ERROR',
      'Email is required',
      { field: 'email', code: 'REQUIRED' }
    );
  }

  if (!input.email.includes('@')) {
    throw createError(
      'VALIDATION_ERROR',
      'Invalid email format',
      { field: 'email', code: 'INVALID_FORMAT' }
    );
  }
}
```

### Loading States

```javascript
// Loading state management
class LoadingManager {
  constructor() {
    this.loadingStates = new Map();
  }

  setLoading(key, isLoading) {
    this.loadingStates.set(key, isLoading);
    this.updateUI();
  }

  isLoading(key) {
    return this.loadingStates.get(key) || false;
  }

  updateUI() {
    // Update loading indicators in UI
    for (const [key, isLoading] of this.loadingStates) {
      const element = document.querySelector(`[data-loading="${key}"]`);
      if (element) {
        element.classList.toggle('loading', isLoading);
      }
    }
  }
}

const loadingManager = new LoadingManager();

// Usage in async operations
async function fetchUserData(userId) {
  loadingManager.setLoading('user-data', true);
  try {
    const userData = await apiClient.get(`/users/${userId}`);
    return userData;
  } finally {
    loadingManager.setLoading('user-data', false);
  }
}
```

### Caching

```javascript
// Simple in-memory cache
class Cache {
  constructor(ttl = 300000) { // 5 minutes default
    this.cache = new Map();
    this.ttl = ttl;
  }

  set(key, value) {
    this.cache.set(key, {
      value,
      timestamp: Date.now()
    });
  }

  get(key) {
    const item = this.cache.get(key);
    if (!item) return null;

    if (Date.now() - item.timestamp > this.ttl) {
      this.cache.delete(key);
      return null;
    }

    return item.value;
  }

  clear() {
    this.cache.clear();
  }
}

const userCache = new Cache();

// Cached API call
async function getCachedUser(userId) {
  const cacheKey = `user_${userId}`;
  let user = userCache.get(cacheKey);

  if (!user) {
    user = await apiClient.get(`/users/${userId}`);
    userCache.set(cacheKey, user);
  }

  return user;
}
```

## Next Steps

Now that you have the basics working, here are some recommended next steps:

### 1. Explore Advanced Features
- [Authentication Guide](../api/authentication.md)
- [Advanced API Usage](../api/responses.md)
- [Component Customization](../components/ui/README.md)

### 2. Build More Complex Features
- [User Management Tutorial](tutorials/user-management.md)
- [Data Visualization Examples](tutorials/data-visualization.md)
- [Real-time Features](tutorials/real-time.md)

### 3. Performance Optimization
- [Caching Strategies](../guides/performance.md)
- [Bundle Size Optimization](../guides/optimization.md)
- [Error Monitoring](../guides/monitoring.md)

### 4. Testing Your Integration
- [Unit Testing Guide](../guides/testing.md)
- [Integration Testing](../guides/integration-testing.md)
- [End-to-End Testing](../guides/e2e-testing.md)

### 5. Deployment
- [Production Deployment](../guides/deployment.md)
- [Environment Configuration](../guides/configuration.md)
- [Monitoring and Logging](../guides/monitoring.md)

## Getting Help

If you run into issues:

1. **Check the Documentation**: Browse the comprehensive docs in the `/docs` folder
2. **Search Issues**: Look through existing GitHub issues
3. **Community Support**: Join our Discord/Slack community
4. **Create an Issue**: Use our issue templates for bug reports or feature requests

## Sample Projects

Check out these complete example projects:

- [Basic CRUD App](https://github.com/example/basic-crud-example)
- [Dashboard with Charts](https://github.com/example/dashboard-example)
- [Real-time Chat App](https://github.com/example/chat-example)
- [E-commerce Integration](https://github.com/example/ecommerce-example)

---

Happy coding! 🚀