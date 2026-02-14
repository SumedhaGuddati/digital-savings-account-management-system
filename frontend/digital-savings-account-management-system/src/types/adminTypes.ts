// @author Bharadi Harshitha
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

// @author Bharadi Harshitha
export interface AdminDashboardStatsResponse {
    managedAssets: number;
    managedAssetsGrowth: number;
    approvedUsersCount: number;
    activeUsersCount: number;
    newUsersToday: number;
    averageYield: number;
    pendingAppsCount: number;
    adminCount: number;
    customerCount: number;
}

// @author Bharadi Harshitha
export interface ApplicationAction {
    remarks: string;
    reviewedBy?: string;
}

// @author Bharadi Harshitha
export interface SavingsAccountResponse {
    id: number;
    accountNumber: string;
    balance: number;
    status: string;
    interestRate: number;
    openedDate: string;
}

// @author Bharadi Harshitha
export interface ApiResponse {
    success: boolean;
    message: string;
    data?: Record<string, unknown>;
}

// @author Bharadi Harshitha
export interface AccountApprovalRequest {
    remarks: string;
    reviewedBy: string;
}

// @author Bharadi Harshitha
export interface BulkActionRequest {
    ids: number[];
    remarks: string;
    reviewedBy: string;
}
