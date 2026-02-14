<template>
    <div class="relative" ref="menuRef">
        <button @click.stop="toggleMenu" class="flex items-center gap-2 sm:gap-3 rounded-full py-1 px-1 sm:px-2 hover:bg-slate-50">
            <div class="text-right hidden sm:block">
                <p class="text-xs sm:text-sm font-medium text-gray-700 truncate max-w-[120px]">{{ user?.name }}</p>
                <p class="text-[10px] sm:text-xs text-gray-500 truncate max-w-[120px]">{{ user?.email }}</p>
            </div>

            <img class="w-8 h-8 sm:w-10 sm:h-10 rounded-full object-cover border" :src="user?.avatar" />

            <span class="material-icons-round text-xs sm:text-sm text-slate-600">
                {{ isOpen ? 'arrow_drop_up' : 'arrow_drop_down' }}
            </span>
        </button>

        <div v-if="isOpen" class="absolute right-0 mt-2 w-48 sm:w-56 bg-white rounded-xl shadow-lg border right-[-8px] sm:right-0">
            <div class="p-3 sm:p-4 border-b">
                <p class="text-sm sm:font-semibold truncate">{{ user?.name }}</p>
                <p class="text-[10px] sm:text-xs text-gray-500 truncate">{{ user?.email }}</p>
            </div>

            <div class="p-1 sm:p-2">
                <router-link to="/profile" class="block px-3 sm:px-4 py-1.5 sm:py-2 text-sm sm:text-base hover:bg-slate-50 rounded-lg" @click="closeMenu">
                    Profile
                </router-link>

                <router-link to="/settings" class="block px-3 sm:px-4 py-1.5 sm:py-2 text-sm sm:text-base hover:bg-slate-50 rounded-lg" @click="closeMenu">
                    Settings
                </router-link>

                <hr class="my-1 sm:my-2" />

                <button @click="$emit('logout')"
                    class="block w-full text-left px-3 sm:px-4 py-1.5 sm:py-2 text-sm sm:text-base text-red-600 hover:bg-red-50 rounded-lg">
                    Logout
                </button>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
    name: 'AdminUserMenu',
    props: {
        user: Object
    },

    data() {
        return {
            isOpen: false
        }
    },

    methods: {
        toggleMenu() {
            this.isOpen = !this.isOpen
        },

        closeMenu() {
            this.isOpen = false
        },

        handleClickOutside(event: MouseEvent) {
            if (!(this.$refs.menuRef as HTMLElement)?.contains(event.target as Node)) {
                this.isOpen = false
            }
        }
    },

    mounted() {
        document.addEventListener('click', this.handleClickOutside)
    },

    beforeUnmount() {
        document.removeEventListener('click', this.handleClickOutside)
    }
})
</script>