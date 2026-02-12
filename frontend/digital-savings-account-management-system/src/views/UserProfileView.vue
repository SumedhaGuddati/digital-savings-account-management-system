<template>
  <div class="min-h-screen bg-gradient-to-br from-gray-50 via-gray-50 to-gray-100">

    <!-- Loading -->
    <div v-if="isLoading" class="bg-white rounded-2xl border border-gray-200 shadow-sm p-12">
      <div class="flex flex-col items-center justify-center">
        <div class="w-16 h-16 border-4 border-gray-200 border-t-indigo-600 rounded-full animate-spin"></div>
        <p class="mt-4 text-gray-600">Fetching application details...</p>
      </div>
    </div>

    <div v-else-if="userProfile" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 sm:py-12">
      <ProfileHeader />

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 lg:gap-8">
        <!-- Left -->
        <ProfileSidebarCard :userProfile="userProfile" />

        <!-- Right -->
        <div class="lg:col-span-2 space-y-6">
          <PersonalInfoCard :userProfile="userProfile" />

          <AddressInfoCard :userProfile="userProfile"/>

          <AccountInfoCard :userProfile="userProfile" @copy="copyToClipboard" />

          <SecuritySettingsCard />
        </div>
      </div>
    </div>
    <Toaster />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import ProfileHeader from '@/components/profile/ProfileHeader.vue'
import ProfileSidebarCard from '@/components/profile/ProfileSidebarCard.vue'
import PersonalInfoCard from '@/components/profile/PersonalInfoCard.vue'
import AccountInfoCard from '@/components/profile/AccountInfoCard.vue'
import SecuritySettingsCard from '@/components/profile/SecuritySettingsCard.vue'
import { UserProfile } from '@/types/api/user'
import { toast, Toaster } from '@steveyuowo/vue-hot-toast';
import "@steveyuowo/vue-hot-toast/vue-hot-toast.css";
import { getUserProfileDetails } from '@/api/endpoints/users/users.api'

export default defineComponent({
  name: 'UserProfileView',
  components: {
    ProfileHeader,
    ProfileSidebarCard,
    PersonalInfoCard,
    AccountInfoCard,
    SecuritySettingsCard
  },

  data() {
    return {
      isLoading: false,
      userProfile: null as UserProfile | null,
    }
  },

  methods: {
    copyToClipboard(text: string) {
      navigator.clipboard.writeText(text).then(
        () => toast.success('Copied to clipboard!'),
        () => toast.error('Failed to copy!')
      )
    },

    async fetchUserProfile() {
      try {
        this.isLoading = true;
        const response = await getUserProfileDetails()
        this.userProfile = response.data;
      } catch (error) {
        console.error('Error fetching user profile:', error)
        toast.error('Failed to load profile. Please try again later.')
      }
      finally {
        this.isLoading = false;
      }
    }
  },

  mounted() {
    this.fetchUserProfile()
  }
})
</script>
