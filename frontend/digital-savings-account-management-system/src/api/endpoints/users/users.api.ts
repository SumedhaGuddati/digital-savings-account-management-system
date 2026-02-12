import apiManager from "@/api/apiManager";
import { USER_ACCOUNT_ENDPOINTS } from "./users.endpoints";

export const getUserHomeDetails = () => apiManager.get(USER_ACCOUNT_ENDPOINTS.GET_USER_HOME_DETAILS);
export const getUserProfileDetails = () => apiManager.get(USER_ACCOUNT_ENDPOINTS.GET_USER_PROFILE_DETAILS);
export const depositToAccount = (amount: number) => apiManager.post(USER_ACCOUNT_ENDPOINTS.DEPOSIT, { amount });
export const withdrawFromAccount = (amount: number) => apiManager.post(USER_ACCOUNT_ENDPOINTS.WITHDRAW, { amount });
export const getUserApplicationStatus = () => apiManager.get(USER_ACCOUNT_ENDPOINTS.GET_USER_APPLICATION_STATUS);
export const getUserTransactions = () => apiManager.get(USER_ACCOUNT_ENDPOINTS.GET_USER_TRANSACTIONS);