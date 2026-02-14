<!-- @author Khamitkar Sai Chandan -->
<template>
    <div class="relative bg-gradient-to-br from-indigo-50 via-white to-purple-50 overflow-hidden">
        <!-- Animated Background -->
        <div class="absolute inset-0">
            <div
                class="absolute top-20 left-10 w-72 h-72 bg-indigo-200 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob">
            </div>
            <div
                class="absolute top-40 right-10 w-72 h-72 bg-purple-200 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob animation-delay-2000">
            </div>
            <div
                class="absolute bottom-20 left-1/2 w-72 h-72 bg-pink-200 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob animation-delay-4000">
            </div>
        </div>

        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 lg:py-24 relative">
            <div class="grid lg:grid-cols-2 gap-12 items-center">
                <!-- Left -->
                <div class="text-center lg:text-left">
                    <div
                        class="inline-flex items-center gap-2 bg-gradient-to-r from-red-500 to-pink-500 text-white px-4 py-2 rounded-full text-sm font-medium mb-6 shadow-lg shadow-red-200">
                        <span class="relative flex h-3 w-3">
                            <span
                                class="animate-ping absolute inline-flex h-full w-full rounded-full bg-white opacity-75"></span>
                            <span class="relative inline-flex rounded-full h-3 w-3 bg-white"></span>
                        </span>
                        LIVE · TRENDING NOW
                    </div>

                    <AppLogo class="text-4xl mb-6" />

                    <h1 class="text-4xl sm:text-5xl lg:text-6xl font-bold text-gray-900 leading-tight">
                        <span class="block">Live Big with</span>
                        <span
                            class="block text-transparent bg-clip-text bg-gradient-to-r from-indigo-600 to-purple-600 mt-2">
                            Ultra Savings
                        </span>
                    </h1>

                    <p class="text-xl text-gray-600 mt-6 max-w-2xl mx-auto lg:mx-0">
                        Unlock 6.5% interest rate, and premium lifestyle benefits.
                        Your money works harder here.
                    </p>

                    <div class="flex flex-col sm:flex-row gap-4 mt-8 justify-center lg:justify-start">
                        <button @click="$emit('apply')"
                            class="group relative px-8 py-4 bg-gradient-to-r from-indigo-600 to-purple-600 text-white font-semibold rounded-xl hover:from-indigo-700 hover:to-purple-700 transition-all transform hover:scale-105 hover:shadow-2xl shadow-lg shadow-indigo-200 flex items-center justify-center gap-3 cursor-pointer">
                            <span>Apply for Savings Account</span>
                            <FontAwesomeIcon :icon="faArrowRight"
                                class="group-hover:translate-x-1 transition-transform" />
                            <div
                                class="absolute inset-0 rounded-xl bg-white opacity-0 group-hover:opacity-10 transition-opacity">
                            </div>
                        </button>

                        <button @click="$emit('login')"
                            class="px-8 py-4 bg-white border-2 border-indigo-200 text-indigo-700 font-semibold rounded-xl hover:border-indigo-400 hover:bg-indigo-50 transition-all flex items-center justify-center gap-2 cursor-pointer">
                            <FontAwesomeIcon :icon="faRightToBracket" />
                            Already Applied? Login
                        </button>
                    </div>

                    <div class="mt-8 flex items-center gap-4 justify-center lg:justify-start">
                        <div class="flex -space-x-2">
                            <img v-for="i in 4" :key="i"
                                :src="`https://randomuser.me/api/portraits/${i % 2 ? 'men' : 'women'}/${i}.jpg`"
                                class="w-8 h-8 rounded-full border-2 border-white" alt="User">
                        </div>
                        <div class="text-sm text-gray-600">
                            <span class="font-bold text-gray-900">10,000+</span> applications this week
                        </div>
                    </div>
                </div>

                <!-- Right Card -->
                <div class="relative hidden lg:block">
                    <div
                        class="bg-white rounded-3xl shadow-2xl p-8 transform rotate-3 hover:rotate-0 transition-transform duration-500">
                        <div
                            class="absolute -top-4 -right-4 w-20 h-20 bg-gradient-to-br from-yellow-400 to-yellow-600 rounded-2xl rotate-12 flex items-center justify-center shadow-xl">
                            <FontAwesomeIcon :icon="faCrown" class="text-3xl text-white" />
                        </div>

                        <div class="flex justify-between items-start mb-6">
                            <div>
                                <p class="text-xs text-gray-500 uppercase tracking-wider">Premium Savings</p>
                                <p class="text-2xl font-bold text-gray-900 mt-1">Ultra</p>
                            </div>
                            <div
                                class="w-12 h-12 bg-gradient-to-br from-indigo-100 to-purple-100 rounded-xl flex items-center justify-center">
                                <FontAwesomeIcon :icon="faBuildingColumns" class="text-2xl text-indigo-600" />
                            </div>
                        </div>

                        <div class="space-y-4">
                            <div v-for="text in features" :key="text" class="flex items-center gap-3 text-sm">
                                <div class="w-8 h-8 bg-emerald-100 rounded-full flex items-center justify-center">
                                    <FontAwesomeIcon :icon="faCheck" class="text-emerald-600 text-xs" />
                                </div>
                                <span class="text-gray-700">{{ text }}</span>
                            </div>
                        </div>

                        <div class="mt-6 pt-6 border-t border-gray-100">
                            <p class="text-3xl font-bold text-gray-900">
                                6.5%<span class="text-sm font-normal text-gray-500 ml-2">p.a.</span>
                            </p>
                            <p class="text-xs text-gray-500 mt-1">Highest in the industry</p>
                        </div>
                    </div>

                    <div
                        class="absolute -bottom-4 -right-4 w-full h-full bg-gradient-to-r from-indigo-200 to-purple-200 rounded-3xl -z-10">
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import {
    faArrowRight,
    faRightToBracket,
    faCrown,
    faBuildingColumns,
    faCheck
} from "@fortawesome/free-solid-svg-icons";

import AppLogo from "../AppLogo.vue";

export default defineComponent({
    name: "HeroSection",

    components: {
        FontAwesomeIcon,
        AppLogo
    },

    emits: ["apply", "login"],

    data() {
        return {
            features: [
                "6.5% interest rate",
                "Airport lounge access",
                "Zero ATM fees Indiawide"
            ],
            faArrowRight,
            faRightToBracket,
            faCrown,
            faBuildingColumns,
            faCheck
        };
    },

    methods: {
        apply() {
            this.$emit("apply");
        },
        login() {
            this.$emit("login");
        }
    }
});
</script>


<style scoped>
@keyframes blob {
    0% {
        transform: translate(0px, 0px) scale(1);
    }

    33% {
        transform: translate(30px, -50px) scale(1.1);
    }

    66% {
        transform: translate(-20px, 20px) scale(0.9);
    }

    100% {
        transform: translate(0px, 0px) scale(1);
    }
}

.animate-blob {
    animation: blob 7s infinite;
}

.animation-delay-2000 {
    animation-delay: 2s;
}

.animation-delay-4000 {
    animation-delay: 4s;
}
</style>
