<template>
    <div class="bg-white rounded-2xl border border-gray-200/80 shadow-sm overflow-hidden">
        <!-- Header -->
        <div
            class="grid grid-cols-12 gap-4 px-6 py-4 bg-gray-50/80 border-b border-gray-200 text-xs font-medium text-gray-500 uppercase tracking-wider">
            <div class="col-span-5 sm:col-span-6">Description</div>
            <div class="col-span-3 sm:col-span-3">Date</div>
            <div class="col-span-2 sm:col-span-3 text-right">Amount</div>
        </div>

        <div v-if="transactions.length > 0">
            <TransactionRow v-for="txn in transactions" :key="txn.id" :transaction="txn"
                class="cursor-pointer hover:bg-gray-50/80 transition-colors" />
        </div>

        <TransactionsEmptyState v-else :has-active-filters="hasActiveFilters" :message="emptyStateMessage"
            @clear="$emit('clear')" />
    </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import TransactionRow from './TransactionRow.vue';
import TransactionsEmptyState from './TransactionsEmptyState.vue'
import { Transaction } from '@/types/app/banking'

export default defineComponent({
    name: 'TransactionsList',
    components: { TransactionRow, TransactionsEmptyState },
    props: {
        transactions: { type: Array as PropType<Transaction[]>, required: true },
        hasActiveFilters: { type: Boolean, required: true },
        emptyStateMessage: { type: String, required: true }
    },
    emits: ['clear']
})
</script>
