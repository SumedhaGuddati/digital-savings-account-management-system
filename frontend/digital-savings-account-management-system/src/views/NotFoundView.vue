<template>
    <div class="min-h-screen flex flex-col items-center justify-center bg-gradient-to-br from-gray-50 to-gray-100 px-6">

        <div class="w-full max-w-xl flex justify-center">
            <LottieAnimation :animationData="notFoundAnimation" :height="500" :width="500" :loop="true"
                :autoPlay="true" />
        </div>

        <!-- Message -->
        <h1 class="text-3xl font-semibold text-gray-700 mt-6">
            Page Not Found
        </h1>

        <p class="text-gray-600 mt-2 text-center max-w-sm">
            The page you're looking for doesn't exist or has been moved.
        </p>

        <!-- Button -->
        <button @click="goHome" class="mt-6 px-6 py-3 bg-gradient-to-r from-indigo-600 to-purple-600 
             text-white font-medium rounded-xl 
             hover:from-indigo-700 hover:to-purple-700 
             transition-all shadow-md hover:shadow-lg cursor-pointer">
            Go Back Home
        </button>

    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import notFoundAnimation from '@/assets/animation/404.json'

export default defineComponent({
    name: 'NotFoundView',
    data() {
        return {
            notFoundAnimation
        }
    },
    methods: {
        goHome() {
            this.$router.push('/')
        }
    }
})
</script>
