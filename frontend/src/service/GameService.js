import axios from 'axios';

const header = (token) => ({
  headers: {
    Authorization: `Bearer ${token}`,
  },
});

export const signInGame = (token, gameId) =>
  axios.post('/api/game/signin' + gameId, header(token));
