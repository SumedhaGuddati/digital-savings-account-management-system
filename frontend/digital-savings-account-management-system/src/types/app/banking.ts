import { TransactionType } from "../api/banking"

export interface Transaction {
    id: string
    description: string
    date: string
    amount: number
    type: TransactionType
}

export enum AccountStatus {
  ACTIVE = 'ACTIVE',
  CLOSED = 'CLOSED'
}

export interface Account {
  accountType: string
  accountNumber: string
  accountHolderName: string
  balance: number
  status: AccountStatus
}

