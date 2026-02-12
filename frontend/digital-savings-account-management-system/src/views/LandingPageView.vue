<template>
    <div class="min-h-screen bg-white">
        <AnnouncementBar />

        <HeroSection @apply="applyNow" @login="goToLogin" />

        <TrustBadges :badges="trustBadges" />

        <BenefitsSection :benefits="benefits" />

        <PlansSection :plans="accountPlans" @apply="applyForPlan" @compare="comparePlans" />

        <FAQSection :faqs="faqs" @contact="contactSupport" />

        <LandingFooter :productLinks="productLinks" :companyLinks="companyLinks" :supportLinks="supportLinks" />
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AnnouncementBar from '@/components/landing/AnnouncementBar.vue'
import HeroSection from '@/components/landing/HeroSection.vue'
import TrustBadges from '@/components/landing/TrustBadges.vue'
import BenefitsSection from '@/components/landing/BenefitsSection.vue'
import PlansSection from '@/components/landing/PlansSection.vue'
import FAQSection from '@/components/landing/FAQSection.vue'
import LandingFooter from '@/components/landing/LandingFooter.vue'
import { useLandingData } from '@/composables/landing/useLandingData'
import type { AccountPlan } from '@/types/app/landing'

export default defineComponent({
    name: 'LandingPageView',
    components: {
        AnnouncementBar,
        HeroSection,
        TrustBadges,
        BenefitsSection,
        PlansSection,
        FAQSection,
        LandingFooter
    },
    setup() {
        const {
            trustBadges,
            benefits,
            accountPlans,
            faqs,
            productLinks,
            companyLinks,
            supportLinks
        } = useLandingData()

        const applyNow = () => {
            console.log('Apply now clicked')
        }

        const goToLogin = () => {
            console.log('Navigate to login')
        }

        const applyForPlan = (plan: AccountPlan) => {
            console.log('Applying for plan:', plan.name)
        }

        const comparePlans = () => {
            console.log('Compare plans')
        }

        const contactSupport = () => {
            console.log('Contact support')
        }

        return {
            trustBadges,
            benefits,
            accountPlans,
            faqs,
            productLinks,
            companyLinks,
            supportLinks,
            applyNow,
            goToLogin,
            applyForPlan,
            comparePlans,
            contactSupport
        }
    }
})
</script>
