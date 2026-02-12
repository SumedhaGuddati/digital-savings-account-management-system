<template>
    <div class="bg-gradient-to-r from-emerald-50 to-teal-50 rounded-2xl border border-emerald-200 p-6">
        <div class="flex items-start gap-4">
            <div class="w-12 h-12 bg-emerald-100 rounded-full flex items-center justify-center flex-shrink-0">
                <FontAwesomeIcon :icon="faCircleCheck" class="text-2xl text-emerald-600" />
            </div>
            <div class="flex-1">
                <h3 class="font-semibold text-emerald-800 mb-1">Congratulations! Your application is approved</h3>
                <p class="text-sm text-emerald-700 mb-4">
                    Your savings account has been created successfully. You can now start banking.
                </p>
                <button @click="$router.push('/me')"
                    class="px-4 py-2 bg-emerald-600 text-white text-sm font-medium rounded-lg hover:bg-emerald-700 transition-colors">
                    Go to Dashboard
                </button>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faCircleCheck } from '@fortawesome/free-solid-svg-icons'

export default defineComponent({
    name: 'ApprovedCard',
    components: { FontAwesomeIcon },
    setup() {
        return { faCircleCheck }
    }
})
</script>
