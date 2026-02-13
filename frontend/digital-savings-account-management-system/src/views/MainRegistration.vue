<template>
  <div class="registration-grid bg-geometric">
    <aside class="branding-sidebar">
      <div class="sidebar-image-bg"></div>
      <div class="sidebar-overlay"></div>
      <div class="content-layer">
        <div class="brand-top">
          <div class="brand-logo">Zeta<span>rise</span></div>
          <h1 class="sidebar-hero">
            Elevate your <br /><span class="text-indigo-400">Financial Future.</span>
          </h1>
        </div>
        <div class="brand-bottom">
          <p class="copyright">SECURE DIGITAL BANKING © 2026</p>
        </div>
      </div>
    </aside>

    <main class="form-viewport">
      <div class="form-centering-wrapper">
        <header class="main-header">
          <h1 class="page-title">Account Application</h1>
          <p class="page-subtitle">
            Complete your profile to open your new account.
          </p>
        </header>

        <SignUp ref="signupRef" />

        <div class="spacer"></div>

        <ApplyAccountModule ref="financialModuleRef" />

        <footer class="form-actions">
          <div class="agreement-row" @click="isAgreed = !isAgreed">
            <div :class="['custom-checkbox', { 'is-checked': isAgreed }]">
              <span v-if="isAgreed">✓</span>
            </div>
            <p class="agreement-text">
              I verify that all information is accurate and complete.
            </p>
          </div>

          <button @click="handleSubmit" :disabled="!isAgreed || isSubmitting" class="submit-button">
            <span v-if="!isSubmitting">Submit Application</span>
            <span v-else>Processing...</span>
          </button>
        </footer>
      </div>
    </main>
  </div>
</template>

<script>
import { toast } from "@steveyuowo/vue-hot-toast";
import "@steveyuowo/vue-hot-toast/vue-hot-toast.css";
import SignUp from "../components/SignUp.vue";
import ApplyAccountModule from "../components/ApplyAccount.vue";
import { applyForAccount } from "@/api/endpoints/auth/auth.api";

export default {
  name: "MainRegistration",
  components: {
    SignUp,
    ApplyAccountModule
  },
  data() {
    return {
      isAgreed: false,
      isSubmitting: false,
      initialPlanType: ""
    };
  },
  computed: {
    isFormValid() {
      const signup = this.$refs.signupRef?.formData;
      const financial = this.$refs.financialModuleRef?.formData;

      if (!signup || !financial) return false;

      const authValid = signup.name && signup.email && signup.password;
      const passwordsMatch = signup.password === signup.confirmPassword;
      const passwordStrong = this.$refs.signupRef?.isValidPassword === true;

      const financialValid =
        financial.accountType &&
        financial.parentName &&
        financial.dob &&
        financial.occupation &&
        financial.incomeSource &&
        financial.annualIncome !== null &&
        financial.annualIncome !== undefined &&
        financial.annualIncome !== "";

      return authValid && passwordsMatch && passwordStrong && financialValid && this.isAgreed;
    },
  },
  methods: {
    async handleSubmit() {
      const signupData = this.$refs.signupRef?.formData;
      const financialData = this.$refs.financialModuleRef?.formData;

      if (!signupData || !financialData) return;

      if (!this.$refs.signupRef?.isValidPassword) {
        toast.error("Please choose a strong password before submitting.");
      }
    },
    async handleSubmit() {
      const s = this.$refs.signupRef?.formData;
      const f = this.$refs.financialModuleRef?.formData;
      const termsChecked = this.$refs.financialModuleRef?.termsChecked;

      if (!s || !f) {
        toast.error("Form data is missing. Please refresh.");
        return;
      }

      if (!s.name || !s.email || !s.password) {
        toast.error("Please fill in all Sign Up fields.");
        return;
      }

      if (s.password !== s.confirmPassword) {
        toast.error("Passwords do not match.");
        return;
      }

      if (!f.accountType) {
        toast.error("Please select an Account Type.");
        return;
      }

      if (!f.annualIncome || !f.occupation) {
        toast.error("Please complete your Financial Profile.");
        return;
      }

      if (!termsChecked) {
        toast.error("Please accept the Terms and Conditions.");
        return;
      }

      if (!this.isAgreed) {
        toast.error("Please verify your information.");
        return;
      }

      const toastId = toast.loading("Initializing secure session...");

      try {
        this.isSubmitting = true;
        
        await this.$store.dispatch("auth/signup", {
          name: s.name,
          email: s.email,
          password: s.password,
        });

        toast.loading("Creating application...", { id: toastId });

        const annualIncomeValue =
          financialData.annualIncome === null || financialData.annualIncome === ""
            ? null
            : Number(financialData.annualIncome);

        await applyForAccount({
          occupation: financialData.occupation,
          incomeSource: financialData.incomeSource,
          annualIncome: annualIncomeValue,
          parentName: financialData.parentName,
          dob: financialData.dob,
          accountType: financialData.accountType || "ZETA_BASIC",
        });

        toast.success("Application Submitted!", { id: toastId });
        setTimeout(() => this.$router.push("/me/application"), 1000);
        await applyForAccount({
          occupation: f.occupation,
          incomeSource: f.incomeSource,
          annualIncome: Number(f.annualIncome),
          parentName: f.parentName,
          dob: f.dob,
          accountType: f.accountType,
          termsAccepted: true,
          termsAcceptedAt: new Date().toISOString()
        });

        toast.success("Application Submitted!", { id: toastId });

        setTimeout(() => this.$router.push("/me/application"), 1000);
      } catch (error) {
        const msg = error.response?.data?.message;
        if (msg === "A savings account application already exists for this user.") {
          toast.success("Redirecting to your application...", { id: toastId });
          setTimeout(() => this.$router.push("/me/application"), 1000);
        } else {
          toast.error(msg || "Request failed", { id: toastId });
        }
      } finally {
        this.isSubmitting = false;
      }
    }
  },
  mounted() {
    const urlParams = new URLSearchParams(window.location.search);
    const accountType = urlParams.get('type');

    if (accountType) {
      this.initialPlanType = accountType;
    }
  },
  watch: {
    '$route.query.accountType': {
      immediate: true,
      handler(newAccountType) {
        if (newAccountType) {
          this.initialPlanType = newAccountType;
        }

        window.scrollTo({
          top: 0,
          behavior: "smooth"
        });
      }
    }
  }
}
</script>

