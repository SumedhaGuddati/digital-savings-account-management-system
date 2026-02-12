import type {
    Transaction as ApiTransaction,
} from '@/types/api/banking'

import type {
    Transaction as UiTransaction
} from '@/types/app/banking'

export const mapApiTransactionToUi = (api: ApiTransaction): UiTransaction => {
    return {
        id: api.transactionId,
        description: api.description,
        date: api.transactionDate,
        amount: api.amount,
        type: api.transactionType
    }
}

export const mapApiTransactionsToUi = (apiList: ApiTransaction[]): UiTransaction[] => {
    return apiList.map(mapApiTransactionToUi)
}
