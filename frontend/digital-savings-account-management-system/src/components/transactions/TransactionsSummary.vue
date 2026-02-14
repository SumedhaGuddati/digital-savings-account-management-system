<!-- @author Khamitkar Sai Chandan -->
<template>
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
        <div class="bg-white rounded-xl border border-gray-200/80 p-4 shadow-sm">
            <p class="text-xs text-gray-500 uppercase tracking-wider">Total Deposits</p>
            <p class="text-2xl font-bold text-emerald-600 mt-1">+₹{{ summary.totalDeposits.toLocaleString('en-IN') }}</p>
            <p class="text-xs text-gray-400 mt-1">{{ summary.depositCount }} transactions</p>
        </div>

        <div class="bg-white rounded-xl border border-gray-200/80 p-4 shadow-sm">
            <p class="text-xs text-gray-500 uppercase tracking-wider">Total Withdrawals</p>
            <p class="text-2xl font-bold text-red-500 mt-1">-₹{{ summary.totalWithdrawals.toLocaleString('en-IN') }}</p>
            <p class="text-xs text-gray-400 mt-1">{{ summary.withdrawalCount }} transactions</p>
        </div>

        <div class="bg-white rounded-xl border border-gray-200/80 p-4 shadow-sm">
            <p class="text-xs text-gray-500 uppercase tracking-wider">Net Flow</p>
            <p class="text-2xl font-bold" :class="summary.netFlow >= 0 ? 'text-emerald-600' : 'text-red-500'">
                {{ summary.netFlow >= 0 ? '+' : '-' }}₹{{ Math.abs(summary.netFlow).toLocaleString('en-IN') }}
            </p>
            <p class="text-xs text-gray-400 mt-1">Period balance</p>
        </div>

        <div class="bg-white rounded-xl border border-gray-200/80 p-4 shadow-sm">
            <p class="text-xs text-gray-500 uppercase tracking-wider">Avg. Transaction</p>
            <p class="text-2xl font-bold text-gray-900 mt-1">₹{{ summary.avgTransaction.toLocaleString('en-IN') }}</p>
            <p class="text-xs text-gray-400 mt-1">Per transaction</p>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'

export interface TransactionsSummaryData {
    totalDeposits: number
    totalWithdrawals: number
    netFlow: number
    depositCount: number
    withdrawalCount: number
    avgTransaction: number
}

export default defineComponent({
    name: 'TransactionsSummary',
    props: {
        summary: { type: Object as PropType<TransactionsSummaryData>, required: true }
    }
})
</script>
