import { TransactionType } from "../api/banking"

// @author Khamitkar Sai Chandan
export interface Transaction {
    id: string
    description: string
    date: string
    amount: number
    type: TransactionType
}

// @author Khamitkar Sai Chandan
export enum AccountStatus {
  ACTIVE = 'ACTIVE',
  CLOSED = 'CLOSED'
}

// @author Khamitkar Sai Chandan
export interface Account {
  accountType: string
  accountNumber: string
  accountHolderName: string
  balance: number
  status: AccountStatus
}

