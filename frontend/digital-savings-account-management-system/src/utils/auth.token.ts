const TOKEN_KEY = 'auth_token'

export const getAuthToken = (): string | null => {
  return sessionStorage.getItem(TOKEN_KEY)
}

export const setAuthToken = (): void => {
  sessionStorage.setItem(TOKEN_KEY, "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MUBleGFtcGxlLmNvbSIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzcwODk3MzE3LCJleHAiOjE3NzA5ODM3MTd9.Ca3nNUjGkp1kcfw6hFZjI1NeTCnrTmvq9gMsv93tJkg")
}

export const clearAuthToken = (): void => {
  sessionStorage.removeItem(TOKEN_KEY)
}
