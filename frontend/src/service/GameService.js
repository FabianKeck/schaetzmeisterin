import axios from 'axios';

const header = (token) => ({
  headers: {
    Authorization: `Bearer ${token}`,
  },
});

export const signInGamePost = (token, gameId) =>
  axios.post('/api/game/signin' + gameId, null, header(token));
