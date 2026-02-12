import { createStore } from "vuex";
import auth, { AuthState } from "./modules/auth";
import axios from "axios";

// Root state interface
export interface RootState {
  auth: AuthState;
}

// Create store
const store = createStore<RootState>({
  modules: {
    auth,
  },
});

// Initialize auth state from sessionStorage
const token = sessionStorage.getItem("token");
if (token) {
  axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}

export default store;
