/**
 * api.js – Centralized API utility functions for SkillSwap frontend.
 *
 * All fetch() calls go through these helpers so we:
 *   1. Always use the correct base URL
 *   2. Always include credentials (session cookies)
 *   3. Handle errors consistently
 *   4. Show loading states automatically
 *
 * fetch() with { credentials: 'include' } is CRITICAL for session-based auth.
 * Without it, the browser won't send the JSESSIONID cookie, and every
 * request will be treated as unauthenticated.
 */

const API_BASE = '';

// ============================================================
// CORE FETCH WRAPPER
// ============================================================

/**
 * Makes an authenticated API request and returns the parsed JSON.
 * Throws an error object { status, message } on failure.
 *
 * @param {string} endpoint - The API path (e.g., '/api/users/me')
 * @param {Object} options - fetch() options (method, body, etc.)
 */
async function apiRequest(endpoint, options = {}) {
    const url = `${API_BASE}${endpoint}`;

    const defaultOptions = {
        credentials: 'include',  // Send cookies (JSESSIONID)
        headers: {
            'Content-Type': 'application/json',
            ...options.headers
        }
    };

    const response = await fetch(url, { ...defaultOptions, ...options });

    // Only redirect to login for protected API calls (not auth checks or login attempts)
    if (response.status === 401) {
        if (!endpoint.startsWith('/api/auth/')) {
            redirectToLogin();
        }
        throw { status: 401, message: 'Unauthorized. Please log in.' };
    }

    // Parse response body
    let data;
    const contentType = response.headers.get('Content-Type') || '';
    if (contentType.includes('application/json')) {
        data = await response.json();
    } else {
        data = await response.text();
    }

    if (!response.ok) {
        const errorMessage = data?.message || data?.error ||
            `Request failed with status ${response.status}`;
        throw { status: response.status, message: errorMessage, data };
    }

    return data;
}

// Convenience wrappers
const api = {
    get: (endpoint) =>
        apiRequest(endpoint, { method: 'GET' }),

    post: (endpoint, body) =>
        apiRequest(endpoint, {
            method: 'POST',
            body: JSON.stringify(body)
        }),

    put: (endpoint, body) =>
        apiRequest(endpoint, {
            method: 'PUT',
            body: JSON.stringify(body)
        }),

    patch: (endpoint, body = null) =>
        apiRequest(endpoint, {
            method: 'PATCH',
            body: body ? JSON.stringify(body) : undefined
        }),

    delete: (endpoint) =>
        apiRequest(endpoint, { method: 'DELETE' })
};

// ============================================================
// AUTH HELPERS
// ============================================================

/**
 * Check if the user is currently logged in by calling /api/auth/me.
 * Returns the user object { id, name, email } or null without throwing.
 */
async function getCurrentUser() {
    try {
        const response = await fetch(`${API_BASE}/api/auth/me`, {
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' }
        });
        if (response.ok) {
            return await response.json();
        }
        return null;
    } catch (err) {
        return null;
    }
}

/**
 * Redirect to login page if not authenticated.
 * Saves the current page URL so we can redirect back after login.
 */
function redirectToLogin() {
    const currentPath = window.location.pathname;
    if (currentPath.includes('login') || currentPath.includes('register')) {
        return; // Already on auth page, do nothing!
    }

    if (!currentPath.endsWith('/') && !currentPath.endsWith('index.html')) {
        const fullUrl = window.location.pathname + window.location.search;
        sessionStorage.setItem('redirectAfterLogin', fullUrl);
    }
    window.location.href = '/login.html';
}

/**
 * Guard for protected pages. Call at the top of any page that requires login.
 * Returns the current user object.
 */
async function requireAuth() {
    const user = await getCurrentUser();
    if (!user) {
        redirectToLogin();
        return null;
    }
    return user;
}

/**
 * Logout the current user and redirect to landing page.
 */
async function logout() {
    try {
        await api.post('/api/auth/logout');
    } catch (e) {
        // Ignore errors during logout
    }
    window.location.href = '/index.html';
}

