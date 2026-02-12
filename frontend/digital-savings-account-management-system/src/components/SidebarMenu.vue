<template>
  <aside class="w-20 lg:w-24 flex flex-col items-center py-8 bg-white sidebar-shadow z-50">
    <div class="mb-12">
      <div class="w-12 h-12 rounded-xl bg-indigo-600 flex items-center justify-center shadow-lg shadow-indigo-200">
        <span class="material-icons-round text-white text-3xl font-bold">bolt</span>
      </div>
    </div>
    
    <nav class="flex-1 flex flex-col gap-6">
      <router-link 
        v-for="item in navItems" 
        :key="item.path"
        :to="item.path"
        class="group relative flex items-center justify-center w-12 h-12 rounded-xl transition-all"
        :class="[
          $route.path === item.path 
            ? 'bg-indigo-50 text-indigo-600' 
            : 'text-slate-muted hover:text-indigo-600 hover:bg-slate-50'
        ]"
      >
        <span class="material-icons-round">{{ item.icon }}</span>
        <div class="absolute left-16 px-2 py-1 bg-indigo-600 text-white text-xs font-medium rounded opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap pointer-events-none shadow-sm">
          {{ item.name }}
        </div>
      </router-link>
    </nav>
    
   
  </aside>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useRoute } from "vue-router";

export default defineComponent({
  name: "SidebarMenu",
  setup() {
    const route = useRoute();
    
    const navItems = [
      { path: "/", name: "Overview", icon: "dashboard" },
      { path: "/approvals", name: "Approvals", icon: "verified_user" },
      { path: "/audit-logs", name: "Audit Logs", icon: "history_edu" }
    ];

    return {
      navItems,
      route
    };
  }
});
</script>

<style scoped>
.sidebar-shadow {
  box-shadow: 4px 0 12px rgba(0, 0, 0, 0.02);
}

/* Exact colors from Managed Assets component */
.text-slate-muted {
  color: #64748b;
}

.bg-indigo-600 {
  background-color: #4f46e5;
}

.text-indigo-600 {
  color: #4f46e5;
}

.bg-indigo-50 {
  background-color: #eef2ff;
}

.hover\:text-indigo-600:hover {
  color: #4f46e5;
}

.hover\:bg-slate-50:hover {
  background-color: #f8fafc;
}

.border-slate-100 {
  border-color: #f1f5f9;
}

.hover\:border-indigo-200:hover {
  border-color: #c7d2fe;
}

.shadow-indigo-200 {
  --tw-shadow-color: #c7d2fe;
  box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1);
}

/* Success green from the +0.0% badge */
.text-success-green {
  color: #10b981;
}

.bg-emerald-50 {
  background-color: #ecfdf5;
}

.border-emerald-100 {
  border-color: #d1fae5;
}

/* Material icons */
.material-icons-round {
  font-size: 20px;
}
</style>