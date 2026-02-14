<template>
  <div class="apply-account-container animate-fade-in">
    <div class="section-branding">
      <div class="indigo-accent-bar"></div>
      <h3 class="section-title">Security Credentials</h3>
    </div>

    <div class="input-grid">
      <div class="field-wrapper">
        <label class="zeta-label">Full Legal Name <span class="required">*</span></label>
        <input
          v-model="formData.name"
          type="text"
          class="zeta-input"
          placeholder="John Doe"
        />
      </div>

      <div class="field-wrapper">
        <label class="zeta-label">Email Address <span class="required">*</span></label>
        <div class="flex gap-2">
          <input
            v-model="formData.email"
            type="email"
            class="zeta-input flex-1"
            :disabled="otpVerified"
            placeholder="name@zeta.com"
          />
          <button
            class="btn-indigo"
            type="button"
            @click="sendOtp"
            :disabled="sendingOtp || otpVerified || !isEmailValid"
          >
            {{ otpVerified ? "Verified" : sendingOtp ? "Sending..." : "Send OTP" }}
          </button>
        </div>
        <p v-if="formData.email && !isEmailValid" class="text-red-500 text-xs mt-1">
          Invalid email format
        </p>
      </div>

      <div class="field-wrapper" v-if="otpSent && !otpVerified">
        <label class="zeta-label">Enter OTP <span class="required">*</span></label>
        <div class="flex gap-2">
          <input
            v-model="formData.otp"
            type="text"
            class="zeta-input flex-1"
            placeholder="Enter OTP"
          />
          <button
            class="btn-green"
            type="button"
            @click="verifyOtp"
            :disabled="verifyingOtp"
          >
            {{ verifyingOtp ? "Verifying..." : "Verify OTP" }}
          </button>
        </div>
      </div>

      <div class="field-wrapper">
        <label class="zeta-label">Password <span class="required">*</span></label>
        <input
          v-model="formData.password"
          type="password"
          class="zeta-input"
          placeholder="••••••••"
        />
        <div v-if="formData.password" class="mt-2">
          <div class="flex items-center gap-2">
            <div class="flex-1 h-1.5 bg-gray-200 rounded-full overflow-hidden">
              <div
                class="h-full rounded-full transition-all duration-300"
                :class="strengthConfig.color"
                :style="{ width: strengthConfig.width }"
              ></div>
            </div>
            <span class="text-[0.65rem] font-semibold rounded-full px-2 py-px text-white" :class="strengthConfig.color">
              {{ passwordStrength }}
            </span>
          </div>
        </div>
      </div>

      <div class="field-wrapper">
        <label class="zeta-label">Confirm Password <span class="required">*</span></label>
        <input
          v-model="formData.confirmPassword"
          type="password"
          class="zeta-input"
          placeholder="••••••••"
        />
      </div>
    </div>
    <Toaster/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { sendOtpToEmail, verifyOtpToken } from "@/api/endpoints/auth/auth.api";
import { Toaster, toast } from "@steveyuowo/vue-hot-toast";
export default defineComponent({
  name: "SignUpModule",
  data() {
    return {
      formData: {
        name: "",
        email: "",
        password: "",
        confirmPassword: "",
        otp: "",
      },
      otpSent: false,
      otpVerified: false,
      sendingOtp: false,
      verifyingOtp: false,
    };
  },
  computed: {
    passwordStrength(): string | null {
      const pwd = this.formData.password;
      if (!pwd) return null;
      let score = 0;
      if (pwd.length >= 8) score++;
      if (/[a-z]/.test(pwd)) score++;
      if (/[A-Z]/.test(pwd)) score++;
      if (/\d/.test(pwd)) score++;
      if (/[!@#$%^&*]/.test(pwd)) score++;
      if (score <= 2) return "weak";
      if (score <= 4) return "medium";
      return "strong";
    },
    isValidPassword(): boolean {
      return this.passwordStrength === "strong";
    },
    strengthConfig(): { color: string; width: string } {
      switch (this.passwordStrength) {
        case "weak":
          return { color: "bg-red-500", width: "33%" };
        case "medium":
          return { color: "bg-yellow-500", width: "66%" };
        case "strong":
          return { color: "bg-green-500", width: "100%" };
        default:
          return { color: "bg-gray-200", width: "0%" };
      }
    },
    isEmailValid(): boolean {
      const email = this.formData.email;
      if (!email) return false;
      const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return regex.test(email);
    },
  },
  methods: {
    async sendOtp() {
      if (!this.formData.email) return alert("Enter email first");
      if (!this.isEmailValid) return alert("Invalid email format");

      this.sendingOtp = true;
      try {
        await sendOtpToEmail(this.formData.email);
        this.otpSent = true;
        toast.success("OTP sent! It will expire in 5 minutes.");
      } catch (err: any) {
        console.error(err);
        if (err.response?.data?.message) {
          alert(err.response.data.message);
        } else {
          alert("Failed to send OTP");
        }
      } finally {
        this.sendingOtp = false;
      }
    },
    async verifyOtp() {
      if (!this.formData.otp) return alert("Enter OTP");

      this.verifyingOtp = true;
      try {
        const response = await verifyOtpToken(
            this.formData.email,
            this.formData.otp,
        );

        if (response.data.valid) {
          this.otpVerified = true;
          alert("OTP verified successfully!");
        } else {
          alert("Invalid OTP or OTP expired");
        }
      } catch (err) {
        console.error(err);
        alert("OTP verification failed");
      } finally {
        this.verifyingOtp = false;
      }
    },
  },
});
</script>

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
.required { color: #ef4444; }
.zeta-input, .zeta-select {
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
.btn-indigo {
  background: #6366f1;
  color: white;
  padding: 8px 14px;
  border-radius: 6px;
  font-weight: 700;
  transition: all 0.2s;
}
.btn-indigo:hover { background: #4f46e5; }
.btn-green {
  background: #22c55e;
  color: white;
  padding: 8px 14px;
  border-radius: 6px;
  font-weight: 700;
  transition: all 0.2s;
}
.btn-green:hover { background: #16a34a; }

@keyframes fadeIn { from {opacity:0;transform:translateY(10px);} to {opacity:1;transform:translateY(0);} }
.animate-fade-in { animation: fadeIn 0.5s ease-in-out; }

@media (max-width: 768px) {
  .input-grid { grid-template-columns: 1fr; }
  .flex { flex-direction: column; gap: 8px; }
}
</style>
