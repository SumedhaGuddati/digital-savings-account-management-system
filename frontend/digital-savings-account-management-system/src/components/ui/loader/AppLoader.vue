<template>
    <div class="flex items-center justify-center w-full min-h-screen bg-gradient-to-br from-gray-50 to-gray-100">
        <LottieAnimation :animationData="loader" :height="200" :width="200" :autoPlay="true" />
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import loader from '@/assets/animation/loader.json'

export default defineComponent({
    name: 'AppLoader',
    data() {
        return { loader }
    }
})
</script>
