<template>
  <div class="min-h-screen flex overflow-hidden bg-slate-100">
    <SidebarMenu />

    <main class="flex-1 flex flex-col min-w-0">
      <!-- Enhanced Header with User Menu -->
      <header class="h-20 flex items-center justify-between px-8 bg-white border-b border-slate-200 sticky top-0 z-10">
        <h1 class="text-xl font-bold text-indigo-700">{{ pageTitle }}</h1>
        
        <!-- Right Section with User Menu -->
        <div class="flex items-center gap-4">
          <!-- User Menu Dropdown -->
          <div class="relative" ref="userMenuRef">
            <button 
              @click.stop="toggleUserMenu"
              class="flex items-center gap-3 focus:outline-none focus:ring-2 focus:ring-indigo-100 rounded-full py-1 px-2 hover:bg-slate-50 transition-colors"
              :aria-expanded="isUserMenuOpen"
              aria-label="User menu"
            >
              <div class="text-right hidden sm:block">
                <p class="text-sm font-medium text-gray-700">{{ user?.name || 'Admin User' }}</p>
                <p class="text-xs text-gray-500">{{ user?.email || 'admin@bank.com' }}</p>
              </div>
              <div class="relative">
                <img 
                  class="w-10 h-10 rounded-full object-cover border-2 border-slate-200"
                  :src="user?.avatar || 'https://i.pravatar.cc/100?img=12'"
                  :alt="`${user?.name || 'Admin'} avatar`"
                />
                <span class="absolute bottom-0 right-0 w-2.5 h-2.5 bg-green-500 border-2 border-white rounded-full"></span>
              </div>
              <span class="material-icons-round text-gray-400 text-sm">
                {{ isUserMenuOpen ? 'arrow_drop_up' : 'arrow_drop_down' }}
              </span>
            </button>

            <!-- User Menu Dropdown -->
            <div v-if="isUserMenuOpen" 
                 class="absolute right-0 mt-2 w-56 bg-white rounded-xl shadow-lg border border-slate-200 overflow-hidden z-50"
            >
              <div class="p-4 border-b border-slate-100">
                <p class="font-semibold text-slate-700">{{ user?.name || 'Admin User' }}</p>
                <p class="text-xs text-slate-500">{{ user?.email || 'admin@bank.com' }}</p>
              </div>
              <div class="p-2">
                <router-link 
                  to="/profile" 
                  class="flex items-center gap-2 px-4 py-2 text-sm text-slate-600 hover:bg-slate-50 rounded-lg w-full"
                  @click="closeUserMenu"
                >
                  <span class="material-icons-round text-base">person</span>
                  Profile
                </router-link>
                <router-link 
                  to="/settings" 
                  class="flex items-center gap-2 px-4 py-2 text-sm text-slate-600 hover:bg-slate-50 rounded-lg w-full"
                  @click="closeUserMenu"
                >
                  <span class="material-icons-round text-base">settings</span>
                  Settings
                </router-link>
                <hr class="my-2 border-slate-100">
                <button 
                  @click="handleLogout" 
                  class="flex items-center gap-2 px-4 py-2 text-sm text-red-600 hover:bg-red-50 rounded-lg w-full"
                >
                  <span class="material-icons-round text-base">logout</span>
                  Logout
                </button>
              </div>
            </div>
          </div>
        </div>
      </header>

      <!-- Main Content -->
      <div class="flex-1 overflow-y-auto p-8">
        <RouterView v-slot="{ Component }">
          <Transition name="fade" mode="out-in">
            <Suspense>
              <component :is="Component" />
              <template #fallback>
                <div class="flex items-center justify-center min-h-[400px]">
                  <div class="flex flex-col items-center gap-4">
                    <div class="w-12 h-12 border-4 border-indigo-200 border-t-indigo-600 rounded-full animate-spin"></div>
                    <p class="text-sm text-slate-500">Loading...</p>
                  </div>
                </div>
              </template>
            </Suspense>
          </Transition>
        </RouterView>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import SidebarMenu from '@/components/SidebarMenu.vue'

const router = useRouter()
const route = useRoute()

// User state
const user = ref<{ name?: string; email?: string; avatar?: string } | null>(null)
const isUserMenuOpen = ref(false)
const userMenuRef = ref<HTMLElement | null>(null)

// Dynamic page title based on route
const pageTitle = computed(() => {
  const path = route.path
  if (path === '/') return 'Dashboard'
  if (path === '/approvals') return 'Pending Applications'
  if (path === '/audit-logs') return 'Audit Logs'
  if (path.includes('/profile')) return 'Profile'
  if (path.includes('/settings')) return 'Settings'
  return 'Dashboard'
})

// Toggle user menu
const toggleUserMenu = () => {
  isUserMenuOpen.value = !isUserMenuOpen.value
}

const closeUserMenu = () => {
  isUserMenuOpen.value = false
}

// Logout function
const handleLogout = async () => {
  try {
    // Clear all auth tokens and user data
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    sessionStorage.clear()
    
    // Clear user state
    user.value = null
    
    // Close menu
    closeUserMenu()
    
    // Redirect to login page
    await router.push('/login')
  } catch (error) {
    console.error('Logout failed:', error)
  }
}

// Load user data from storage
const loadUserData = () => {
  try {
    const userData = localStorage.getItem('user')
    if (userData) {
      user.value = JSON.parse(userData)
    } else {
      // Set default user for demo
      user.value = {
        name: 'Admin User',
        email: 'admin@bank.com',
        avatar: 'https://i.pravatar.cc/100?img=12'
      }
    }
  } catch (error) {
    console.error('Failed to load user data:', error)
    // Set default user on error
    user.value = {
      name: 'Admin User',
      email: 'admin@bank.com',
      avatar: 'https://i.pravatar.cc/100?img=12'
    }
  }
}

// Handle click outside
const handleClickOutside = (event: MouseEvent) => {
  if (userMenuRef.value && !userMenuRef.value.contains(event.target as Node)) {
    isUserMenuOpen.value = false
  }
}

// Lifecycle
onMounted(() => {
  loadUserData()
  // Add click outside listener
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  // Remove click outside listener
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.15s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.flex-1.overflow-y-auto {
  scrollbar-width: thin;
  scrollbar-color: #cbd5e1 #f1f5f9;
}

.flex-1.overflow-y-auto::-webkit-scrollbar {
  width: 6px;
}

.flex-1.overflow-y-auto::-webkit-scrollbar-track {
  background: #f1f5f9;
}

.flex-1.overflow-y-auto::-webkit-scrollbar-thumb {
  background-color: #cbd5e1;
  border-radius: 3px;
}

.flex-1.overflow-y-auto::-webkit-scrollbar-thumb:hover {
  background-color: #94a3b8;
}

.animate-spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>