<!-- @author Khamitkar Sai Chandan -->
<template>
    <div
        class="fixed inset-0 bg-gray-950/60 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-fade-in">
        <!-- Modal Card -->
        <div class="bg-white rounded-3xl max-w-md w-full shadow-2xl overflow-hidden animate-slide-up">
            <!-- Header with Dynamic Gradient -->
            <div class="px-6 py-8 text-center" :class="headerGradientClass">
                <div
                    class="w-20 h-20 mx-auto bg-white/20 backdrop-blur-md rounded-2xl flex items-center justify-center mb-4 shadow-lg">
                    <FontAwesomeIcon :icon="transactionIcon" class="text-4xl" :class="transactionIconClass" />
                </div>
                <h2 class="text-2xl font-bold text-white mb-1">{{ transactionTitle }}</h2>
                <p class="text-white/80 text-sm">{{ transactionSubtitle }}</p>
            </div>

            <!-- Amount Input Section -->
            <div class="p-8">
                <div class="space-y-6">
                    <!-- Label -->
                    <label class="block text-xs font-semibold text-gray-400 uppercase tracking-wider text-center">
                        Enter Amount
                    </label>

                    <!-- Currency Input -->
                    <div class="relative">
                        <span
                            class="absolute left-0 top-1/2 -translate-y-1/2 text-4xl font-light text-gray-400">₹</span>
                        <input ref="amountInput" v-model="amountDisplay" type="text" inputmode="numeric"
                            @focus="onFocus" @blur="onBlur" @keydown="handleKeydown"
                            class="w-full pl-10 pr-4 py-4 text-5xl font-semibold bg-transparent border-b-3 outline-none transition-all placeholder:text-gray-300"
                            :class="[
                                error ? 'border-red-400 focus:border-red-500' : 'border-gray-200 focus:border-indigo-400',
                                isFocused ? 'border-indigo-500' : ''
                            ]" placeholder="0" />
                    </div>

                    <!-- Error Message -->
                    <transition enter-active-class="transition duration-200 ease-out"
                        enter-from-class="opacity-0 -translate-y-2" enter-to-class="opacity-100 translate-y-0">
                        <p v-if="error" class="text-sm text-red-500 flex items-center justify-center gap-1.5 mt-2">
                            <FontAwesomeIcon :icon="faCircleExclamation" class="text-xs" />
                            {{ error }}
                        </p>
                    </transition>

                    <!-- Quick Amount Selector - Only show for deposit/withdraw -->
                    <div v-if="!error && amount && amount > 0" class="flex justify-center mt-4">
                        <span class="text-sm text-gray-500 bg-gray-100 px-4 py-2 rounded-full">
                            {{ formattedAmount }}
                        </span>
                    </div>

                    <!-- Quick Amount Buttons -->
                    <div class="grid grid-cols-4 gap-2 pt-4">
                        <button v-for="quickAmount in quickAmounts" :key="quickAmount" type="button"
                            @click="setQuickAmount(quickAmount)"
                            class="px-3 py-2.5 text-sm font-medium border rounded-xl transition-all cursor-pointer" :class="[
                                isQuickAmountSelected(quickAmount)
                                    ? type === TransactionType.DEPOSIT
                                        ? 'bg-emerald-50 border-emerald-300 text-emerald-700'
                                        : 'bg-red-50 border-red-300 text-red-700'
                                    : 'border-gray-200 text-gray-700 hover:bg-gray-50 hover:border-gray-300'
                            ]">
                            ₹{{ quickAmount.toLocaleString() }}
                        </button>
                    </div>

                    <!-- Action Buttons -->
                    <div class="flex gap-3 pt-6">
                        <button type="button" @click="$emit('close')"
                            class="flex-1 px-4 py-3.5 border border-gray-200 rounded-xl text-gray-700 font-medium hover:bg-gray-50 transition-all cursor-pointer">
                            Cancel
                        </button>
                        <button type="button" @click="handleSubmit" :disabled="!isValid || isProcessing"
                            class="flex-1 px-4 py-3.5 rounded-xl text-white font-medium transition-all transform active:scale-[0.98] disabled:opacity-50 disabled:cursor-not-allowed disabled:active:scale-100 cursor-pointer"
                            :class="buttonGradientClass">
                            <span v-if="isProcessing" class="flex items-center justify-center gap-2">
                                <FontAwesomeIcon :icon="faSpinner" spin />
                                Processing...
                            </span>
                            <span v-else>{{ actionButtonText }}</span>
                        </button>
                    </div>

                    <!-- Quick Tips -->
                    <div v-if="type === TransactionType.WITHDRAW" class="text-xs text-center text-gray-400 pt-2">
                        <FontAwesomeIcon :icon="faInfoCircle" class="mr-1" />
                        Withdrawals are processed instantly to your linked account
                    </div>
                    <div v-else-if="type === TransactionType.DEPOSIT" class="text-xs text-center text-gray-400 pt-2">
                        <FontAwesomeIcon :icon="faInfoCircle" class="mr-1" />
                        Deposits reflect instantly in your account
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, nextTick } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import {
    faCirclePlus,
    faWallet,
    faArrowDown,
    faArrowUp,
    faSpinner,
    faCircleExclamation,
    faInfoCircle
} from '@fortawesome/free-solid-svg-icons'
import { TransactionType } from '@/types/api/banking';
import { depositToAccount, withdrawFromAccount } from '@/api/endpoints/users/users.api';
import { toast } from '@steveyuowo/vue-hot-toast';

