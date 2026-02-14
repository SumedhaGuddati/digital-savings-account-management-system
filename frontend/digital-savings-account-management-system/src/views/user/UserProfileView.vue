<template>
  <div class="min-h-screen bg-gradient-to-br from-gray-50 via-gray-50 to-gray-100">

    <AppLoader v-show="isLoading" />

    <div v-if="userProfile" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-10 lg:py-12"
      :class="{ 'pointer-events-none opacity-50': isLoading }">
      <ProfileHeader class="mb-6 sm:mb-8" />

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 lg:gap-8">

        <div class="lg:sticky lg:top-8 h-fit">
          <ProfileSidebarCard :userProfile="userProfile" />
        </div>

        <div class="lg:col-span-2 space-y-6 sm:space-y-8">
          <PersonalInfoCard :userProfile="userProfile" />

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
import AppLoader from '@/components/ui/loader/AppLoader.vue'

export default defineComponent({
  name: 'UserProfileView',
  components: {
    ProfileHeader,
    ProfileSidebarCard,
    PersonalInfoCard,
    AccountInfoCard,
    SecuritySettingsCard,
    AppLoader
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
