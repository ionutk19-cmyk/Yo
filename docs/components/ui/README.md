# UI Components Documentation

This document provides comprehensive documentation for all UI components in the project.

## Table of Contents

- [Overview](#overview)
- [Button Components](#button-components)
- [Form Components](#form-components)
- [Modal Components](#modal-components)
- [Navigation Components](#navigation-components)
- [Layout Components](#layout-components)
- [Data Display Components](#data-display-components)
- [Styling Guidelines](#styling-guidelines)
- [Accessibility](#accessibility)

## Overview

UI components are reusable, self-contained pieces of user interface that follow consistent design patterns and accessibility standards.

## Button Components

### `Button`

A versatile button component that supports multiple variants, sizes, and states.

**Props:**
- `variant` (string): Button style variant
  - `'primary'` (default): Primary action button
  - `'secondary'`: Secondary action button
  - `'outline'`: Outlined button
  - `'ghost'`: Minimal styling button
  - `'danger'`: Destructive action button
- `size` (string): Button size
  - `'sm'`: Small button
  - `'md'` (default): Medium button
  - `'lg'`: Large button
- `disabled` (boolean): Whether button is disabled (default: false)
- `loading` (boolean): Show loading state (default: false)
- `fullWidth` (boolean): Make button full width (default: false)
- `onClick` (function): Click handler
- `children` (ReactNode): Button content
- `type` (string): HTML button type (default: 'button')

**Examples:**

```jsx
import { Button } from '@/components/ui/Button';

// Basic button
<Button onClick={handleClick}>
  Click me
</Button>

// Primary button with loading state
<Button 
  variant="primary" 
  loading={isSubmitting}
  onClick={handleSubmit}
>
  {isSubmitting ? 'Submitting...' : 'Submit'}
</Button>

// Danger button for destructive actions
<Button 
  variant="danger" 
  onClick={handleDelete}
  disabled={!canDelete}
>
  Delete Account
</Button>

// Full width button
<Button fullWidth variant="primary">
  Sign Up
</Button>

// Small outline button
<Button size="sm" variant="outline">
  Cancel
</Button>
```

**Styling:**
```css
.button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  font-weight: 500;
  transition: all 0.2s;
}

.button--primary {
  background-color: #3b82f6;
  color: white;
  border: 1px solid #3b82f6;
}

.button--primary:hover {
  background-color: #2563eb;
}
```

---

### `IconButton`

A button component specifically designed for icon-only actions.

**Props:**
- `icon` (ReactNode): Icon component or element
- `aria-label` (string, required): Accessibility label
- `size` (string): Button size ('sm', 'md', 'lg')
- `variant` (string): Style variant
- `onClick` (function): Click handler

**Examples:**

```jsx
import { IconButton } from '@/components/ui/IconButton';
import { PlusIcon, TrashIcon } from '@/icons';

// Add button
<IconButton 
  icon={<PlusIcon />}
  aria-label="Add item"
  onClick={handleAdd}
/>

// Delete button
<IconButton 
  icon={<TrashIcon />}
  aria-label="Delete item"
  variant="danger"
  onClick={handleDelete}
/>
```

## Form Components

### `Input`

A versatile input component with built-in validation and styling.

**Props:**
- `type` (string): Input type (default: 'text')
- `placeholder` (string): Placeholder text
- `value` (string): Input value
- `onChange` (function): Change handler
- `error` (string): Error message to display
- `disabled` (boolean): Whether input is disabled
- `required` (boolean): Whether input is required
- `label` (string): Input label
- `helperText` (string): Helper text below input
- `size` (string): Input size ('sm', 'md', 'lg')

**Examples:**

```jsx
import { Input } from '@/components/ui/Input';

// Basic input
<Input 
  label="Email"
  type="email"
  placeholder="Enter your email"
  value={email}
  onChange={(e) => setEmail(e.target.value)}
/>

// Input with validation
<Input 
  label="Password"
  type="password"
  value={password}
  onChange={(e) => setPassword(e.target.value)}
  error={passwordError}
  helperText="Must be at least 8 characters"
  required
/>

// Disabled input
<Input 
  label="Username"
  value={username}
  disabled
  helperText="Cannot be changed"
/>
```

---

### `Select`

A dropdown select component with customizable options.

**Props:**
- `options` (Array): Array of option objects
  - `value` (string): Option value
  - `label` (string): Option display text
  - `disabled` (boolean): Whether option is disabled
- `value` (string): Selected value
- `onChange` (function): Change handler
- `placeholder` (string): Placeholder text
- `label` (string): Select label
- `error` (string): Error message
- `multiple` (boolean): Allow multiple selections

**Examples:**

```jsx
import { Select } from '@/components/ui/Select';

const countryOptions = [
  { value: 'us', label: 'United States' },
  { value: 'ca', label: 'Canada' },
  { value: 'uk', label: 'United Kingdom' },
  { value: 'de', label: 'Germany', disabled: true }
];

// Basic select
<Select 
  label="Country"
  options={countryOptions}
  value={selectedCountry}
  onChange={setSelectedCountry}
  placeholder="Select a country"
/>

// Multi-select
<Select 
  label="Skills"
  options={skillOptions}
  value={selectedSkills}
  onChange={setSelectedSkills}
  multiple
  placeholder="Select skills"
/>
```

---

### `Checkbox`

A checkbox component with label and validation support.

**Props:**
- `checked` (boolean): Whether checkbox is checked
- `onChange` (function): Change handler
- `label` (string): Checkbox label
- `disabled` (boolean): Whether checkbox is disabled
- `error` (string): Error message
- `indeterminate` (boolean): Indeterminate state

**Examples:**

```jsx
import { Checkbox } from '@/components/ui/Checkbox';

// Basic checkbox
<Checkbox 
  label="I agree to the terms and conditions"
  checked={agreedToTerms}
  onChange={setAgreedToTerms}
/>

// Checkbox with error
<Checkbox 
  label="Subscribe to newsletter"
  checked={subscribed}
  onChange={setSubscribed}
  error={subscriptionError}
/>

// Indeterminate checkbox (for "select all" functionality)
<Checkbox 
  label="Select all items"
  checked={allSelected}
  indeterminate={someSelected}
  onChange={handleSelectAll}
/>
```

## Modal Components

### `Modal`

A flexible modal component with backdrop and focus management.

**Props:**
- `isOpen` (boolean): Whether modal is open
- `onClose` (function): Close handler
- `title` (string): Modal title
- `children` (ReactNode): Modal content
- `size` (string): Modal size ('sm', 'md', 'lg', 'xl')
- `closeOnBackdrop` (boolean): Close on backdrop click (default: true)
- `closeOnEscape` (boolean): Close on escape key (default: true)

**Examples:**

```jsx
import { Modal, Button } from '@/components/ui';

// Basic modal
<Modal 
  isOpen={isModalOpen}
  onClose={() => setIsModalOpen(false)}
  title="Confirm Action"
>
  <p>Are you sure you want to delete this item?</p>
  <div className="modal-actions">
    <Button variant="outline" onClick={() => setIsModalOpen(false)}>
      Cancel
    </Button>
    <Button variant="danger" onClick={handleDelete}>
      Delete
    </Button>
  </div>
</Modal>

// Large modal with custom content
<Modal 
  isOpen={isSettingsOpen}
  onClose={() => setIsSettingsOpen(false)}
  title="Settings"
  size="lg"
>
  <SettingsForm onSave={handleSave} />
</Modal>
```

---

### `Drawer`

A slide-out panel component, typically used for navigation or forms.

**Props:**
- `isOpen` (boolean): Whether drawer is open
- `onClose` (function): Close handler
- `position` (string): Drawer position ('left', 'right', 'top', 'bottom')
- `size` (string): Drawer size
- `title` (string): Drawer title
- `children` (ReactNode): Drawer content

**Examples:**

```jsx
import { Drawer } from '@/components/ui/Drawer';

// Side navigation drawer
<Drawer 
  isOpen={isNavOpen}
  onClose={() => setIsNavOpen(false)}
  position="left"
  title="Navigation"
>
  <NavigationMenu />
</Drawer>

// Settings drawer
<Drawer 
  isOpen={isSettingsOpen}
  onClose={() => setIsSettingsOpen(false)}
  position="right"
  title="User Settings"
  size="md"
>
  <UserSettingsForm />
</Drawer>
```

## Navigation Components

### `Navbar`

A responsive navigation bar component.

**Props:**
- `brand` (ReactNode): Brand logo or text
- `items` (Array): Navigation items
- `user` (Object): Current user information
- `onLogout` (function): Logout handler

**Examples:**

```jsx
import { Navbar } from '@/components/ui/Navbar';

const navItems = [
  { label: 'Dashboard', href: '/dashboard' },
  { label: 'Projects', href: '/projects' },
  { label: 'Settings', href: '/settings' }
];

<Navbar 
  brand={<Logo />}
  items={navItems}
  user={currentUser}
  onLogout={handleLogout}
/>
```

---

### `Breadcrumb`

A breadcrumb navigation component for hierarchical navigation.

**Props:**
- `items` (Array): Breadcrumb items
  - `label` (string): Item label
  - `href` (string): Item link
  - `current` (boolean): Whether item is current page

**Examples:**

```jsx
import { Breadcrumb } from '@/components/ui/Breadcrumb';

const breadcrumbItems = [
  { label: 'Home', href: '/' },
  { label: 'Projects', href: '/projects' },
  { label: 'Project Alpha', href: '/projects/alpha' },
  { label: 'Settings', current: true }
];

<Breadcrumb items={breadcrumbItems} />
```

## Data Display Components

### `Table`

A flexible table component with sorting, filtering, and pagination.

**Props:**
- `columns` (Array): Column definitions
  - `key` (string): Column key
  - `label` (string): Column header
  - `sortable` (boolean): Whether column is sortable
  - `render` (function): Custom render function
- `data` (Array): Table data
- `loading` (boolean): Loading state
- `onSort` (function): Sort handler
- `pagination` (Object): Pagination configuration

**Examples:**

```jsx
import { Table } from '@/components/ui/Table';

const columns = [
  { key: 'name', label: 'Name', sortable: true },
  { key: 'email', label: 'Email', sortable: true },
  { key: 'role', label: 'Role' },
  { 
    key: 'actions', 
    label: 'Actions',
    render: (row) => (
      <div>
        <Button size="sm" onClick={() => editUser(row.id)}>
          Edit
        </Button>
        <Button size="sm" variant="danger" onClick={() => deleteUser(row.id)}>
          Delete
        </Button>
      </div>
    )
  }
];

<Table 
  columns={columns}
  data={users}
  loading={isLoading}
  onSort={handleSort}
  pagination={{
    page: currentPage,
    total: totalPages,
    onPageChange: setCurrentPage
  }}
/>
```

---

### `Card`

A container component for grouping related content.

**Props:**
- `title` (string): Card title
- `subtitle` (string): Card subtitle
- `children` (ReactNode): Card content
- `actions` (ReactNode): Card action buttons
- `variant` (string): Card variant
- `padding` (string): Card padding size

**Examples:**

```jsx
import { Card, Button } from '@/components/ui';

// Basic card
<Card title="User Profile">
  <p>Name: John Doe</p>
  <p>Email: john@example.com</p>
</Card>

// Card with actions
<Card 
  title="Project Alpha"
  subtitle="Last updated 2 hours ago"
  actions={
    <div>
      <Button size="sm" variant="outline">Edit</Button>
      <Button size="sm" variant="primary">View</Button>
    </div>
  }
>
  <p>Project description goes here...</p>
</Card>
```

## Styling Guidelines

### CSS Variables

```css
:root {
  /* Colors */
  --color-primary: #3b82f6;
  --color-secondary: #6b7280;
  --color-success: #10b981;
  --color-warning: #f59e0b;
  --color-danger: #ef4444;
  
  /* Spacing */
  --spacing-xs: 0.25rem;
  --spacing-sm: 0.5rem;
  --spacing-md: 1rem;
  --spacing-lg: 1.5rem;
  --spacing-xl: 2rem;
  
  /* Typography */
  --font-size-xs: 0.75rem;
  --font-size-sm: 0.875rem;
  --font-size-md: 1rem;
  --font-size-lg: 1.125rem;
  --font-size-xl: 1.25rem;
  
  /* Border radius */
  --radius-sm: 0.25rem;
  --radius-md: 0.375rem;
  --radius-lg: 0.5rem;
  
  /* Shadows */
  --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}
```

### Component Structure

```css
/* Component base styles */
.component {
  /* Base styles */
}

/* Component variants */
.component--variant {
  /* Variant-specific styles */
}

/* Component sizes */
.component--size-sm {
  /* Small size styles */
}

/* Component states */
.component:hover {
  /* Hover styles */
}

.component:disabled {
  /* Disabled styles */
}

.component--loading {
  /* Loading state styles */
}
```

## Accessibility

All components follow WCAG 2.1 AA guidelines:

### Keyboard Navigation
- All interactive elements are keyboard accessible
- Focus indicators are clearly visible
- Tab order is logical and intuitive

### Screen Reader Support
- Proper ARIA labels and roles
- Semantic HTML elements
- Descriptive error messages

### Color and Contrast
- Minimum contrast ratio of 4.5:1 for normal text
- Minimum contrast ratio of 3:1 for large text
- Color is not the only means of conveying information

### Focus Management
- Focus is properly managed in modals and dropdowns
- Focus is restored when closing overlays
- Skip links are provided for keyboard users

### Examples of Accessible Components

```jsx
// Accessible button
<Button 
  aria-label="Close dialog"
  onClick={handleClose}
>
  <CloseIcon aria-hidden="true" />
</Button>

// Accessible form
<form onSubmit={handleSubmit}>
  <Input 
    label="Email Address"
    type="email"
    required
    aria-describedby="email-error"
    error={emailError}
  />
  {emailError && (
    <div id="email-error" role="alert">
      {emailError}
    </div>
  )}
</form>

// Accessible modal
<Modal 
  isOpen={isOpen}
  onClose={handleClose}
  aria-labelledby="modal-title"
  aria-describedby="modal-description"
>
  <h2 id="modal-title">Confirm Deletion</h2>
  <p id="modal-description">
    This action cannot be undone.
  </p>
</Modal>
```

## Testing Components

### Unit Testing

```jsx
import { render, screen, fireEvent } from '@testing-library/react';
import { Button } from './Button';

describe('Button', () => {
  it('renders button text', () => {
    render(<Button>Click me</Button>);
    expect(screen.getByText('Click me')).toBeInTheDocument();
  });

  it('calls onClick when clicked', () => {
    const handleClick = jest.fn();
    render(<Button onClick={handleClick}>Click me</Button>);
    
    fireEvent.click(screen.getByText('Click me'));
    expect(handleClick).toHaveBeenCalledTimes(1);
  });

  it('is disabled when disabled prop is true', () => {
    render(<Button disabled>Click me</Button>);
    expect(screen.getByText('Click me')).toBeDisabled();
  });
});
```

### Visual Testing

```jsx
// Storybook stories for visual testing
export default {
  title: 'UI/Button',
  component: Button,
};

export const Primary = {
  args: {
    variant: 'primary',
    children: 'Primary Button',
  },
};

export const AllVariants = () => (
  <div style={{ display: 'flex', gap: '1rem' }}>
    <Button variant="primary">Primary</Button>
    <Button variant="secondary">Secondary</Button>
    <Button variant="outline">Outline</Button>
    <Button variant="ghost">Ghost</Button>
    <Button variant="danger">Danger</Button>
  </div>
);
```