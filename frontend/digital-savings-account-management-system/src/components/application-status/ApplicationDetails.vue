<!-- @author Karanam Hima Varsha -->
<template>
    <div class="p-8">
        <h3 class="font-semibold text-gray-900 mb-6 flex items-center gap-2">
            <FontAwesomeIcon :icon="faFileLines" class="text-indigo-600" />
            Application Details
        </h3>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- Left -->
            <div class="space-y-4">
                <DetailRow label="Account Type">
                    <span class="bg-gray-100 px-3 py-1 rounded-full font-medium">
                        {{ application.accountType }}
                    </span>
                </DetailRow>

                <DetailRow label="Applied On">{{ formatDate(application.applicationDate) }}</DetailRow>
                <DetailRow label="Initial Deposit">₹25,000 (Estimated)</DetailRow>

                <DetailRow label="Email Verification">
                    <span class="text-emerald-600 bg-emerald-50 px-3 py-1 rounded-full font-medium">
                        Verified
                    </span>
                </DetailRow>
            </div>

            <!-- Right -->
            <div class="space-y-4">
                <DetailRow label="Reviewer">{{ application.reviewedBy || 'Not assigned yet' }}</DetailRow>

                <DetailRow label="Review Date">
                    {{ application.reviewDate ? formatDate(application.reviewDate) : 'Pending review' }}
                </DetailRow>

                <DetailRow label="Processing Time">{{ processingTime }}</DetailRow>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faFileLines } from "@fortawesome/free-solid-svg-icons";
import type { Application } from "@/types/app/application";
import DetailRow from "./DetailRow.vue";

export default defineComponent({
    name: "ApplicationDetails",

    components: {
        FontAwesomeIcon,
        DetailRow
    },

    props: {
        application: {
            type: Object as PropType<Application>,
            required: true
        }
    },

    data() {
        return {
            faFileLines
        };
    },

    computed: {
        processingTime(): string {
            const start = new Date(this.application.applicationDate);

            const diff = Math.ceil(
                (Date.now() - start.getTime()) / (1000 * 60 * 60 * 24)
            );

            return `${diff} day${diff > 1 ? "s" : ""}`;
        }
    },

    methods: {
        formatDate(d: string): string {
            return new Date(d).toLocaleDateString("en-IN", {
                day: "numeric",
                month: "long",
                year: "numeric"
            });
        }
    }
});
</script>
