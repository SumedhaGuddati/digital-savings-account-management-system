export interface AdminApplicationViewResponse {
    applicationId: number;
    applicantName: string;
    applicantEmail: string;
    accountType?: string;
    status: string;
    applicationDate: string;
    reviewDate?: string | null;
    reviewedBy?: string | null;
}

export interface AdminDashboardStatsResponse {
    managedAssets: number;
    managedAssetsGrowth: number;
    approvedAppsCount: number;
    activeUsersCount: number;
    newUsersToday: number;
    averageYield: number;
    pendingAppsCount: number;
    adminCount: number;
    customerCount: number;
}

export interface ApplicationAction {
    remarks: string;
    reviewedBy?: string;
}

export interface SavingsAccountResponse {
    id: number;
    accountNumber: string;
    balance: number;
    status: string;
    interestRate: number;
    openedDate: string;
}

export interface ApiResponse {
    success: boolean;
    message: string;
    data?: Record<string, unknown>;
}

export interface AccountApprovalRequest {
    remarks: string;
    reviewedBy: string;
}

export interface BulkActionRequest {
    ids: number[];
    remarks: string;
    reviewedBy: string;
}
