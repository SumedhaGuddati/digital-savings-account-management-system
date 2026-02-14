import axios from 'axios';
import { getAuthToken } from '@/utils/auth.token';

const apiManager = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL,
    timeout: 15000,
    headers: {
        'Content-Type': 'application/json',
    },
})

apiManager.interceptors.request.use(config => {
    const token = getAuthToken()
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

apiManager.interceptors.response.use(
    response => response,
    error => {
        return Promise.reject(error.response?.data || error)
    }
)

export default apiManager