// @author Khamitkar Sai Chandan
export enum TransactionType {
    DEPOSIT = 'DEPOSIT',
    WITHDRAW = 'WITHDRAW'
}

// @author Khamitkar Sai Chandan
export interface Transaction {
    transactionId: string
    description: string
    transactionDate: string
    amount: number
    transactionType: TransactionType
}