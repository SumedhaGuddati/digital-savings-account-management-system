<template>
  <div> 
    <!-- Stats Cards - Now in 2 rows of 4 cards each -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
      <!-- Row 1: Original 4 KPI Cards -->
      
      <!-- Managed Assets Card -->
      <div class="glass-card p-6 rounded-2xl">
        <div class="flex justify-between items-start mb-4">
          <span class="text-[10px] font-bold text-slate-muted uppercase tracking-widest">Managed Assets</span>
          <span 
            v-if="stats.managedAssetsGrowth !== undefined"
            :class="stats.managedAssetsGrowth >= 0 ? 'text-success-green bg-emerald-50 border-emerald-100' : 'text-coral-red bg-red-50 border-red-100'"
            class="text-xs font-bold px-2 py-0.5 rounded-full border"
          >
            {{ stats.managedAssetsGrowth >= 0 ? '+' : '' }}{{ stats.managedAssetsGrowth.toFixed(1) }}%
          </span>
        </div>
        <h3 class="text-3xl font-bold text-primary-indigo mb-2">{{ formatCurrency(stats.managedAssets) }}</h3>
        <div class="h-8 w-full flex items-end">
          <div class="w-full h-[2px] bg-mint-accent/20 relative">
            <div class="w-3/4 h-full bg-mint-accent relative">
              <div class="absolute -right-1 -top-1 w-2.5 h-2.5 bg-mint-accent rounded-full border-2 border-white shadow-sm"></div>
            </div>
          </div>
        </div>
        <p class="text-[10px] text-slate-400 mt-2">vs previous month</p>
      </div>

      <!-- Approved Apps Card -->
      <div class="glass-card p-6 rounded-2xl border-l-4 border-l-success-green">
        <div class="flex justify-between items-start mb-4">
          <span class="text-[10px] font-bold text-slate-muted uppercase tracking-widest">Approved Apps</span>
          <div class="w-2 h-2 bg-success-green rounded-full"></div>
        </div>
        <h3 class="text-3xl font-bold text-primary-indigo mb-2">{{ formatNumber(stats.approvedAppsCount) }}</h3>
        <p class="text-[11px] text-slate-muted flex items-center gap-1">
          <span class="material-icons-round text-sm">schedule</span> 
          {{ formatNumber(stats.pendingAppsCount) }} pending reviews
        </p>
      </div>

      <!-- Active Users Card -->
      <div class="glass-card p-6 rounded-2xl">
        <div class="flex justify-between items-start mb-4">
          <span class="text-[10px] font-bold text-slate-muted uppercase tracking-widest">Applied Users</span>
          <span class="material-icons-round text-indigo-200">groups</span>
        </div>
        <h3 class="text-3xl font-bold text-primary-indigo mb-2">{{ formatNumber(stats.activeUsersCount) }}</h3>
        <div class="flex items-center gap-2">
          <div class="flex -space-x-2">
            <img 
              v-for="(user, index) in recentUsers" 
              :key="index"
              class="w-6 h-6 rounded-full border-2 border-white" 
              :src="user.avatar" 
              :alt="user.name"
            />
            <div 
              v-if="stats.newUsersToday > 3"
              class="w-6 h-6 rounded-full border-2 border-white bg-indigo-100 flex items-center justify-center text-[8px] font-bold text-primary-indigo"
            >
              +{{ stats.newUsersToday - 3 }}
            </div>
          </div>
          <span class="text-[10px] text-success-green font-bold">+{{ stats.newUsersToday }} today</span>
        </div>
      </div>

      <!-- Avg. Yield Card -->
      <div class="glass-card p-6 rounded-2xl">
        <div class="flex justify-between items-start mb-4">
          <span class="text-[10px] font-bold text-slate-muted uppercase tracking-widest">Avg. Yield</span>
          <span class="text-mint-accent text-[10px] font-bold uppercase">Standard</span>
        </div>
        <h3 class="text-3xl font-bold text-primary-indigo mb-2">{{ stats.averageYield.toFixed(2) }}%</h3>
        <p class="text-[11px] text-slate-muted">Average rate across {{ formatNumber(stats.activeUsersCount) }} active accounts</p>
      </div>
    </div>

    <!-- Row 2: New Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-10">
      
      <!-- User Role Distribution Card - NEW -->
      <div class="glass-card p-6 rounded-2xl bg-gradient-to-br from-white to-indigo-50/30">
        <div class="flex justify-between items-start mb-4">
          <span class="text-[10px] font-bold text-slate-muted uppercase tracking-widest">User Roles</span>
          <span class="material-icons-round text-indigo-300">admin_panel_settings</span>
        </div>
        
        <div class="flex items-center justify-between mb-3">
          <div class="flex items-center gap-2">
            <div class="w-8 h-8 rounded-full bg-primary-indigo/10 flex items-center justify-center">
              <span class="material-icons-round text-primary-indigo text-sm">shield</span>
            </div>
            <div>
              <p class="text-xs text-slate-muted">Administrators</p>
              <p class="text-xl font-bold text-primary-indigo">{{ formatNumber(stats.adminCount) }}</p>
            </div>
          </div>
          <div class="text-right">
            <span class="text-[10px] text-slate-400">Role</span>
            <p class="text-xs font-bold text-primary-indigo">{{ ((stats.adminCount / (stats.adminCount + stats.customerCount)) * 100 || 0).toFixed(1) }}%</p>
          </div>
        </div>
        
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-2">
            <div class="w-8 h-8 rounded-full bg-success-green/10 flex items-center justify-center">
              <span class="material-icons-round text-success-green text-sm">person</span>
            </div>
            <div>
              <p class="text-xs text-slate-muted">Customers</p>
              <p class="text-xl font-bold text-success-green">{{ formatNumber(stats.customerCount) }}</p>
            </div>
          </div>
          <div class="text-right">
            <span class="text-[10px] text-slate-400">Role</span>
            <p class="text-xs font-bold text-success-green">{{ ((stats.customerCount / (stats.adminCount + stats.customerCount)) * 100 || 0).toFixed(1) }}%</p>
          </div>
        </div>
        
        <!-- Progress bar showing ratio -->
        <div class="mt-4 h-1.5 w-full bg-slate-100 rounded-full overflow-hidden">
          <div 
            class="h-full bg-primary-indigo rounded-l-full" 
            :style="{ width: ((stats.adminCount / (stats.adminCount + stats.customerCount)) * 100 || 0) + '%' }"
          ></div>
          <div 
            class="h-full bg-success-green" 
            :style="{ width: ((stats.customerCount / (stats.adminCount + stats.customerCount)) * 100 || 0) + '%' }"
          ></div>
        </div>
        <p class="text-[9px] text-slate-400 mt-2 text-center">Admin : Customer ratio</p>
      </div>

      <!-- Account Type Distribution Card - Optional Add-on -->
      <div class="glass-card p-6 rounded-2xl">
        <div class="flex justify-between items-start mb-4">
          <span class="text-[10px] font-bold text-slate-muted uppercase tracking-widest">Account Types</span>
          <span class="material-icons-round text-indigo-200">pie_chart</span>
        </div>
        <div class="space-y-3">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-2">
              <span class="w-2.5 h-2.5 bg-amber-glow rounded-full"></span>
              <span class="text-xs text-slate-muted">Premium</span>
            </div>
            <span class="text-sm font-bold text-primary-indigo">8</span>
          </div>
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-2">
              <span class="w-2.5 h-2.5 bg-primary-indigo rounded-full"></span>
              <span class="text-xs text-slate-muted">High-Yield</span>
            </div>
            <span class="text-sm font-bold text-primary-indigo">12</span>
          </div>
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-2">
              <span class="w-2.5 h-2.5 bg-mint-accent rounded-full"></span>
              <span class="text-xs text-slate-muted">Standard</span>
            </div>
            <span class="text-sm font-bold text-primary-indigo">{{ stats.activeUsersCount }}</span>
          </div>
        </div>
      </div>

      <!-- Recent Activity Card -->
      <div class="glass-card p-6 rounded-2xl">
        <div class="flex justify-between items-start mb-4">
          <span class="text-[10px] font-bold text-slate-muted uppercase tracking-widest">Today's Activity</span>
          <span class="material-icons-round text-indigo-200">trending_up</span>
        </div>
        <div class="space-y-3">
          <div class="flex items-center gap-3">
            <div class="w-7 h-7 rounded-full bg-emerald-50 flex items-center justify-center">
              <span class="material-icons-round text-success-green text-sm">person_add</span>
            </div>
            <div class="flex-1">
              <p class="text-xs text-slate-muted">New Users</p>
              <p class="text-sm font-bold text-primary-indigo">{{ stats.newUsersToday }}</p>
            </div>
            <span class="text-[10px] text-success-green">+{{ stats.newUsersToday }} today</span>
          </div>
          <div class="flex items-center gap-3">
            <div class="w-7 h-7 rounded-full bg-amber-50 flex items-center justify-center">
              <span class="material-icons-round text-amber-glow text-sm">check_circle</span>
            </div>
            <div class="flex-1">
              <p class="text-xs text-slate-muted">Approvals</p>
              <p class="text-sm font-bold text-primary-indigo">{{ stats.approvedAppsCount }}</p>
            </div>
            <span class="text-[10px] text-amber-glow">total</span>
          </div>
          <div class="flex items-center gap-3">
            <div class="w-7 h-7 rounded-full bg-blue-50 flex items-center justify-center">
              <span class="material-icons-round text-primary-indigo text-sm">hourglass_empty</span>
            </div>
            <div class="flex-1">
              <p class="text-xs text-slate-muted">Pending</p>
              <p class="text-sm font-bold text-primary-indigo">{{ stats.pendingAppsCount }}</p>
            </div>
            <span class="text-[10px] text-slate-400">need review</span>
          </div>
        </div>
      </div>

      <!-- System Health Card -->
      <div class="glass-card p-6 rounded-2xl">
        <div class="flex justify-between items-start mb-4">
          <span class="text-[10px] font-bold text-slate-muted uppercase tracking-widest">System Health</span>
          <span class="material-icons-round text-indigo-200">monitor_heart</span>
        </div>
        <div class="flex items-center gap-3 mb-3">
          <div class="w-10 h-10 rounded-full bg-success-green/10 flex items-center justify-center">
            <span class="material-icons-round text-success-green">check_circle</span>
          </div>
          <div>
            <p class="text-xs text-slate-muted">API Status</p>
            <p class="text-sm font-bold text-success-green">Operational</p>
          </div>
        </div>
        <div class="grid grid-cols-2 gap-2 mt-2">
          <div class="bg-slate-50 p-2 rounded-lg">
            <p class="text-[9px] text-slate-400">Response</p>
            <p class="text-xs font-bold text-primary-indigo">124ms</p>
          </div>
          <div class="bg-slate-50 p-2 rounded-lg">
            <p class="text-[9px] text-slate-400">Uptime</p>
            <p class="text-xs font-bold text-primary-indigo">99.9%</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Approved Applications Table -->
    <div class="bg-white rounded-2xl border border-border-soft shadow-sm overflow-hidden">
      <div class="px-8 py-6 border-b border-border-soft flex items-center justify-between">
        <div>
          <h2 class="text-lg font-bold text-primary-indigo">Approved Applications</h2>
          <p class="text-sm text-slate-muted">Successfully verified and activated accounts.</p>
        </div>
        <div class="flex items-center gap-3">
          <span class="text-xs text-slate-muted">Last updated: {{ lastUpdated }}</span>
          <button 
            @click="refreshData" 
            class="flex items-center gap-2 text-xs font-bold text-primary-indigo px-4 py-2 rounded-lg bg-indigo-50 hover:bg-indigo-100 transition-all border border-indigo-100"
            :disabled="loading"
          >
            <span class="material-icons-round text-sm" :class="{ 'animate-spin': loading }">refresh</span>
            Refresh
          </button>
        </div>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="flex justify-center items-center py-16">
        <div class="flex flex-col items-center gap-3">
          <div class="w-10 h-10 border-4 border-indigo-200 border-t-primary-indigo rounded-full animate-spin"></div>
          <p class="text-sm text-slate-muted">Loading dashboard data...</p>
        </div>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="flex justify-center items-center py-16">
        <div class="flex flex-col items-center gap-3">
          <span class="material-icons-round text-4xl text-coral-red">error_outline</span>
          <p class="text-sm text-slate-muted">{{ error }}</p>
          <button 
            @click="refreshData" 
            class="px-4 py-2 text-xs font-bold text-primary-indigo bg-indigo-50 rounded-lg hover:bg-indigo-100"
          >
            Try Again
          </button>
        </div>
      </div>

      <!-- Empty State -->
      <div v-else-if="approvedApplications.length === 0" class="flex justify-center items-center py-16">
        <div class="flex flex-col items-center gap-3">
          <span class="material-icons-round text-4xl text-slate-300">check_circle_outline</span>
          <p class="text-sm text-slate-muted">No approved applications yet</p>
        </div>
      </div>

      <!-- Data Table -->
      <div v-else class="overflow-x-auto">
        <table class="w-full text-left">
          <thead>
            <tr class="bg-slate-50">
              <th class="px-8 py-4 text-[11px] uppercase tracking-widest text-slate-muted font-bold">Applicant Name</th>
              <th class="px-8 py-4 text-[11px] uppercase tracking-widest text-slate-muted font-bold">Account Type</th>
              <th class="px-8 py-4 text-[11px] uppercase tracking-widest text-slate-muted font-bold">Application Date</th>
              <th class="px-8 py-4 text-[11px] uppercase tracking-widest text-slate-muted font-bold">Review Date</th>
              <th class="px-8 py-4 text-[11px] uppercase tracking-widest text-slate-muted font-bold">Reviewed By</th>
              <th class="px-8 py-4 text-[11px] uppercase tracking-widest text-slate-muted font-bold text-right">Status</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-border-soft">
            <tr v-for="application in approvedApplications" :key="application.applicationId" class="hover:bg-slate-50 transition-all">
              <td class="px-8 py-5">
                <div>
                  <p class="font-bold text-sm text-primary-indigo">{{ application.applicantName }}</p>
                  <p class="text-xs text-slate-muted">{{ application.applicantEmail }}</p>
                </div>
              </td>
              <td class="px-8 py-5">
                <span class="px-3 py-1.5 rounded-full text-[10px] font-bold bg-success-green/10 text-success-green border border-success-green/20">
                  {{ application.accountType || 'Standard' }}
                </span>
              </td>
              <td class="px-8 py-5">
                <p class="text-sm text-primary-indigo">{{ formatDate(application.applicationDate) }}</p>
              </td>
              <td class="px-8 py-5">
                <p class="text-sm text-primary-indigo">{{ formatDate(application.reviewDate) }}</p>
              </td>
              <td class="px-8 py-5">
                <p class="text-sm text-primary-indigo">{{ application.reviewedBy || 'System' }}</p>
              </td>
              <td class="px-8 py-5 text-right">
                <span class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-[10px] font-bold bg-success-green/10 text-success-green">
                  <span class="w-1.5 h-1.5 bg-success-green rounded-full"></span>
                  APPROVED
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from "vue";
import { 
  getDashboardStats, 
  getApprovedApplications,  
} from "@/services/adminApi";
import { AdminApplicationViewResponse, AdminDashboardStatsResponse } from "@/types/adminTypes";

