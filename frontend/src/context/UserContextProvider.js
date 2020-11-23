import React, { useState } from 'react';
import UserContext from './UserContext';
import axios from 'axios';
import jwtDecode from 'jwt-decode';
import {
  loadTokenFromLocalStorage,
  loadUserDataFromLocalStorage,
  saveTokenToLocalStorage,
  saveUserDataToLocalStorage,
} from '../service/LocalStorage';

export default function UserContextProvider({ children }) {
  const [token, setToken] = useState(loadTokenFromLocalStorage());
  const [userData, setUserData] = useState(loadUserDataFromLocalStorage);

  const userSignIn = (username) =>
    axios
      .get('/signin/?username=' + username)
      .then((response) => response.data)
      .then((token) => {
        setToken(token);
        saveTokenToLocalStorage(token);
        try {
          const decoded = jwtDecode(token);
          console.log(decoded);
          setUserData(decoded);
          saveUserDataToLocalStorage(decoded);
        } catch (e) {
          console.log(e);
        }
      });

  return (
    <UserContext.Provider value={{ userData, token, userSignIn }}>
      {children}
    </UserContext.Provider>
  );
}
