<template>
  <div class="flex flex-col justify-center items-center">
    <img alt="Vue logo" src="../assets/logo.png" />
    <HelloWorld msg="Welcome to Digital Savings Account Management System" />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import HelloWorld from "@/components/HelloWorld.vue";

export default defineComponent({
  name: "HomeView",
  components: {
    HelloWorld,
  },
});
</script>
