// @author Khamitkar Sai Chandan
export interface Application {
    id: number
    userId: number
    accountType: string
    applicationDate: string
    status: 'PENDING' | 'APPROVED' | 'REJECTED'
    reviewedBy: string | null
    reviewDate: string | null
}