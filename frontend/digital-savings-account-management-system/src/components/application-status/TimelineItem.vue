<template>
  <div class="flex gap-3">
    <div class="relative">
      <div :class="['w-8 h-8 rounded-full flex items-center justify-center', iconBg]">
        <FontAwesomeIcon :icon="icon" :class="[iconColor, 'text-sm']" />
      </div>
      <div v-if="showLine" class="absolute top-8 left-4 w-0.5 h-12 bg-gray-200"></div>
    </div>

    <div class="flex-1 pb-6">
      <p class="font-medium text-gray-900">{{ title }}</p>
      <p class="text-sm text-gray-600">{{ date }}</p>
      <p class="text-xs text-gray-500 mt-1">{{ description }}</p>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { IconDefinition } from '@fortawesome/fontawesome-svg-core'

export default defineComponent({
  name: 'TimelineItem',
  components: { FontAwesomeIcon },
  props: {
    icon: { type: Object as PropType<IconDefinition>, required: true },
    iconBg: { type: String, required: true },
    iconColor: { type: String, required: true },
    title: { type: String, required: true },
    date: { type: String, required: true },
    description: { type: String, required: true },
    showLine: { type: Boolean, required: true }
  }
})
</script>
