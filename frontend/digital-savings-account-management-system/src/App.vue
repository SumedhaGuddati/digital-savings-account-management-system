<template>
  <router-view />
  <Toaster />
</template>

<script lang="ts">
import { Toaster } from '@steveyuowo/vue-hot-toast';
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'App',
  components: { Toaster, },
});
</script>

<style>
body,
html {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  overflow-x: hidden;
  background-color: #ffffff;
}
</style>