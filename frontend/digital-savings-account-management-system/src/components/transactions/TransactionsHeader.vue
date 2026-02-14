<!-- @author Khamitkar Sai Chandan -->
<template>
    <div class="relative mb-8">
        <!-- Decorative Background -->
        <div class="absolute inset-0 overflow-hidden">
            <div class="absolute -top-20 -right-20 w-64 h-64 bg-indigo-100 rounded-full blur-3xl opacity-20"></div>
            <div class="absolute -bottom-20 -left-20 w-64 h-64 bg-purple-100 rounded-full blur-3xl opacity-20"></div>
        </div>

        <div class="relative">
            <!-- Back -->
            <div class="flex items-center gap-4 mb-4">
                <button @click="$router.back()"
                    class="group flex items-center gap-2 px-3 py-2 -ml-3 text-gray-600 hover:text-indigo-600 transition-all rounded-lg hover:bg-white/50 backdrop-blur-sm cursor-pointer">
                    <FontAwesomeIcon :icon="faArrowLeft"
                        class="text-sm group-hover:-translate-x-0.5 transition-transform" />
                    <span class="text-sm font-medium">Back</span>
                </button>
            </div>

            <!-- Title -->
            <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                <div class="flex items-center gap-3">
                    <div
                        class="w-12 h-12 bg-gradient-to-br from-indigo-600 to-purple-600 rounded-2xl shadow-lg shadow-indigo-200 flex items-center justify-center">
                        <FontAwesomeIcon :icon="faArrowRightArrowLeft" class="text-2xl text-white" />
                    </div>
                    <div>
                        <h1 class="text-3xl font-bold text-gray-900 tracking-tight">All Transactions</h1>
                        <p class="text-gray-500 mt-1.5 flex items-center gap-2">
                            <FontAwesomeIcon :icon="faCircleInfo" class="text-xs" />
                            {{ count }} transactions • Last 30 days
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import {
    faArrowLeft,
    faArrowRightArrowLeft,
    faCircleInfo,
    faDownload
} from "@fortawesome/free-solid-svg-icons";

export default defineComponent({
    name: "TransactionsHeader",

    components: {
        FontAwesomeIcon
    },

    props: {
        count: {
            type: Number,
            required: true
        }
    },

    data() {
        return {
            faArrowLeft,
            faArrowRightArrowLeft,
            faCircleInfo,
            faDownload
        };
    },
});
</script>
