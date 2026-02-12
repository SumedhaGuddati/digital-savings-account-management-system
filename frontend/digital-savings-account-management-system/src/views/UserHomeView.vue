<template>
    <div class="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100">
        <!-- Navbar -->
        <!-- // TODO: get email from token or user store -->
        <AppNavbar :user-name="account?.accountHolderName || 'Guest'" :user-email="userEmail" @profile="goProfile"
            @statements="goStatements" @logout="handleLogout" />

        <!-- Main Content -->
        <div class="max-w-7xl mx-auto px-6 py-8">
            <!-- Loading -->
            <div v-if="isLoading" class="bg-white rounded-2xl border border-gray-200 shadow-sm p-12">
                <div class="flex flex-col items-center justify-center">
                    <div class="w-16 h-16 border-4 border-gray-200 border-t-indigo-600 rounded-full animate-spin"></div>
                    <p class="mt-4 text-gray-600">Fetching application details...</p>
                </div>
            </div>
            <div v-else-if="account" class="space-y-8">
                <!-- Welcome Header -->
                <div class="flex justify-between items-end">
                    <div>
                        <div class="flex items-center gap-3">
                            <h1 class="text-3xl font-semibold text-gray-900">
                                Welcome back,
                            </h1>
                            <span
                                class="text-3xl font-semibold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                                {{ account?.accountHolderName }}
                            </span>
                        </div>
                        <p class="text-sm text-gray-500 mt-2 flex items-center gap-2">
                            <FontAwesomeIcon :icon="faCircle" class="text-[8px] text-emerald-500" />
                            Account active · Last login today at 9:42 AM
                        </p>
                    </div>

                    <button
                        class="px-4 py-2 text-sm text-gray-600 hover:text-gray-900 bg-white rounded-lg border border-gray-200 hover:border-gray-300 transition-all flex items-center gap-2 shadow-sm cursor-not-allowed">
                        <FontAwesomeIcon :icon="faArrowDown" class="text-emerald-500" />
                        Download Statement
                    </button>
                </div>

                <!-- Account Cards -->
                <div class="flex flex-col lg:flex-row lg:items-start lg:space-x-6 space-y-6 lg:space-y-0">
                    <AccountCard :accountNumber="account?.accountNumber || 'N/A'" :tier="'SMART'"
                        @view-details="viewAccountDetails" />

                    <div class="w-full lg:w-80 shrink-0">
                        <BalanceCard :balance="account?.balance || 0" @deposit="openDepositModal"
                            @withdraw="openWithdrawModal" />
                    </div>
                </div>

                <!-- Quick Actions -->
                <QuickActions @deposit="openDepositModal" @withdraw="openWithdrawModal" @statements="goStatements" />

                <!-- Transactions & Sidebar -->
                <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
                    <div class="lg:col-span-2">
                        <TransactionsPreview :transactions="transactions" @view-all="viewAllTransactions" />
                    </div>

                    <div class="lg:col-span-1 space-y-6">
                        <AccountBenefitsCard :benefits="benefits" @explore="goBenefits" />
                        <HelpCard @contact="contactSupport" />
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal -->
        <TransactionModal v-if="showTransactionModal" :type="transactionType as TransactionType"
            :balance="account?.balance || 0" :max-amount="account?.balance || 0" @close="showTransactionModal = false"
            @confirm="handleTransaction" />

        <Toaster />
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faCircle, faArrowDown } from '@fortawesome/free-solid-svg-icons'

import { toast, Toaster } from '@steveyuowo/vue-hot-toast';
import "@steveyuowo/vue-hot-toast/vue-hot-toast.css";

import AppNavbar from '@/components/layout/AppNavbar.vue'
import QuickActions from '@/components/userHome/QuickActions.vue'
import AccountBenefitsCard from '@/components/userHome/AccountBenefitsCard.vue'
import HelpCard from '@/components/userHome/HelpCard.vue'

import AccountCard from '@/components/userHome/AccountCard.vue'
import BalanceCard from '@/components/userHome//BalanceCard.vue'
import TransactionsPreview from '@/components/userHome/TransactionsPreview.vue'
import TransactionModal from '@/components/userHome/TransactionModal.vue'

import { Account, Transaction } from '@/types/app/banking'


import axios from 'axios'
import { TransactionType } from '@/types/api/banking';

export default defineComponent({
    name: 'UserHomeView',
    components: {
        FontAwesomeIcon,
        AppNavbar,
        QuickActions,
        AccountBenefitsCard,
        HelpCard,
        AccountCard,
        BalanceCard,
        TransactionsPreview,
        TransactionModal,
        Toaster
    },
    data() {
        return {
            isLoading: false,
            account: null as Account | null,
            userEmail: 'rahul.sharma@example.com',
            showTransactionModal: false,
            transactionType: TransactionType.DEPOSIT as TransactionType,
            transactions: [
            ] as Transaction[],
            benefits: [
                '5.5% p.a. interest rate',
                'Zero maintenance fee',
                'Free international transactions'
            ]
        }
    },
    computed: {
        faCircle() { return faCircle },
        faArrowDown() { return faArrowDown }
    },
    methods: {
        // Navbar
        goProfile() {
            this.$router.push('/me/profile')
        },
        goStatements() {
            this.$router.push('/me/transactions')
        },
        handleLogout() {
            this.$router.push('/login')
        },

        // Dashboard
        viewAccountDetails() {
            this.$router.push('/me/account/details')
        },
        viewAllTransactions() {
            this.$router.push('/me/transactions')
        },
        openDepositModal() {
            this.transactionType = TransactionType.DEPOSIT
            this.showTransactionModal = true
        },
        openWithdrawModal() {
            this.transactionType = TransactionType.WITHDRAW
            this.showTransactionModal = true
        },
        handleTransaction(amount: number) {
            toast.success(`${this.transactionType} of ₹${amount} successful!`)
            console.log(`${this.transactionType} of ₹${amount} successful`)
        },
        goBenefits() {
            this.$router.push('/account-types')
        },
        contactSupport() {
            console.log('Contact support')
        },
        async fetchAccountDetails() {
            try {
                this.isLoading = true
                const response = await axios.get('/api/accounts/me', {
                    headers: {
                        Authorization: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MUBleGFtcGxlLmNvbSIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzcwODY4MTM0LCJleHAiOjE3NzA5NTQ1MzR9.k4a5bw62l7xCilJHVVZnKXfi1mbnjqYrd2YXhu74nQA`
                    },
                })
                this.account = response.data
            } catch (error) {
                console.error('Error fetching account details:', error)
                toast.error('Failed to fetch account details. Please try again later.')
                this.account = null
            }
            finally {
                this.isLoading = false
            }
        }
    },
    mounted() {
        this.fetchAccountDetails()
        // console.log(process.env.VUE_APP_USER_AUTH_TOKEN);
    }
})
</script>
