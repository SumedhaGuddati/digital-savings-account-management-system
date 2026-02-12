import apiClient from "./apiClient";
import { AuditEventResponse } from "@/types/auditTypes"; // adjust path if needed

// ==================== AUDIT LOGS ====================

/**
 * Get audit logs with optional filters and pagination
 */
export const getAuditLogs = async (
    startDate?: string,
    endDate?: string,
    eventType?: string,
    severity?: string,
    page = 0,
    size = 10
): Promise<AuditEventResponse[]> => {
    try {
        const params: Record<string, unknown> = { page, size };
        if (startDate) params.startDate = startDate;
        if (endDate) params.endDate = endDate;
        if (eventType) params.eventType = eventType;
        if (severity) params.severity = severity;

        const response = await apiClient.get<AuditEventResponse[]>('/admin/audit/logs', { params });
        return response.data;
    } catch (error) {
        console.error('Error fetching audit logs:', error);
        throw error;
    }
};

/**
 * Get the total count of audit logs with optional filters
 */
export const getAuditLogsCount = async (
    startDate?: string,
    endDate?: string,
    eventType?: string,
    severity?: string
): Promise<number> => {
    try {
        const params: Record<string, unknown> = {};
        if (startDate) params.startDate = startDate;
        if (endDate) params.endDate = endDate;
        if (eventType) params.eventType = eventType;
        if (severity) params.severity = severity;

        const response = await apiClient.get<{ count: number }>('/admin/audit/logs/count', { params });
        return response.data.count;
    } catch (error) {
        console.error('Error fetching audit logs count:', error);
        throw error;
    }
};

/**
 * Get all possible event types for audit logs
 */
export const getEventTypes = async (): Promise<string[]> => {
    try {
        const response = await apiClient.get<string[]>('/admin/audit/event-types');
        return response.data;
    } catch (error) {
        console.error('Error fetching event types:', error);
        throw error;
    }
};
