import { shallowMount } from '@vue/test-utils'
import TransactionsView from '@/views/user/TransactionsView.vue'
import { getUserTransactions } from '@/api/endpoints/users/users.api'
import * as transactionMapper from '@/mapper/banking/transaction.mapper'
import { TransactionType } from '@/types/api/banking'

/*
 @author Khamitkar Sai Chandan
*/

const stubs = {
    TransactionsHeader: true,
    TransactionsFilters: true,
    TransactionsSummary: true,
    TransactionsList: true,
    TransactionsQuickFilters: true,
    AppLoader: true
}

const flushPromises = () => new Promise(resolve => setTimeout(resolve, 0))

jest.mock('@/api/endpoints/users/users.api', () => ({
    getUserTransactions: jest.fn()
}))
jest.mock('@/mapper/banking/transaction.mapper', () => ({
    mapApiTransactionsToUi: jest.fn()
}))

describe('TransactionsView.vue - Unit Tests', () => {
    let wrapper: any

    const mockTransactions = [
        { id: 1, date: '2026-02-14T12:00:00Z', type: TransactionType.DEPOSIT, amount: 100, description: 'Salary' },
        { id: 2, date: '2026-02-13T12:00:00Z', type: TransactionType.WITHDRAW, amount: -50, description: 'Grocery' }
    ]

    beforeEach(() => {
        jest.clearAllMocks();

        (getUserTransactions as jest.Mock).mockResolvedValue({
            data: { transactions: mockTransactions }
        });

        (transactionMapper.mapApiTransactionsToUi as jest.Mock).mockReturnValue(mockTransactions);

        wrapper = shallowMount(TransactionsView, { stubs })
    })

    afterEach(() => {
        if (wrapper) {
            wrapper.unmount()
        }
    })

    it('sets isLoading correctly during fetch', async () => {
        expect(wrapper.vm.isLoading).toBe(false)

        wrapper.vm.fetchTransactions()
        expect(wrapper.vm.isLoading).toBe(true)

        await flushPromises()
        expect(wrapper.vm.isLoading).toBe(false)
    })

    it('fetches transactions and maps them', async () => {
        await flushPromises()
        expect(getUserTransactions).toHaveBeenCalled()
        expect(transactionMapper.mapApiTransactionsToUi).toHaveBeenCalledWith(mockTransactions)
        expect(wrapper.vm.transactions.length).toBe(2)
    })

    it('computes filtered transactions based on search query', async () => {
        await flushPromises()
        wrapper.vm.searchQuery = 'salary'
        const filtered = wrapper.vm.filteredTransactions
        expect(filtered.length).toBe(1)
        expect(filtered[0].description).toBe('Salary')
    })

    it('applies type and date filters correctly', async () => {
        await flushPromises()
        wrapper.vm.typeFilter = 'DEPOSIT'
        expect(wrapper.vm.filteredTransactions.every((t: any) => t.type === 'DEPOSIT')).toBe(true)

        wrapper.vm.dateFilter = 'today'
        const today = new Date().toDateString()
        expect(wrapper.vm.filteredTransactions.every((t: any) => new Date(t.date).toDateString() === today)).toBe(true)
    })

    it('computes summary correctly', async () => {
        await flushPromises()
        const summary = wrapper.vm.summary
        expect(summary.totalDeposits).toBe(100)
        expect(summary.totalWithdrawals).toBe(50)
        expect(summary.netFlow).toBe(50)
        expect(summary.depositCount).toBe(1)
        expect(summary.withdrawalCount).toBe(1)
    })

    it('handles quick filters', () => {
        wrapper.vm.applyQuickFilter('deposit')
        expect(wrapper.vm.typeFilter).toBe('DEPOSIT')

        wrapper.vm.applyQuickFilter('withdraw')
        expect(wrapper.vm.typeFilter).toBe('WITHDRAW')
    })

    it('clears all filters correctly', () => {
        wrapper.vm.searchQuery = 'test'
        wrapper.vm.typeFilter = 'DEPOSIT'
        wrapper.vm.dateFilter = 'today'
        wrapper.vm.pagination.page = 5

        wrapper.vm.clearAllFilters()
        expect(wrapper.vm.searchQuery).toBe('')
        expect(wrapper.vm.typeFilter).toBe('all')
        expect(wrapper.vm.dateFilter).toBe('all')
        expect(wrapper.vm.pagination.page).toBe(1)
    })

    it('returns empty array if no transactions match filters', async () => {
        await flushPromises()
        wrapper.vm.searchQuery = 'non-existent'
        expect(wrapper.vm.filteredTransactions).toEqual([])
    })
})