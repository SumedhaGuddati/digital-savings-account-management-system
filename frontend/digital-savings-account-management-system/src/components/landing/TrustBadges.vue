<template>
    <div class="bg-white border-y border-gray-100 py-6">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex flex-wrap items-center justify-center gap-8 md:gap-12">
                <div v-for="badge in badges" :key="badge.text" class="flex items-center gap-2 text-gray-600">
                    <FontAwesomeIcon :icon="badge.icon" class="text-indigo-600" />
                    <span class="text-sm font-medium">{{ badge.text }}</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import type { TrustBadge } from '@/types/app/landing'

export default defineComponent({
    name: 'TrustBadges',
    components: { FontAwesomeIcon },
    props: {
        badges: {
            type: Array as PropType<TrustBadge[]>,
            required: true
        }
    }
})
</script>
