
// @author Guddati Venkata Sri Sumedha
export interface SignupData {
  name: string;
  email: string;
  password: string;
}

// @author Guddati Venkata Sri Sumedha
export interface LoginData {
  email: string;
  password: string;
}

// @author Guddati Venkata Sri Sumedha
export interface AuthResponse {
  token: string;
  role?: string;
}