<!-- @author Bharadi Harshitha -->
<template>
  <div class="space-y-4 sm:space-y-6">
    <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3 sm:gap-0">
      <div>
        <h1 class="text-xl sm:text-2xl font-bold text-primary-indigo">Audit Logs</h1>
        <p class="text-xs sm:text-sm text-slate-muted mt-1">System activity and security events from applications, users, accounts, and transactions</p>
      </div>
      <div class="flex flex-col sm:flex-row gap-2 sm:gap-3 w-full sm:w-auto">
        <button @click="exportLogs" class="w-full sm:w-auto px-3 sm:px-4 py-2 border border-slate-200 rounded-lg text-xs font-bold text-slate-600 hover:bg-slate-50 flex items-center justify-center gap-2 cursor-pointer">
          <span class="material-icons-round text-sm">download</span>
          Export Logs
        </button>
        <button @click="clearFilters" class="w-full sm:w-auto px-3 sm:px-4 py-2 bg-primary-indigo text-white rounded-lg text-xs font-bold hover:bg-indigo-900 cursor-pointer">
          Clear Filters
        </button>
      </div>
    </div>

    <!-- Date Range Picker -->
    <div class="bg-white rounded-xl border border-border-soft p-4 sm:p-5">
      <div class="flex flex-col lg:flex-row gap-3 lg:gap-4 items-start lg:items-center">
        <span class="text-xs font-bold text-slate-muted uppercase">Time Period:</span>
        <div class="flex flex-wrap gap-2">
          <button 
            v-for="period in timePeriods" 
            :key="period.value"
            @click="setTimePeriod(period.value)"
            :class="selectedPeriod === period.value ? 'bg-primary-indigo text-white cursor-pointer' : 'border border-slate-200 text-slate-600 hover:bg-slate-50 cursor-pointer'"
            class="px-3 sm:px-4 py-2 rounded-lg text-xs font-bold transition-all">
            {{ period.label }}
          </button>
        </div>
        
        <div class="flex flex-col sm:flex-row gap-2 w-full lg:w-auto lg:ml-auto">
          <select v-model="filters.eventType" @change="fetchAuditLogs" class="w-full sm:w-auto px-3 py-2 border border-slate-200 rounded-lg text-xs bg-white min-w-[150px] cursor-pointer">
            <option value="">All Event Types</option>
            <option v-for="type in eventTypes" :key="type" :value="type">{{ formatAction(type) }}</option>
          </select>
          <select v-model="filters.severity" @change="fetchAuditLogs" class="w-full sm:w-auto px-3 py-2 border border-slate-200 rounded-lg text-xs bg-white min-w-[120px] cursor-pointer">
            <option value="">All Severities</option>
            <option value="INFO">Info</option>
            <option value="WARNING">Warning</option>
            <option value="CRITICAL">Critical</option>
          </select>
        </div>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="bg-white rounded-2xl border border-border-soft shadow-sm p-8 sm:p-16 flex justify-center">
      <div class="flex flex-col items-center gap-3">
        <div class="w-8 h-8 sm:w-10 sm:h-10 border-4 border-indigo-200 border-t-primary-indigo rounded-full animate-spin"></div>
        <p class="text-xs sm:text-sm text-slate-muted">Loading audit logs from database...</p>
      </div>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="bg-white rounded-2xl border border-border-soft shadow-sm p-8 sm:p-16 flex justify-center">
      <div class="flex flex-col items-center gap-3">
        <span class="material-icons-round text-3xl sm:text-4xl text-coral-red">error_outline</span>
        <p class="text-xs sm:text-sm text-slate-muted text-center">{{ error }}</p>
        <button @click="fetchAuditLogs" class="px-3 sm:px-4 py-2 text-xs font-bold text-primary-indigo bg-indigo-50 rounded-lg hover:bg-indigo-100 cursor-pointer">
          Try Again
        </button>
      </div>
    </div>

    <!-- Audit Log Timeline -->
    <div v-else class="bg-white rounded-2xl border border-border-soft shadow-sm overflow-hidden">
      <div class="px-4 sm:px-6 py-3 sm:py-4 border-b border-border-soft bg-slate-50">
        <div class="flex flex-col sm:flex-row items-start sm:items-center gap-2 sm:gap-4">
          <span class="text-xs font-bold text-primary-indigo">Event Timeline</span>
          <span class="text-[10px] text-slate-muted">Showing {{ auditEvents.length }} of {{ totalEvents }} events</span>
        </div>
      </div>

      <div v-if="auditEvents.length === 0" class="px-4 sm:px-6 py-12 sm:py-16 flex flex-col items-center">
        <span class="material-icons-round text-3xl sm:text-4xl text-slate-300 mb-3">receipt_long</span>
        <p class="text-sm text-slate-muted">No audit events found</p>
        <p class="text-xs text-slate-400 mt-1">Try adjusting your filters</p>
      </div>

      <div v-else class="divide-y divide-border-soft">
        <div v-for="event in auditEvents" :key="`${event.entityType}-${event.entityId}-${event.timestamp}`" class="px-4 sm:px-6 py-3 sm:py-4 hover:bg-slate-50 transition-all">
          <div class="flex flex-col sm:flex-row items-start gap-3 sm:gap-4">
            <div :class="getSeverityClass(event.severity)" class="w-6 h-6 sm:w-8 sm:h-8 rounded-full flex items-center justify-center flex-shrink-0">
              <span :class="getSeverityIconClass(event.severity)" class="material-icons-round text-xs sm:text-sm">
                {{ getSeverityIcon(event.severity) }}
              </span>
            </div>
            <div class="flex-1 w-full sm:w-auto">
              <div class="flex flex-col sm:flex-row justify-between items-start gap-2 sm:gap-0">
                <div class="w-full sm:w-auto">
                  <p class="text-sm font-bold text-primary-indigo">
                    {{ formatAction(event.action) }}
                  </p>
                  <p class="text-xs text-slate-muted mt-0.5 break-words">
                    {{ event.details }}
                  </p>
                  <div class="flex flex-wrap items-center gap-2 mt-1">
                    <span class="text-[10px] px-2 py-0.5 bg-slate-100 rounded-full text-slate-600">
                      {{ event.entityType }}
                    </span>
                    <span class="text-[10px] text-slate-400">
                      ID: {{ event.entityId }}
                    </span>
                  </div>
                </div>
                <span :class="getSeverityBadgeClass(event.severity)" class="px-2 py-1 rounded-full text-[10px] font-bold self-start sm:self-auto">
                  {{ event.severity }}
                </span>
              </div>
              <div class="flex flex-col xs:flex-row gap-2 sm:gap-4 mt-2">
                <span class="text-[10px] text-slate-400 flex items-center gap-1">
                  <span class="material-icons-round text-[12px]">schedule</span>
                  {{ formatDate(event.timestamp) }}
                </span>
                <span class="text-[10px] text-slate-400 flex items-center gap-1">
                  <span class="material-icons-round text-[12px]">person</span>
                  <span class="truncate max-w-[150px] sm:max-w-[200px]">{{ event.username || event.userEmail || 'System' }}</span>
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Pagination -->
      <div class="px-4 sm:px-6 py-3 sm:py-4 border-t border-border-soft bg-slate-50 flex flex-col sm:flex-row justify-between items-center gap-3 sm:gap-0">
        <p class="text-xs text-slate-muted order-2 sm:order-1">
          Showing {{ startIndex + 1 }}-{{ endIndex }} of {{ totalEvents }} events
        </p>
        <div class="flex gap-2 order-1 sm:order-2">
          <button 
            @click="prevPage" 
            :disabled="currentPage === 0"
            class="w-7 h-7 sm:w-8 sm:h-8 flex items-center justify-center rounded border border-slate-200 bg-white text-slate-600 hover:bg-slate-50 disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer">
            <span class="material-icons-round text-sm">chevron_left</span>
          </button>
          <button 
            v-for="page in displayedPages" 
            :key="page"
            @click="goToPage(page)"
            :class="currentPage === page ? 'bg-primary-indigo text-white cursor-pointer' : 'border border-slate-200 bg-white text-slate-600 hover:bg-slate-50 cursor-pointer'"
            class="w-7 h-7 sm:w-8 sm:h-8 flex items-center justify-center rounded text-xs font-bold">
            {{ page + 1 }}
          </button>
          <button 
            @click="nextPage" 
            :disabled="currentPage >= totalPages - 1"
            class="w-7 h-7 sm:w-8 sm:h-8 flex items-center justify-center rounded border border-slate-200 bg-white text-slate-600 hover:bg-slate-50 disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer">
            <span class="material-icons-round text-sm">chevron_right</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { getAuditLogs, getAuditLogsCount, getEventTypes } from "@/api/endpoints/admin/admin.api";
