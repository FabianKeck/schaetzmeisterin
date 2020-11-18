import axios from 'axios';

export const signIn = (signInUser) => {
  return axios.post('/signin', signInUser).then((response) => response.data);
};
