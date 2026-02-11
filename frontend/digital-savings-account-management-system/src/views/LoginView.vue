<template>
  <div class="min-h-screen bg-white font-sans flex flex-col">
    <!-- HEADER -->
    <header class="w-full bg-[#5B21B6] shadow-sm p-4 flex items-center">
      <h1 class="text-xl font-bold text-white">ZetaRise</h1>
    </header>

    <!-- MAIN CONTENT -->
    <div class="flex-1 flex items-center justify-center p-4">
      <div
        class="w-full max-w-6xl flex flex-col md:flex-row rounded-3xl shadow-2xl overflow-hidden bg-white"
      >
        <!-- LEFT COLUMN: Login Card -->
        <div
          class="w-full md:w-1/2 p-8 sm:p-10 lg:p-12 flex flex-col justify-center"
        >
          <!-- Header -->
          <h1
            class="text-3xl lg:text-4xl font-bold text-[#5B21B6] tracking-tight mb-2"
          >
            Login
          </h1>
          <p class="text-[#6B7280] text-sm lg:text-base mb-8 lg:mb-10">
            Login into your account
          </p>

          <!-- LOGIN FORM -->
          <form @submit.prevent="handleLogin" class="space-y-5">
            <!-- Email -->
            <div>
              <label
                class="block text-xs font-medium text-[#6B7280] uppercase tracking-wide mb-1.5"
              >
                Email address
              </label>
              <input
                v-model="loginEmail"
                type="email"
                placeholder="Enter your email"
                class="w-full px-4 py-3 bg-[#F9FAFB] border border-[#E5E7EB] rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-[#7C3AED] focus:border-transparent transition"
                required
              />
            </div>

            <!-- Password with toggle -->
            <div>
              <label
                class="block text-xs font-medium text-[#6B7280] uppercase tracking-wide mb-1.5"
              >
                Password
              </label>
              <div class="relative">
                <input
                  :type="showPassword ? 'text' : 'password'"
                  v-model="loginPassword"
                  placeholder="Enter your password"
                  class="w-full px-4 py-3 bg-[#F9FAFB] border border-[#E5E7EB] rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-[#7C3AED] focus:border-transparent transition pr-12"
                  required
                />
                <button
                  type="button"
                  @click="showPassword = !showPassword"
                  class="absolute inset-y-0 right-0 flex items-center px-3 text-[#6B7280] hover:text-[#7C3AED] transition"
                >
                  <svg
                    v-if="showPassword"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke-width="1.5"
                    stroke="currentColor"
                    class="w-5 h-5"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M3.98 8.223A10.477 10.477 0 001.934 12C3.226 16.338 7.244 19.5 12 19.5c.993 0 1.953-.138 2.863-.395M6.228 6.228A10.45 10.45 0 0112 4.5c4.756 0 8.773 3.162 10.065 7.498a10.523 10.523 0 01-4.293 5.774M6.228 6.228L3 3m3.228 3.228l3.65 3.65m7.894 7.894L21 21m-3.228-3.228l-3.65-3.65m0 0a3 3 0 10-4.243-4.243m4.242 4.242L9.88 9.88"
                    />
                  </svg>
                  <svg
                    v-else
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke-width="1.5"
                    stroke="currentColor"
                    class="w-5 h-5"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M2.036 12.322a1.012 1.012 0 010-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178z"
                    />
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                    />
                  </svg>
                </button>
              </div>
            </div>

            <!-- Forgot password -->
            <div class="flex justify-end">
              <a
                href="#"
                class="text-sm text-[#7C3AED] hover:text-[#5B21B6] font-medium transition"
              >
                Forgot password?
              </a>
            </div>

            <!-- Login button -->
            <button
              type="submit"
              class="w-full bg-[#5B21B6] hover:bg-[#4C1D95] text-white font-semibold py-3 px-4 rounded-lg transition shadow-sm mt-2"
            >
              Login now
            </button>

            <!-- Link to Signup page -->
            <p class="text-center text-sm text-[#6B7280] pt-2">
              Don't have an account?
              <router-link
                to="/signup"
                class="text-[#7C3AED] hover:text-[#5B21B6] font-medium transition ml-1"
              >
                Signup now
              </router-link>
            </p>
          </form>
        </div>

        <!-- RIGHT COLUMN: Decorative image -->
        <div
          class="w-full md:w-1/2 bg-gradient-to-br from-[#F5F3FF] to-[#EDE9FE] flex items-center justify-center p-8 lg:p-12"
        >
          <img
            src="../assets/2953936.jpg"
            alt="Decorative purple illustration"
            class="w-full h-auto object-contain rounded-2xl shadow-lg"
            loading="lazy"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";

export default defineComponent({
  name: "LoginView",
  setup() {
    const loginEmail = ref("");
    const loginPassword = ref("");
    const showPassword = ref(false);

    const handleLogin = () => {
      console.log("Login", loginEmail.value, loginPassword.value);
      alert(`Login demo: ${loginEmail.value}`);
      // TODO: Connect to backend API here
    };

    return {
      loginEmail,
      loginPassword,
      showPassword,
      handleLogin,
    };
  },
});
</script>
