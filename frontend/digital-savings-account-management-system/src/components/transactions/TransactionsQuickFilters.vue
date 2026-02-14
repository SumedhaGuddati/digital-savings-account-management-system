<!-- @author Khamitkar Sai Chandan -->
<template>
    <div class="mt-6 flex items-center justify-between">
        <div class="flex items-center gap-3">
            <span class="text-xs text-gray-500">Quick filters:</span>
            <button v-for="filter in quickFilters" :key="filter.label" @click="$emit('apply', filter.value)"
                class="px-3 py-1.5 text-xs font-medium border border-gray-200 rounded-full hover:border-indigo-300 hover:bg-indigo-50 hover:text-indigo-700 transition-all cursor-pointer"
                :class="filter.active ? 'bg-indigo-50 border-indigo-300 text-indigo-700' : 'bg-white text-gray-600'">
                {{ filter.label }}
            </button>
        </div>

        <div class="flex items-center gap-2">
            <span class="text-xs text-gray-500">Sort by:</span>
            <select :value="sortOrder" @change="$emit('update:sortOrder', ($event.target as HTMLSelectElement).value)"
                class="text-xs border border-gray-200 rounded-lg px-3 py-1.5 bg-white focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 outline-none">
                <option value="newest">Newest first</option>
                <option value="oldest">Oldest first</option>
                <option value="highest">Highest amount</option>
                <option value="lowest">Lowest amount</option>
            </select>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'

export interface QuickFilter {
    label: string
    value: string
    active: boolean
}

export default defineComponent({
    name: 'TransactionsQuickFilters',
    props: {
        quickFilters: { type: Array as PropType<QuickFilter[]>, required: true },
        sortOrder: { type: String, required: true }
    },
    emits: ['apply', 'update:sortOrder']
})
</script>
