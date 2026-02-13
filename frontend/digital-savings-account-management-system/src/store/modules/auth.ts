// store/modules/auth.module.ts
import { Module } from "vuex";
import { clearAuthToken, setAuthToken, TOKEN_KEY } from "@/utils/auth.token";
import { getApplicationStatus, loginRequest, signupRequest, resetPassword, sendForgotPasswordOtp, sendVerificationToken } from "@/api/endpoints/auth/auth.api";

export interface SignupRequest {
  name: string;
  email: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  role: string; // backend may return lowercase
}

export type Role = "USER" | "ADMIN" | "SUPER_ADMIN";

export interface User {
  id: number;
  name: string;
  email: string;
  role: Role;
  createdAt: string;
}

export type AccountStatus =
  | "ACTIVE"
  | "PENDING"
  | "REJECTED"
  | "NONE";

/* =======================
   State
======================= */

export interface AuthState {
  user: User | null;
  token: string | null;
  loading: boolean;
  error: string | null;
  accountStatus: AccountStatus;
  accountStatusLoading: boolean;
}

const state: AuthState = {
  user: safelyParseUser(),
  token: sessionStorage.getItem(TOKEN_KEY),
  loading: false,
  error: null,
  accountStatus: "NONE",
  accountStatusLoading: false,
};

/* =======================
   Helpers
======================= */

function safelyParseUser(): User | null {
  try {
    return JSON.parse(sessionStorage.getItem("user") || "null");
  } catch {
    return null;
  }
}

/* =======================
   Getters
======================= */

const getters = {
  isAuthenticated: (state: AuthState) => !!state.token,
  isAdmin: (state: AuthState) =>
    state.user?.role === "ADMIN" || state.user?.role === "SUPER_ADMIN",
  isUser: (state: AuthState) => state.user?.role === "USER",
  accountStatus: (state: AuthState) => state.accountStatus,
};

/* =======================
   Mutations
======================= */

const mutations = {
  SET_LOADING(state: AuthState, payload: boolean) {
    state.loading = payload;
  },
  SET_ERROR(state: AuthState, payload: string | null) {
    state.error = payload;
  },
  SET_AUTH(state: AuthState, payload: { user: User; token: string }) {
    state.user = payload.user;
    state.token = payload.token;
    state.error = null;
    setAuthToken(payload.token);
    sessionStorage.setItem("user", JSON.stringify(payload.user));
  },
  CLEAR_AUTH(state: AuthState) {
    state.user = null;
    state.token = null;
    state.accountStatus = "NONE";
    clearAuthToken();
    sessionStorage.removeItem("user");
    sessionStorage.removeItem("accountStatus");
  },
  SET_ACCOUNT_STATUS(state: AuthState, payload: AccountStatus) {
    state.accountStatus = payload;
  },
  SET_ACCOUNT_STATUS_LOADING(state: AuthState, payload: boolean) {
    state.accountStatusLoading = payload;
  },
};

/* =======================
   Actions
======================= */

const actions = {
  // LOGIN
  async login({ commit }: any, credentials: LoginRequest) {
    commit("SET_LOADING", true);
    commit("SET_ERROR", null);
    try {
      const response = await loginRequest(credentials);
      const token = response.data.token;
      const role = response.data.role.toUpperCase();

      const user: User = {
        id: 0, // replace when backend returns real user
        name: credentials.email.split("@")[0],
        email: credentials.email,
        role: role as Role,
        createdAt: new Date().toISOString(),
      };

      commit("SET_AUTH", { user, token });
    
      return { role };
    } catch (error: any) {
      const msg = error.response?.data?.message || "Login failed";
      commit("SET_ERROR", msg);
      throw error;
    } finally {
      commit("SET_LOADING", false);
    }
  },

  async signup({ commit }: any, payload: SignupRequest) { 
    commit("SET_LOADING", true);
    commit("SET_ERROR", null);

    try {
      const response = await signupRequest(payload);
      
      const token = response.data.token;
      const role = response.data.role.toUpperCase();

      const user: User = {
        id: response.data.userId || 0,
        name: payload.name,
        email: payload.email,
        role: role as Role,
        createdAt: new Date().toISOString(),
      }
      commit("SET_AUTH", { user, token });
      
      return { role };
    } catch (error: any) {
      const msg =
        error.response?.data?.message || "Signup failed";
      commit("SET_ERROR", msg);
      throw error;
    } finally {
      commit("SET_LOADING", false);
    }
  },

  // LOGOUT
  logout({ commit }: any) {
    commit("CLEAR_AUTH");
  },

  // SEND VERIFICATION OTP (signup)
  async sendOtp({ commit }: any, payload: { email: string }) {
    commit("SET_LOADING", true);
    commit("SET_ERROR", null);
    try {
      await sendVerificationToken(payload.email);
    } catch (error: any) {
      const msg = error.response?.data?.message || "Failed to send OTP";
      commit("SET_ERROR", msg);
      throw error;
    } finally {
      commit("SET_LOADING", false);
    }
  },

  // FORGOT PASSWORD: Send OTP
  async sendForgotPasswordOtp({ commit }: any, payload: { email: string }) {
    commit("SET_LOADING", true);
    commit("SET_ERROR", null);
    try {
      await sendForgotPasswordOtp(payload.email);
    } catch (error: any) {
      const msg = error.response?.data?.message || "Failed to send OTP";
      commit("SET_ERROR", msg);
      throw error;
    } finally {
      commit("SET_LOADING", false);
    }
  },

  // FORGOT PASSWORD: Reset
  async resetPassword(
    { commit }: any,
    payload: { email: string; otp: string; newPassword: string }
  ) {
    commit("SET_LOADING", true);
    commit("SET_ERROR", null);
    try {
      await resetPassword(payload);
    } catch (error: any) {
      const msg = error.response?.data?.message || "Reset password failed";
      commit("SET_ERROR", msg);
      throw error;
    } finally {
      commit("SET_LOADING", false);
    }
  },

  // FETCH ACCOUNT STATUS
  async fetchAccountStatus({ commit }: any) {
    commit("SET_ACCOUNT_STATUS_LOADING", true);
    try {
      const response = await getApplicationStatus();
      const status = response.data.status?.toUpperCase() || "NONE";
      commit("SET_ACCOUNT_STATUS", status);
      return status;
    } catch (error: any) {
      if (error.response?.status === 404) {
        commit("SET_ACCOUNT_STATUS", "NONE");
        return "NONE";
      }
      throw error;
    } finally {
      commit("SET_ACCOUNT_STATUS_LOADING", false);
    }
  },
};

/* =======================
   Module
======================= */

const authModule: Module<AuthState, any> = {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
};

export default authModule;
