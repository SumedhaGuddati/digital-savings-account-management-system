import { createStore } from "vuex";
import auth, { AuthState } from "./modules/auth";
import axios from "axios";
import { TOKEN_KEY } from "@/utils/auth.token";

export interface RootState {
  auth: AuthState;
}

const store = createStore<RootState>({
  modules: {
    auth,
  },
});

const token = sessionStorage.getItem(TOKEN_KEY);
if (token) {
  axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}

export default store;
