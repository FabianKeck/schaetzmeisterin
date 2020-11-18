import axios from 'axios';

export const signIn = (signInUser, gameId) => {
  return axios
    .post('/api/signin/' + gameId, signInUser)
    .then((response) => response.data);
};
