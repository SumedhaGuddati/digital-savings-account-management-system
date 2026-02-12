<template>
    <div class="bg-white rounded-2xl border border-gray-200/80 shadow-sm hover:shadow-md transition-shadow">
        <div class="p-6 border-b border-gray-100">
            <div class="flex items-center gap-3">
                <div class="w-10 h-10 bg-indigo-100 rounded-xl flex items-center justify-center">
                    <FontAwesomeIcon :icon="faUser" class="text-indigo-600" />
                </div>
                <div>
                    <h3 class="font-semibold text-gray-900">Personal Information</h3>
                    <p class="text-xs text-gray-500 mt-0.5">Your basic personal details</p>
                </div>
            </div>
        </div>

        <div class="p-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <!-- Left -->
                <div class="space-y-4">
                    <InfoField label="Full Name" :value="userProfile.accountHolderName" />
                    <InfoField label="Date of Birth" :value="formatDateShort(userProfile.dob)" />
                    <InfoField label="Occupation" :value="userProfile.occupation" />
                </div>

                <!-- Right -->
                <div class="space-y-4">
                    <InfoField label="Email Address" :value="`test@example.com`" />
                    <InfoField label="Income Source" :value="userProfile.incomeSource" />
                    <InfoField label="Annual Income" :value="userProfile.annualIncome ? `₹${userProfile.annualIncome.toLocaleString()}` : 'N/A'" />
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, h } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faUser } from '@fortawesome/free-solid-svg-icons'
import { UserProfile } from '@/types/api/user'
import { formatDateShort } from '@/utils/dateFormatter'

const InfoField = defineComponent({
    props: {
        label: { type: String, required: true },
        value: { type: [String, Number], default: null }
    },
    setup(props, { slots }) {
        return () =>
            h('div', [
                h('label', { class: 'text-xs font-medium text-gray-500 uppercase tracking-wider' }, props.label),
                h('div', { class: 'mt-1 text-gray-900 font-medium' }, props.value ?? slots.default?.())
            ])
    }
})

export default defineComponent({
    name: 'PersonalInfoCard',
    components: { FontAwesomeIcon, InfoField },
    props: {
        userProfile: { type: Object, required: true },
    },
    data() {
        return {
            faUser,
            formatDateShort
        }
    }
})
</script>
