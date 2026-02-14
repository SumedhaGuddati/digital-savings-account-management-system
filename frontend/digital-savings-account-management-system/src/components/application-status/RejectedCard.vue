<template>
    <div class="bg-gradient-to-r from-red-50 to-rose-50 rounded-2xl border border-red-200 p-6">
        <div class="flex items-start gap-4">
            <div class="w-12 h-12 bg-red-100 rounded-full flex items-center justify-center flex-shrink-0">
                <FontAwesomeIcon :icon="faCircleExclamation" class="text-2xl text-red-500" />
            </div>
            <div class="flex-1">
                <h3 class="font-semibold text-red-800 mb-1">Application Not Approved</h3>
                <p class="text-sm text-red-700 mb-2">
                    Your application couldn't be approved at this time. Please contact support for more details.
                </p>
                <p class="text-xs text-red-600 mb-4">
                    Reason: Documentation verification failed.
                </p>
                <div class="flex gap-3">
                    <button
                        class="px-4 py-2 bg-red-600 text-white text-sm font-medium rounded-lg hover:bg-red-700 transition-colors cursor-not-allowed">
                        Contact Support
                    </button>
                    <button @click="$router.push('/accounts/apply')"
                        class="px-4 py-2 bg-white text-red-600 text-sm font-medium rounded-lg border border-red-300 hover:bg-red-50 transition-colors cursor-pointer">
                        Reapply
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faCircleExclamation } from "@fortawesome/free-solid-svg-icons";

export default defineComponent({
    name: "RejectedCard",

    components: {
        FontAwesomeIcon
    },

    data() {
        return {
            faCircleExclamation
        };
    }
});
</script>
