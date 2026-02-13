<template>
    <div class="rounded-xl border border-gray-200 bg-white p-6 shadow-sm hover:shadow-md transition-shadow">
        <div class="flex items-start justify-between mb-4">
            <div>
                <p class="text-xs uppercase tracking-wider text-gray-500 font-medium">
                    Account Benefits
                </p>
                <h3 class="font-semibold text-gray-900 mt-1">
                    Elite Savings Plan
                </h3>
            </div>
            <div
                class="w-10 h-10 bg-gradient-to-br from-indigo-100 to-purple-100 rounded-lg flex items-center justify-center">
                <FontAwesomeIcon :icon="faCrown" class="text-indigo-600 text-lg" />
            </div>
        </div>

        <div class="space-y-3">
            <div v-for="benefit in benefits" :key="benefit" class="flex items-center gap-3 text-sm">
                <div class="w-5 h-5 bg-emerald-100 rounded-full flex items-center justify-center flex-shrink-0">
                    <FontAwesomeIcon :icon="faCheck" class="text-emerald-600 text-xs" />
                </div>
                <span class="text-gray-600">{{ benefit }}</span>
            </div>
        </div>

        <button
            class="w-full mt-5 px-4 py-2.5 bg-gradient-to-r from-indigo-600 to-purple-600 text-white text-sm font-medium rounded-lg hover:from-indigo-700 hover:to-purple-700 transition-all shadow-sm flex items-center justify-center gap-2 cursor-not-allowed">
            <FontAwesomeIcon :icon="faArrowRight" />
            Explore Benefits
        </button>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faCrown, faCheck, faArrowRight } from '@fortawesome/free-solid-svg-icons'

export default defineComponent({
    name: 'AccountBenefitsCard',
    components: { FontAwesomeIcon },
    props: {
        benefits: {
            type: Array as () => string[],
            required: true
        }
    },
    computed: {
        faCrown() { return faCrown },
        faCheck() { return faCheck },
        faArrowRight() { return faArrowRight }
    }
})
</script>
