<script setup>
import { reactive, defineExpose } from "vue";

defineOptions({
  name: "SignUpModule",
});

const formData = reactive({
  name: "",
  email: "",
  password: "",
  confirmPassword: "",
});

// Expose form data to MainRegistration.vue
defineExpose({ formData });
</script>

<template>
  <div class="signup-module animate-fade-in">
    <div class="section-branding">
      <div class="indigo-accent-bar"></div>
      <h3 class="section-title">Security Credentials</h3>
    </div>

    <div class="input-grid">
      <div class="field-wrapper">
        <label class="zeta-label"
          >Full Legal Name <span class="required">*</span></label
        >
        <input
          v-model="formData.name"
          type="text"
          placeholder="John Doe"
          class="zeta-input"
          required
        />
      </div>

      <div class="field-wrapper">
        <label class="zeta-label"
          >Email Address <span class="required">*</span></label
        >
        <input
          v-model="formData.email"
          type="email"
          placeholder="name@zeta.com"
          class="zeta-input"
          required
        />
      </div>

      <div class="field-wrapper">
        <label class="zeta-label"
          >Password <span class="required">*</span></label
        >
        <input
          v-model="formData.password"
          type="password"
          placeholder="••••••••"
          class="zeta-input"
          required
        />
        <p class="helper-hint">Min 8 chars with uppercase and numbers.</p>
      </div>

      <div class="field-wrapper">
        <label class="zeta-label"
          >Confirm Password <span class="required">*</span></label
        >
        <input
          v-model="formData.confirmPassword"
          type="password"
          placeholder="••••••••"
          class="zeta-input"
          :class="{
            'input-error':
              formData.confirmPassword &&
              formData.password !== formData.confirmPassword,
          }"
          required
        />
        <p
          v-if="
            formData.confirmPassword &&
            formData.password !== formData.confirmPassword
          "
          class="error-hint"
        >
          Passwords do not match
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.signup-module {
  padding: 20px 0;
  width: 100%;
}
.section-branding {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 30px;
}
.indigo-accent-bar {
  width: 3px;
  height: 18px;
  background: #6366f1;
  border-radius: 2px;
}
.section-title {
  font-size: 0.75rem;
  font-weight: 800;
  color: #94a3b8;
  text-transform: uppercase;
  letter-spacing: 0.1em;
}

.input-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px 32px;
}
.field-wrapper {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.zeta-label {
  font-size: 0.7rem;
  font-weight: 700;
  color: #475569;
  text-transform: uppercase;
}
.required {
  color: #ef4444;
}

.zeta-input {
  width: 100%;
  padding: 10px 14px;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 0.9rem;
  color: #1e293b;
  transition: all 0.2s;
}
.zeta-input:focus {
  outline: none;
  border-color: #6366f1;
  background: white;
}

.input-error {
  border-color: #ef4444 !important;
  background-color: #fef2f2 !important;
}
.error-hint {
  font-size: 0.65rem;
  color: #ef4444;
  font-weight: 700;
  margin-top: 4px;
}
.helper-hint {
  font-size: 0.65rem;
  color: #64748b;
  margin-top: 2px;
}

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
  animation: fadeIn 0.5s ease-in-out forwards;
}
@media (max-width: 768px) {
  .input-grid {
    grid-template-columns: 1fr;
  }
}
</style>
