<!-- @author Bharadi Harshitha -->
<template>
    <div class="min-h-screen flex overflow-hidden bg-slate-100">
        <AdminSidebar />

        <main class="flex-1 flex flex-col min-w-0">
            <AdminHeader />

            <div class="flex-1 overflow-y-auto p-8">
                <router-view v-slot="{ Component }">
                    <Transition name="fade" mode="out-in">
                        <component :is="Component" />
                    </Transition>
                </router-view>
            </div>
        </main>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AdminSidebar from '@/components/admin/AdminSidebar.vue'
import AdminHeader from '@/components/admin/AdminHeader.vue'

export default defineComponent({
    name: 'AdminLayout',
    components: {
        AdminSidebar,
        AdminHeader
    }
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
</style>