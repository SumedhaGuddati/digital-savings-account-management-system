<template>
  <div class="fixed inset-0 flex items-center justify-center bg-black/40 z-50">
    <div class="bg-white rounded-3xl p-6 w-full max-w-md shadow-2xl">
      <h2 class="text-2xl font-bold text-gray-900 mb-6">Reset Password</h2>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label class="text-xs font-semibold text-gray-700 uppercase">Email Address</label>
          <input
            v-model="email"
            type="email"
            readonly
            class="w-full border border-gray-300 rounded-xl px-4 py-3 bg-gray-100 text-gray-700 focus:outline-none"
          />
        </div>

        <div v-if="otpSent">
          <label class="text-xs font-semibold text-gray-700 uppercase">OTP</label>
          <input
            v-model="otp"
            type="text"
            placeholder="Enter OTP"
            class="w-full border border-gray-300 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-600/20"
          />
        </div>

        <div v-if="otpSent">
          <label class="text-xs font-semibold text-gray-700 uppercase mt-2">New Password</label>
          <input
            v-model="newPassword"
            type="password"
            placeholder="New Password"
            class="w-full border border-gray-300 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-600/20"
          />

          <label class="text-xs font-semibold text-gray-700 uppercase mt-2">Confirm Password</label>
          <input
            v-model="confirmPassword"
            type="password"
            placeholder="Confirm Password"
            class="w-full border border-gray-300 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-600/20"
          />
        </div>

        <div class="flex justify-end gap-3 mt-4">
          <button
            type="button"
            class="px-4 py-2 rounded-xl border border-gray-300 hover:bg-gray-100"
            @click="$emit('close')"
          >
            Cancel
          </button>
          <button
            type="submit"
            :disabled="loading"
            class="px-4 py-2 rounded-xl bg-indigo-600 text-white hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ otpSent ? (loading ? 'Resetting...' : 'Reset Password') : (loading ? 'Sending...' : 'Send OTP') }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { mapActions } from "vuex";
import { toast } from "@steveyuowo/vue-hot-toast";

export default defineComponent({
  name: "ForgotPassword",
  props: {
    emailProp: { type: String, default: "" } 
  },
  data() {
    return {
      email: this.emailProp,
      otpSent: false,
      otp: "",
      newPassword: "",
      confirmPassword: "",
      loading: false
    };
  },
  methods: {
    ...mapActions("auth", ["sendForgotPasswordOtp", "resetPassword"]),

    isPasswordStrong(password: string): boolean {
      const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
      return regex.test(password);
    },

    async handleSubmit() {
      if (!this.email) {
        toast.error("Email is required.");
        return;
      }

      this.loading = true;

      try {
        if (!this.otpSent) {
          await this.sendForgotPasswordOtp({ email: this.email });
          toast.success(`OTP sent to ${this.email}`);
          this.otpSent = true;
        } else {
          if (!this.otp || !this.newPassword || !this.confirmPassword) {
            toast.error("All fields are required.");
            return;
          }

          if (this.newPassword !== this.confirmPassword) {
            toast.error("Passwords do not match.");
            return;
          }

          if (!this.isPasswordStrong(this.newPassword)) {
            toast.error(
              "Password must be at least 8 characters and include uppercase, lowercase, number, and special character."
            );
            return;
          }

          await this.resetPassword({
            email: this.email,
            otp: this.otp,
            newPassword: this.newPassword
          });

          toast.success("Password reset successful!");
          this.otp = "";
          this.newPassword = "";
          this.confirmPassword = "";
          this.$emit("close"); 
        }
      } catch (error: any) {
        const msg = error.response?.data?.message || error.message || "Something went wrong.";
        toast.error(msg);
      } finally {
        this.loading = false;
      }
    }
  }
});
</script>

