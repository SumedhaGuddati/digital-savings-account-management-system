// @author Guddati Venkata Sri Sumedha
export interface SignupRequest {
  name: string;
  email: string;
  password: string;
}

// @author Guddati Venkata Sri Sumedha
export interface LoginRequest {
  email: string;
  password: string;
}

// @author Guddati Venkata Sri Sumedha
export interface AuthResponse {
  token: string;
  role: "USER" | "ADMIN";
}
