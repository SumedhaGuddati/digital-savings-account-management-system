<!-- @author Karanam Hima Varsha -->
<template>
  <div class="apply-account-container animate-fade-in">
    <div class="section-branding">
      <div class="indigo-accent-bar"></div>
      <h3 class="section-title">Financial & Professional Profile</h3>
    </div>

    <div class="account-type-section">
      <label class="zeta-label">Select Your Plan <span class="required">*</span></label>
      
      <div class="account-cards-grid">
        <div 
          class="account-card" 
          :class="{ 'selected': formData.accountType === 'ZETA_BASIC' }"
          @click="selectAccount('ZETA_BASIC')"
        >
          <div class="card-icon">Basic</div>
          <div class="card-info">
            <h4>ZETA BASIC</h4>
            <p>Min. Balance: ₹10,000</p>
          </div>
          <div class="check-circle" v-if="formData.accountType === 'ZETA_BASIC'">✓</div>
        </div>

        <div 
          class="account-card" 
          :class="{ 'selected': formData.accountType === 'ZETA_PRIME' }"
          @click="selectAccount('ZETA_PRIME')"
        >
          <div class="card-icon prime">Prime</div>
          <div class="card-info">
            <h4>ZETA PRIME</h4>
            <p>Min. Balance: ₹15,000</p>
          </div>
          <div class="check-circle" v-if="formData.accountType === 'ZETA_PRIME'">✓</div>
        </div>

        <div 
          class="account-card" 
          :class="{ 'selected': formData.accountType === 'ZETA_ULTRA' }"
          @click="selectAccount('ZETA_ULTRA')"
        >
          <div class="card-icon ultra">Ultra</div>
          <div class="card-info">
            <h4>ZETA ULTRA</h4>
            <p>Min. Balance: ₹25,000</p>
          </div>
          <div class="check-circle" v-if="formData.accountType === 'ZETA_ULTRA'">✓</div>
        </div>
      </div>
      <p class="helper-hint">Click a card to select your preferred account tier.</p>
    </div>

    <div class="input-grid">
      <div class="field-wrapper">
        <label class="zeta-label">
          Parent / Guardian Full Name <span class="required">*</span>
        </label>
        <input
          v-model="formData.parentName"
          type="text"
          placeholder="Legal Name"
          class="zeta-input"
        />
      </div>

      <div class="field-wrapper">
        <label class="zeta-label">
          Date of Birth <span class="required">*</span>
        </label>
        <input 
          v-model="formData.dob" 
          type="date" 
          class="zeta-input" 
          :max="maxDate"
        />
        <p class="helper-hint">You must be at least 18 years old.</p>
      </div>

      <div class="field-wrapper">
        <label class="zeta-label">Occupation Status <span class="required">*</span></label>
        <select v-model="formData.occupation" class="zeta-select">
          <option value="" disabled selected>Select status</option>
          <option value="SALARIED">Salaried</option>
          <option value="SELF_EMPLOYED">Self-Employed</option>
          <option value="STUDENT">Student</option>
          <option value="OTHERS">Others</option>
        </select>
      </div>

      <div class="field-wrapper">
        <label class="zeta-label">
          Primary Income Source <span class="required">*</span>
        </label>
        <select v-model="formData.incomeSource" class="zeta-select">
          <option value="" disabled selected>Select source</option>
          <option value="SALARY">Salary</option>
          <option value="BUSINESS">Business</option>
          <option value="PENSION">Pension</option>
          <option value="OTHERS">Others</option>
        </select>
      </div>

      <div class="field-wrapper">
        <label class="zeta-label">
          Gross Annual Income (INR) <span class="required">*</span>
        </label>
        <div class="currency-container">
          <span class="currency-symbol">₹</span>
          <input
            v-model="formData.annualIncome"
            type="number"
            placeholder="e.g. 800000"
            min="1"
            class="zeta-input currency-input"
          />
        </div>
      </div>
    </div>

    <div class="terms-section">
      <div class="terms-checkbox-row" @click="toggleTermsCheckbox">
        <div :class="['custom-checkbox', { 'is-checked': termsChecked }]">
          <span v-if="termsChecked">✓</span>
        </div>
        <p class="terms-text">
          I have read and agree to the 
          <a href="#" @click.stop="openTermsModal" class="terms-link">
            Terms and Conditions
          </a>
        </p>
      </div>
    </div>

    <TermsAndConditions 
      :show="showTerms"
      @close="closeTermsModal"
    />
  </div>
