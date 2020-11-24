const TOKEN = 'TOKEN';
const USER_DATA = 'USER_DATA';

export const saveTokenToLocalStorage = (token) =>
  localStorage.setItem(TOKEN, token);

export const loadTokenFromLocalStorage = () => localStorage.getItem(TOKEN);

export const saveUserDataToLocalStorage = (userData) =>
  localStorage.setItem(USER_DATA, JSON.stringify(userData));

export const loadUserDataFromLocalStorage = () => {
  const raw = localStorage.getItem(USER_DATA);
  try {
    return JSON.parse(raw);
  } catch (e) {
    console.error(e);
  }
};
