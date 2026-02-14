// @author Bharadi Harshitha

export const ADMIN_ENDPOINTS = {
    // Dashboard
    GET_DASHBOARD_STATS: '/admin/dashboard/stats',

    // Applications
    GET_PENDING_APPLICATIONS: '/admin/applications/pending',
    GET_APPROVED_APPLICATIONS: '/admin/applications/approved',
    GET_ALL_APPLICATIONS: '/admin/applications',
    GET_APPLICATION_BY_ID: (id: number) => `/admin/applications/${id}`,

    // Single actions
    APPROVE_APPLICATION: (id: number) => `/admin/applications/${id}/approve`,
    REJECT_APPLICATION: (id: number) => `/admin/applications/${id}/reject`,

    // Bulk actions
    BULK_APPROVE: '/admin/applications/bulk-approve',
    BULK_REJECT: '/admin/applications/bulk-reject',

    // Audit
    GET_AUDIT_LOGS: '/admin/audit/logs',
    GET_AUDIT_LOGS_COUNT: '/admin/audit/logs/count',
    GET_EVENT_TYPES: '/admin/audit/event-types',
};
