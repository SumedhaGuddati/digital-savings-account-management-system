<!-- @author Khamitkar Sai Chandan -->
<template>
    <div class="text-2xl font-bold tracking-tight" @click="$router.push('/')">
        Zeta<span class="text-indigo-600">rise</span>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
    name: 'AppLogo',
});
</script>