<template>
    <div class="relative w-full max-w-md aspect-[1.586/1] rounded-2xl p-6 text-white shadow-md overflow-hidden border border-white/10 bg-cover bg-center flex flex-col justify-between"
        :style="{ backgroundImage: `url(${cardBg})` }">
        <div class="absolute inset-0 bg-black/35"></div>

        <div class="relative flex justify-between items-center">
            <p class="text-xs font-medium tracking-wide text-white/80">
                Savings Account
            </p>

            <span
                class="px-3 py-1 text-[11px] rounded-full font-semibold uppercase bg-white/15 backdrop-blur border border-white/20 text-white">
                {{ theme.name }}
            </span>
        </div>

        <div class="relative space-y-4">
            <img src="@/assets/card-chip.png" alt="" class="w-12 h-9" />

            <div class="text-lg tracking-[0.28em] font-semibold text-white">
                •••• •••• •••• {{ last4Digits }}
            </div>
        </div>

        <div class="relative flex justify-between items-end">
            <div>
                <p class="text-[10px] uppercase tracking-wide text-white/70">
                    Account Holder
                </p>
                <p class="text-sm font-semibold tracking-wide text-white">
                    John Doe
                </p>
            </div>

            <p class="text-sm font-semibold tracking-wide text-white/90">
                VISA
            </p>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { accountThemes } from '@/theme/accountThemes'
import cardBg from '@/assets/card-bg.png'

export default defineComponent({
    name: 'AccountCard',
    props: {
        accountNumber: { type: String, required: true },
        tier: { type: String as () => 'ELITE' | 'PRIME' | 'SMART', required: true }
    },
    computed: {
        last4Digits(): string {
            return this.accountNumber.slice(-4)
        },
        theme(): typeof accountThemes.ELITE {
            return accountThemes[this.tier]
        },
        cardBg(): string {
            return cardBg
        }
    }
})
</script>