<style scoped>
.registration-grid {
  display: grid;
  grid-template-columns: 280px 1fr;
  min-height: 100vh;
}

.branding-sidebar {
  position: sticky;
  top: 0;
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.sidebar-image-bg {
  position: absolute;
  inset: 0;
  background-image: url("@/assets/image.jpeg");
  background-size: cover;
  background-position: center;
  z-index: 0;
}

.sidebar-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to bottom,
      rgba(15, 23, 42, 0.85) 0%,
      rgba(79, 70, 229, 0.4) 100%);
  z-index: 1;
}

.content-layer {
  position: relative;
  z-index: 2;
  height: 100%;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  color: white;
}

.brand-logo {
  font-size: 1.5rem;
  font-weight: 900;
  letter-spacing: -1px;
}

.brand-logo span {
  color: #818cf8;
}

.sidebar-hero {
  font-size: 2rem;
  font-weight: 700;
  margin-top: 50px;
  line-height: 1.1;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.form-viewport {
  padding: 40px 60px;
  display: flex;
  justify-content: center;
  background: #fff;
}

.form-centering-wrapper {
  width: 100%;
  max-width: 700px;
}

.spacer {
  height: 20px;
}

.page-title {
  font-size: 2.2rem;
  font-weight: 900;
  color: #0f172a;
  letter-spacing: -1px;
}

.page-subtitle {
  font-size: 0.95rem;
  color: #64748b;
  margin-top: 5px;
}

.form-actions {
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #f1f5f9;
}

.agreement-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  cursor: pointer;
}

.custom-checkbox {
  width: 18px;
  height: 18px;
  border: 1px solid #e2e8f0;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.7rem;
}

.custom-checkbox.is-checked {
  background: #6366f1;
  color: white;
  border-color: #6366f1;
}

.agreement-text {
  font-size: 0.85rem;
  color: #64748b;
  font-weight: 500;
}

.submit-button {
  width: 100%;
  padding: 16px;
  background: #0f172a;
  color: white;
  border-radius: 10px;
  border: none;
  font-size: 0.95rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s;
}

.submit-button:hover:not(:disabled) {
  background: #1e293b;
  transform: translateY(-1px);
}

.submit-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

@media (max-width: 1024px) {
  .registration-grid {
    grid-template-columns: 1fr;
  }

  .branding-sidebar {
    display: none;
  }
}

.bg-geometric {
  background-color: #f9fafb;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='28' height='49' viewBox='0 0 28 49'%3E%3Cg fill-rule='evenodd'%3E%3Cg id='hexagons' fill='%235b21b6' fill-opacity='0.03' fill-rule='nonzero'%3E%3Cpath d='M13.99 9.25l13 7.5v15l-13 7.5L1 31.75v-15l12.99-7.5zM3 17.9l10.99-6.34L25 17.9v12.7l-11.01 6.34L3 30.6V17.9zM0 15l12.98-7.5V0h-2v6.35L0 12.69v2.3zm0 18.5L12.98 41v8h-2v-6.85L0 35.81v-2.31zM28 15l-12.98-7.5V0h2v6.35L28 12.69v2.3zm0 18.5L15.02 41v8h2v-6.85L28 35.81v-2.31zM12.99 20l.01-13h2v13h-2.01zm0 29V36h2v13h-2z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}
</style>