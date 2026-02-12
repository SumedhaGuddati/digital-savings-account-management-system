const TOKEN_KEY = 'zeta_token'

export const getAuthToken = (): string | null => {
  return sessionStorage.getItem(TOKEN_KEY)
}

export const setAuthToken = (token:string): void => {
  sessionStorage.setItem(TOKEN_KEY, token)
}

export const clearAuthToken = (): void => {
  sessionStorage.removeItem(TOKEN_KEY)
}
