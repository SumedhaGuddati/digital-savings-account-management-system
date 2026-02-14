<!-- @author Khamitkar Sai Chandan -->
<template>
    <div class="bg-white rounded-2xl border border-gray-200/80 shadow-sm p-5 mb-6">
        <div class="flex flex-col lg:flex-row gap-4">
            <!-- Search -->
            <div class="flex-1 relative">
                <FontAwesomeIcon :icon="faMagnifyingGlass"
                    class="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 text-sm" />
                <input :value="searchQuery"
                    @input="$emit('update:searchQuery', ($event.target as HTMLInputElement).value)" type="text"
                    placeholder="Search by description, amount, or ID..."
                    class="w-full pl-10 pr-4 py-3 border border-gray-200 rounded-xl focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100 outline-none transition-all text-sm" />
            </div>

            <!-- Filters -->
            <div class="flex gap-3">
                <!-- Date -->
                <div class="relative">
                    <select :value="dateFilter"
                        @change="$emit('update:dateFilter', ($event.target as HTMLSelectElement).value)"
                        class="appearance-none pl-4 pr-10 py-3 border border-gray-200 rounded-xl focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100 outline-none transition-all text-sm bg-white cursor-pointer">
                        <option value="all">All Time</option>
                        <option value="today">Today</option>
                        <option value="yesterday">Yesterday</option>
                        <option value="week">This Week</option>
                        <option value="month">This Month</option>
                        <option value="year">This Year</option>
                    </select>
                    <FontAwesomeIcon :icon="faChevronDown"
                        class="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 text-sm pointer-events-none" />
                </div>

                <!-- Type -->
                <div class="relative">
                    <select :value="typeFilter"
                        @change="$emit('update:typeFilter', ($event.target as HTMLSelectElement).value)"
                        class="appearance-none pl-4 pr-10 py-3 border border-gray-200 rounded-xl focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100 outline-none transition-all text-sm bg-white cursor-pointer">
                        <option value="all">All Types</option>
                        <option value="DEPOSIT">Deposits Only</option>
                        <option value="WITHDRAW">Withdrawals Only</option>
                    </select>
                    <FontAwesomeIcon :icon="faChevronDown"
                        class="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 text-sm pointer-events-none" />
                </div>
            </div>
        </div>

        <!-- Active Filters -->
        <div v-if="hasActiveFilters" class="flex flex-wrap items-center gap-2 mt-4 pt-4 border-t border-gray-100">
            <span class="text-xs text-gray-500 mr-1">Active filters:</span>

            <span v-if="searchQuery"
                class="inline-flex items-center gap-1.5 px-3 py-1.5 bg-indigo-50 text-indigo-700 text-xs rounded-full">
                Search: "{{ searchQuery }}"
                <button @click="$emit('update:searchQuery', '')" class="hover:text-indigo-900 cursor-pointer">
                    <FontAwesomeIcon :icon="faXmark" />
                </button>
            </span>

            <span v-if="dateFilter !== 'all'"
                class="inline-flex items-center gap-1.5 px-3 py-1.5 bg-indigo-50 text-indigo-700 text-xs rounded-full">
                {{ dateFilterLabel }}
                <button @click="$emit('update:dateFilter', 'all')" class="hover:text-indigo-900 cursor-pointer">
                    <FontAwesomeIcon :icon="faXmark" />
                </button>
            </span>

            <span v-if="typeFilter !== 'all'"
                class="inline-flex items-center gap-1.5 px-3 py-1.5 bg-indigo-50 text-indigo-700 text-xs rounded-full">
                {{ typeFilter === 'DEPOSIT' ? 'Deposits only' : 'Withdrawals only' }}
                <button @click="$emit('update:typeFilter', 'all')" class="hover:text-indigo-900 cursor-pointer">
                    <FontAwesomeIcon :icon="faXmark" />
                </button>
            </span>

            <button @click="$emit('clear')" class="text-xs text-gray-500 hover:text-gray-700 underline ml-2 cursor-pointer">
                Clear all
            </button>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import {
    faMagnifyingGlass,
    faChevronDown,
    faXmark
} from "@fortawesome/free-solid-svg-icons";

export default defineComponent({
    name: "TransactionsFilters",

    components: {
        FontAwesomeIcon
    },

    props: {
        searchQuery: {
            type: String,
            required: true
        },
        dateFilter: {
            type: String,
            required: true
        },
        typeFilter: {
            type: String,
            required: true
        }
    },

    emits: [
        "update:searchQuery",
        "update:dateFilter",
        "update:typeFilter",
        "clear"
    ],

    data() {
        return {
            faMagnifyingGlass,
            faChevronDown,
            faXmark
        };
    },

    computed: {
        hasActiveFilters(): boolean {
            return (
                !!this.searchQuery ||
                this.dateFilter !== "all" ||
                this.typeFilter !== "all"
            );
        },

        dateFilterLabel(): string {
            const labels: Record<string, string> = {
                today: "Today",
                yesterday: "Yesterday",
                week: "This week",
                month: "This month",
                year: "This year"
            };

            return labels[this.dateFilter] || this.dateFilter;
        }
    },

    methods: {
        updateSearchQuery(value: string) {
            this.$emit("update:searchQuery", value);
        },
        updateDateFilter(value: string) {
            this.$emit("update:dateFilter", value);
        },
        updateTypeFilter(value: string) {
            this.$emit("update:typeFilter", value);
        },
        clear() {
            this.$emit("clear");
        }
    }
});
</script>
