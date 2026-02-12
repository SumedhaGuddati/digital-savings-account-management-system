<template>
    <div class="bg-white rounded-2xl border border-gray-200/80 shadow-sm hover:shadow-md transition-shadow">
        <div class="p-6 border-b border-gray-100">
            <div class="flex items-center gap-3">
                <div class="w-10 h-10 bg-emerald-100 rounded-xl flex items-center justify-center">
                    <FontAwesomeIcon :icon="faBuilding" class="text-emerald-600" />
                </div>
                <div>
                    <h3 class="font-semibold text-gray-900">Account Information</h3>
                    <p class="text-xs text-gray-500 mt-0.5">Your savings account details</p>
                </div>
            </div>
        </div>

        <div class="p-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div class="space-y-4">
                    <InfoBox label="Account Number">
                        <p class="mt-1 text-lg font-mono font-bold text-gray-900">
                            {{ userProfile.accountNumber }}
                        </p>
                    </InfoBox>

                    <div class="flex justify-between items-center p-4 bg-gray-50 rounded-xl">
                        <div>
                            <label class="text-xs font-medium text-gray-500 uppercase tracking-wider">IFSC Code</label>
                            <p class="mt-1 font-mono text-gray-900">ZETA0001971</p>
                        </div>
                        <button @click="$emit('copy', 'ZETA0001971')"
                            class="p-2 hover:bg-white rounded-lg transition-colors">
                            <FontAwesomeIcon :icon="faCopy" class="text-gray-400 hover:text-indigo-600" />
                        </button>
                    </div>
                </div>

                <div class="space-y-4">
                    <InfoBox label="Account Type">
                        <p class="mt-1 text-lg font-bold text-gray-900">{{ userProfile.accountType }}</p>
                    </InfoBox>

                    <InfoBox label="Opened On">
                        <p class="mt-1 text-gray-900">{{ formatDateShort(userProfile.reviewDate) }}</p>
                    </InfoBox>
                </div>
            </div>

            <div class="grid grid-cols-2 gap-4 mt-4">
                <StatBox label="Current Balance" color="indigo">
                    ₹{{ userProfile.balance.toLocaleString('en-IN') }}
                </StatBox>

                <StatBox label="Interest Rate" color="amber">
                    6.5% p.a.
                </StatBox>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, h } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faBuilding, faCopy } from '@fortawesome/free-solid-svg-icons'
import { UserProfile } from '@/types/api/user'
import { formatDateShort } from '@/utils/dateFormatter'

const InfoBox = defineComponent({
    props: { label: { type: String, required: true } },
    setup(props, { slots }) {
        return () =>
            h(
                'div',
                { class: 'bg-gray-50 p-4 rounded-xl' },
                [
                    h(
                        'label',
                        { class: 'text-xs font-medium text-gray-500 uppercase tracking-wider' },
                        props.label
                    ),
                    slots.default?.()
                ]
            )
    }
})

const StatBox = defineComponent({
    props: {
        label: { type: String, required: true },
        color: { type: String, required: true }
    },
    setup(props, { slots }) {
        const bg =
            props.color === 'indigo'
                ? 'from-indigo-50 to-purple-50 text-indigo-600'
                : 'from-amber-50 to-orange-50 text-amber-600'

        return () =>
            h(
                'div',
                { class: `bg-gradient-to-br ${bg} p-4 rounded-xl` },
                [
                    h('p', { class: 'text-xs font-medium' }, props.label),
                    h('p', { class: 'text-2xl font-bold text-gray-900 mt-1' }, slots.default?.())
                ]
            )
    }
})

export default defineComponent({
    name: 'AccountInfoCard',
    components: { FontAwesomeIcon, InfoBox, StatBox },
    props: {
        userProfile: { type: Object as () => UserProfile, required: true }
    },
    emits: ['copy'],
    data() {
        return {
            faBuilding,
            faCopy,
            formatDateShort
        }
    }
})
</script>
