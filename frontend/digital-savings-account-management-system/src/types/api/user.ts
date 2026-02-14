import { AccountStatus } from "../app/banking"

// @author Khamitkar Sai Chandan
export enum UserRole {
    ADMIN = "ADMIN",
    USER = "USER",
    SUPER_ADMIN = "SUPER_ADMIN"
}
// @author Khamitkar Sai Chandan
export interface UserProfile {
    accountId: number
    accountNumber: string
    accountHolderName: string
    balance: number
    status: AccountStatus
    accountType: string
    dob: string
    applicationDate: string
    reviewDate: string
    occupation: string
    incomeSource: string
    annualIncome: number
    parentName: string
    transactionsCount: number
}
