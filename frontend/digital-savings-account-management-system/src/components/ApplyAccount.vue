<script setup>
import { reactive, defineExpose } from "vue";

defineOptions({
  name: "ApplyAccountModule",
});

const formData = reactive({
  accountType: "",
  parentName: "",
  dob: "",
  occupation: "",
  incomeSource: "",
  annualIncome: null,
});

// Expose data to MainRegistration.vue
defineExpose({ formData });
</script>

<template>
  <div class="apply-account-container animate-fade-in">
    <div class="section-branding">
      <div class="indigo-accent-bar"></div>
      <h3 class="section-title">Financial & Professional Profile</h3>
    </div>

    <div class="input-grid">
      <div class="field-wrapper">
        <label class="zeta-label"
          >Account Type <span class="required">*</span></label
        >
        <select v-model="formData.accountType" class="zeta-select">
          <option value="" disabled selected>Select account type</option>
          <option value="SAVINGS">Regular Savings</option>
        </select>
        <p class="helper-hint">Currently, only Regular Savings is supported.</p>
      </div>

      <div class="field-wrapper">
        <label class="zeta-label"
          >Parent / Guardian Full Name <span class="required">*</span></label
        >
        <input
          v-model="formData.parentName"
          type="text"
          placeholder="Legal Name"
          class="zeta-input"
        />
      </div>

      <div class="field-wrapper">
        <label class="zeta-label"
          >Date of Birth <span class="required">*</span></label
        >
        <input v-model="formData.dob" type="date" class="zeta-input" />
      </div>

      <div class="field-wrapper">
        <label class="zeta-label"
          >Occupation Status <span class="required">*</span></label
        >
        <select v-model="formData.occupation" class="zeta-select">
          <option value="" disabled selected>Select status</option>
          <option value="SALARIED">Salaried</option>
          <option value="SELF_EMPLOYED">Self-Employed</option>
          <option value="STUDENT">Student</option>
          <option value="OTHERS">Others</option>
        </select>
      </div>

      <div class="field-wrapper">
        <label class="zeta-label"
          >Primary Income Source <span class="required">*</span></label
        >
        <select v-model="formData.incomeSource" class="zeta-select">
          <option value="" disabled selected>Select source</option>
          <option value="SALARY">Salary</option>
          <option value="BUSINESS">Business</option>
          <option value="PENSION">Pension</option>
          <option value="OTHERS">Others</option>
        </select>
      </div>

      <div class="field-wrapper">
        <label class="zeta-label"
          >Gross Annual Income (INR) <span class="required">*</span></label
        >
        <div class="currency-container">
          <span class="currency-symbol">₹</span>
          <input
            v-model="formData.annualIncome"
            type="number"
            placeholder="e.g. 800000"
            class="zeta-input currency-input"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.apply-account-container {
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

/* Grid Layout */
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

.zeta-input,
.zeta-select {
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

.currency-container {
  position: relative;
  display: flex;
  align-items: center;
}
.currency-symbol {
  position: absolute;
  left: 12px;
  font-weight: 700;
  color: #6366f1;
  font-size: 0.9rem;
}
.currency-input {
  padding-left: 28px !important;
}

.helper-hint {
  font-size: 0.65rem;
  color: #94a3b8;
  margin-top: 4px;
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
  animation: fadeIn 0.5s ease-in-out;
}

@media (max-width: 768px) {
  .input-grid {
    grid-template-columns: 1fr;
  }
}
</style>
