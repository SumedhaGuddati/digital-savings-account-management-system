<!-- @author Khamitkar Sai Chandan -->
<template>
    <nav class="bg-white/80 backdrop-blur-md border-b border-gray-200/60 sticky top-0 z-50">
        <div class="max-w-7xl mx-auto px-6 py-3">
            <div class="flex justify-between items-center">
                <!-- Logo -->
                <div class="flex items-center gap-3">
                    <div
                        class="w-8 h-8 bg-gradient-to-br from-indigo-600 to-purple-600 rounded-lg flex items-center justify-center shadow-sm">
                        <span class="text-white font-bold text-lg">Z</span>
                    </div>
                    <span class="font-semibold text-gray-900">ZetaRise</span>
                </div>

                <!-- Right -->
                <div class="flex items-center gap-4">
                    <!-- Notifications -->
                    <button
                        class="relative p-2 text-gray-600 hover:text-indigo-600 hover:bg-indigo-50 rounded-full transition-all cursor-pointer">
                        <FontAwesomeIcon :icon="faBell" class="text-lg" />
                        <span class="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full ring-2 ring-white"></span>
                    </button>

                    <!-- Profile -->
                    <ProfileDropdown :user-name="userName" :user-email="userEmail" />
                </div>
            </div>
        </div>
    </nav>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faBell } from '@fortawesome/free-solid-svg-icons'
import ProfileDropdown from '@/components/layout/user/ProfileDropdown.vue'

export default defineComponent({
    name: 'AppNavbar',
    components: { FontAwesomeIcon, ProfileDropdown },
    props: {
        userName: { type: String, required: true },
        userEmail: { type: String, required: true }
    },
    computed: {
        faBell() {
            return faBell
        }
    },
})
</script>
