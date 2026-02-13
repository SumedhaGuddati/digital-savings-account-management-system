<template>
  <div class="space-y-4 sm:space-y-6">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3 sm:gap-0">
      <h1 class="text-xl sm:text-2xl font-bold text-[#312E81]">
        Pending Applications
      </h1>
      <button 
        @click="loadApplications" 
        class="w-full sm:w-auto px-4 py-2 bg-[#312E81] text-white rounded-lg text-sm font-medium hover:bg-[#312E81]/90 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2 transition-colors cursor-pointer"
        :disabled="isLoading"
      >
        <svg v-if="!isLoading" xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
        </svg>
        <span>{{ isLoading ? 'Loading...' : 'Refresh' }}</span>
      </button>
    </div>

    <!-- Loading State -->
    <div v-if="isLoading" class="flex flex-col items-center justify-center py-8 sm:py-12">
      <div class="animate-spin rounded-full h-10 w-10 sm:h-12 sm:w-12 border-b-2 border-[#312E81]"></div>
      <p class="mt-3 sm:mt-4 text-sm sm:text-base text-gray-500">Loading applications...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="bg-red-50 border border-red-200 text-red-700 px-3 sm:px-4 py-3 rounded-lg">
      <div class="flex items-center gap-2">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 sm:h-5 sm:w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <p class="text-sm sm:text-base font-medium">Error loading applications</p>
      </div>
      <p class="text-xs sm:text-sm mt-1">{{ error }}</p>
      <button 
        @click="loadApplications" 
        class="mt-2 text-xs sm:text-sm bg-red-100 px-2 sm:px-3 py-1 rounded-md hover:bg-red-200 transition-colors cursor-pointer"
      >
        Try Again
      </button>
    </div>

    <!-- Empty State -->
    <div v-else-if="applications.length === 0" class="bg-white p-8 sm:p-12 rounded-xl shadow border text-center">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 sm:h-16 sm:w-16 mx-auto text-[#312E81]" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
      </svg>
      <p class="text-gray-500 mt-3 sm:mt-4 text-base sm:text-lg font-medium">No pending applications</p>
      <p class="text-gray-400 text-xs sm:text-sm mt-1">All applications have been processed</p>
    </div>

    <!-- Applications List -->
    <div v-else class="space-y-3 sm:space-y-4">
      <div
        v-for="app in applications"
        :key="app.applicationId"
        class="bg-white p-4 sm:p-6 rounded-xl shadow border border-gray-300 hover:shadow-md transition-shadow"
      >
        <div class="flex flex-col sm:flex-row justify-between items-start gap-4 sm:gap-0">
          <!-- Application Info -->
          <div class="flex-1 w-full sm:w-auto">
            <div class="flex flex-col sm:flex-row sm:items-center gap-2 sm:gap-3 flex-wrap">
              <h2 class="font-bold text-base sm:text-lg text-[#312E81]">{{ app.applicantName }}</h2>
              <div class="flex flex-wrap gap-2">
                <span class="px-2 py-1 bg-yellow-100 text-yellow-800 text-xs rounded-full font-medium w-fit">
                  {{ app.status || 'PENDING' }}
                </span>
                <span v-if="app.accountType" class="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full w-fit">
                  {{ app.accountType }}
                </span>
              </div>
            </div>
            
            <p class="text-xs sm:text-sm text-gray-600 mt-2 flex items-center gap-1 break-all">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 sm:h-4 sm:w-4 text-[#312E81] flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
              </svg>
              <span class="truncate">{{ app.applicantEmail }}</span>
            </p>
            
            <div class="flex flex-col sm:flex-row items-start sm:items-center gap-2 sm:gap-4 mt-2 text-xs text-gray-500">
              <div class="flex items-center gap-1">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 sm:h-4 sm:w-4 text-[#312E81] flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                <span class="text-xs">Submitted: {{ formatDate(app.applicationDate) }}</span>
              </div>
              <div v-if="app.reviewDate" class="flex items-center gap-1">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 sm:h-4 sm:w-4 text-[#312E81] flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-5m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <span class="text-xs">Reviewed: {{ formatDate(app.reviewDate) }}</span>
              </div>
            </div>
          </div>

          <!-- Action Buttons -->
          <div class="flex flex-col sm:flex-row gap-2 w-full sm:w-auto">
            <button
              @click="handleApprove(app.applicationId)"
              :disabled="actionLoading[app.applicationId]"
              class="w-full sm:w-auto px-3 sm:px-4 py-2 bg-green-600 text-white rounded-lg text-xs sm:text-sm font-medium hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2 transition-colors min-w-[100px] sm:min-w-[80px] cursor-pointer"
            >
              <svg v-if="actionLoading[app.applicationId]" class="animate-spin h-3 w-3 sm:h-4 sm:w-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <span>{{ actionLoading[app.applicationId] ? '...' : 'Approve' }}</span>
            </button>

            <button
              @click="handleReject(app.applicationId)"
              :disabled="actionLoading[app.applicationId]"
              class="w-full sm:w-auto px-3 sm:px-4 py-2 bg-red-600 text-white rounded-lg text-xs sm:text-sm font-medium hover:bg-red-700 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2 transition-colors min-w-[100px] sm:min-w-[80px] cursor-pointer"
            >
              <svg v-if="actionLoading[app.applicationId]" class="animate-spin h-3 w-3 sm:h-4 sm:w-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <span>{{ actionLoading[app.applicationId] ? '...' : 'Reject' }}</span>
            </button>
          </div>
        </div>

        <!-- Success Message -->
        <div v-if="successMessage[app.applicationId]" class="mt-3 text-xs sm:text-sm text-green-700 bg-green-50 border border-green-200 p-2 sm:p-3 rounded-md">
          {{ successMessage[app.applicationId] }}
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { getPendingApplications, approveApplication, rejectApplication } from "@/api/endpoints/admin/admin.api";
import type { AdminApplicationViewResponse } from "@/types/adminTypes";

export default defineComponent({
  name: "PendingApplications",
  
  data() {
    return {
      applications: [] as AdminApplicationViewResponse[],
      isLoading: false,
      error: null as string | null,
      actionLoading: {} as Record<number, boolean>,
      successMessage: {} as Record<number, string>
    };
  },
  
  methods: {
    async loadApplications(): Promise<void> {
      this.isLoading = true;
      this.error = null;
      
      try {
        const data = await getPendingApplications();
        this.applications = data;
      } catch (err: unknown) {
        this.error = err instanceof Error ? err.message : 'Failed to load applications';
      } finally {
        this.isLoading = false;
      }
    },

    async handleApprove(id: number): Promise<void> {
      const applicationId = Number(id);
      
      if (isNaN(applicationId)) {
        alert('Invalid application ID');
        return;
      }
      
      // Use Vue.set pattern for reactive objects
      this.actionLoading = { ...this.actionLoading, [applicationId]: true };
      
      // Clear any existing success message
      const { [applicationId]: _, ...restSuccess } = this.successMessage;
      this.successMessage = restSuccess;
      
      try {
        const response = await approveApplication(applicationId, {
          remarks: `Approved by admin on ${new Date().toLocaleString()}`,
          reviewedBy: 'admin_user'
        });
        
        // Set success message
        this.successMessage = { 
          ...this.successMessage, 
          [applicationId]: `Application approved! Account #${response.accountNumber} created.` 
        };
        
        // Remove after delay
        setTimeout(() => {
          this.applications = this.applications.filter(app => app.applicationId !== applicationId);
          const { [applicationId]: _, ...updatedSuccess } = this.successMessage;
          this.successMessage = updatedSuccess;
        }, 2000);
        
      } catch (err: unknown) {
        alert(err instanceof Error ? err.message : 'Failed to approve application');
      } finally {
        // Remove loading state
        const { [applicationId]: _, ...restLoading } = this.actionLoading;
        this.actionLoading = restLoading;
      }
    },

    async handleReject(id: number): Promise<void> {
      const applicationId = Number(id);
      
      if (isNaN(applicationId)) {
        alert('Invalid application ID');
        return;
      }
      
      // Set loading state
      this.actionLoading = { ...this.actionLoading, [applicationId]: true };
      
      // Clear any existing success message
      const { [applicationId]: _, ...restSuccess } = this.successMessage;
      this.successMessage = restSuccess;
      
      try {
        await rejectApplication(applicationId, {
          remarks: `Rejected by admin on ${new Date().toLocaleString()}`,
          reviewedBy: 'admin_user'
        });
        
        // Set success message
        this.successMessage = { 
          ...this.successMessage, 
          [applicationId]: `Application rejected.` 
        };
        
        // Remove after delay
        setTimeout(() => {
          this.applications = this.applications.filter(app => app.applicationId !== applicationId);
          const { [applicationId]: _, ...updatedSuccess } = this.successMessage;
          this.successMessage = updatedSuccess;
        }, 2000);
        
      } catch (err: unknown) {
        alert(err instanceof Error ? err.message : 'Failed to reject application');
      } finally {
        // Remove loading state
        const { [applicationId]: _, ...restLoading } = this.actionLoading;
        this.actionLoading = restLoading;
      }
    },

    formatDate(dateString: string): string {
      if (!dateString) return 'N/A';
      try {
        const date = new Date(dateString);
        if (isNaN(date.getTime())) return dateString;
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
    }
  },
  
  mounted(): void {
    this.loadApplications();
  }
});
</script>

<style scoped>
.animate-spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>