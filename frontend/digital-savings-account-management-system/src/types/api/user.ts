
import { AccountStatus } from "../app/banking"

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
