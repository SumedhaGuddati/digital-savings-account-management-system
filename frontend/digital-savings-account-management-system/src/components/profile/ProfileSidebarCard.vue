<template>
    <div
        class="bg-white rounded-2xl border border-gray-200/80 shadow-sm hover:shadow-md transition-shadow overflow-hidden sticky top-24 h-fit">
        <!-- Cover -->
        <div class="h-32 bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500 relative">
            <div class="absolute inset-0 bg-black/10"></div>
            <div class="absolute -bottom-12 left-6">
                <div class="relative">
                    <div class="w-28 h-28 rounded-2xl bg-white p-1.5 shadow-xl">
                        <div
                            class="w-full h-full bg-gradient-to-br from-indigo-500 to-purple-600 rounded-xl flex items-center justify-center">
                            <span class="text-white text-3xl font-bold">{{ userInitials }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Info -->
        <div class="pt-16 pb-6 px-6">
            <div class="flex justify-between items-start">
                <div>
                    <h2 class="text-xl font-bold text-gray-900">{{ userProfile.accountHolderName }}</h2>
                    <p class="text-sm text-gray-500 mt-1 flex items-center gap-1.5">
                        <FontAwesomeIcon :icon="faEnvelope" class="text-xs" />
                        <!-- {{ userProfile.email }} -->
                          test@example.com
                    </p>
                    <p class="text-xs text-gray-400 mt-2 flex items-center gap-1.5">
                        <FontAwesomeIcon :icon="faCalendar" class="text-xs" />
                        Member since {{ formatDate(userProfile.reviewDate) }}
                    </p>
                </div>
                <span
                    class="px-3 py-1.5 bg-gradient-to-r from-indigo-50 to-purple-50 text-indigo-700 text-xs font-medium rounded-full border border-indigo-200 text-nowrap">
                    {{ userProfile.accountType }} Tier
                </span>
            </div>

            <!-- Stats -->
            <div class="flex justify-center items-center mt-6 pt-6 border-t border-gray-100">
                <div class="text-center">
                    <p class="text-2xl font-bold text-gray-900">{{ userProfile.transactionsCount }}</p>
                    <p class="text-xs text-gray-500 mt-1">Transactions</p>
                </div>
            </div>

            <!-- Verification -->
            <div class="mt-6 pt-6 border-t border-gray-100">
                <div class="flex items-center justify-between mb-3">
                    <span class="text-sm font-medium text-gray-700">Verification Status</span>
                    <span class="text-xs font-medium text-emerald-600 bg-emerald-50 px-2.5 py-1 rounded-full">
                        Verified
                    </span>
                </div>
                <div class="space-y-2">
                    <div class="flex items-center gap-2 text-sm">
                        <FontAwesomeIcon :icon="faCircleCheck" class="text-emerald-500" />
                        <span class="text-gray-600">Email verified</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faCamera, faEnvelope, faCalendar, faCircleCheck } from '@fortawesome/free-solid-svg-icons'
import { formatDate } from '@/utils/dateFormatter';
import { UserProfile } from '@/types/api/user'

export default defineComponent({
    name: 'ProfileSidebarCard',
    components: { FontAwesomeIcon },
    props: {
        userProfile: { type: Object as () => UserProfile, required: true },
    },
    data() {
        return {
            faCamera,
            faEnvelope,
            faCalendar,
            faCircleCheck,
            formatDate
        }
    },
    computed: {
        userInitials(): string {
            return this.userProfile.accountHolderName
                .split(' ')
                .map((n: string) => n[0])
                .join('')
                .toUpperCase()
                .slice(0, 2)
        }
    }
})
</script>