</template>

<script>
import TermsAndConditions from "./ui/TermsAndConditions.vue";

export default {
  name: "ApplyAccountModule",
  components: {
    TermsAndConditions
  },
  data() {
    return {
      formData: {
        accountType: "", 
        parentName: "",
        dob: "",
        occupation: "",
        incomeSource: "",
        annualIncome: null,
      },
      showTerms: false,
      termsChecked: false
    };
  },
  mounted() {
    const urlParams = new URLSearchParams(window.location.search);
    const typeFromUrl = urlParams.get('type');
    
    const validTypes = ['ZETA_BASIC', 'ZETA_PRIME', 'ZETA_ULTRA'];
    if (typeFromUrl && validTypes.includes(typeFromUrl)) {
      this.formData.accountType = typeFromUrl;
    }
  },
  computed: {
    maxDate() {
      const today = new Date();
      today.setFullYear(today.getFullYear() - 18);
      return today.toISOString().split("T")[0];
    }
  },
  methods: {
    selectAccount(type) {
      this.formData.accountType = type;
    },
    openTermsModal() {
      this.showTerms = true;
    },
    closeTermsModal() {
      this.showTerms = false;
    },
    toggleTermsCheckbox() {
      this.termsChecked = !this.termsChecked;
    }
  }
};
</script>

<style scoped>
.apply-account-container {
  padding: 20px 0 0 0;
  width: 100%;
}

.section-branding {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
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

.account-type-section {
  margin-bottom: 32px;
}

.account-cards-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-top: 12px;
}

.account-card {
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  background: #ffffff;
  transition: all 0.2s ease;
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.account-card:hover {
  border-color: #a5b4fc;
  background: #f8fafc;
  transform: translateY(-2px);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
}

.account-card.selected {
  border-color: #6366f1;
  background: #eff6ff;
  box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.2);
}

.card-icon {
  font-size: 0.7rem;
  font-weight: 700;
  text-transform: uppercase;
  color: #64748b;
  background: #f1f5f9;
  width: fit-content;
  padding: 4px 8px;
  border-radius: 4px;
}

.card-icon.prime {
  color: #4f46e5;
  background: #e0e7ff;
}

.card-icon.ultra {
  color: #b45309;
  background: #fef3c7;
}

.card-info h4 {
  margin: 0;
  font-size: 0.9rem;
  font-weight: 700;
  color: #1e293b;
}

.card-info p {
  margin: 4px 0 0 0;
  font-size: 0.75rem;
  color: #64748b;
}

.check-circle {
  position: absolute;
  top: 12px;
  right: 12px;
  width: 20px;
  height: 20px;
  background: #6366f1;
  color: white;
  border-radius: 50%;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.input-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px 32px;
  margin-bottom: 0;
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

.zeta-input:focus, .zeta-select:focus {
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

.terms-section {
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #f1f5f9;
}

.terms-checkbox-row {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 8px;
  transition: background-color 0.2s ease;
}

.terms-checkbox-row:hover {
  background-color: #f8fafc;
}

.custom-checkbox {
  width: 18px;
  height: 18px;
  border: 2px solid #cbd5e1;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  transition: all 0.2s ease;
  flex-shrink: 0;
  background-color: white;
}

.custom-checkbox.is-checked {
  background: #6366f1;
  color: white;
  border-color: #6366f1;
}

.terms-text {
  font-size: 0.9rem;
  color: #334155;
  margin: 0;
  line-height: 1.4;
}

.terms-link {
  color: #6366f1;
  text-decoration: none;
  font-weight: 600;
  padding: 2px 4px;
  border-radius: 4px;
  transition: all 0.2s ease;
  display: inline-block;
  cursor: pointer;
  border-bottom: 1px dotted #6366f1;
}

.terms-link:hover {
  background-color: #e0e7ff;
  color: #4f46e5;
  border-bottom: 1px solid #4f46e5;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.animate-fade-in {
  animation: fadeIn 0.5s ease-in-out;
}

@media (max-width: 768px) {
  .input-grid {
    grid-template-columns: 1fr;
  }
  
  .account-cards-grid {
    grid-template-columns: 1fr;
  }
}
</style>