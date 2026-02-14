<template>
  <div class="min-h-screen bg-gradient-to-br from-indigo-50 via-white to-purple-50 font-sans flex flex-col">
    <header class="w-full bg-white/80 backdrop-blur-md border-b border-gray-200/60 sticky top-0 z-50 py-3 px-6">
      <div class="max-w-7xl mx-auto flex justify-between items-center">
        <AppLogo />
        <div class="flex items-center gap-3">
          <span class="text-xs text-gray-500 hidden sm:inline">Secure Banking</span>
          <div class="w-2 h-2 bg-emerald-500 rounded-full animate-pulse"></div>
        </div>
      </div>
    </header>

    <div class="flex-1 flex items-center justify-center p-4 md:p-6 relative">
      <div class="absolute inset-0 overflow-hidden">
        <div
          class="absolute -top-40 -right-40 w-80 h-80 bg-indigo-200 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob">
        </div>
        <div
          class="absolute -bottom-40 -left-40 w-80 h-80 bg-purple-200 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob animation-delay-2000">
        </div>
        <div
          class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-80 h-80 bg-pink-200 rounded-full mix-blend-multiply filter blur-3xl opacity-10 animate-blob animation-delay-4000">
        </div>
      </div>

      <div class="w-full max-w-6xl relative z-20">
        <div
          class="grid md:grid-cols-2 bg-white rounded-3xl shadow-2xl shadow-indigo-100/50 border border-gray-200/80 overflow-hidden backdrop-blur-sm">

          <div class="p-8 lg:p-12 flex flex-col justify-center order-1">
            <div class="mb-8">
              <div class="flex items-center gap-2 mb-2">
                <div class="w-1.5 h-1.5 bg-indigo-600 rounded-full animate-pulse"></div>
                <span class="text-xs font-medium text-indigo-600 uppercase tracking-wider">Secure Access</span>
              </div>
              <h1 class="text-3xl lg:text-4xl font-bold text-gray-900 tracking-tight mb-2">
                Welcome Back
              </h1>
              <p class="text-gray-500 text-sm lg:text-base">
                Login to access your <span class="text-indigo-600 font-medium">ZetaRise</span> account
              </p>
            </div>

            <form @submit.prevent="handleLogin" class="space-y-6">
              <div class="space-y-2">
                <label class="block text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Email Address
                </label>
                <div class="relative group">
                  <div
                    class="absolute inset-0 bg-indigo-600/5 rounded-lg blur-sm opacity-0 group-hover:opacity-100 transition-opacity">
                  </div>
                  <div class="relative flex items-center">
                    <span class="absolute left-4 text-gray-400">
                      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                        stroke="currentColor" class="w-5 h-5">
                        <path stroke-linecap="round" stroke-linejoin="round"
                          d="M21.75 6.75v10.5a2.25 2.25 0 01-2.25 2.25h-15a2.25 2.25 0 01-2.25-2.25V6.75m19.5 0A2.25 2.25 0 0019.5 4.5h-15a2.25 2.25 0 00-2.25 2.25m19.5 0v.243a2.25 2.25 0 01-1.07 1.916l-7.5 4.615a2.25 2.25 0 01-2.36 0L3.57 8.91a2.25 2.25 0 01-1.07-1.916V6.75" />
                      </svg>
                    </span>
                    <input v-model="loginEmail" type="email" placeholder="Enter your email"
                      class="w-full pl-12 pr-4 py-3.5 bg-gray-50 border border-gray-200 rounded-xl text-gray-900 placeholder:text-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-600/20 focus:border-indigo-600 transition-all"
                      required />
                  </div>
                </div>
              </div>

              <div class="space-y-2">
                <div class="flex justify-between items-center">
                  <label class="block text-xs font-semibold text-gray-700 uppercase tracking-wider">
                    Password
                  </label>
                  <button type="button" @click="forgotModalOpen = true"
                    class="text-xs font-medium text-indigo-600 hover:text-indigo-700 transition-colors underline underline-offset-2">
                    Forgot?
                  </button>

                <ForgotPassword v-if="forgotModalOpen"
                  :emailProp="loginEmail"
                  @close="forgotModalOpen = false" />
                </div>
                <div class="relative group">
                  <div
                    class="absolute inset-0 bg-indigo-600/5 rounded-lg blur-sm opacity-0 group-hover:opacity-100 transition-opacity">
                  </div>
                  <div class="relative flex items-center">
                    <span class="absolute left-4 text-gray-400">
                      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                        stroke="currentColor" class="w-5 h-5">
                        <path stroke-linecap="round" stroke-linejoin="round"
                          d="M16.5 10.5V6.75a4.5 4.5 0 10-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 002.25-2.25v-6.75a2.25 2.25 0 00-2.25-2.25H6.75a2.25 2.25 0 00-2.25 2.25v6.75a2.25 2.25 0 002.25 2.25z" />
                      </svg>
                    </span>
                    <input :type="showPassword ? 'text' : 'password'" v-model="loginPassword"
                      placeholder="Enter your password"
                      class="w-full pl-12 pr-12 py-3.5 bg-gray-50 border border-gray-200 rounded-xl text-gray-900 placeholder:text-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-600/20 focus:border-indigo-600 transition-all"
                      required />
                    <button type="button" @click="showPassword = !showPassword"
                      class="absolute right-4 text-gray-400 hover:text-indigo-600 transition-colors">
                      <svg v-if="showPassword" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                        stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                        <path stroke-linecap="round" stroke-linejoin="round"
                          d="M3.98 8.223A10.477 10.477 0 001.934 12C3.226 16.338 7.244 19.5 12 19.5c.993 0 1.953-.138 2.863-.395M6.228 6.228A10.45 10.45 0 0112 4.5c4.756 0 8.773 3.162 10.065 7.498a10.523 10.523 0 01-4.293 5.774M6.228 6.228L3 3m3.228 3.228l3.65 3.65m7.894 7.894L21 21m-3.228-3.228l-3.65-3.65m0 0a3 3 0 10-4.243-4.243m4.242 4.242L9.88 9.88" />
                      </svg>
                      <svg v-else xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                        stroke="currentColor" class="w-5 h-5">
                        <path stroke-linecap="round" stroke-linejoin="round"
                          d="M2.036 12.322a1.012 1.012 0 010-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178z" />
                        <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                      </svg>
                    </button>
                  </div>
                </div>
              </div>
  
              <button type="submit" :disabled="isLoading" class="relative w-full group mt-4">
                <div
                  class="absolute inset-0 bg-gradient-to-r from-indigo-600 to-purple-600 rounded-xl blur-lg opacity-60 group-hover:opacity-80 transition-opacity">
                </div>
                <div
                  class="relative w-full py-3.5 px-4 bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 text-white font-semibold rounded-xl transition-all transform active:scale-[0.98] flex items-center justify-center gap-2 shadow-lg shadow-indigo-600/25">
                  <span v-if="!isLoading">Login to Dashboard</span>
                  <div v-else class="flex items-center gap-2">
                    <svg class="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none"
                      viewBox="0 0 24 24">
                      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                      <path class="opacity-75" fill="currentColor"
                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                      </path>
                    </svg>
                    <span>Authenticating...</span>
                  </div>
                  <FontAwesomeIcon :icon="faArrowRight" class="text-sm group-hover:translate-x-1 transition-transform"
                    v-if="!isLoading" />
                </div>
              </button>

              <div class="relative pt-4">
                <div class="absolute inset-0 flex items-center">
                  <div class="w-full border-t border-gray-200"></div>
                </div>
                <div class="relative flex justify-center text-xs uppercase mt-4">
                  <span class="bg-white px-4 text-gray-500">New to ZetaRise?</span>
                </div>
              </div>

              <router-link to="/apply"
                class="w-full flex items-center justify-center gap-2 py-3.5 px-4 bg-white border-2 border-gray-200 hover:border-indigo-600 text-gray-700 hover:text-indigo-600 font-medium rounded-xl transition-all group">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                  stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
                </svg>
                Apply for new account
                <FontAwesomeIcon :icon="faArrowRight"
                  class="text-sm opacity-0 group-hover:opacity-100 group-hover:translate-x-1 transition-all" />
              </router-link>
            </form>

            <div class="mt-8 flex items-center justify-center gap-6 text-xs text-gray-400">
              <span class="flex items-center gap-1">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                </svg>
                256-bit SSL
              </span>
              <span class="flex items-center gap-1">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M9 12l2 2 4-5m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                </svg>
                RBI Regulated
              </span>
            </div>
          </div>

          <div
            class="relative order-2 bg-gradient-to-br from-indigo-600 to-purple-700 p-8 lg:p-12 flex flex-col justify-between overflow-hidden">
            <div class="absolute inset-0 opacity-10">
              <svg class="absolute inset-0 w-full h-full" xmlns="http://www.w3.org/2000/svg">
                <defs>
                  <pattern id="grid" width="32" height="32" patternUnits="userSpaceOnUse">
                    <path d="M0 0 L32 0 L32 32 L0 32 Z" fill="none" stroke="white" stroke-width="0.5"
                      stroke-opacity="0.3" />
                  </pattern>
                </defs>
                <rect width="100%" height="100%" fill="url(#grid)" />
              </svg>
            </div>

            <div class="absolute -top-20 -right-20 w-64 h-64 bg-white/10 rounded-full blur-3xl"></div>
            <div class="absolute -bottom-20 -left-20 w-64 h-64 bg-white/10 rounded-full blur-3xl"></div>

            <div class="relative">
              <div class="text-2xl font-bold text-white mb-6">
                ZetaRise
              </div>

              <div class="space-y-6">
                <div
                  class="inline-flex items-center gap-2 bg-white/20 backdrop-blur-sm px-4 py-2 rounded-full border border-white/30">
                  <span class="relative flex h-2 w-2">
                    <span
                      class="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
                    <span class="relative inline-flex rounded-full h-2 w-2 bg-emerald-500"></span>
                  </span>
                  <span class="text-xs font-medium text-white">TRUSTED BY 50K+ CUSTOMERS</span>
                </div>

                <h2 class="text-3xl lg:text-4xl font-bold text-white leading-tight">
                  Your Financial Future,
                  <span class="text-indigo-200">Reimagined</span>
                </h2>

                <p class="text-white/80 text-sm lg:text-base leading-relaxed">
                  Experience premium banking with 6.5% interest rate, zero balance options, and exclusive lifestyle
                  benefits. Join the digital banking revolution.
                </p>

                <div class="flex flex-wrap gap-3 pt-4">
                  <div v-for="feature in features" :key="feature"
                    class="px-3 py-1.5 bg-white/10 backdrop-blur-sm rounded-full border border-white/20 text-xs text-white/90 flex items-center gap-1.5">
                    <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                    </svg>
                    {{ feature }}
                  </div>
                </div>
              </div>
            </div>

            <div class="relative mt-12 bg-white/10 backdrop-blur-md rounded-2xl p-6 border border-white/20">
              <div class="flex items-center gap-3 mb-3">
                <div
                  class="w-10 h-10 bg-gradient-to-br from-amber-300 to-amber-500 rounded-full flex items-center justify-center text-white font-bold">
                  RS
                </div>
                <div>
                  <p class="text-sm font-semibold text-white">Rahul Sharma</p>
                  <p class="text-xs text-white/70">Premium Customer</p>
                </div>
              </div>
              <p class="text-sm text-white/90 italic">
                "Best banking experience! Zero hidden charges and the lounge access is a game-changer."
              </p>
              <div class="flex items-center gap-1 mt-3">
                <svg v-for="i in 5" :key="i" class="w-4 h-4 text-yellow-400 fill-current" viewBox="0 0 20 20">
                  <path
                    d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                </svg>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <footer class="w-full bg-white/80 backdrop-blur-md border-t border-gray-200/60 py-5 px-6">
      <div class="max-w-7xl mx-auto flex flex-col md:flex-row items-center justify-between gap-4">
        <div class="text-xs text-gray-500">
          <span class="font-semibold text-indigo-600">ZetaRise</span> © 2026 | RBI Licensed Digital Bank
        </div>
        <div class="flex items-center gap-6">
          <a href="#" class="text-xs text-gray-500 hover:text-indigo-600 transition-colors">Privacy</a>
          <a href="#" class="text-xs text-gray-500 hover:text-indigo-600 transition-colors">Terms</a>
          <a href="#" class="text-xs text-gray-500 hover:text-indigo-600 transition-colors">Support</a>
        </div>
      </div>
    </footer>
    <Toaster />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { mapActions } from "vuex";
