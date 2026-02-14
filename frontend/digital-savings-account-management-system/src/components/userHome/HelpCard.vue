<!-- @author Khamitkar Sai Chandan -->
<template>
    <div class="rounded-xl bg-gradient-to-br from-gray-900 to-gray-800 p-6 shadow-lg">
        <div class="flex items-center gap-3 mb-3">
            <div class="w-8 h-8 bg-white/10 rounded-lg flex items-center justify-center">
                <FontAwesomeIcon :icon="faHeadset" class="text-white text-lg" />
            </div>
            <h3 class="font-semibold text-white">Need Help?</h3>
        </div>
        <p class="text-sm text-gray-300 mb-4">
            Our support team is available 24/7 to assist you with any queries.
        </p>
        <button
            class="w-full px-4 py-2 bg-white/10 hover:bg-white/20 text-white text-sm font-medium rounded-lg transition-colors border border-white/20 cursor-not-allowed">
            Contact Support
        </button>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faHeadset } from '@fortawesome/free-solid-svg-icons'

export default defineComponent({
    name: 'HelpCard',
    components: { FontAwesomeIcon },
    computed: {
        faHeadset() { return faHeadset }
    }
})
</script>
