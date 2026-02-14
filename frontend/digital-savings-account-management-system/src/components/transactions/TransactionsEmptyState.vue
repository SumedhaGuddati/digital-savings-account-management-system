<template>
    <div class="py-16 text-center">
        <div class="w-20 h-20 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <FontAwesomeIcon :icon="faClock" class="text-3xl text-gray-400" />
        </div>
        <h3 class="text-lg font-medium text-gray-900 mb-2">No transactions found</h3>
        <p class="text-sm text-gray-500 max-w-md mx-auto">
            {{ message }}
        </p>
        <button v-if="hasActiveFilters" @click="$emit('clear')"
            class="mt-4 px-4 py-2 bg-indigo-600 text-white text-sm font-medium rounded-lg hover:bg-indigo-700 transition-colors cursor-pointer">
            Clear Filters
        </button>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faClock } from "@fortawesome/free-solid-svg-icons";

export default defineComponent({
    name: "TransactionsEmptyState",

    components: {
        FontAwesomeIcon
    },

    props: {
        hasActiveFilters: {
            type: Boolean,
            required: true
        },
        message: {
            type: String,
            required: true
        }
    },

    emits: ["clear"],

    data() {
        return {
            faClock
        };
    },

    methods: {
        clear() {
            this.$emit("clear");
        }
    }
});
</script>
