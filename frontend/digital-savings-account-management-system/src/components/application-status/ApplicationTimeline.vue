<template>
    <div class="bg-white rounded-2xl border border-gray-200 shadow-sm p-6">
        <h3 class="font-semibold text-gray-900 mb-4 flex items-center gap-2">
            <FontAwesomeIcon :icon="faTimeline" class="text-indigo-600" />
            Application Timeline
        </h3>

        <div class="space-y-4">
            <!-- Submitted -->
            <TimelineItem :icon="faFile" icon-bg="bg-indigo-100" icon-color="text-indigo-600"
                title="Application Submitted" :date="formatDate(application.applicationDate)"
                description="Your application has been successfully submitted and is in queue for review."
                :show-line="true" />

            <!-- Under Review -->
            <TimelineItem :icon="faBuilding"
                :icon-bg="application.status !== 'PENDING' ? 'bg-indigo-100' : 'bg-gray-100'"
                :icon-color="application.status !== 'PENDING' ? 'text-indigo-600' : 'text-gray-400'"
                title="Under Review" :date="application.reviewDate ? formatDate(application.reviewDate) : 'In progress'"
                :description="application.status === 'PENDING'
                        ? 'Your application is being reviewed by our team.'
                        : 'Application has been reviewed.'
                    " :show-line="application.status === 'PENDING'" />

            <!-- Decision -->
            <TimelineItem v-if="application.status !== 'PENDING'"
                :icon="application.status === 'APPROVED' ? faCircleCheck : faCircleExclamation"
                :icon-bg="application.status === 'APPROVED' ? 'bg-emerald-100' : 'bg-red-100'"
                :icon-color="application.status === 'APPROVED' ? 'text-emerald-600' : 'text-red-500'"
                :title="application.status === 'APPROVED' ? 'Approved' : 'Rejected'"
                :date="formatDate(application.reviewDate!)" :description="application.status === 'APPROVED'
                        ? 'Your application has been approved. Your account is now active.'
                        : 'Your application was not approved. Please check the reason above.'
                    " :show-line="false" />
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import {
    faTimeline,
    faFile,
    faBuilding,
    faCircleCheck,
    faCircleExclamation
} from '@fortawesome/free-solid-svg-icons'
import TimelineItem from './TimelineItem.vue'
import { Application } from '@/types/app/application'

export default defineComponent({
    name: 'ApplicationTimeline',
    components: {
        FontAwesomeIcon,
        TimelineItem
    },
    props: {
        application: { type: Object as () => Application, required: true }
    },
    setup() {
        const formatDate = (d: string) =>
            new Date(d).toLocaleDateString('en-IN', {
                day: 'numeric',
                month: 'long',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            })

        return {
            faTimeline,
            faFile,
            faBuilding,
            faCircleCheck,
            faCircleExclamation,
            formatDate
        }
    }
})
</script>
