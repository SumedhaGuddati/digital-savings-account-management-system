<template>
    <div class="bg-gradient-to-br from-gray-900 to-gray-800 rounded-2xl p-6 shadow-lg">
        <div class="flex items-center justify-between">
            <div class="flex items-center gap-3">
                <div class="w-10 h-10 bg-white/10 rounded-lg flex items-center justify-center">
                    <FontAwesomeIcon :icon="faHeadset" class="text-white text-lg" />
                </div>
                <div>
                    <h4 class="font-semibold text-white">Need help with your application?</h4>
                    <p class="text-sm text-gray-300 mt-0.5">Our support team is here to assist you</p>
                </div>
            </div>
            <button
                class="px-4 py-2 bg-white/10 hover:bg-white/20 text-white text-sm font-medium rounded-lg transition-colors border border-white/20 cursor-not-allowed">
                Contact Support
            </button>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faHeadset } from "@fortawesome/free-solid-svg-icons";

export default defineComponent({
    name: "HelpCard",

    components: {
        FontAwesomeIcon
    },

    data() {
        return {
            faHeadset
        };
    }
});
</script>
