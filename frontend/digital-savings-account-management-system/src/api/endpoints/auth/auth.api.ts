import apiManager from "@/api/apiManager";
import { AUTH_ENDPOINTS } from "./auth.endpoints";

// @author Guddati Venkata Sri Sumedha

export const loginRequest = (data: {
    email: string;
    password: string;
}) => apiManager.post(AUTH_ENDPOINTS.LOGIN, data);

export const signupRequest = (data: {
    name: string;
    email: string;
    password: string;
}) => apiManager.post(AUTH_ENDPOINTS.SIGNUP, data);

export const applyForAccount = (data: any) =>
    apiManager.post(AUTH_ENDPOINTS.APPLY_ACCOUNT, data);

export const getApplicationStatus = () =>
    apiManager.get(AUTH_ENDPOINTS.GET_APPLICATION_STATUS);

export const sendVerificationToken = (email: string) =>
    apiManager.post(AUTH_ENDPOINTS.SEND_VERIFICATION, { email });

export const verifyOtpToken = (email: string, otp: string) =>
    apiManager.post(AUTH_ENDPOINTS.VERIFY_OTP, { email, otp });

export const sendForgotPasswordOtp = (email: string) =>
    apiManager.post(AUTH_ENDPOINTS.FORGOT_PASSWORD_SEND_OTP, null, { params: { email } });

export const resetPassword = (data: { email: string; otp: string; newPassword: string }) =>
    apiManager.post(AUTH_ENDPOINTS.FORGOT_PASSWORD_RESET, data);

export const sendOtpToEmail = (email: string) =>
    apiManager.post(AUTH_ENDPOINTS.SEND_OTP, null, { params: { email } });
