<script setup>
import { ref, reactive, computed } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { signup } from "@/services/authService";
import SignupModule from "../components/SignupModule.vue";

const router = useRouter();

// ============ SIGNUP FORM ============
const signupModuleRef = ref(null);
const isAgreed = ref(false);
const isSubmitting = ref(false);
const signupError = ref("");

// ============ ACCOUNT APPLICATION FORM ============
const applicationForm = reactive({
  accountType: "",
  parentName: "",
  dob: "",
  occupation: "",
  incomeSource: "",
  annualIncome: null,
});

const isApplicationAgreed = ref(false);
const applicationError = ref("");

// ============ OVERALL FORM VALIDATION ============
const isFormValid = computed(() => {
  const formData = signupModuleRef.value?.formData;
  if (!formData) return false;

  return (
    // Signup validation
    formData.name &&
    formData.email &&
    formData.password &&
    formData.confirmPassword &&
    isAgreed.value &&
    // Application validation
    applicationForm.accountType &&
    applicationForm.parentName &&
    applicationForm.dob &&
    applicationForm.occupation &&
    applicationForm.incomeSource &&
    applicationForm.annualIncome &&
    isApplicationAgreed.value
  );
});

// Regex for strong password
const strongPasswordRegex =
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;

// ============ HANDLE FORM SUBMIT ============
const handleSubmit = async () => {
  const formData = signupModuleRef.value?.formData;
  if (!formData) return;

  signupError.value = "";
  applicationError.value = "";

  // Validate password strength
  if (!strongPasswordRegex.test(formData.password)) {
    signupError.value =
      "Password is too weak. Use uppercase, lowercase, number, and special character.";
    return;
  }

  // Check if passwords match
  if (formData.password !== formData.confirmPassword) {
    signupError.value = "Passwords do not match.";
    return;
  }

  try {
    isSubmitting.value = true;

    // STEP 1: Call signup API
    await signup({
      name: formData.name,
      email: formData.email,
      password: formData.password,
    });

    // STEP 2: Call account application API
    const payload = {
      accountType: applicationForm.accountType,
      occupation: applicationForm.occupation,
      incomeSource: applicationForm.incomeSource,
      annualIncome: Number(applicationForm.annualIncome),
      parentName: applicationForm.parentName,
      dob: applicationForm.dob,
      fullName: formData.name,
      email: formData.email,
    };

    await axios.post("http://localhost:8080/api/accounts/apply", payload);

    // Success - redirect to status page
    alert("Account created and application submitted successfully!");
    setTimeout(() => router.push("/status"), 500);
  } catch (error) {
    if (error.response?.config?.url?.includes("/auth/signup")) {
      signupError.value = error.response?.data?.message || "Signup failed.";
    } else {
      applicationError.value =
        error.response?.data?.message || "Application submission failed.";
    }
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<template>
  <div class="grid grid-cols-1 lg:grid-cols-[280px_1fr] min-h-screen bg-white">
    <!-- LEFT SIDEBAR - Branding -->
    <aside
      class="bg-[#0f172a] text-white p-10 lg:min-h-screen lg:sticky lg:top-0 flex flex-col justify-between"
    >
      <div>
        <div class="text-2xl font-black tracking-tight">
          Zeta<span class="text-[#6366f1]">rise</span>
        </div>
        <h1 class="text-2xl font-light mt-10 leading-tight">
          Grow your wealth with precision.
        </h1>
      </div>
      <p class="text-[0.65rem] text-[#475569]">© 2026 Zeta Digital Banking</p>
    </aside>

    <!-- RIGHT MAIN CONTENT -->
    <main class="p-6 md:p-10 lg:p-12 flex justify-center">
      <div class="w-full max-w-[700px]">
        <!-- PAGE HEADER -->
        <header class="mb-8">
          <h1
            class="text-4xl lg:text-[2.2rem] font-black text-[#0f172a] leading-tight"
          >
            Open Your Digital Savings Account
          </h1>
          <p class="text-[0.95rem] text-[#64748b] mt-2">
            Complete all sections below to create your account and apply for a
            savings account.
          </p>
        </header>

        <!-- FORM - NO SUBMIT BUTTON INSIDE SECTIONS -->
        <form @submit.prevent="handleSubmit">
          <!-- ============ SECTION 1: ACCOUNT CREATION ============ -->
          <div class="mb-12">
            <!-- Section Header with Indigo Bar -->
            <div class="flex items-center gap-3 mb-6">
              <div class="w-1 h-[18px] bg-[#6366f1] rounded-full"></div>
              <h2
                class="text-[0.75rem] font-extrabold text-[#94a3b8] uppercase tracking-[0.1em]"
              >
                SECTION 01: ACCOUNT CREATION
              </h2>
            </div>

            <!-- Signup Module -->
            <SignupModule ref="signupModuleRef" />

            <!-- Agreement Checkbox -->
            <div class="mt-6">
              <div
                class="flex items-center gap-2.5 mb-5 cursor-pointer"
                @click="isAgreed = !isAgreed"
              >
                <div
                  :class="[
                    'w-[18px] h-[18px] border border-[#e2e8f0] rounded flex items-center justify-center text-[0.7rem] transition-colors',
                    { 'bg-[#6366f1] text-white border-[#6366f1]': isAgreed },
                  ]"
                >
                  <span v-if="isAgreed">✓</span>
                </div>
                <p class="text-[0.85rem] text-[#64748b]">
                  I agree to the
                  <a href="#" class="text-[#6366f1] hover:underline"
                    >Terms of Service</a
                  >
                  and
                  <a href="#" class="text-[#6366f1] hover:underline"
                    >Privacy Policy</a
                  >.
                </p>
              </div>
            </div>

            <!-- Signup Error Message -->
            <div
              v-if="signupError"
              class="mt-4 p-3 bg-red-50 border border-red-100 text-red-500 text-[0.8rem] text-center rounded-lg"
            >
              {{ signupError }}
            </div>
          </div>

          <!-- ============ SECTION 2: ACCOUNT APPLICATION ============ -->
          <div class="mb-12">
            <!-- Section Header with Indigo Bar -->
            <div class="flex items-center gap-3 mb-6">
              <div class="w-1 h-[18px] bg-[#6366f1] rounded-full"></div>
              <h2
                class="text-[0.75rem] font-extrabold text-[#94a3b8] uppercase tracking-[0.1em]"
              >
                SECTION 02: FINANCIAL & PROFESSIONAL PROFILE
              </h2>
            </div>

            <!-- Identity Summary (Pre-filled from Signup) -->
            <div
              class="bg-[#f8fafc] border border-[#e2e8f0] rounded-lg p-4 mb-6"
            >
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label
                    class="text-[0.65rem] font-bold text-[#475569] uppercase"
                    >Full Legal Name</label
                  >
                  <p class="text-[0.95rem] font-medium text-[#0f172a]">
                    {{ signupModuleRef?.formData?.name || "Not provided" }}
                  </p>
                </div>
                <div>
                  <label
                    class="text-[0.65rem] font-bold text-[#475569] uppercase"
                    >Email Address</label
                  >
                  <p class="text-[0.95rem] font-medium text-[#0f172a]">
                    {{ signupModuleRef?.formData?.email || "Not provided" }}
                  </p>
                </div>
              </div>
            </div>

            <!-- 2-Column Grid for Application Form -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6 md:gap-8">
              <!-- Account Type -->
              <div class="flex flex-col gap-2">
                <label class="text-[0.7rem] font-bold text-[#475569] uppercase">
                  Account Type <span class="text-red-500">*</span>
                </label>
                <select
                  v-model="applicationForm.accountType"
                  class="w-full px-3.5 py-2.5 bg-[#f8fafc] border border-[#e2e8f0] rounded-lg text-[0.9rem] text-[#1e293b] focus:outline-none focus:border-[#6366f1] focus:bg-white transition-colors appearance-none"
                >
                  <option value="" disabled selected>
                    Select account type
                  </option>
                  <option value="SAVINGS">Regular Savings</option>
                  <option value="PREMIUM_SAVINGS">Premium Savings</option>
                  <option value="SALARY_ACCOUNT">Salary Account</option>
                </select>
              </div>

              <!-- Parent / Guardian Full Name -->
              <div class="flex flex-col gap-2">
                <label class="text-[0.7rem] font-bold text-[#475569] uppercase">
                  Parent / Guardian Full Name
                  <span class="text-red-500">*</span>
                </label>
                <input
                  v-model="applicationForm.parentName"
                  type="text"
                  placeholder="Legal Name"
                  class="w-full px-3.5 py-2.5 bg-[#f8fafc] border border-[#e2e8f0] rounded-lg text-[0.9rem] text-[#1e293b] focus:outline-none focus:border-[#6366f1] focus:bg-white transition-colors"
                />
              </div>

              <!-- Date of Birth -->
              <div class="flex flex-col gap-2">
                <label class="text-[0.7rem] font-bold text-[#475569] uppercase">
                  Date of Birth <span class="text-red-500">*</span>
                </label>
                <input
                  v-model="applicationForm.dob"
                  type="date"
                  class="w-full px-3.5 py-2.5 bg-[#f8fafc] border border-[#e2e8f0] rounded-lg text-[0.9rem] text-[#1e293b] focus:outline-none focus:border-[#6366f1] focus:bg-white transition-colors"
                />
              </div>

              <!-- Occupation Status -->
              <div class="flex flex-col gap-2">
                <label class="text-[0.7rem] font-bold text-[#475569] uppercase">
                  Occupation Status <span class="text-red-500">*</span>
                </label>
                <select
                  v-model="applicationForm.occupation"
                  class="w-full px-3.5 py-2.5 bg-[#f8fafc] border border-[#e2e8f0] rounded-lg text-[0.9rem] text-[#1e293b] focus:outline-none focus:border-[#6366f1] focus:bg-white transition-colors appearance-none"
                >
                  <option value="" disabled selected>Select status</option>
                  <option value="SALARIED">Salaried</option>
                  <option value="SELF_EMPLOYED">Self-Employed</option>
                  <option value="BUSINESS">Business Owner</option>
                  <option value="STUDENT">Student</option>
                </select>
              </div>

              <!-- Primary Income Source -->
              <div class="flex flex-col gap-2">
                <label class="text-[0.7rem] font-bold text-[#475569] uppercase">
                  Primary Income Source <span class="text-red-500">*</span>
                </label>
                <select
                  v-model="applicationForm.incomeSource"
                  class="w-full px-3.5 py-2.5 bg-[#f8fafc] border border-[#e2e8f0] rounded-lg text-[0.9rem] text-[#1e293b] focus:outline-none focus:border-[#6366f1] focus:bg-white transition-colors appearance-none"
                >
                  <option value="" disabled selected>Select source</option>
                  <option value="SALARY">Salary</option>
                  <option value="BUSINESS">Business</option>
                  <option value="INVESTMENT">Investment</option>
                </select>
              </div>

              <!-- Gross Annual Income -->
              <div class="flex flex-col gap-2">
                <label class="text-[0.7rem] font-bold text-[#475569] uppercase">
                  Gross Annual Income (INR) <span class="text-red-500">*</span>
                </label>
                <div class="relative flex items-center">
                  <span
                    class="absolute left-3 font-bold text-[#6366f1] text-[0.9rem]"
                    >₹</span
                  >
                  <input
                    v-model="applicationForm.annualIncome"
                    type="number"
                    placeholder="e.g. 800000"
                    class="w-full pl-7 pr-3.5 py-2.5 bg-[#f8fafc] border border-[#e2e8f0] rounded-lg text-[0.9rem] text-[#1e293b] focus:outline-none focus:border-[#6366f1] focus:bg-white transition-colors"
                  />
                </div>
              </div>
            </div>

            <!-- Agreement Checkbox -->
            <div class="mt-6">
              <div
                class="flex items-center gap-2.5 mb-5 cursor-pointer"
                @click="isApplicationAgreed = !isApplicationAgreed"
              >
                <div
                  :class="[
                    'w-[18px] h-[18px] border border-[#e2e8f0] rounded flex items-center justify-center text-[0.7rem] transition-colors',
                    {
                      'bg-[#6366f1] text-white border-[#6366f1]':
                        isApplicationAgreed,
                    },
                  ]"
                >
                  <span v-if="isApplicationAgreed">✓</span>
                </div>
                <p class="text-[0.85rem] text-[#64748b]">
                  I verify that all information is accurate.
                </p>
              </div>
            </div>

            <!-- Application Error Message -->
            <div
              v-if="applicationError"
              class="mt-4 p-3 bg-red-50 border border-red-100 text-red-500 text-[0.8rem] text-center rounded-lg"
            >
              {{ applicationError }}
            </div>
          </div>

          <!-- ============ SINGLE SUBMIT BUTTON AT THE END ============ -->
          <div class="mt-8 pt-6 border-t border-[#f1f5f9]">
            <button
              type="submit"
              :disabled="!isFormValid || isSubmitting"
              class="w-full py-4 bg-[#0f172a] hover:bg-[#1e293b] text-white font-bold rounded-xl transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {{
                isSubmitting
                  ? "Processing..."
                  : "Create Account & Submit Application"
              }}
            </button>
          </div>
        </form>

        <!-- LOGIN LINK -->
        <div class="text-center mt-6">
          <p class="text-[0.85rem] text-[#64748b]">
            Already have an account?
            <router-link
              to="/login"
              class="font-bold text-[#6366f1] hover:underline ml-1"
            >
              Login
            </router-link>
          </p>
        </div>
      </div>
    </main>
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
