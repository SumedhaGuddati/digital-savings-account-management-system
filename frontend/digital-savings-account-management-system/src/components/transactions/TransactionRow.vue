<template>
    <div class="grid grid-cols-1 sm:grid-cols-12 gap-3 px-4 sm:px-6 py-3 sm:py-4 hover:bg-gray-50/80 transition-colors">
    
        <div class="sm:col-span-6 flex items-center gap-3">
    
            <div class="w-9 h-9 sm:w-10 sm:h-10 rounded-xl flex items-center justify-center flex-shrink-0" :class="transaction.type === TransactionType.DEPOSIT
                ? 'bg-emerald-100'
                : 'bg-red-100'">
                <FontAwesomeIcon :icon="faArrowRight" class="text-xs sm:text-sm transition-transform" :class="transaction.type === TransactionType.DEPOSIT
                    ? 'text-emerald-600 rotate-45'
                    : 'text-red-600 rotate-225'" />
            </div>

            <div class="min-w-0 flex-1">
                <p class="text-sm sm:text-base font-medium text-gray-900 truncate">
                    {{ transaction.description }}
                </p>

                <p class="text-xs text-gray-500 mt-0.5 truncate">
                    {{ transaction.category ||
                        (transaction.type === TransactionType.DEPOSIT
                            ? 'Deposit'
                    : 'Withdraw') }}
                    • ID: #{{ transaction.id }}
                </p>
            </div>
        </div>

        <div class="sm:col-span-3 flex flex-row sm:flex-col justify-between sm:justify-center text-left sm:text-left">
            <p class="text-sm sm:text-base text-gray-900">
                {{ formatDate(transaction.date, 'DD MMM YYYY') }}
            </p>
            <p class="text-xs text-gray-400">
                {{ formatDate(transaction.date, 'hh:mm A') }}
            </p>
        </div>

        <div class="sm:col-span-3 flex flex-row sm:flex-col justify-between sm:items-end items-start">
            <p class="text-sm sm:text-base font-semibold" :class="transaction.type === TransactionType.DEPOSIT
                ? 'text-emerald-600'
                : 'text-red-500'">
                {{ transaction.type === TransactionType.DEPOSIT ? '+' : '-' }}₹{{
                    Math.abs(transaction.amount).toLocaleString('en-IN')
                }}
            </p>

            <p class="text-xs text-gray-400">
                {{ transaction.type }}
            </p>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import {
    faArrowRight
} from '@fortawesome/free-solid-svg-icons'
import { TransactionType } from '@/types/api/banking';

export default defineComponent({
    name: 'TransactionRow',
    components: { FontAwesomeIcon },
    props: {
        transaction: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            TransactionType,
            faArrowRight
        }
    },
    computed: {
        statusClass() {
            const status = this.transaction.status || 'completed'
            switch (status.toLowerCase()) {
                case 'completed':
                    return 'bg-emerald-100 text-emerald-700'
                case 'pending':
                    return 'bg-amber-100 text-amber-700'
                case 'failed':
                    return 'bg-red-100 text-red-700'
                default:
                    return 'bg-gray-100 text-gray-700'
            }
        }
    },
    methods: {
        formatDate(dateString: string, format: string) {
            const date = new Date(dateString)
            if (format === 'DD MMM YYYY') {
                return date.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' })
            } else if (format === 'hh:mm A') {
                return date.toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit', hour12: true })
            }
            return dateString
        },
    }
})
</script>
