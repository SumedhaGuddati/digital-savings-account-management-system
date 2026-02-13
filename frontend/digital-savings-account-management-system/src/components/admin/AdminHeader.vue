<template>
    <header class="h-20 flex items-center justify-between px-8 bg-white border-b border-slate-200 sticky top-0 z-10">
        <h1 class="text-xl font-bold text-indigo-700">{{ pageTitle }}</h1>

        <AdminUserMenu :user="user" @logout="handleLogout" />
    </header>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AdminUserMenu from './AdminUserMenu.vue'

export default defineComponent({
    name: 'AdminHeader',
    components: { AdminUserMenu },

    data() {
        return {
            user: null as any
        }
    },

    computed: {
        pageTitle(): string {
            const path = this.$route.path
            if (path === '/') return 'Dashboard'
            if (path === '/approvals') return 'Pending Applications'
            if (path === '/audit-logs') return 'Audit Logs'
            if (path.includes('/profile')) return 'Profile'
            if (path.includes('/settings')) return 'Settings'
            return 'Dashboard'
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
                    email: 'admin@bank.com',
                    avatar: 'https://i.pravatar.cc/100?img=12'
                }
            }
        }
    },

    mounted() {
        this.loadUser()
    }
})
</script>
