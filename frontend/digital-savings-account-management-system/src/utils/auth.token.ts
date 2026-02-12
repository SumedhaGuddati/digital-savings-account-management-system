const TOKEN_KEY = 'auth_token'

export const getAuthToken = (): string | null => {
  return sessionStorage.getItem(TOKEN_KEY)
}

export const setAuthToken = (): void => {
  sessionStorage.setItem(TOKEN_KEY, "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MUBleGFtcGxlLmNvbSIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzcwODgwOTAyLCJleHAiOjE3NzA5NjczMDJ9.cdWEbuNDWzx6OYavx8ED9Blp4pdYUJCreVAdQtkG0Sk")
}

export const clearAuthToken = (): void => {
  sessionStorage.removeItem(TOKEN_KEY)
}