import { toast, Toaster } from "@steveyuowo/vue-hot-toast";
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { UserRole } from "@/types/api/user";
import AppLogo from "@/components/AppLogo.vue";
import ForgotPassword from "@/components/ForgotPassword.vue";

export default defineComponent({
  name: "LoginView",
  components: {
    Toaster,
    FontAwesomeIcon,
    AppLogo,
    ForgotPassword
  },
  data() {
    return {
      loginEmail: "",
      loginPassword: "",
      showPassword: false,
      isLoading: false,
      forgotModalOpen: false,
      features: [
        "6.5% Interest",
        "Zero Balance",
        "Lounge Access",
        "24/7 Support"
      ],
      faArrowRight
    };
  },
  methods: {
    ...mapActions("auth", ["login"]),
    async handleLogin() {
      if (!this.loginEmail || !this.loginPassword) {
        toast.error("Please enter both email and password.");
        return;
      }

      this.isLoading = true;

      try {
        const response = await this.$store.dispatch("auth/login", {
          email: this.loginEmail,
          password: this.loginPassword
        })

        const currentUserRole = response.role;

        if (currentUserRole === UserRole.ADMIN || currentUserRole === UserRole.SUPER_ADMIN) {
          this.$router.push("/admin");
        } else if (currentUserRole === UserRole.USER) {
          this.$router.push("/me");
        } else {
          toast.error("Unknown user role. Please contact support.");
          this.$router.push("/");
        }
      } catch (error: any) {
        const msg =
          error.response?.data?.message ||
          "Login failed. Please check your credentials.";
        toast.error(msg);
      } finally {
        this.isLoading = false;
      }
    }
  }
});
</script>


<style scoped>
.bg-gradient-to-br .brand-logo span {
  color: #c7d2fe;
}

@keyframes blob {
  0% {
    transform: translate(0px, 0px) scale(1);
  }

  33% {
    transform: translate(30px, -50px) scale(1.1);
  }

  66% {
    transform: translate(-20px, 20px) scale(0.9);
  }

  100% {
    transform: translate(0px, 0px) scale(1);
  }
}

.animate-blob {
  animation: blob 7s infinite;
}

.animation-delay-2000 {
  animation-delay: 2s;
}

.animation-delay-4000 {
  animation-delay: 4s;
}

.focus\:ring-indigo-600\/20:focus {
  --tw-ring-color: rgba(79, 70, 229, 0.2);
}

.transition-all {
  transition-property: all;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 150ms;
}
</style>