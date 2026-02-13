import apiManager from "@/api/apiManager";
import { AUTH_ENDPOINTS } from "./auth.endpoints";

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

export const verifyOtpToken = (email: string, token: string) =>
    apiManager.post(AUTH_ENDPOINTS.VERIFY_TOKEN, { email, token });
