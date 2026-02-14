<!-- @author Karanam Hima Varsha -->
<template>
    <div class="px-8 py-6 border-b border-gray-100" :class="headerClass">
        <div class="flex flex-col sm:flex-row items-start sm:items-center sm:justify-between">
            <div class="flex items-start sm:items-center gap-4">
                <div class="w-14 h-14 rounded-full bg-white/20 backdrop-blur-sm flex items-center justify-center">
                    <FontAwesomeIcon :icon="statusIcon" class="text-2xl text-white" />
                </div>
                <div>
                    <div class="flex items-center gap-3">
                        <h2 class="text-xl font-semibold text-white">
                            Application {{ application.status }}
                        </h2>
                        <span
                            class="px-3 py-1 rounded-full text-xs font-medium bg-white/20 text-white border border-white/30">
                            {{ application.status }}
                        </span>
                    </div>
                    <p class="text-sm text-white/90 mt-1">
                        {{ statusDescription }}
                    </p>
                </div>
            </div>
            <div class="flex flex-row sm:flex-col items-center sm:items-end text-right gap-2 sm:gap-0 text-white mt-4 sm:mt-0">
                <p class="text-sm">Application ID</p>
                <p class="text-lg font-mono font-medium sm:mt-1">#{{ application.id }}</p>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import {
    faCheckCircle,
    faXmarkCircle,
    faHourglassHalf
} from "@fortawesome/free-solid-svg-icons";

import type { Application } from "@/types/app/application";

export default defineComponent({
    name: "StatusHeader",

    components: {
        FontAwesomeIcon
    },

    props: {
        application: {
            type: Object as PropType<Application>,
            required: true
        }
    },

    data() {
        return {
            faCheckCircle,
            faXmarkCircle,
            faHourglassHalf
        };
    },

    computed: {
        statusIcon() {
            const status = this.application.status;

            if (status === "APPROVED") return this.faCheckCircle;
            if (status === "REJECTED") return this.faXmarkCircle;
            return this.faHourglassHalf;
        },

        headerClass(): string {
            const status = this.application.status;

            if (status === "APPROVED")
                return "bg-gradient-to-r from-emerald-500 to-teal-500";

            if (status === "REJECTED")
                return "bg-gradient-to-r from-red-500 to-rose-500";

            return "bg-gradient-to-r from-amber-500 to-orange-500";
        },

        statusDescription(): string {
            const status = this.application.status;

            if (status === "APPROVED")
                return "Your savings account application has been approved. Welcome!";

            if (status === "REJECTED")
                return "Your application could not be approved at this time.";

            return "Your application is being reviewed. This usually takes 2–3 business days.";
        }
    }
});
</script>
