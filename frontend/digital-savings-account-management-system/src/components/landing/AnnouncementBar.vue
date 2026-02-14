<template>
    <div class="bg-gradient-to-r from-indigo-900 via-purple-900 to-indigo-900 text-white py-3 relative overflow-hidden">
        <div class="absolute inset-0 opacity-10">
            <div class="absolute -top-6 -right-6 w-24 h-24 bg-white rounded-full blur-2xl"></div>
            <div class="absolute -bottom-6 -left-6 w-24 h-24 bg-white rounded-full blur-2xl"></div>
        </div>
        <div @click="$router.push('/apply')" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative cursor-pointer">
            <p
                class="text-sm sm:text-base font-medium text-center flex-col flex sm:flex-row items-center justify-center gap-2">
                <FontAwesomeIcon :icon="faBolt" class="text-yellow-300 animate-pulse" />
                <span class="bg-white/20 px-3 py-0.5 rounded-full text-xs sm:text-sm">NEW</span>
                🚀 50,000+ customers have already saved ₹2.5Cr+ this month. Don't let your money sleep!
                <span class="hidden sm:inline bg-white/10 px-3 py-1 rounded-full text-yellow-200 ">
                    Start earning 6.5% today →
                </span>
            </p>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faBolt } from "@fortawesome/free-solid-svg-icons";

export default defineComponent({
    name: "AnnouncementBar",

    components: {
        FontAwesomeIcon
    },

    data() {
        return {
            faBolt
        };
    }
});
</script>