import type { AuditEventResponse } from "@/types/auditTypes";

export default defineComponent({
  name: "AuditLogs",
  
  data() {
    return {
      auditEvents: [] as AuditEventResponse[],
      eventTypes: [] as string[],
      loading: true,
      error: null as string | null,
      totalEvents: 0,
      currentPage: 0,
      pageSize: 10,
      selectedPeriod: 'today',
      filters: {
        startDate: null as string | null,
        endDate: null as string | null,
        eventType: '',
        severity: ''
      },
      timePeriods: [
        { label: 'Today', value: 'today' },
        { label: 'Yesterday', value: 'yesterday' },
        { label: 'Last 7 Days', value: 'last7' },
        { label: 'This Month', value: 'thisMonth' },
        { label: 'Custom Range', value: 'custom' }
      ]
    };
  },

  computed: {
    totalPages(): number {
      return Math.ceil(this.totalEvents / this.pageSize);
    },
    
    startIndex(): number {
      return this.currentPage * this.pageSize;
    },
    
    endIndex(): number {
      return Math.min(this.startIndex + this.pageSize, this.totalEvents);
    },
    
    displayedPages(): number[] {
      const pages = [];
      const maxVisible = 5;
      let start = Math.max(0, this.currentPage - Math.floor(maxVisible / 2));
      let end = Math.min(this.totalPages, start + maxVisible);
      
      if (end - start < maxVisible) {
        start = Math.max(0, end - maxVisible);
      }
      
      for (let i = start; i < end; i++) {
        pages.push(i);
      }
      return pages;
    }
  },

  methods: {
    formatAction(action: string): string {
      return action
        .toLowerCase()
        .replace(/_/g, ' ')
        .replace(/\b\w/g, l => l.toUpperCase());
    },

    formatDate(dateString: string): string {
      const date = new Date(dateString);
      return date.toLocaleString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      });
    },

    getSeverityClass(severity: string): string {
      switch (severity) {
        case 'CRITICAL': return 'bg-red-100';
        case 'WARNING': return 'bg-amber-100';
        default: return 'bg-emerald-100';
      }
    },

    getSeverityIconClass(severity: string): string {
      switch (severity) {
        case 'CRITICAL': return 'text-coral-red';
        case 'WARNING': return 'text-amber-glow';
        default: return 'text-success-green';
      }
    },

    getSeverityIcon(severity: string): string {
      switch (severity) {
        case 'CRITICAL': return 'error';
        case 'WARNING': return 'warning';
        default: return 'check_circle';
      }
    },

    getSeverityBadgeClass(severity: string): string {
      switch (severity) {
        case 'CRITICAL': return 'bg-red-50 text-coral-red';
        case 'WARNING': return 'bg-amber-50 text-amber-glow';
        default: return 'bg-emerald-50 text-success-green';
      }
    },

    setTimePeriod(period: string) {
      this.selectedPeriod = period;
      const now = new Date();
      const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
      
      switch (period) {
        case 'today':
          this.filters.startDate = today.toISOString();
          this.filters.endDate = new Date(today.getTime() + 86400000).toISOString();
          break;
        case 'yesterday':
          const yesterday = new Date(today.getTime() - 86400000);
          this.filters.startDate = yesterday.toISOString();
          this.filters.endDate = today.toISOString();
          break;
        case 'last7':
          const last7 = new Date(today.getTime() - 7 * 86400000);
          this.filters.startDate = last7.toISOString();
          this.filters.endDate = new Date(today.getTime() + 86400000).toISOString();
          break;
        case 'thisMonth':
          const firstDay = new Date(now.getFullYear(), now.getMonth(), 1);
          const lastDay = new Date(now.getFullYear(), now.getMonth() + 1, 1);
          this.filters.startDate = firstDay.toISOString();
          this.filters.endDate = lastDay.toISOString();
          break;
        case 'custom':
          break;
      }
      
      this.fetchAuditLogs();
    },

    async fetchAuditLogs() {
      this.loading = true;
      this.error = null;
      
      try {
        const [logs, count, types] = await Promise.all([
          getAuditLogs(
            this.filters.startDate || undefined,
            this.filters.endDate || undefined,
            this.filters.eventType || undefined,
            this.filters.severity || undefined,
            this.currentPage,
            this.pageSize
          ),
          getAuditLogsCount(
            this.filters.startDate || undefined,
            this.filters.endDate || undefined,
            this.filters.eventType || undefined,
            this.filters.severity || undefined
          ),
          getEventTypes()
        ]);
        
        this.auditEvents = logs;
        this.totalEvents = count;
        this.eventTypes = types;
      } catch (err) {
        this.error = 'Failed to load audit logs from database. Please try again.';
      } finally {
        this.loading = false;
      }
    },

    clearFilters() {
      this.filters.startDate = null;
      this.filters.endDate = null;
      this.filters.eventType = '';
      this.filters.severity = '';
      this.selectedPeriod = 'today';
      this.currentPage = 0;
      this.fetchAuditLogs();
    },

    exportLogs() {
      const csv = [
        ['Timestamp', 'User', 'Action', 'Entity Type', 'Entity ID', 'Details', 'Severity'].join(','),
        ...this.auditEvents.map(e => [
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
    },

    prevPage() {
      if (this.currentPage > 0) {
        this.currentPage--;
        this.fetchAuditLogs();
      }
    },

    nextPage() {
      if (this.currentPage < this.totalPages - 1) {
        this.currentPage++;
        this.fetchAuditLogs();
      }
    },

    goToPage(page: number) {
      this.currentPage = page;
      this.fetchAuditLogs();
    }
  },

  watch: {
    'filters.eventType': {
      handler() {
        this.currentPage = 0;
        this.fetchAuditLogs();
      }
    },
    'filters.severity': {
      handler() {
        this.currentPage = 0;
        this.fetchAuditLogs();
      }
    }
  },

  mounted() {
    this.setTimePeriod('today');
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