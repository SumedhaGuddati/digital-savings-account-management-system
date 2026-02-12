import axios from 'axios';
import { getAuthToken } from '@/utils/auth.token';

const apiClient = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL,
    timeout: 15000,
    headers: {
        'Content-Type': 'application/json',
    },
})

apiClient.interceptors.request.use(config => {
    const token = getAuthToken()
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

apiClient.interceptors.response.use(
    response => response,
    error => {
        return Promise.reject(error.response?.data || error)
    }
)

export default apiClient