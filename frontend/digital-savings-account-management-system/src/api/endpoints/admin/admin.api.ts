// @author Bharadi Harshitha

import apiManager from "@/api/apiManager";
import { ADMIN_ENDPOINTS } from "./admin.endpoints";

import type {
    AdminApplicationViewResponse,
    AdminDashboardStatsResponse,
    ApplicationAction,
    SavingsAccountResponse,
    ApiResponse,
    AccountApprovalRequest,
    BulkActionRequest,
} from "@/types/adminTypes";

import type { AuditEventResponse } from "@/types/auditTypes";

// ==================== DASHBOARD ====================

export const getDashboardStats = async (): Promise<AdminDashboardStatsResponse> => {
    const response = await apiManager.get<AdminDashboardStatsResponse>(
        ADMIN_ENDPOINTS.GET_DASHBOARD_STATS
    );
    return response.data;
};

// ==================== APPLICATION LIST ====================

export const getPendingApplications = async (): Promise<AdminApplicationViewResponse[]> => {
    const response = await apiManager.get<AdminApplicationViewResponse[]>(
        ADMIN_ENDPOINTS.GET_PENDING_APPLICATIONS
    );
    return response.data;
};

export const getApprovedApplications = async (): Promise<AdminApplicationViewResponse[]> => {
    const response = await apiManager.get<AdminApplicationViewResponse[]>(
        ADMIN_ENDPOINTS.GET_APPROVED_APPLICATIONS
    );
    return response.data;
};

export const getAllApplications = async (filters?: {
    status?: string;
    fromDate?: string;
    toDate?: string;
}): Promise<AdminApplicationViewResponse[]> => {
    const response = await apiManager.get<AdminApplicationViewResponse[]>(
        ADMIN_ENDPOINTS.GET_ALL_APPLICATIONS,
        { params: filters }
    );
    return response.data;
};

export const getApplicationById = async (
    applicationId: number
): Promise<AdminApplicationViewResponse> => {
    if (isNaN(Number(applicationId))) {
        throw new Error("Invalid application ID");
    }

    const response = await apiManager.get<AdminApplicationViewResponse>(
        ADMIN_ENDPOINTS.GET_APPLICATION_BY_ID(applicationId)
    );

    return response.data;
};

// ==================== SINGLE ACTIONS ====================

export const approveApplication = async (
    applicationId: number,
    data: ApplicationAction
): Promise<SavingsAccountResponse> => {
    if (isNaN(Number(applicationId))) {
        throw new Error(`Invalid application ID: ${applicationId}`);
    }

    const requestData: AccountApprovalRequest = {
        remarks: data.remarks || "Approved by admin",
        reviewedBy: data.reviewedBy || "admin_user",
    };

    const response = await apiManager.post<SavingsAccountResponse>(
        ADMIN_ENDPOINTS.APPROVE_APPLICATION(applicationId),
        requestData
    );

    return response.data;
};

export const rejectApplication = async (
    applicationId: number,
    data: ApplicationAction
): Promise<ApiResponse> => {
    if (isNaN(Number(applicationId))) {
        throw new Error(`Invalid application ID: ${applicationId}`);
    }

    const requestData: AccountApprovalRequest = {
        remarks: data.remarks || "Rejected by admin",
        reviewedBy: data.reviewedBy || "admin_user",
    };

    const response = await apiManager.post<ApiResponse>(
        ADMIN_ENDPOINTS.REJECT_APPLICATION(applicationId),
        requestData
    );

    return response.data;
};

// ==================== BULK ACTIONS ====================

export const bulkApproveApplications = async (
    ids: number[],
    remarks: string
): Promise<ApiResponse> => {
    if (!ids || ids.length === 0) {
        throw new Error("Invalid application IDs");
    }

    const requestData: BulkActionRequest = {
        ids,
        remarks: remarks || "Bulk approved by admin",
        reviewedBy: "admin_user",
    };

    const response = await apiManager.post<ApiResponse>(
        ADMIN_ENDPOINTS.BULK_APPROVE,
        requestData
    );

    return response.data;
};

export const bulkRejectApplications = async (
    ids: number[],
    remarks: string
): Promise<ApiResponse> => {
    if (!ids || ids.length === 0) {
        throw new Error("Invalid application IDs");
    }

    const requestData: BulkActionRequest = {
        ids,
        remarks: remarks || "Bulk rejected by admin",
        reviewedBy: "admin_user",
    };

    const response = await apiManager.post<ApiResponse>(
        ADMIN_ENDPOINTS.BULK_REJECT,
        requestData
    );

    return response.data;
};

// ==================== AUDIT ====================

export const getAuditLogs = async (
    startDate?: string,
    endDate?: string,
    eventType?: string,
    severity?: string,
    page = 0,
    size = 10
): Promise<AuditEventResponse[]> => {
    const params: Record<string, unknown> = { page, size };

    if (startDate) params.startDate = startDate;
    if (endDate) params.endDate = endDate;
    if (eventType) params.eventType = eventType;
    if (severity) params.severity = severity;

    const response = await apiManager.get<AuditEventResponse[]>(
        ADMIN_ENDPOINTS.GET_AUDIT_LOGS,
        { params }
    );

    return response.data;
};

export const getAuditLogsCount = async (
    startDate?: string,
    endDate?: string,
    eventType?: string,
    severity?: string
): Promise<number> => {
    const params: Record<string, unknown> = {};

    if (startDate) params.startDate = startDate;
    if (endDate) params.endDate = endDate;
    if (eventType) params.eventType = eventType;
    if (severity) params.severity = severity;

    const response = await apiManager.get<{ count: number }>(
        ADMIN_ENDPOINTS.GET_AUDIT_LOGS_COUNT,
        { params }
    );

    return response.data.count;
};

export const getEventTypes = async (): Promise<string[]> => {
    const response = await apiManager.get<string[]>(
        ADMIN_ENDPOINTS.GET_EVENT_TYPES
    );

    return response.data;
};
