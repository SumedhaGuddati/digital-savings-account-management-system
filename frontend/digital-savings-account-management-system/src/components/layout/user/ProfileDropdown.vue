<!-- @author Khamitkar Sai Chandan -->
<template>
    <div class="relative" ref="wrapper">
        <button ref="profileButton" @click="toggle"
            class="flex items-center gap-3 p-1.5 pr-3 rounded-full hover:bg-gray-100 transition-all border border-transparent hover:border-gray-200 cursor-pointer">
            <div
                class="w-8 h-8 bg-gradient-to-r from-indigo-500 to-purple-500 rounded-full flex items-center justify-center text-white font-medium shadow-sm">
                {{ initials }}
            </div>
            <span class="text-sm font-medium text-gray-700 hidden sm:block">{{ userName }}</span>
            <FontAwesomeIcon :icon="faChevronDown" class="text-xs text-gray-500 transition-transform"
                :class="{ 'rotate-180': open }" />
        </button>

        <transition enter-active-class="transition duration-200 ease-out" enter-from-class="opacity-0 -translate-y-2"
            enter-to-class="opacity-100 translate-y-0" leave-active-class="transition duration-150 ease-in"
            leave-from-class="opacity-100 translate-y-0" leave-to-class="opacity-0 -translate-y-2">
            <div v-show="open"
                class="absolute right-0 mt-2 w-64 bg-white rounded-xl shadow-lg border border-gray-100 py-2 z-50">
                <!-- User Info -->
                <div class="px-4 py-3 border-b border-gray-100">
                    <p class="text-sm font-medium text-gray-900">{{ userName }}</p>
                    <p class="text-xs text-gray-500 mt-0.5">{{ this.$store.getters['auth/getEmail'] }}</p>
                </div>

                <!-- Menu -->
                <div class="py-1">
                    <button v-for="item in menuItems" :key="item.label" @click="$router.push(item.route); close()"
                        class="w-full px-4 py-2.5 text-sm text-gray-700 hover:bg-indigo-50 hover:text-indigo-700 flex items-center gap-3 transition-colors cursor-pointer">
                        <FontAwesomeIcon :icon="item.icon" class="w-4 text-gray-400" />
                        {{ item.label }}
                    </button>
                </div>


                <div class="border-t border-gray-100 py-1">
                    <button @click="logout()"
                        class="w-full px-4 py-2.5 text-sm text-red-600 hover:bg-red-50 flex items-center gap-3 transition-colors cursor-pointer">
                        <FontAwesomeIcon :icon="faSignOutAlt" class="w-4" />
                        Sign Out
                    </button>
                </div>
            </div>
        </transition>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import {
    faChevronDown,
    faUser,
    faFileLines,
    faSignOutAlt,
    faBarsStaggered
} from '@fortawesome/free-solid-svg-icons'

export default defineComponent({
    name: 'ProfileDropdown',
    components: { FontAwesomeIcon },
    props: {
        userName: { type: String, required: true },
        userEmail: { type: String, required: true }
    },
    data() {
        return {
            open: false as boolean
        }
    },
    computed: {
        initials(): string {
            return this.userName
                .split(' ')
                .map(n => n[0])
                .join('')
                .toUpperCase()
                .slice(0, 2)
        },
        faChevronDown() { return faChevronDown },
        faUser() { return faUser },
        faFileLines() { return faFileLines },
        faSignOutAlt() { return faSignOutAlt },
        faBarsStaggered() { return faBarsStaggered },
        menuItems() {
            return [
                {
                    label: 'View Profile',
                    icon: faUser,
                    route: '/me/profile'
                },
                {
                    label: 'Statements',
                    icon: faBarsStaggered,
                    route: '/me/transactions'
                },
                {
                    label: 'View Application',
                    icon: faFileLines,
                    route: '/me/application'
                }
            ]
        }
    },
    methods: {
        toggle() {
            this.open = !this.open
        },
        close() {
            this.open = false
        },
        onClickOutside(event: MouseEvent) {
            const wrapper = this.$refs.wrapper as HTMLElement
            if (wrapper && !wrapper.contains(event.target as Node)) {
                this.close()
            }
        },
        logout() {
            this.$store.dispatch('auth/logout')
            this.$router.push('/')
            this.close()
        }
    },
    mounted() {
        document.addEventListener('click', this.onClickOutside)
    },
    unmounted() {
        document.removeEventListener('click', this.onClickOutside)
    }
})
</script>

<style scoped>
.rotate-180 {
    transform: rotate(180deg);
}
</style>
