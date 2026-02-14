<template>
    <div class="py-20 bg-gradient-to-br from-gray-50 via-white to-gray-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="text-center max-w-3xl mx-auto mb-16">
                <span class="text-indigo-600 font-semibold text-sm uppercase tracking-wider">Compare Plans</span>
                <h2 class="text-3xl md:text-4xl font-bold text-gray-900 mt-2">
                    Choose Your Perfect Plan
                </h2>
                <p class="text-lg text-gray-600 mt-4">
                    Every plan comes with core benefits. Premium plans get extra privileges.
                </p>
            </div>

            <div class="grid lg:grid-cols-3 gap-8">
                <div v-for="plan in plans" :key="plan.code"
                    class="bg-white rounded-2xl border overflow-hidden transition-all duration-300 hover:shadow-2xl relative"
                    :class="plan.recommended
                        ? 'border-indigo-300 shadow-xl lg:scale-105 ring-2 ring-indigo-500 ring-opacity-20'
                        : 'border-gray-200'">

                    <div class="p-6 text-center border-b" :class="plan.headerClass">
                        <div class="w-16 h-16 mx-auto rounded-2xl flex items-center justify-center mb-4"
                            :class="plan.iconBgClass">
                            <FontAwesomeIcon :icon="plan.icon" :class="plan.iconClass" class="text-2xl" />
                        </div>
                        <h3 class="text-2xl font-bold text-gray-900">{{ formatPlanName(plan.name) }}</h3>
                        <p class="text-sm text-gray-600 mt-2">{{ plan.description }}</p>
                        <p class="mt-4">
                            <span class="text-3xl font-bold text-gray-900">
                                ₹{{ plan.minBalance.toLocaleString('en-IN') }}
                            </span>
                            <span class="text-sm text-gray-500 ml-1">min. balance</span>
                        </p>
                        <p class="text-2xl font-bold text-indigo-600 mt-2">
                            {{ plan.interest }}%
                            <span class="text-sm font-normal text-gray-500">p.a.</span>
                        </p>
                    </div>

                    <div class="p-6 space-y-4">
                        <div v-for="feature in plan.features" :key="feature.name"
                            class="flex items-center justify-between text-sm">
                            <span class="text-gray-700">{{ feature.name }}</span>
                            <span v-if="feature.included" class="text-emerald-600">
                                <FontAwesomeIcon :icon="faCheckCircle" class="text-lg" />
                            </span>
                            <span v-else class="text-gray-400">
                                <FontAwesomeIcon :icon="faXmarkCircle" class="text-lg" />
                            </span>
                        </div>
                    </div>

                    <div class="p-6 pt-0">
                        <button @click="$router.push({ path: '/apply', query: { type: plan.code } })"
                            class="w-full py-3 rounded-xl font-semibold transition-all" :class="plan.recommended
                                ? 'bg-gradient-to-r from-indigo-600 to-purple-600 text-white hover:from-indigo-700 hover:to-purple-700 shadow-lg shadow-indigo-200'
                                : 'border-2 border-indigo-200 text-indigo-700 hover:border-indigo-400 hover:bg-indigo-50'">
                            {{ plan.recommended ? 'Get Started' : 'Choose Plan' }}
                        </button>
                    </div>
                </div>
            </div>

            <div class="text-center mt-12">
                <button
                    class="inline-flex items-center gap-2 text-indigo-600 hover:text-indigo-700 font-medium group cursor-not-allowed">
                    <span>Detailed plan comparison</span>
                    <FontAwesomeIcon :icon="faArrowRight" class="group-hover:translate-x-1 transition-transform" />
                </button>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import {
    faCrown,
    faCheckCircle,
    faXmarkCircle,
    faArrowRight
} from '@fortawesome/free-solid-svg-icons'
import type { AccountPlan } from '@/types/app/landing'

export default defineComponent({
    name: 'PlansSection',
    components: { FontAwesomeIcon },
    props: {
        plans: {
            type: Array as PropType<AccountPlan[]>,
            required: true
        }
    },
    data() {
        return {
            faCheckCircle,
            faXmarkCircle,
            faArrowRight,
            faCrown
        }
    },
    methods: {
        formatPlanName(name: string): string {
            if (!name) return '';
            return name.replace(/_/g, ' ');
        },
        
        handlePlanSelection(planCode: string) {
            this.$router.push({ 
                path: '/apply', 
                query: { type: planCode } 
            });
        }
    }
})
</script>