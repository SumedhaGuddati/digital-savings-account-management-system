<template>
    <div class="flex justify-between items-center py-3 border-b border-gray-100">
        <span class="text-sm text-gray-600">{{ label }}</span>
        <span class="text-sm font-medium text-gray-900">
            <slot />
        </span>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
    name: 'DetailRow',
    props: {
        label: { type: String, required: true }
    }
})
</script>
