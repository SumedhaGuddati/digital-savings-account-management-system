<template>
    <div class="min-h-screen bg-gradient-to-br from-gray-50 via-gray-50 to-indigo-50/30 py-8 px-4 sm:px-6">
        <div class="max-w-7xl mx-auto">
            <TransactionsHeader :count="filteredTransactions.length" @export="exportTransactions" />

            <TransactionsFilters v-model:searchQuery="searchQuery" v-model:dateFilter="dateFilter"
                v-model:typeFilter="typeFilter" @clear="clearAllFilters" />

            <TransactionsSummary :summary="summary" />

            <TransactionsList :transactions="filteredTransactions" :has-active-filters="hasActiveFilters as boolean"
                :empty-state-message="emptyStateMessage" @select="viewTransactionDetails" @clear="clearAllFilters" />

            <TransactionsQuickFilters :quick-filters="quickFilters" v-model:sortOrder="sortOrder"
                @apply="applyQuickFilter" />
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import TransactionsHeader from '@/components/transactions/TransactionsHeader.vue'
import TransactionsFilters from '@/components/transactions/TransactionsFilters.vue'
import TransactionsSummary from '@/components/transactions/TransactionsSummary.vue'
import TransactionsList from '@/components/transactions/TransactionsList.vue'
import TransactionsQuickFilters from '@/components/transactions/TransactionsQuickFilters.vue'
import { Transaction } from '@/types/app/banking'
import { mapApiTransactionsToUi } from '@/mapper/banking/transaction.mapper'
import { TransactionType } from '@/types/api/banking'
import { getUserTransactions } from '@/api/endpoints/users/users.api'

export default defineComponent({
    name: 'TransactionsView',
    components: {
        TransactionsHeader,
        TransactionsFilters,
        TransactionsSummary,
        TransactionsList,
        TransactionsQuickFilters
    },

    data() {
        return {
            searchQuery: '',
            dateFilter: 'all',
            typeFilter: 'all',
            sortOrder: 'newest',
            pagination: {
                page: 1,
                perPage: 10
            },
            transactions: [] as Transaction[],
            quickFilters: [
                { label: 'Today', value: 'today', active: false },
                { label: 'This Week', value: 'week', active: false },
                { label: 'This Month', value: 'month', active: false },
                { label: 'Deposits', value: 'deposit', active: false },
                { label: 'Withdrawals', value: 'withdraw', active: false }
            ]
        }
    },

    computed: {
        filteredTransactions(): Transaction[] {
            let filtered = [...this.transactions]

            if (this.searchQuery) {
                const q = this.searchQuery.toLowerCase()
                filtered = filtered.filter(txn =>
                    txn.description.toLowerCase().includes(q) ||
                    txn.amount.toString().includes(q) ||
                    txn.id.includes(q)
                )
            }

            if (this.dateFilter !== 'all') {
                const today = new Date()
                const yesterday = new Date(today)
                yesterday.setDate(yesterday.getDate() - 1)

                filtered = filtered.filter(txn => {
                    const d = new Date(txn.date)
                    switch (this.dateFilter) {
                        case 'today':
                            return d.toDateString() === today.toDateString()
                        case 'yesterday':
                            return d.toDateString() === yesterday.toDateString()
                        case 'week':
                            const weekAgo = new Date(today)
                            weekAgo.setDate(weekAgo.getDate() - 7)
                            return d >= weekAgo
                        case 'month':
                            const monthAgo = new Date(today)
                            monthAgo.setMonth(monthAgo.getMonth() - 1)
                            return d >= monthAgo
                        case 'year':
                            const yearAgo = new Date(today)
                            yearAgo.setFullYear(yearAgo.getFullYear() - 1)
                            return d >= yearAgo
                        default:
                            return true
                    }
                })
            }

            if (this.typeFilter !== 'all') {
                filtered = filtered.filter(txn => txn.type === this.typeFilter)
            }

            filtered.sort((a, b) => {
                switch (this.sortOrder) {
                    case 'newest':
                        return new Date(b.date).getTime() - new Date(a.date).getTime()
                    case 'oldest':
                        return new Date(a.date).getTime() - new Date(b.date).getTime()
                    case 'highest':
                        return Math.abs(b.amount) - Math.abs(a.amount)
                    case 'lowest':
                        return Math.abs(a.amount) - Math.abs(b.amount)
                    default:
                        return 0
                }
            })

            return filtered
        },

        summary() {
            const deposits = this.filteredTransactions.filter(t => t.type === TransactionType.DEPOSIT)
            const withdrawals = this.filteredTransactions.filter(t => t.type === TransactionType.WITHDRAW)

            const totalDeposits = deposits.reduce((s, t) => s + t.amount, 0)
            const totalWithdrawals = Math.abs(withdrawals.reduce((s, t) => s + t.amount, 0))

            return {
                totalDeposits: totalDeposits,
                totalWithdrawals: totalWithdrawals,
                netFlow: totalDeposits - totalWithdrawals,
                depositCount: deposits.length,
                withdrawalCount: withdrawals.length,
                avgTransaction:
                    Math.round(
                        this.filteredTransactions.reduce((s, t) => s + Math.abs(t.amount), 0) /
                        this.filteredTransactions.length
                    ) || 0
            }
        },

        hasActiveFilters(): boolean {
            return !!this.searchQuery || this.dateFilter !== 'all' || this.typeFilter !== 'all'
        },

        emptyStateMessage(): string {
            if (this.searchQuery) {
                return `No transactions found matching "${this.searchQuery}". Try adjusting your search terms.`
            }
            if (this.dateFilter !== 'all' || this.typeFilter !== 'all') {
                return 'No transactions match the selected filters. Try clearing some filters.'
            }
            return "You don't have any transactions yet. Your transaction history will appear here."
        }
    },

    methods: {
        clearAllFilters() {
            this.searchQuery = ''
            this.dateFilter = 'all'
            this.typeFilter = 'all'
            this.pagination.page = 1
        },

        applyQuickFilter(filter: string) {
            switch (filter) {
                case 'today':
                case 'week':
                case 'month':
                    this.dateFilter = filter
                    break
                case 'withdraw':
                    this.typeFilter = 'WITHDRAW'
                    break
                case 'deposit':
                    this.typeFilter = 'DEPOSIT'
                    break
            }
            this.pagination.page = 1
        },

        viewTransactionDetails(txn: Transaction) {
            console.log('Viewing transaction:', txn)
        },

        exportTransactions() {
            console.log('Exporting transactions...')
        },

        async fetchTransactions() {
            try {
                const response = await getUserTransactions()
                this.transactions = mapApiTransactionsToUi(response.data.transactions)
            } catch (error) {
                console.error('Error fetching transactions:', error)
            }
        }
    },

    mounted() {
        this.fetchTransactions()
    }
})
</script>
