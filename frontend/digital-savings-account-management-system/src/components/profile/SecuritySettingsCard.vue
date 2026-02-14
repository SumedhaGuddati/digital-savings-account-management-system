<!-- @author Khamitkar Sai Chandan -->
<template>
    <div class="bg-white rounded-2xl border border-gray-200/80 shadow-sm hover:shadow-md transition-shadow">
        <div class="p-6 border-b border-gray-100">
            <div class="flex items-center gap-3">
                <div class="w-10 h-10 bg-amber-100 rounded-xl flex items-center justify-center">
                    <FontAwesomeIcon :icon="faShield" class="text-amber-600" />
                </div>
                <div>
                    <h3 class="font-semibold text-gray-900">Security Settings</h3>
                    <p class="text-xs text-gray-500 mt-0.5">Manage your account security</p>
                </div>
            </div>
        </div>

        <div class="p-6">
            <div class="space-y-4">
                <div class="flex items-center justify-between p-3 hover:bg-gray-50 rounded-xl transition-colors">
                    <div class="flex items-center gap-3">
                        <div class="w-9 h-9 bg-gray-100 rounded-lg flex items-center justify-center">
                            <FontAwesomeIcon :icon="faLock" class="text-gray-600" />
                        </div>
                        <div>
                            <p class="font-medium text-gray-900">Password</p>
                        </div>
                    </div>
                    <button
                        @click="$store.dispatch('auth/logout'); $router.push('/login')"
                        class="px-4 py-2 text-sm font-medium text-indigo-600 hover:bg-indigo-50 rounded-lg transition-colors cursor-pointer">
                        Change
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faShield, faLock } from "@fortawesome/free-solid-svg-icons";

export default defineComponent({
    name: "SecuritySettingsCard",

    components: {
        FontAwesomeIcon
    },

    data() {
        return {
            faShield,
            faLock
        };
    }
});
</script>
