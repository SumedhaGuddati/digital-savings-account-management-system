<template>
    <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
        <button @click="$emit('deposit')"
            class="group p-5 bg-white rounded-xl border border-gray-200 hover:border-indigo-200 hover:shadow-md transition-all cursor-pointer">
            <div
                class="w-10 h-10 bg-emerald-100 rounded-lg flex items-center justify-center mb-3 group-hover:bg-emerald-200 transition-colors">
                <FontAwesomeIcon :icon="faCirclePlus" class="text-xl text-emerald-600" />
            </div>
            <h3 class="font-medium text-gray-900">Deposit</h3>
            <p class="text-xs text-gray-500 mt-1">Add money to your account</p>
        </button>

        <button @click="$emit('withdraw')"
            class="group p-5 bg-white rounded-xl border border-gray-200 hover:border-indigo-200 hover:shadow-md transition-all cursor-pointer">
            <div
                class="w-10 h-10 bg-red-100 rounded-lg flex items-center justify-center mb-3 group-hover:bg-red-200 transition-colors">
                <FontAwesomeIcon :icon="faWallet" class="text-xl text-red-500" />
            </div>
            <h3 class="font-medium text-gray-900">Withdraw</h3>
            <p class="text-xs text-gray-500 mt-1">Withdraw money securely</p>
        </button>

        <button @click="$emit('statements')"
            class="group p-5 bg-white rounded-xl border border-gray-200 hover:border-indigo-200 hover:shadow-md transition-all cursor-pointer">
            <div
                class="w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center mb-3 group-hover:bg-purple-200 transition-colors">
                <FontAwesomeIcon :icon="faFileInvoice" class="text-xl text-purple-600" />
            </div>
            <h3 class="font-medium text-gray-900">Statements</h3>
            <p class="text-xs text-gray-500 mt-1">View account statements</p>
        </button>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faCirclePlus, faWallet, faFileInvoice } from '@fortawesome/free-solid-svg-icons'

export default defineComponent({
    name: 'QuickActions',
    components: { FontAwesomeIcon },
    emits: ['deposit', 'withdraw', 'statements'],
    computed: {
        faCirclePlus() { return faCirclePlus },
        faWallet() { return faWallet },
        faFileInvoice() { return faFileInvoice }
    }
})
</script>
