export enum TransactionType {
    DEPOSIT = 'DEPOSIT',
    WITHDRAW = 'WITHDRAW'
}

export interface Transaction {
    transactionId: string
    description: string
    transactionDate: string
    amount: number
    transactionType: TransactionType
}