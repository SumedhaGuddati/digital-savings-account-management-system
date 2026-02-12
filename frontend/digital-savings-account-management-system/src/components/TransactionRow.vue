<template>
    <div class="grid grid-cols-12 gap-4 px-6 py-4 hover:bg-gray-50/80 transition-colors">
        <!-- Description & Category -->
        <div class="col-span-5 sm:col-span-6 flex items-center gap-3">
            <div class="w-10 h-10 rounded-xl flex items-center justify-center flex-shrink-0"
                :class="transaction.type === TransactionType.DEPOSIT ? 'bg-emerald-100' : getCategoryColor(transaction.category)">
                <FontAwesomeIcon :icon="getCategoryIcon(transaction.category)"
                    :class="transaction.type === TransactionType.DEPOSIT? 'text-emerald-600' : getCategoryIconColor(transaction.category)"
                    class="text-sm" />
            </div>
            <div>
                <p class="font-medium text-gray-900">{{ transaction.description }}</p>
                <p class="text-xs text-gray-500 mt-0.5 flex items-center gap-1">
                    <span>{{ transaction.category || (transaction.type === TransactionType.DEPOSIT ? 'Income' : 'Expense') }}</span>
                    <span class="w-1 h-1 bg-gray-300 rounded-full"></span>
                    <span>ID: {{ transaction.id }}</span>
                </p>
            </div>
        </div>

        <!-- Date & Time -->
        <div class="col-span-3 sm:col-span-2 flex flex-col justify-center">
            <p class="text-sm text-gray-900">{{ formatDate(transaction.date, 'DD MMM YYYY') }}</p>
            <p class="text-xs text-gray-400">{{ formatDate(transaction.date, 'hh:mm A') }}</p>
        </div>

        <!-- Amount -->
        <div class="col-span-2 sm:col-span-2 flex flex-col items-end justify-center">
            <p class="text-base font-semibold" :class="transaction.type === TransactionType.DEPOSIT ? 'text-emerald-600' : 'text-red-500'">
                {{ transaction.type === TransactionType.DEPOSIT? '+' : '-' }}₹{{ Math.abs(transaction.amount).toLocaleString('en-IN') }}
            </p>
            <p class="text-xs text-gray-400 mt-0.5">{{ transaction.type }}</p>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import {
    faShoppingCart,
    faBolt,
    faFilm,
    faUtensils,
    faWallet,
    faBuilding,
    faArrowUp,
    faArrowDown,
    faQuestionCircle
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
            TransactionType
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
        getCategoryIcon(category = '') {
            const icons: Record<string, any> = {
                Shopping: faShoppingCart,
                Utilities: faBolt,
                Entertainment: faFilm,
                'Food & Dining': faUtensils,
                Transfer: faWallet,
                Income: faArrowUp,
                Interest: faBuilding,
                Expense: faArrowDown
            }
            return icons[category] || faQuestionCircle
        },
        getCategoryColor(category = '') {
            const colors: Record<string, string> = {
                Shopping: 'bg-blue-100',
                Utilities: 'bg-yellow-100',
                Entertainment: 'bg-purple-100',
                'Food & Dining': 'bg-orange-100',
                Transfer: 'bg-indigo-100',
                Interest: 'bg-emerald-100'
            }
            return colors[category] || 'bg-gray-100'
        },
        getCategoryIconColor(category = '') {
            const colors: Record<string, string> = {
                Shopping: 'text-blue-600',
                Utilities: 'text-yellow-600',
                Entertainment: 'text-purple-600',
                'Food & Dining': 'text-orange-600',
                Transfer: 'text-indigo-600',
                Interest: 'text-emerald-600'
            }
            return colors[category] || 'text-gray-600'
        }
    }
})
</script>
