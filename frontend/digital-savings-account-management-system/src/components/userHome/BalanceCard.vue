<template>
    <div
        class=" bg-white rounded-xl border border-gray-200 shadow-sm p-5 flex flex-col justify-between">
        <!-- Header -->
        <p class="text-xs uppercase tracking-wide text-gray-500">
            Total Balance
        </p>

        <!-- Balance -->
        <div class="mt-6 flex justify-between items-center">
            <p class="text-3xl font-semibold text-gray-900 tracking-tight tabular-nums">
                <span v-if="visible">₹{{ formattedBalance }}</span>
                <span v-else class="tracking-widest text-gray-400">₹ ••••••</span>
            </p>

            <button @click="toggle"
                class="text-xs font-medium text-indigo-600 hover:text-indigo-700 transition cursor-pointer flex items-center gap-1 cursor-pointer"
                aria-label="Toggle balance visibility">
                <span>{{ visible ? 'Hide' : 'View Balance' }}</span>
                <FontAwesomeIcon :icon="visible ? faEyeSlash : faEye" class="text-base" />
            </button>
        </div>

        <!-- Footer -->
        <div class="pt-4 border-t border-gray-100 flex justify-between items-center mt-6">
            <p class="text-xs text-gray-500">
                Account Status
            </p>
            <span class="text-xs font-medium text-emerald-600 bg-emerald-50 px-2 py-0.5 rounded-full">
                Active
            </span>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';

export default defineComponent({
    name: 'BalanceCard',
    components: { FontAwesomeIcon },
    props: {
        balance: { type: Number, required: true }
    },
    data() {
        return {
            visible: false as boolean
        }
    },
    computed: {
        formattedBalance(): string {
            return this.balance.toLocaleString('en-IN')
        },
        faEye() {
            return faEye;
        },
        faEyeSlash() {
            return faEyeSlash;
        }
    },
    methods: {
        toggle() {
            this.visible = !this.visible
        }
    }
})
</script>
