<script setup>
import { reactive, defineExpose } from "vue";

defineOptions({
  name: "SignupModule",
});

const formData = reactive({
  name: "",
  email: "",
  password: "",
  confirmPassword: "",
});

// Expose form data to parent component
defineExpose({ formData });
</script>

<template>
  <div class="w-full py-5 animate-fade-in">
    <!-- Section Header with Indigo Bar -->
    <div class="flex items-center gap-3 mb-8">
      <div class="w-1 h-[18px] bg-[#6366f1] rounded-full"></div>
      <h3
        class="text-[0.75rem] font-extrabold text-[#94a3b8] uppercase tracking-[0.1em]"
      >
        Security Credentials
      </h3>
    </div>

    <!-- 2-Column Grid -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 md:gap-8">
      <!-- Full Name -->
      <div class="flex flex-col gap-2">
        <label class="text-[0.7rem] font-bold text-[#475569] uppercase">
          Full Legal Name <span class="text-red-500">*</span>
        </label>
        <input
          v-model="formData.name"
          type="text"
          placeholder="John Doe"
          class="w-full px-3.5 py-2.5 bg-[#f8fafc] border border-[#e2e8f0] rounded-lg text-[0.9rem] text-[#1e293b] focus:outline-none focus:border-[#6366f1] focus:bg-white transition-colors"
          required
        />
      </div>

      <!-- Email Address -->
      <div class="flex flex-col gap-2">
        <label class="text-[0.7rem] font-bold text-[#475569] uppercase">
          Email Address <span class="text-red-500">*</span>
        </label>
        <input
          v-model="formData.email"
          type="email"
          placeholder="name@zeta.com"
          class="w-full px-3.5 py-2.5 bg-[#f8fafc] border border-[#e2e8f0] rounded-lg text-[0.9rem] text-[#1e293b] focus:outline-none focus:border-[#6366f1] focus:bg-white transition-colors"
          required
        />
      </div>

      <!-- Password -->
      <div class="flex flex-col gap-2">
        <label class="text-[0.7rem] font-bold text-[#475569] uppercase">
          Password <span class="text-red-500">*</span>
        </label>
        <input
          v-model="formData.password"
          type="password"
          placeholder="••••••••"
          class="w-full px-3.5 py-2.5 bg-[#f8fafc] border border-[#e2e8f0] rounded-lg text-[0.9rem] text-[#1e293b] focus:outline-none focus:border-[#6366f1] focus:bg-white transition-colors"
          required
        />
        <p class="text-[0.65rem] text-[#64748b] mt-1">
          Min 8 chars, uppercase, lowercase, number & special char.
        </p>
      </div>

      <!-- Confirm Password -->
      <div class="flex flex-col gap-2">
        <label class="text-[0.7rem] font-bold text-[#475569] uppercase">
          Confirm Password <span class="text-red-500">*</span>
        </label>
        <input
          v-model="formData.confirmPassword"
          type="password"
          placeholder="••••••••"
          class="w-full px-3.5 py-2.5 bg-[#f8fafc] border border-[#e2e8f0] rounded-lg text-[0.9rem] text-[#1e293b] focus:outline-none focus:border-[#6366f1] focus:bg-white transition-colors"
          required
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in {
  animation: fadeIn 0.5s ease-in-out;
}
</style>