export default defineComponent({
  name: "DashboardView",

  setup() {
    // State
    const stats = ref<AdminDashboardStatsResponse>({
      managedAssets: 0,
      managedAssetsGrowth: 0,
      approvedAppsCount: 0,
      activeUsersCount: 0,
      newUsersToday: 0,
      averageYield: 0,
      pendingAppsCount: 0,
      adminCount: 0,
      customerCount: 0
    });
    
    const approvedApplications = ref<AdminApplicationViewResponse[]>([]);
    const loading = ref(true);
    const error = ref<string | null>(null);
    const lastUpdated = ref<string>("");

    // Mock recent users for avatar display
    const recentUsers = ref([
      { 
        name: "John Doe",
        avatar: "https://lh3.googleusercontent.com/aida-public/AB6AXuDpOoOyrXB_mr8DawSZfYh-XV-GZiMB9JAZV9Z3uQrXnanCXQVKsJppp-fKgx95zYwpYaIcyl43cqYEVI8NHLSEnJY_ZN2Pue2Mx2vUMea1V2Bv0gnPMFi4CEdI7IR5LfYvO14dyGusXkTuVotRq4v0Yfin_ttv8Dcasbkn44IGgXSsnPDvK3KwrPNxUnc6CeWRj3h3NTJDHE15DmS74jFR8R8N_5VDEjywmvXn0PjNTYWeXhpWfpATGhobtKB8IdLnY9ZS4SX1oQ06" 
      },
      { 
        name: "Jane Smith",
        avatar: "https://lh3.googleusercontent.com/aida-public/AB6AXuBNgf-wZd8tl2FbTdmnZuaTJNobQHC9btF6suFJOpsrBrt5XpUhn2Z10AVIstjJpdXkU_dPq_1VZDO9bdEkunWL16szh64ylP1HPinCX2if9SNfxkXSa0KGQeddbO7eJWNtU0M-m0XiGxLSOGU4yE0b2im3PSQqY1kSj0aCexg88Xn-aftlyGp0WcXM4F39t81oxqGcszBibzCjpUqK7FI_AHz9NWLwqSIjfvTkFn1UxEjW1fkeDam6X4MBiVFgyLFcjSFVDNnDvz8u" 
      },
      { 
        name: "Mike Johnson",
        avatar: "https://lh3.googleusercontent.com/aida-public/AB6AXuANjMbPiS47rpgAtzgqJ2xOw9SGBMTGIgC7oKU-C2TSaasqLSs4j9I54JNObQc7cBh08JqEM_35wdhRUAuKqEkacScnSMaMjopZ5HZir10MYWDd_lYo7yATEdc3_7cnG5cbD9rsOKzKPjBgXiGfEMuXJy5fIMFPCGso65GZWKOIJfAr81CAVRd3XZRrYfc0CjbeOJgO5plxeGJdRLRWQydc9TqPVqesKacEVxnVtyKTPoHBrTwXcsCD30D7QdyzvxLzoFf9KNPesjVr" 
      }
    ]);

    // Helper functions
    const formatCurrency = (value: number): string => {
      if (!value) return '$0';
      if (value >= 1000000000) {
        return `$${(value / 1000000000).toFixed(2)}B`;
      }
      if (value >= 1000000) {
        return `$${(value / 1000000).toFixed(2)}M`;
      }
      if (value >= 1000) {
        return `$${(value / 1000).toFixed(1)}K`;
      }
      return `$${value.toLocaleString()}`;
    };

    const formatNumber = (value: number): string => {
      if (!value) return '0';
      return value.toLocaleString();
    };

    const formatDate = (dateString?: string | null): string => {
      if (!dateString) return 'N/A';
      try {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
          year: 'numeric',
          month: 'short',
          day: 'numeric',
          hour: '2-digit',
          minute: '2-digit'
        });
      } catch {
        return dateString;
      }
    };

    // Data fetching
    const fetchDashboardData = async () => {
      loading.value = true;
      error.value = null;
      
      try {
        const [statsData, approvedData] = await Promise.all([
          getDashboardStats(),
          getApprovedApplications()
        ]);
        
        stats.value = statsData;
        approvedApplications.value = approvedData;
        
        lastUpdated.value = new Date().toLocaleTimeString('en-US', {
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit'
        });
      } catch (err: any) {
        console.error('Error fetching dashboard data:', err);
        
        if (err.response) {
          if (err.response.status === 404) {
            error.value = 'Dashboard API endpoint not found. Please check backend.';
          } else if (err.response.status === 500) {
            error.value = 'Server error. Please check backend logs.';
          } else {
            error.value = `Error ${err.response.status}: ${err.response.data?.message || 'Failed to load'}`;
          }
        } else if (err.request) {
          error.value = 'Cannot connect to server. Please check if backend is running.';
        } else {
          error.value = 'Failed to load dashboard data. Please try again.';
        }
      } finally {
        loading.value = false;
      }
    };

    const refreshData = () => {
      fetchDashboardData();
    };

    onMounted(() => {
      fetchDashboardData();
    });

    return {
      stats,
      approvedApplications,
      recentUsers,
      loading,
      error,
      lastUpdated,
      formatCurrency,
      formatNumber,
      formatDate,
      refreshData
    };
  },
});
</script>

<style scoped>
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