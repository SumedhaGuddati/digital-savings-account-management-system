<template>
    <div class="bg-white rounded-xl border border-gray-200 shadow-sm p-5">
        <div class="flex justify-between items-center mb-4">
            <h3 class="text-base font-bold text-gray-800">
                Recent Transactions
            </h3>

            <button @click="$router.push('/me/transactions')"
                class="text-sm font-medium text-indigo-600 hover:text-indigo-700 transition">
                View all
            </button>
        </div>

        <template v-if="previewTransactions.length > 0">
            <TransactionRow v-for="txn in previewTransactions" :key="txn.id" :transaction="txn" />
        </template>

        <div v-else class="flex flex-col items-center justify-center text-center py-10 px-4">
            <div class="text-4xl mb-3">💳</div>
            <p class="text-sm font-medium text-gray-700">
                No transactions yet
            </p>
            <p class="text-xs text-gray-500 mt-1 max-w-xs">
                Start your first transaction and watch your savings grow.
                Your activity will appear here.
            </p>
        </div>
    </div>
</template>


<script lang="ts">
import { defineComponent, PropType } from 'vue'
import TransactionRow from '../transactions/TransactionRow.vue';
import { Transaction } from '@/types/app/banking'

export default defineComponent({
    name: 'TransactionsPreview',
    components: { TransactionRow },
    props: {
        transactions: { type: Array as PropType<Transaction[]>, required: true }
    },
    computed: {
        previewTransactions(): Transaction[] {
            return this.transactions.slice(0, 3)
        }
    }
})
</script>
