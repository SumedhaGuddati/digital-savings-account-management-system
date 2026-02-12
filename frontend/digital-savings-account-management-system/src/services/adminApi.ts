import apiClient from "./apiClient";
import {
  AdminApplicationViewResponse,
  AdminDashboardStatsResponse,
  ApplicationAction,
  SavingsAccountResponse,
  ApiResponse,
  AccountApprovalRequest,
  BulkActionRequest,
} from "../types/adminTypes";

//dashboard stats
export const getDashboardStats = async (): Promise<AdminDashboardStatsResponse> => {
  const response = await apiClient.get<AdminDashboardStatsResponse>('/admin/dashboard/stats');
  return response.data;
};

//pending and approved applications
export const getPendingApplications = async (): Promise<AdminApplicationViewResponse[]> => {
  const response = await apiClient.get<AdminApplicationViewResponse[]>('/admin/applications/pending');
  return response.data;
};

export const getApprovedApplications = async (): Promise<AdminApplicationViewResponse[]> => {
  const response = await apiClient.get<AdminApplicationViewResponse[]>('/admin/applications/approved');
  return response.data;
};

//Single Application Actions
export const approveApplication = async (applicationId: number, data: ApplicationAction): Promise<SavingsAccountResponse> => {
  if (isNaN(Number(applicationId))) throw new Error(`Invalid application ID: ${applicationId}`);

  const requestData: AccountApprovalRequest = {
    remarks: data.remarks || 'Approved by admin',
    reviewedBy: data.reviewedBy || 'admin_user',
  };

  const response = await apiClient.post<SavingsAccountResponse>(`/admin/applications/${applicationId}/approve`, requestData);
  return response.data;
};

export const rejectApplication = async (applicationId: number, data: ApplicationAction): Promise<ApiResponse> => {
  if (isNaN(Number(applicationId))) throw new Error(`Invalid application ID: ${applicationId}`);

  const requestData: AccountApprovalRequest = {
    remarks: data.remarks || 'Rejected by admin',
    reviewedBy: data.reviewedBy || 'admin_user',
  };

  const response = await apiClient.post<ApiResponse>(`/admin/applications/${applicationId}/reject`, requestData);
  return response.data;
};

export const getApplicationById = async (applicationId: number): Promise<AdminApplicationViewResponse> => {
  if (isNaN(Number(applicationId))) throw new Error('Invalid application ID');

  const response = await apiClient.get<AdminApplicationViewResponse>(`/admin/applications/${applicationId}`);
  return response.data;
};

// ==================== BULK ACTIONS ====================
export const bulkApproveApplications = async (ids: number[], remarks: string): Promise<ApiResponse> => {
  if (!ids || ids.length === 0) throw new Error('Invalid application IDs');

  const requestData: BulkActionRequest = { ids, remarks: remarks || 'Bulk approved by admin', reviewedBy: 'admin_user' };
  const response = await apiClient.post<ApiResponse>('/admin/applications/bulk-approve', requestData);
  return response.data;
};

export const bulkRejectApplications = async (ids: number[], remarks: string): Promise<ApiResponse> => {
  if (!ids || ids.length === 0) throw new Error('Invalid application IDs');

  const requestData: BulkActionRequest = { ids, remarks: remarks || 'Bulk rejected by admin', reviewedBy: 'admin_user' };
  const response = await apiClient.post<ApiResponse>('/admin/applications/bulk-reject', requestData);
  return response.data;
};

// ==================== FILTERED APPLICATIONS ====================
export const getAllApplications = async (filters?: {
  status?: string;
  fromDate?: string;
  toDate?: string;
}): Promise<AdminApplicationViewResponse[]> => {
  const response = await apiClient.get<AdminApplicationViewResponse[]>('/admin/applications', { params: filters });
  return response.data;
};

