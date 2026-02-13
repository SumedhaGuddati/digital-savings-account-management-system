<template>
  <div class="space-y-6">
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-2xl font-bold text-primary-indigo">Audit Logs</h1>
        <p class="text-sm text-slate-muted mt-1">System activity and security events from applications, users, accounts, and transactions</p>
      </div>
      <div class="flex gap-3">
        <button @click="exportLogs" class="px-4 py-2 border border-slate-200 rounded-lg text-xs font-bold text-slate-600 hover:bg-slate-50 flex items-center gap-2">
          <span class="material-icons-round text-sm">download</span>
          Export Logs
        </button>
        <button @click="clearFilters" class="px-4 py-2 bg-primary-indigo text-white rounded-lg text-xs font-bold hover:bg-indigo-900">
          Clear Filters
        </button>
      </div>
    </div>

    <!-- Date Range Picker -->
    <div class="bg-white rounded-xl border border-border-soft p-5">
      <div class="flex flex-wrap gap-4 items-center">
        <span class="text-xs font-bold text-slate-muted uppercase">Time Period:</span>
        <button 
          v-for="period in timePeriods" 
          :key="period.value"
          @click="setTimePeriod(period.value)"
          :class="selectedPeriod === period.value ? 'bg-primary-indigo text-white' : 'border border-slate-200 text-slate-600 hover:bg-slate-50'"
          class="px-4 py-2 rounded-lg text-xs font-bold transition-all">
          {{ period.label }}
        </button>
        
        <div class="ml-auto flex gap-2">
          <select v-model="filters.eventType" @change="fetchAuditLogs" class="px-3 py-2 border border-slate-200 rounded-lg text-xs bg-white min-w-[150px]">
            <option value="">All Event Types</option>
            <option v-for="type in eventTypes" :key="type" :value="type">{{ formatAction(type) }}</option>
          </select>
          <select v-model="filters.severity" @change="fetchAuditLogs" class="px-3 py-2 border border-slate-200 rounded-lg text-xs bg-white min-w-[120px]">
            <option value="">All Severities</option>
            <option value="INFO">Info</option>
            <option value="WARNING">Warning</option>
            <option value="CRITICAL">Critical</option>
          </select>
        </div>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="bg-white rounded-2xl border border-border-soft shadow-sm p-16 flex justify-center">
      <div class="flex flex-col items-center gap-3">
        <div class="w-10 h-10 border-4 border-indigo-200 border-t-primary-indigo rounded-full animate-spin"></div>
        <p class="text-sm text-slate-muted">Loading audit logs from database...</p>
      </div>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="bg-white rounded-2xl border border-border-soft shadow-sm p-16 flex justify-center">
      <div class="flex flex-col items-center gap-3">
        <span class="material-icons-round text-4xl text-coral-red">error_outline</span>
        <p class="text-sm text-slate-muted">{{ error }}</p>
        <button @click="fetchAuditLogs" class="px-4 py-2 text-xs font-bold text-primary-indigo bg-indigo-50 rounded-lg hover:bg-indigo-100">
          Try Again
        </button>
      </div>
    </div>

    <!-- Audit Log Timeline -->
    <div v-else class="bg-white rounded-2xl border border-border-soft shadow-sm overflow-hidden">
      <div class="px-6 py-4 border-b border-border-soft bg-slate-50">
        <div class="flex items-center gap-4">
          <span class="text-xs font-bold text-primary-indigo">Event Timeline</span>
          <span class="text-[10px] text-slate-muted">Showing {{ auditEvents.length }} of {{ totalEvents }} events</span>
        </div>
      </div>

      <div v-if="auditEvents.length === 0" class="px-6 py-16 flex flex-col items-center">
        <span class="material-icons-round text-4xl text-slate-300 mb-3">receipt_long</span>
        <p class="text-sm text-slate-muted">No audit events found</p>
        <p class="text-xs text-slate-400 mt-1">Try adjusting your filters</p>
      </div>

      <div v-else class="divide-y divide-border-soft">
        <div v-for="event in auditEvents" :key="`${event.entityType}-${event.entityId}-${event.timestamp}`" class="px-6 py-4 hover:bg-slate-50 transition-all">
          <div class="flex items-start gap-4">
            <div :class="getSeverityClass(event.severity)" class="w-8 h-8 rounded-full flex items-center justify-center">
              <span :class="getSeverityIconClass(event.severity)" class="material-icons-round text-sm">
                {{ getSeverityIcon(event.severity) }}
              </span>
            </div>
            <div class="flex-1">
              <div class="flex justify-between items-start">
                <div>
                  <p class="text-sm font-bold text-primary-indigo">
                    {{ formatAction(event.action) }}
                  </p>
                  <p class="text-xs text-slate-muted mt-0.5">
                    {{ event.details }}
                  </p>
                  <div class="flex items-center gap-2 mt-1">
                    <span class="text-[10px] px-2 py-0.5 bg-slate-100 rounded-full text-slate-600">
                      {{ event.entityType }}
                    </span>
                    <span class="text-[10px] text-slate-400">
                      ID: {{ event.entityId }}
                    </span>
                  </div>
                </div>
                <span :class="getSeverityBadgeClass(event.severity)" class="px-2 py-1 rounded-full text-[10px] font-bold">
                  {{ event.severity }}
                </span>
              </div>
              <div class="flex gap-4 mt-2">
                <span class="text-[10px] text-slate-400 flex items-center gap-1">
                  <span class="material-icons-round text-[12px]">schedule</span>
                  {{ formatDate(event.timestamp) }}
                </span>
                <span class="text-[10px] text-slate-400 flex items-center gap-1">
                  <span class="material-icons-round text-[12px]">person</span>
                  {{ event.username || event.userEmail || 'System' }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Pagination -->
      <div class="px-6 py-4 border-t border-border-soft bg-slate-50 flex justify-between items-center">
        <p class="text-xs text-slate-muted">
          Showing {{ startIndex + 1 }}-{{ endIndex }} of {{ totalEvents }} events
        </p>
        <div class="flex gap-2">
          <button 
            @click="prevPage" 
            :disabled="currentPage === 0"
            class="w-8 h-8 flex items-center justify-center rounded border border-slate-200 bg-white text-slate-600 hover:bg-slate-50 disabled:opacity-50 disabled:cursor-not-allowed">
            <span class="material-icons-round text-sm">chevron_left</span>
          </button>
          <button 
            v-for="page in displayedPages" 
            :key="page"
            @click="goToPage(page)"
            :class="currentPage === page ? 'bg-primary-indigo text-white' : 'border border-slate-200 bg-white text-slate-600 hover:bg-slate-50'"
            class="w-8 h-8 flex items-center justify-center rounded text-xs font-bold">
            {{ page + 1 }}
          </button>
          <button 
            @click="nextPage" 
            :disabled="currentPage >= totalPages - 1"
            class="w-8 h-8 flex items-center justify-center rounded border border-slate-200 bg-white text-slate-600 hover:bg-slate-50 disabled:opacity-50 disabled:cursor-not-allowed">
            <span class="material-icons-round text-sm">chevron_right</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, onMounted, computed, watch } from "vue";
