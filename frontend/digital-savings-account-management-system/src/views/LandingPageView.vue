<!-- @author Khamitkar Sai Chandan -->
<template>
    <div class="min-h-screen bg-white">
        <AnnouncementBar />

        <HeroSection @apply="$router.push('/apply')" @login="$router.push('/login')" />

        <TrustBadges :badges="trustBadges" />

        <BenefitsSection :benefits="benefits" />

        <PlansSection :plans="accountPlans" />

        <FAQSection :faqs="faqs" />

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

        return {
            trustBadges,
            benefits,
            accountPlans,
            faqs,
            productLinks,
            companyLinks,
            supportLinks,
        }
    }
})
</script>