// ============================================================
// UI HELPERS
// ============================================================

/**
 * Show a toast notification at the top-right of the screen.
 *
 * @param {string} message - The message to display
 * @param {'success'|'error'|'warning'|'info'} type - Toast color type
 */
function showToast(message, type = 'success') {
    // Remove any existing toast
    const existing = document.querySelector('.ss-toast');
    if (existing) existing.remove();

    const colorMap = {
        success: '#10b981',
        error:   '#ef4444',
        warning: '#f59e0b',
        info:    '#3b82f6'
    };

    const toast = document.createElement('div');
    toast.className = 'ss-toast';
    toast.style.cssText = `
        position: fixed;
        top: 80px;
        right: 20px;
        background: ${colorMap[type] || colorMap.info};
        color: white;
        padding: 12px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.15);
        font-size: 14px;
        font-weight: 500;
        z-index: 9999;
        max-width: 350px;
        animation: slideIn 0.3s ease;
    `;
    toast.textContent = message;

    document.body.appendChild(toast);

    setTimeout(() => {
        toast.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 3500);
}

/**
 * Show or hide a loading spinner on a button.
 *
 * @param {HTMLButtonElement} button - The button element
 * @param {boolean} loading - Whether to show loading state
 * @param {string} originalText - The original button text
 */
function setButtonLoading(button, loading, originalText = '') {
    if (loading) {
        button.disabled = true;
        button.dataset.originalText = button.textContent;
        button.innerHTML = `<span class="spinner-border spinner-border-sm me-2"></span>Loading...`;
    } else {
        button.disabled = false;
        button.textContent = originalText || button.dataset.originalText || 'Submit';
    }
}

/**
 * Format a date string into a readable format.
 * Example: "2024-01-15T10:30:00" → "Jan 15, 2024"
 */
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('en-IN', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

/**
 * Get a badge HTML string for a request status.
 */
function getStatusBadge(status) {
    const badges = {
        PENDING:    '<span class="badge bg-warning text-dark">⏳ Pending</span>',
        ACCEPTED:   '<span class="badge bg-success">✅ Accepted</span>',
        REJECTED:   '<span class="badge bg-danger">❌ Rejected</span>',
        COMPLETED:  '<span class="badge bg-primary">🎓 Completed</span>'
    };
    return badges[status] || `<span class="badge bg-secondary">${status}</span>`;
}

/**
 * Get a badge for request type.
 */
function getRequestTypeBadge(type) {
    const badges = {
        EXCHANGE:       '<span class="badge bg-indigo">🔄 Skill Exchange</span>',
        FREE_MENTORING: '<span class="badge bg-purple">🎯 Free Mentoring</span>'
    };
    return badges[type] || `<span class="badge bg-secondary">${type}</span>`;
}

/**
 * Build a skill tag HTML element.
 */
function skillTag(skill, type = '') {
    const colorClass = type === 'teach' ? 'skill-tag-teach' : 'skill-tag-learn';
    return `<span class="skill-tag ${colorClass}">${skill.name || skill}</span>`;
}

/**
 * Handle validation error response from the backend.
 * Displays field-level error messages next to form fields.
 */
function handleValidationErrors(errorData, formElement) {
    // Clear previous errors
    formElement?.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
    formElement?.querySelectorAll('.invalid-feedback').forEach(el => el.remove());

    if (errorData?.errors) {
        Object.entries(errorData.errors).forEach(([field, message]) => {
            const input = formElement?.querySelector(`[name="${field}"]`);
            if (input) {
                input.classList.add('is-invalid');
                const feedback = document.createElement('div');
                feedback.className = 'invalid-feedback';
                feedback.textContent = message;
                input.parentNode.appendChild(feedback);
            }
        });
    }
}

// Add CSS animations for toasts
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from { transform: translateX(100px); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
    }
    @keyframes slideOut {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(100px); opacity: 0; }
    }
`;
document.head.appendChild(style);
