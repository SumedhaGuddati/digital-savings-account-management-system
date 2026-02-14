<!-- @author Bharadi Harshitha -->
<template>
    <header class="h-16 sm:h-20 flex items-center justify-between px-3 sm:px-4 md:px-6 bg-white border-b border-slate-200 sticky top-0 z-50">
        <div class="flex items-center gap-1 sm:gap-2 md:gap-4 -ml-12 sm:-ml-16 md:-ml-25">
            <AppLogo />
            <span class="text-lg sm:text-xl md:text-2xl font-light text-slate-300">|</span>
            <h1 class="text-sm sm:text-base md:text-lg lg:text-xl font-bold text-indigo-700 truncate max-w-[120px] xs:max-w-[180px] sm:max-w-[250px] md:max-w-[300px] lg:max-w-full">Dashboard</h1>
        </div>

        <AdminUserMenu :user="user" @logout="handleLogout" />
    </header>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AdminUserMenu from './AdminUserMenu.vue'
import AppLogo from "@/components/AppLogo.vue";

export default defineComponent({
    name: 'AdminHeader',
    components: { 
        AdminUserMenu,
        AppLogo 
    },

    data() {
        return {
            user: null as any
        }
    },

    methods: {
        handleLogout() {
            localStorage.clear()
            sessionStorage.clear()
            this.$router.push('/login')
        },

        loadUser() {
            const userData = localStorage.getItem('user')
            if (userData) {
                this.user = JSON.parse(userData)
            } else {
                this.user = {
                    name: 'Admin User',
                    email: 'superadmin@bank.com',
                    avatar: 'https://avatars.githubusercontent.com/u/141998868?v=4'
                }
            }
        }
    },

    mounted() {
        this.loadUser()
    }
})
</script>