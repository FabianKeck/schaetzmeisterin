import axios from 'axios';

const header = (token) => ({
  headers: {
    Authorization: `Bearer ${token}`,
  },
});

export const signInGamePost = (token, gameId) => {
  const url = '/api/game/signin/' + (gameId ? gameId : '');
  return axios.post(url, null, header(token));
};

export const startGamePost = (token, gameId) => {
  return axios.post('/api/game/startgame/' + gameId, null, header(token));
};

export const betPost = (token, gameId, betValue) =>
  axios.post('/api/game/bet/' + gameId, betValue, header(token));

export const getGame = (token, gameId) =>
  axios.get('/api/game/' + gameId, header(token));
