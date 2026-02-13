<template>
    <div class="py-20 bg-white">
        <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="text-center max-w-3xl mx-auto mb-16">
                <span class="text-indigo-600 font-semibold text-sm uppercase tracking-wider">Got Questions?</span>
                <h2 class="text-3xl md:text-4xl font-bold text-gray-900 mt-2">
                    Frequently Asked Questions
                </h2>
                <p class="text-lg text-gray-600 mt-4">
                    Everything you need to know about opening and managing your savings account
                </p>
            </div>

            <div class="space-y-4">
                <div v-for="(faq, index) in faqs" :key="index"
                    class="bg-white border border-gray-200 rounded-2xl hover:border-indigo-200 transition-all">
                    <button @click="toggle(index)" class="w-full px-6 py-5 flex items-center justify-between text-left">
                        <span class="font-semibold text-gray-900">{{ faq.question }}</span>
                        <FontAwesomeIcon :icon="openFaqs.includes(index) ? faMinus : faPlus"
                            class="text-indigo-600 transition-transform"
                            :class="openFaqs.includes(index) ? 'rotate-180' : ''" />
                    </button>
                    <transition enter-active-class="transition-all duration-300 ease-out"
                        enter-from-class="opacity-0 max-h-0" enter-to-class="opacity-100 max-h-96"
                        leave-active-class="transition-all duration-200 ease-in" leave-from-class="opacity-100 max-h-96"
                        leave-to-class="opacity-0 max-h-0">
                        <div v-show="openFaqs.includes(index)" class="px-6 pb-5 text-gray-600 leading-relaxed">
                            {{ faq.answer }}
                        </div>
                    </transition>
                </div>
            </div>

            <div class="mt-12 bg-gradient-to-r from-indigo-50 to-purple-50 rounded-3xl p-8 text-center">
                <h3 class="text-xl font-bold text-gray-900 mb-2">Still have questions?</h3>
                <p class="text-gray-600 mb-6">Our support team is ready to help you 24/7</p>
                <button
                    class="px-6 py-3 bg-indigo-600 text-white font-medium rounded-xl hover:bg-indigo-700 transition-colors shadow-lg shadow-indigo-200 cursor-not-allowed">
                    Contact Support
                </button>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, PropType, ref } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faPlus, faMinus } from '@fortawesome/free-solid-svg-icons'
import type { FAQ } from '@/types/app/landing'

export default defineComponent({
    name: 'FAQSection',
    components: { FontAwesomeIcon },
    props: {
        faqs: {
            type: Array as PropType<FAQ[]>,
            required: true
        }
    },
    setup() {
        const openFaqs = ref<number[]>([])

        const toggle = (index: number) => {
            openFaqs.value = openFaqs.value.includes(index)
                ? openFaqs.value.filter(i => i !== index)
                : [...openFaqs.value, index]
        }

        return {
            openFaqs,
            toggle,
            faPlus,
            faMinus
        }
    }
})
</script>

<style scoped>
.max-h-0 {
    max-height: 0;
    overflow: hidden;
}

.max-h-96 {
    max-height: 24rem;
}
</style>