import { getAuditLogs, getAuditLogsCount, getEventTypes } from "@/api/endpoints/admin/admin.api";
import type { AuditEventResponse } from "@/types/auditTypes";


export default defineComponent({
  name: "AuditLogs",
  
  setup() {
    // State
    const auditEvents = ref<AuditEventResponse[]>([]);
    const eventTypes = ref<string[]>([]);
    const loading = ref(true);
    const error = ref<string | null>(null);
    const totalEvents = ref(0);
    const currentPage = ref(0);
    const pageSize = ref(10);
    const selectedPeriod = ref('today');
    
    const filters = reactive({
      startDate: null as string | null,
      endDate: null as string | null,
      eventType: '',
      severity: ''
    });

    const timePeriods = [
      { label: 'Today', value: 'today' },
      { label: 'Yesterday', value: 'yesterday' },
      { label: 'Last 7 Days', value: 'last7' },
      { label: 'This Month', value: 'thisMonth' },
      { label: 'Custom Range', value: 'custom' }
    ];

    // Computed
    const totalPages = computed(() => Math.ceil(totalEvents.value / pageSize.value));
    
    const startIndex = computed(() => currentPage.value * pageSize.value);
    
    const endIndex = computed(() => Math.min(startIndex.value + pageSize.value, totalEvents.value));
    
    const displayedPages = computed(() => {
      const pages = [];
      const maxVisible = 5;
      let start = Math.max(0, currentPage.value - Math.floor(maxVisible / 2));
      let end = Math.min(totalPages.value, start + maxVisible);
      
      if (end - start < maxVisible) {
        start = Math.max(0, end - maxVisible);
      }
      
      for (let i = start; i < end; i++) {
        pages.push(i);
      }
      return pages;
    });

    // Methods
    const formatAction = (action: string): string => {
      return action
        .toLowerCase()
        .replace(/_/g, ' ')
        .replace(/\b\w/g, l => l.toUpperCase());
    };

    const formatDate = (dateString: string): string => {
      const date = new Date(dateString);
      return date.toLocaleString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      });
    };

    const getSeverityClass = (severity: string): string => {
      switch (severity) {
        case 'CRITICAL': return 'bg-red-100';
        case 'WARNING': return 'bg-amber-100';
        default: return 'bg-emerald-100';
      }
    };

    const getSeverityIconClass = (severity: string): string => {
      switch (severity) {
        case 'CRITICAL': return 'text-coral-red';
        case 'WARNING': return 'text-amber-glow';
        default: return 'text-success-green';
      }
    };

    const getSeverityIcon = (severity: string): string => {
      switch (severity) {
        case 'CRITICAL': return 'error';
        case 'WARNING': return 'warning';
        default: return 'check_circle';
      }
    };

    const getSeverityBadgeClass = (severity: string): string => {
      switch (severity) {
        case 'CRITICAL': return 'bg-red-50 text-coral-red';
        case 'WARNING': return 'bg-amber-50 text-amber-glow';
        default: return 'bg-emerald-50 text-success-green';
      }
    };

    const setTimePeriod = (period: string) => {
      selectedPeriod.value = period;
      const now = new Date();
      const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
      
      switch (period) {
        case 'today':
          filters.startDate = today.toISOString();
          filters.endDate = new Date(today.getTime() + 86400000).toISOString();
          break;
        case 'yesterday':
          const yesterday = new Date(today.getTime() - 86400000);
          filters.startDate = yesterday.toISOString();
          filters.endDate = today.toISOString();
          break;
        case 'last7':
          const last7 = new Date(today.getTime() - 7 * 86400000);
          filters.startDate = last7.toISOString();
          filters.endDate = new Date(today.getTime() + 86400000).toISOString();
          break;
        case 'thisMonth':
          const firstDay = new Date(now.getFullYear(), now.getMonth(), 1);
          const lastDay = new Date(now.getFullYear(), now.getMonth() + 1, 1);
          filters.startDate = firstDay.toISOString();
          filters.endDate = lastDay.toISOString();
          break;
        case 'custom':
          break;
      }
      
      fetchAuditLogs();
    };

    const fetchAuditLogs = async () => {
      loading.value = true;
      error.value = null;
      
      try {
        const [logs, count, types] = await Promise.all([
          getAuditLogs(
            filters.startDate || undefined,
            filters.endDate || undefined,
            filters.eventType || undefined,
            filters.severity || undefined,
            currentPage.value,
            pageSize.value
          ),
          getAuditLogsCount(
            filters.startDate || undefined,
            filters.endDate || undefined,
            filters.eventType || undefined,
            filters.severity || undefined
          ),
          getEventTypes()
        ]);
        
        auditEvents.value = logs;
        totalEvents.value = count;
        eventTypes.value = types;
      } catch (err) {
        console.error('Error fetching audit logs:', err);
        error.value = 'Failed to load audit logs from database. Please try again.';
      } finally {
        loading.value = false;
      }
    };

    const clearFilters = () => {
      filters.startDate = null;
      filters.endDate = null;
      filters.eventType = '';
      filters.severity = '';
      selectedPeriod.value = 'today';
      currentPage.value = 0;
      fetchAuditLogs();
    };

    const exportLogs = () => {
      const csv = [
        ['Timestamp', 'User', 'Action', 'Entity Type', 'Entity ID', 'Details', 'Severity'].join(','),
        ...auditEvents.value.map(e => [
          e.timestamp,
          e.username || e.userEmail,
          e.action,
          e.entityType,
          e.entityId,
          `"${e.details.replace(/"/g, '""')}"`,
          e.severity
        ].join(','))
      ].join('\n');
      
      const blob = new Blob([csv], { type: 'text/csv' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `audit-logs-${new Date().toISOString().split('T')[0]}.csv`;
      a.click();
      window.URL.revokeObjectURL(url);
    };

    const prevPage = () => {
      if (currentPage.value > 0) {
        currentPage.value--;
        fetchAuditLogs();
      }
    };

    const nextPage = () => {
      if (currentPage.value < totalPages.value - 1) {
        currentPage.value++;
        fetchAuditLogs();
      }
    };

    const goToPage = (page: number) => {
      currentPage.value = page;
      fetchAuditLogs();
    };

    // Watch for filter changes
    watch([() => filters.eventType, () => filters.severity], () => {
      currentPage.value = 0;
      fetchAuditLogs();
    });

    onMounted(() => {
      setTimePeriod('today');
    });

    return {
      auditEvents,
      eventTypes,
      loading,
      error,
      totalEvents,
      currentPage,
      totalPages,
      startIndex,
      endIndex,
      displayedPages,
      timePeriods,
      selectedPeriod,
      filters,
      formatAction,
      formatDate,
      getSeverityClass,
      getSeverityIconClass,
      getSeverityIcon,
      getSeverityBadgeClass,
      setTimePeriod,
      clearFilters,
      exportLogs,
      fetchAuditLogs,
      prevPage,
      nextPage,
      goToPage
    };
  }
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