<template>
    <div class="flex items-center justify-between mb-8">
        <div class="flex items-center gap-4">
            <button @click="$router.back()" class="p-2.5 hover:bg-white rounded-xl transition-all group">
                <FontAwesomeIcon :icon="faArrowLeft" class="text-gray-500 group-hover:text-indigo-600 text-lg" />
            </button>
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Profile</h1>
                <p class="text-sm text-gray-500 mt-1">Manage your personal information and security</p>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faArrowLeft, faPenToSquare, faSave } from '@fortawesome/free-solid-svg-icons'

export default defineComponent({
    name: 'ProfileHeader',
    components: { FontAwesomeIcon },
    data() {
        return { faArrowLeft, faPenToSquare, faSave }
    }
})
</script>
