import { Module } from "vuex";
import axios from "axios";

// Your Interfaces
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
  role: "USER" | "ADMIN";
}

// Extended User interface with more fields
export interface User {
  id: number;
  name: string;
  email: string;
  role: "USER" | "ADMIN";
  createdAt: string;
}

// State interface
export interface AuthState {
  user: User | null;
  token: string | null;
  loading: boolean;
  error: string | null;
}

// Initial state
const state: AuthState = {
  user: JSON.parse(localStorage.getItem("user") || "null"),
  token: localStorage.getItem("token"),
  loading: false,
  error: null,
};

// Getters
const getters = {
  isAuthenticated: (state: AuthState): boolean => !!state.token,

  isAdmin: (state: AuthState): boolean => state.user?.role === "ADMIN",

  isUser: (state: AuthState): boolean => state.user?.role === "USER",

  currentUser: (state: AuthState): User | null => state.user,

  authToken: (state: AuthState): string | null => state.token,

  authLoading: (state: AuthState): boolean => state.loading,

  authError: (state: AuthState): string | null => state.error,
};

// Mutations
const mutations = {
  SET_LOADING(state: AuthState, payload: boolean): void {
    state.loading = payload;
  },

  SET_ERROR(state: AuthState, payload: string | null): void {
    state.error = payload;
  },

  SET_AUTH(state: AuthState, payload: { user: User; token: string }): void {
    state.user = payload.user;
    state.token = payload.token;
    state.error = null;
  },

  CLEAR_AUTH(state: AuthState): void {
    state.user = null;
    state.token = null;
    state.error = null;
  },
};

// Actions
const actions = {
  // Signup Action
  async signup(
    { commit }: any,
    signupData: SignupRequest
  ): Promise<AuthResponse> {
    commit("SET_LOADING", true);
    commit("SET_ERROR", null);

    try {
      const response = await axios.post<AuthResponse>(
        "/auth/signup",
        signupData
      );
      const { token, role } = response.data;

      // Create user object from signup data and response
      const user: User = {
        id: Date.now(), // This will come from backend
        name: signupData.name,
        email: signupData.email,
        role: role,
        createdAt: new Date().toISOString(),
      };

      // Save to localStorage
      localStorage.setItem("token", token);
      localStorage.setItem("user", JSON.stringify(user));

      // Set default Authorization header
      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;

      commit("SET_AUTH", { user, token });
      return response.data;
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || "Signup failed";
      commit("SET_ERROR", errorMessage);
      throw error;
    } finally {
      commit("SET_LOADING", false);
    }
  },

  // Login Action
  async login(
    { commit }: any,
    credentials: LoginRequest
  ): Promise<AuthResponse> {
    commit("SET_LOADING", true);
    commit("SET_ERROR", null);

    try {
      const response = await axios.post<AuthResponse>(
        "/auth/login",
        credentials
      );
      const { token, role } = response.data;

      // Fetch full user details after login
      // You'll need to get user details from backend
      const user: User = {
        id: 1, // This will come from backend
        name: credentials.email.split("@")[0], // Temporary - replace with actual user data
        email: credentials.email,
        role: role,
        createdAt: new Date().toISOString(),
      };

      // Save to localStorage
      localStorage.setItem("token", token);
      localStorage.setItem("user", JSON.stringify(user));

      // Set default Authorization header
      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;

      commit("SET_AUTH", { user, token });
      return response.data;
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || "Login failed";
      commit("SET_ERROR", errorMessage);
      throw error;
    } finally {
      commit("SET_LOADING", false);
    }
  },

  // Logout Action
  logout({ commit }: any): void {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    delete axios.defaults.headers.common["Authorization"];
    commit("CLEAR_AUTH");
  },

  // Clear Error
  clearError({ commit }: any): void {
    commit("SET_ERROR", null);
  },

  // Fetch User Profile (call after login to get full user data)
  async fetchUserProfile({ commit, state }: any): Promise<User> {
    try {
      const response = await axios.get<User>("/auth/profile");
      const user = response.data;

      localStorage.setItem("user", JSON.stringify(user));
      commit("SET_AUTH", { user, token: state.token });
      return user;
    } catch (error) {
      throw error;
    }
  },
};

// Module
const authModule: Module<AuthState, any> = {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
};

export default authModule;
