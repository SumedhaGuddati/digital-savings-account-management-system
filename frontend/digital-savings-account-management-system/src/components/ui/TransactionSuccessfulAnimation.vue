<template>
    <div class="absolute inset-0 flex items-center justify-center max-h-screen backdrop-blur-md z-50">
        <LottieAnimation :animationData="transactionSuccessfull" :height="200" :width="200" :loop="false"
            :autoPlay="true" @onComplete="handleComplete" :speed="0.5" />
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import transactionSuccessfull from '@/assets/animation/transaction_successful.json'

export default defineComponent({
    name: 'TransactionSuccessfulAnimation',
    data() {
        return {
            transactionSuccessfull
        }
    },
    methods: {
        handleComplete() {
            this.$emit('animationComplete')
        }
    }
})
</script>
