<template>
    <footer class="bg-gray-900 text-white">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-8 lg:gap-12">
                <div class="lg:col-span-2">
                    <div class="flex items-center gap-2 mb-4">
                        <div
                            class="w-10 h-10 bg-gradient-to-br from-indigo-500 to-purple-500 rounded-xl flex items-center justify-center">
                            <span class="text-white font-bold text-xl">B</span>
                        </div>
                        <span class="text-xl font-bold">ZetaFi</span>
                    </div>
                    <p class="text-gray-400 text-sm leading-relaxed mb-4">
                        Empowering your financial journey with smart savings, premium benefits, and seamless digital
                        banking.
                    </p>
                </div>

                <div>
                    <h3 class="text-sm font-semibold uppercase tracking-wider text-gray-300 mb-4">Products</h3>
                    <ul class="space-y-3">
                        <li v-for="link in productLinks" :key="link.text">
                            <a :href="link.url" class="text-gray-400 hover:text-white text-sm transition-colors">
                                {{ link.text }}
                            </a>
                        </li>
                    </ul>
                </div>

                <div>
                    <h3 class="text-sm font-semibold uppercase tracking-wider text-gray-300 mb-4">Company</h3>
                    <ul class="space-y-3">
                        <li v-for="link in companyLinks" :key="link.text">
                            <a :href="link.url" class="text-gray-400 hover:text-white text-sm transition-colors">
                                {{ link.text }}
                            </a>
                        </li>
                    </ul>
                </div>

                <div>
                    <h3 class="text-sm font-semibold uppercase tracking-wider text-gray-300 mb-4">Support</h3>
                    <ul class="space-y-3">
                        <li v-for="link in supportLinks" :key="link.text">
                            <a :href="link.url" class="text-gray-400 hover:text-white text-sm transition-colors">
                                {{ link.text }}
                            </a>
                        </li>
                    </ul>
                </div>
            </div>

            <div
                class="mt-12 pt-8 border-t border-gray-800 flex flex-col md:flex-row items-center justify-between gap-4">
                <p class="text-sm text-gray-400">
                    © 2026 ZetaFi. All rights reserved. | RBI Licensed Bank
                </p>
                <div class="flex gap-6 text-sm text-gray-400">
                    <a href="#" class="hover:text-white transition-colors">Privacy Policy</a>
                    <a href="#" class="hover:text-white transition-colors">Terms of Service</a>
                    <a href="#" class="hover:text-white transition-colors">Cookie Policy</a>
                </div>
            </div>
        </div>
    </footer>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import type { FooterLink } from '@/types/app/landing'

export default defineComponent({
    name: 'LandingFooter',
    props: {
        productLinks: {
            type: Array as PropType<FooterLink[]>,
            required: true
        },
        companyLinks: {
            type: Array as PropType<FooterLink[]>,
            required: true
        },
        supportLinks: {
            type: Array as PropType<FooterLink[]>,
            required: true
        }
    }
})
</script>
