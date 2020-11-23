import axios from 'axios';

export const signIn = (signInUser, gameId) => {
  return axios
    .post('/signin/' + gameId, signInUser)
    .then((response) => console.log(response.data));
};