export default defineComponent({
    name: 'TransactionModal',
    components: { FontAwesomeIcon },
    props: {
        type: {
            type: String as () => TransactionType,
            required: true
        },
        maxAmount: {
            type: Number,
            default: null
        },
        balance: {
            type: Number,
            default: 0
        }
    },
    emits: ['close', 'confirm'],

    data() {
        return {
            amount: null as number | null,
            amountDisplay: '',
            isProcessing: false,
            isFocused: false,
            amountInput: null as HTMLInputElement | null,

            // Icons
            faCirclePlus,
            faWallet,
            faSpinner,
            faCircleExclamation,
            faInfoCircle,
            TransactionType
        }
    },

    computed: {
        quickAmounts(): number[] {
            if (this.type === TransactionType.DEPOSIT) {
                return [5000, 10000, 25000, 50000]
            } else {
                return [1000, 5000, 10000, 25000]
            }
        },

        transactionConfig() {
            if (this.type === TransactionType.DEPOSIT) {
                return {
                    title: 'Deposit Money',
                    subtitle: 'Add funds to your savings account',
                    icon: faArrowDown,
                    iconClass: 'text-emerald-100',
                    headerGradient: 'bg-gradient-to-br from-emerald-600 to-teal-600',
                    buttonGradient:
                        'bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-700 hover:to-teal-700',
                    buttonText: 'Deposit Now',
                    minAmount: 1,
                    maxAmount: 1000000
                }
            } else {
                return {
                    title: 'Withdraw Money',
                    subtitle: `Available Balance: ₹${this.balance?.toLocaleString('en-IN', {
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 2
                    }) || '0.00'}`,
                    icon: faArrowUp,
                    iconClass: 'text-red-100',
                    headerGradient: 'bg-gradient-to-br from-red-600 to-rose-600',
                    buttonGradient:
                        'bg-gradient-to-r from-red-600 to-rose-600 hover:from-red-700 hover:to-rose-700',
                    buttonText: 'Withdraw Now',
                    minAmount: 100,
                    maxAmount: this.maxAmount || this.balance || 0
                }
            }
        },

        transactionTitle() {
            return this.transactionConfig.title
        },
        transactionSubtitle() {
            return this.transactionConfig.subtitle
        },
        transactionIcon() {
            return this.transactionConfig.icon
        },
        transactionIconClass() {
            return this.transactionConfig.iconClass
        },
        headerGradientClass() {
            return this.transactionConfig.headerGradient
        },
        buttonGradientClass() {
            return this.transactionConfig.buttonGradient
        },
        actionButtonText() {
            return this.transactionConfig.buttonText
        },

        formattedAmount() {
            if (!this.amount) return '₹0'
            return `₹${this.amount.toLocaleString('en-IN', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            })}`
        },

        error(): string | null {
            if (!this.amount) return null

            const min = this.transactionConfig.minAmount
            const max = this.transactionConfig.maxAmount

            if (this.amount < min) {
                if (this.type === TransactionType.WITHDRAW) {
                    return `Minimum withdrawal amount is ₹${min}`
                }
                return `Minimum deposit amount is ₹${min}`
            }

            if (this.amount > max) {
                if (this.type === TransactionType.WITHDRAW) {
                    return `Insufficient balance. Maximum withdrawal: ₹${max.toLocaleString()}`
                }
                return `Maximum deposit amount is ₹${max.toLocaleString()}`
            }

            if (this.type === TransactionType.WITHDRAW && this.amount % 100 !== 0) {
                return 'Amount must be in multiples of ₹100'
            }

            return null
        },

        isValid() {
            return !!this.amount && !this.error
        }
    },

    watch: {
        amountDisplay(newValue: string) {
            if (newValue) {
                const parsed = this.parseAmount(newValue)
                this.amount = parsed

                if (document.activeElement === this.amountInput) {
                    const caretPosition =
                        newValue.length - newValue.replace(/[^\d]/g, '').length
                    this.amountDisplay = this.formatCurrencyInput(newValue)
                    nextTick(() => {
                        this.amountInput?.setSelectionRange(caretPosition, caretPosition)
                    })
                }
            } else {
                this.amount = null
            }
        },

        type: {
            immediate: true,
            handler() {
                // Reset state and focus the input whenever the transaction type changes
                this.amount = null
                this.amountDisplay = ''
                this.isProcessing = false
                nextTick(() => {
                    this.amountInput?.focus()
                })
            }
        }
    },

    methods: {
        formatCurrencyInput(value: string): string {
            let numeric = value.replace(/[^\d]/g, '')
            numeric = numeric.replace(/^0+/, '')
            if (numeric === '') return ''
            const num = parseInt(numeric, 10)
            return num.toLocaleString('en-IN')
        },

        parseAmount(displayValue: string): number | null {
            if (!displayValue) return null
            const numeric = displayValue.replace(/[^\d]/g, '')
            return numeric ? parseInt(numeric, 10) : null
        },

        setQuickAmount(quickAmount: number) {
            this.amount = quickAmount
            this.amountDisplay = quickAmount.toLocaleString('en-IN')
        },

        isQuickAmountSelected(quickAmount: number): boolean {
            return this.amount === quickAmount
        },

        handleKeydown(e: KeyboardEvent) {
            if (
                [46, 8, 9, 27, 13, 110, 190].includes(e.keyCode) ||
                (e.keyCode === 65 && (e.ctrlKey || e.metaKey)) ||
                (e.keyCode === 67 && (e.ctrlKey || e.metaKey)) ||
                (e.keyCode === 86 && (e.ctrlKey || e.metaKey)) ||
                (e.keyCode === 88 && (e.ctrlKey || e.metaKey)) ||
                (e.keyCode >= 35 && e.keyCode <= 39)
            ) {
                return
            }
            if (e.key < '0' || e.key > '9') {
                e.preventDefault()
            }
        },

        onFocus() {
            this.isFocused = true
        },

        onBlur() {
            this.isFocused = false
            if (this.amountDisplay) {
                this.amountDisplay = this.formatCurrencyInput(this.amountDisplay)
            }
        },

        async handleSubmit() {
            if (!this.isValid) return

            try {
                this.isProcessing = true

                if (this.type === TransactionType.WITHDRAW) {
                    await withdrawFromAccount(this.amount!)
                } else {
                    await depositToAccount(this.amount!)
                }

                this.$emit('confirm', this.amount)
                this.$emit('close')
            } catch (error: any) {
                toast.error(error.message || 'Transaction failed. Please try again.')
            }
            finally {
                this.isProcessing = false
            }
        }
    }
})
</script>


<style scoped>
@keyframes fadeIn {
    from {
        opacity: 0;
    }

    to {
        opacity: 1;
    }
}

@keyframes slideUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.animate-fade-in {
    animation: fadeIn 0.2s ease-out;
}

.animate-slide-up {
    animation: slideUp 0.3s ease-out;
}

/* Remove spinner buttons from number input */
input[type="text"]::-webkit-outer-spin-button,
input[type="text"]::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
}

/* Custom border bottom with 3px thickness */
.border-b-3 {
    border-bottom-width: 3px;
}
</style>