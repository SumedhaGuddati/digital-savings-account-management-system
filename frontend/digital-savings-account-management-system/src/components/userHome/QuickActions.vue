<!-- @author Khamitkar Sai Chandan -->
<template>
    <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
        <button v-for="action in actions" :key="action.label" @click="$emit(action.emit as any)" class="group relative p-5 bg-white rounded-xl border border-gray-200
             transition-all duration-200 active:scale-[0.98]
             focus:outline-none focus:ring-2 focus:ring-offset-2
             cursor-pointer transform-gpu" :class="action.buttonClasses">

            <!-- Ripple Background -->
            <span class="absolute inset-0 rounded-xl transition-all duration-500" :class="action.rippleClasses"></span>

            <!-- Icon -->
            <div class="relative">

                <div class="relative w-12 h-12 rounded-xl flex items-center justify-center mb-3
                 transition-all duration-150 shadow-md" :class="action.iconWrapperClasses">
                    <FontAwesomeIcon :icon="action.icon" class="text-2xl transition-all duration-150"
                        :class="action.iconClasses" />
                </div>
            </div>

            <!-- Text -->
            <div class="relative">
                <h3 class="font-semibold text-gray-900 transition-colors" :class="action.titleClasses">
                    {{ action.label }}
                </h3>

                <p class="text-xs text-gray-500 mt-1 transition-colors" :class="action.subtitleClasses">
                    {{ action.subtitle }}
                </p>
            </div>

            <!-- Bottom Line -->
            <span class="absolute bottom-0 left-1/2 -translate-x-1/2 h-0.5
               group-hover:w-12 group-active:w-16 transition-all duration-300 rounded-full"
                :class="action.bottomLineClasses"></span>
        </button>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import {
    faCirclePlus,
    faWallet,
    faFileInvoice,
} from "@fortawesome/free-solid-svg-icons";

export default defineComponent({
    name: "QuickActions",
    components: { FontAwesomeIcon },
    emits: ["deposit", "withdraw", "statements"],

    data() {
        return {
            faCirclePlus,
            faWallet,
            faFileInvoice,

            actions: [
                {
                    label: "Deposit",
                    subtitle: "Add money instantly",
                    emit: "deposit",
                    icon: faCirclePlus,

                    buttonClasses:
                        "hover:border-emerald-300 hover:shadow-lg hover:shadow-emerald-100/50 active:bg-emerald-50/30 focus:ring-emerald-500/50",

                    rippleClasses:
                        "bg-gradient-to-br from-emerald-500/0 via-emerald-500/0 to-emerald-500/0 group-hover:from-emerald-500/5 group-active:from-emerald-500/10",

                    iconWrapperClasses:
                        "bg-gradient-to-br from-emerald-100 to-emerald-50 group-hover:from-emerald-200 group-active:from-emerald-300 group-hover:shadow-emerald-200/50",

                    iconClasses:
                        "text-emerald-600 group-active:text-emerald-700 group-active:scale-90",

                    titleClasses: "group-active:text-emerald-700",

                    subtitleClasses:
                        "group-hover:text-gray-600 group-active:text-emerald-600/70",

                    bottomLineClasses: "bg-emerald-500",
                },

                {
                    label: "Withdraw",
                    subtitle: "Withdraw securely",
                    emit: "withdraw",
                    icon: faWallet,

                    buttonClasses:
                        "hover:border-red-300 hover:shadow-lg hover:shadow-red-100/50 active:bg-red-50/30 focus:ring-red-500/50",

                    rippleClasses:
                        "bg-gradient-to-br from-red-500/0 via-red-500/0 to-red-500/0 group-hover:from-red-500/5 group-active:from-red-500/10",

                    iconWrapperClasses:
                        "bg-gradient-to-br from-red-100 to-red-50 group-hover:from-red-200 group-active:from-red-300 group-hover:shadow-red-200/50",

                    iconClasses:
                        "text-red-500 group-active:text-red-700 group-active:scale-90",

                    titleClasses: "group-active:text-red-700",

                    subtitleClasses:
                        "group-hover:text-gray-600 group-active:text-red-600/70",

                    bottomLineClasses: "bg-red-500",
                },

                {
                    label: "Statements",
                    subtitle: "View e-statements",
                    emit: "statements",
                    icon: faFileInvoice,

                    buttonClasses:
                        "hover:border-purple-300 hover:shadow-lg hover:shadow-purple-100/50 active:bg-purple-50/30 focus:ring-purple-500/50",

                    rippleClasses:
                        "bg-gradient-to-br from-purple-500/0 via-purple-500/0 to-purple-500/0 group-hover:from-purple-500/5 group-active:from-purple-500/10",

                    iconWrapperClasses:
                        "bg-gradient-to-br from-purple-100 to-purple-50 group-hover:from-purple-200 group-active:from-purple-300 group-hover:shadow-purple-200/50",

                    iconClasses:
                        "text-purple-600 group-active:text-purple-700 group-active:scale-90",

                    titleClasses: "group-active:text-purple-700",

                    subtitleClasses:
                        "group-hover:text-gray-600 group-active:text-purple-600/70",

                    bottomLineClasses: "bg-purple-500",
                },
            ]
        };
    }
});
</script>
