<!-- @author Khamitkar Sai Chandan -->
<template>
    <div class="py-20 bg-white">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="text-center max-w-3xl mx-auto mb-16">
                <span class="text-indigo-600 font-semibold text-sm uppercase tracking-wider">Why Choose Us</span>
                <h2 class="text-3xl md:text-4xl font-bold text-gray-900 mt-2">
                    More Than Just a Savings Account
                </h2>
                <p class="text-lg text-gray-600 mt-4">
                    Experience banking that rewards you every step of the way
                </p>
            </div>

            <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
                <div v-for="benefit in benefits" :key="benefit.title"
                    class="group bg-white rounded-2xl border border-gray-200 p-6 hover:shadow-2xl hover:border-transparent transition-all duration-300 hover:-translate-y-2">
                    <div class="w-14 h-14 rounded-2xl flex items-center justify-center mb-4" :class="benefit.bgColor">
                        <FontAwesomeIcon :icon="benefit.icon" :class="benefit.iconColor" class="text-2xl" />
                    </div>
                    <h3 class="text-xl font-bold text-gray-900 mb-2">{{ benefit.title }}</h3>
                    <p class="text-gray-600 text-sm leading-relaxed">{{ benefit.description }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faArrowRight } from "@fortawesome/free-solid-svg-icons";
import type { Benefit } from "@/types/app/landing";

export default defineComponent({
    name: "BenefitsSection",

    components: {
        FontAwesomeIcon
    },

    props: {
        benefits: {
            type: Array as PropType<Benefit[]>,
            required: true
        }
    },

    data() {
        return {
            faArrowRight
        };
    }
});
</script>
