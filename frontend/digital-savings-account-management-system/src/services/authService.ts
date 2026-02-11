// Send verification token (mock email)
export const sendVerificationToken = async (email: string) => {
  await axios.post(`${API_URL}/send-verification`, { email });
};

// Verify OTP/token
export const verifyOtpToken = async (email: string, token: string) => {
  await axios.post(`${API_URL}/verify-token`, { email, token });
};
import axios from "axios";

export const API_URL = "http://localhost:8080/api/auth"; // backend URL

export interface SignupData {
  name: string;
  email: string;
  password: string;
}

export interface LoginData {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  role?: string;
}

// Signup function
export const signup = async (data: SignupData) => {
  const response = await axios.post<AuthResponse>(`${API_URL}/signup`, data);
  return response.data;
};

// Login function
export const login = async (data: LoginData) => {
  const response = await axios.post<AuthResponse>(`${API_URL}/login`, data);
  return response.data;
};
