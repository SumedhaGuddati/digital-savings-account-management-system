<template>
    <div class="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 py-8 px-6">
        <div class="max-w-5xl mx-auto">

            <!-- Header -->
            <div class="flex items-center gap-4 mb-6">
                <button @click="$router.back()" class="p-2 hover:bg-white rounded-lg transition-colors">
                    <FontAwesomeIcon :icon="faArrowLeft" class="text-gray-600" />
                </button>
                <div>
                    <h1 class="text-2xl font-semibold text-gray-900">Application Status</h1>
                    <p class="text-sm text-gray-500 mt-1">Track your savings account application</p>
                </div>
            </div>

            <!-- Loading -->
            <div v-if="isLoading" class="bg-white rounded-2xl border border-gray-200 shadow-sm p-12">
                <div class="flex flex-col items-center justify-center">
                    <div class="w-16 h-16 border-4 border-gray-200 border-t-indigo-600 rounded-full animate-spin"></div>
                    <p class="mt-4 text-gray-600">Fetching application details...</p>
                </div>
            </div>

            <!-- No Application -->
            <div v-else-if="!application" class="bg-white rounded-2xl border border-gray-200 shadow-sm p-12">
                <div class="text-center max-w-md mx-auto">
                    <div class="w-20 h-20 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
                        <FontAwesomeIcon :icon="faFileCirclePlus" class="text-3xl text-gray-400" />
                    </div>
                    <h2 class="text-xl font-semibold text-gray-900 mb-2">No Application Found</h2>
                    <p class="text-gray-600 mb-6">
                        You haven't applied for a savings account yet. Start your application now!
                    </p>
                    <button @click="$router.push('/')"
                        class="px-6 py-3 bg-gradient-to-r from-indigo-600 to-purple-600 text-white font-medium rounded-xl hover:from-indigo-700 hover:to-purple-700 transition-all shadow-sm">
                        Apply for Savings Account
                    </button>
                </div>
            </div>

            <!-- Application -->
            <div v-else class="space-y-6">
                <div class="bg-white rounded-2xl border border-gray-200 shadow-sm overflow-hidden">
                    <StatusHeader :application="application" />
                    <ApplicationProgress v-if="application.status === 'PENDING'" :application="application" />
                    <ApplicationDetails :application="application" />
                </div>

                <ApprovedCard v-if="application.status === 'APPROVED'" />
                <RejectedCard v-if="application.status === 'REJECTED'" @contact-support="contactSupport" />
                <ApplicationTimeline :application="application" />
                <HelpCard @contact-support="contactSupport" />
            </div>
        </div>
        <Toaster />
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faArrowLeft, faFileCirclePlus } from '@fortawesome/free-solid-svg-icons'

import { toast, Toaster } from '@steveyuowo/vue-hot-toast';
import "@steveyuowo/vue-hot-toast/vue-hot-toast.css";
import StatusHeader from '@/components/application-status/StatusHeader.vue'
import ApplicationProgress from '@/components/application-status/ApplicationProgress.vue'
import ApplicationDetails from '@/components/application-status/ApplicationDetails.vue'
import ApprovedCard from '@/components/application-status/ApprovedCard.vue'
import RejectedCard from '@/components/application-status/RejectedCard.vue'
import ApplicationTimeline from '@/components/application-status/ApplicationTimeline.vue'
import HelpCard from '@/components/application-status/HelpCard.vue'
import { Application } from '@/types/app/application'
import { getUserApplicationStatus } from '@/api/endpoints/users/users.api';
import { mapApiApplicationToUi } from '@/mapper/application/application.mapper';

export default defineComponent({
    name: 'ApplicationStatusView',
    components: {
        FontAwesomeIcon,
        StatusHeader,
        ApplicationProgress,
        ApplicationDetails,
        ApprovedCard,
        RejectedCard,
        ApplicationTimeline,
        HelpCard
    },
    data() {
        return {
            isLoading: false,
            application: null as Application | null
        }
    },
    computed: {
        faArrowLeft() { return faArrowLeft },
        faFileCirclePlus() { return faFileCirclePlus }
    },
    methods: {
        contactSupport() {
            console.log('Contact support clicked')
        },
        async fetchApplicationStatus() {
            this.isLoading = true
            try {
                const response = await getUserApplicationStatus()
                this.application = mapApiApplicationToUi(response.data)
            } catch (error) {
                console.error('Error fetching application status:', error)
                toast.error('Failed to fetch application status. Please try again later.')
                this.application = null
            } finally {
                this.isLoading = false
            }
        }
    },
    mounted() {
        this.fetchApplicationStatus()
    }
})
</script>
