<template>
    <aside class="w-14 xs:w-16 sm:w-20 lg:w-24 flex flex-col items-center py-3 xs:py-4 sm:py-6 lg:py-8 bg-white sidebar-shadow z-50">
        <nav class="flex-1 flex flex-col gap-3 xs:gap-4 sm:gap-5 lg:gap-6 mt-6 xs:mt-10 sm:mt-16 lg:mt-12">
            <router-link v-for="item in navItems" :key="item.path" :to="item.path"
                class="group relative flex items-center justify-center w-8 xs:w-10 sm:w-11 lg:w-12 h-8 xs:h-10 sm:h-11 lg:h-12 rounded-lg xs:rounded-xl transition-all cursor-pointer" :class="[
                    $route.path === item.path
                        ? 'bg-[#312E81]/10 text-[#312E81]'
                        : 'text-slate-muted hover:text-[#312E81] hover:bg-off-white'
                ]">
                <span class="material-icons-round text-xs xs:text-sm sm:text-base lg:text-lg text-current">{{ item.icon }}</span>
                <div
                    class="absolute left-10 xs:left-12 sm:left-14 lg:left-16 px-1 xs:px-1.5 sm:px-2 py-0.5 xs:py-1 bg-[#312E81] text-white text-[8px] xs:text-[10px] sm:text-xs font-medium rounded opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap pointer-events-none shadow-sm">
                    {{ item.name }}
                </div>
            </router-link>
        </nav>
    </aside>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
    name: "AdminSidebar",
    
    data() {
        return {
            navItems: [
                { path: "/admin", name: "Overview", icon: "dashboard" },
                { path: "/admin/approvals", name: "Approvals", icon: "verified_user" },
                { path: "/admin/audit-logs", name: "Audit Logs", icon: "history_edu" }
            ]
        };
    }
});
</script>

<style scoped>
.sidebar-shadow {
    box-shadow: 4px 0 12px rgba(0, 0, 0, 0.02);
}

.material-icons-round {
    margin-top: 10px;
    font-size: 20px;
    font-family: 'Material Icons Round', 'Material Icons', sans-serif;
}

.text-3xl {
    font-size: 30px !important;
}

/* Optional: Add responsive behavior for very small screens */
@media (max-width: 480px) {
    .material-icons-round {
        font-size: 16px;
    }
}
</style>