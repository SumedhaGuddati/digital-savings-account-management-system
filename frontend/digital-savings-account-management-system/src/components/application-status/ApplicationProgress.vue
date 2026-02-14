<template>
    <div class="px-8 py-6 border-b border-gray-100 bg-gray-50/50">
        <p class="text-sm font-medium text-gray-700 mb-4">Application Progress</p>

        <div class="flex flex-col sm:flex-row items-start sm:items-center gap-6 sm:gap-0">
            <div class="flex items-center">
                <div class="w-10 h-10 bg-indigo-600 rounded-full flex items-center justify-center">
                    <FontAwesomeIcon :icon="faFile" class="text-white text-sm" />
                </div>
                <div class="ml-4">
                    <p class="font-medium text-gray-900">Submitted</p>
                    <p class="text-xs text-gray-500">{{ formatDate(application.applicationDate) }}</p>
                </div>
            </div>

            <div class="flex-1 mx-4 h-0.5 bg-gray-200 relative">
                <div class="absolute inset-0 w-2/3 bg-indigo-600 h-0.5"></div>
            </div>

            <div class="flex items-center">
                <div class="w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center">
                    <FontAwesomeIcon :icon="faBuilding" class="text-gray-400 text-sm" />
                </div>
                <div class="ml-4">
                    <p class="font-medium text-gray-400">Under Review</p>
                    <p class="text-xs text-gray-400">Expected: 2–3 business days</p>
                </div>
            </div>

            <div class="flex-1 mx-4 h-0.5 bg-gray-200"></div>

            <div class="flex items-center">
                <div class="w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center">
                    <FontAwesomeIcon :icon="faCircleCheck" class="text-gray-400 text-sm" />
                </div>
                <div class="ml-4">
                    <p class="font-medium text-gray-400">Decision</p>
                    <p class="text-xs text-gray-400">Pending</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faFile, faBuilding, faCircleCheck } from '@fortawesome/free-solid-svg-icons'
import type { Application } from '@/types/app/application'

export default {
    name: 'ApplicationProgress',

    components: {
        FontAwesomeIcon
    },

    props: {
        application: {
            type: Object as () => Application,
            required: true
        }
    },

    data() {
        return {
            faFile,
            faBuilding,
            faCircleCheck
        }
    },

    methods: {
        formatDate(date: string): string {
            return new Date(date).toLocaleDateString('en-IN', {
                day: 'numeric',
                month: 'short',
                year: 'numeric'
            })
        }
    }
}
</script>
