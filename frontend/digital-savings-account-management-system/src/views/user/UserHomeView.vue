<!-- @author Khamitkar Sai Chandan -->
<template>
    <div class="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 relative">

        <AppNavbar :user-name="account?.accountHolderName || 'Guest'" :user-email="this.$store.getters['auth/getEmail']" @profile="goProfile"
            @statements="goStatements" @logout="handleLogout" />

        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8 lg:py-10">

            <AppLoader v-show="isLoading" />

            <div v-if="account" class="space-y-6 sm:space-y-8" :class="{ 'pointer-events-none opacity-50': isLoading }">
                <div class="flex flex-col sm:flex-row sm:justify-between sm:items-end gap-4">
                    <div>
                        <div class="flex flex-col sm:flex-row sm:items-center gap-1 sm:gap-3">
                            <h1 class="text-2xl sm:text-3xl font-semibold text-gray-900">
                                Welcome back,
                            </h1>
                            <span
                                class="text-2xl sm:text-3xl font-semibold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent break-words">
                                {{ account?.accountHolderName }}
                            </span>
                        </div>

                        <p class="text-xs sm:text-sm text-gray-500 mt-2 flex items-center gap-2">
                            <FontAwesomeIcon :icon="faCircle" class="text-[8px] text-emerald-500" />
                            Account active
                        </p>
                    </div>
                </div>

                <div class="flex flex-col lg:flex-row lg:items-stretch gap-6">

                    <div class="flex-1 sm:flex-none">
                        <AccountCard :accountNumber="account?.accountNumber || 'N/A'"
                            :tier="account?.accountType || 'N/A'" :accountHolderName="account?.accountHolderName" @view-details="viewAccountDetails" />
                    </div>

                    <div class="w-full lg:w-96 shrink-0">
                        <BalanceCard :balance="account?.balance || 0" @deposit="openDepositModal"
                            @withdraw="openWithdrawModal" />
                    </div>

                </div>

                <QuickActions @deposit="openDepositModal" @withdraw="openWithdrawModal" @statements="goStatements" />

                <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
                    <div class="lg:col-span-2">
                        <TransactionsPreview :transactions="transactions" @view-all="viewAllTransactions" />
                    </div>

                    <div class="space-y-6">
                        <AccountBenefitsCard :benefits="benefits" />
                        <HelpCard />
                    </div>
                </div>
            </div>
        </div>

        <TransactionModal v-if="showTransactionModal" :type="transactionType as TransactionType"
            :balance="account?.balance || 0" :max-amount="account?.balance || 0" @close="showTransactionModal = false"
            @confirm="handleTransactionConfirmation" />

        <Toaster />
    </div>
</template>


<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faCircle, faArrowDown } from '@fortawesome/free-solid-svg-icons'

import { toast, Toaster } from '@steveyuowo/vue-hot-toast';
import "@steveyuowo/vue-hot-toast/vue-hot-toast.css";

import AppNavbar from '@/components/layout/user/AppNavbar.vue'
import QuickActions from '@/components/userHome/QuickActions.vue'
import AccountBenefitsCard from '@/components/userHome/AccountBenefitsCard.vue'
import HelpCard from '@/components/userHome/HelpCard.vue'

import AccountCard from '@/components/userHome/AccountCard.vue'
import BalanceCard from '@/components/userHome//BalanceCard.vue'
import TransactionsPreview from '@/components/userHome/TransactionsPreview.vue'
import TransactionModal from '@/components/userHome/TransactionModal.vue'

import { Account, Transaction } from '@/types/app/banking'

import { TransactionType } from '@/types/api/banking';
import { getUserHomeDetails, getUserTransactions } from '@/api/endpoints/users/users.api';
import { mapApiTransactionsToUi } from '@/mapper/banking/transaction.mapper';
import AppLoader from '@/components/ui/loader/AppLoader.vue';

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
        Toaster,
        AppLoader
    },
    data() {
        return {
            isLoading: false,
            account: null as Account | null,
            showTransactionModal: false,
            transactionType: TransactionType.DEPOSIT as TransactionType,
            transactions: [
            ] as Transaction[],
            benefits: [
                '5.5% p.a. interest rate',
                'Zero maintenance fee',
                'Free international transactions'
            ],
            showTransactionSuccess: false,
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
        async handleTransactionConfirmation(amount: number) {
            toast.success(`${this.transactionType} of ₹${amount} successful!`)

            await Promise.all([
                this.fetchAccountDetails(),
                this.fetchTransactions()
            ]);

        },
        async fetchAccountDetails() {
            try {
                this.isLoading = true
                const response = await getUserHomeDetails()
                this.account = response.data
            } catch (error) {
                toast.error('Failed to fetch account details. Please try again later.')
                this.account = null
            }
            finally {
                this.isLoading = false
            }
        },
        async fetchTransactions() {
            try {
                const response = await getUserTransactions()
                this.transactions = mapApiTransactionsToUi(response.data.transactions)
            } catch (error) {
                toast.error('Failed to fetch transactions. Please try again later.')
            }
        }
    },
    mounted() {
        this.fetchAccountDetails()
        this.fetchTransactions()
    }
})
</script>
