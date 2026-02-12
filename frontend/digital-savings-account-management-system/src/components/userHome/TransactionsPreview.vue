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

        <TransactionRow v-for="txn in previewTransactions" :key="txn.id" :transaction="txn" />
    </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import TransactionRow from '@/components/TransactionRow.vue'
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
