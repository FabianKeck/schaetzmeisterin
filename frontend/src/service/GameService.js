import axios from 'axios';

const header = (token) => ({
  headers: {
    Authorization: `Bearer ${token}`,
  },
});

export const signInGamePost = (token, username, gameId) => {
  const url = '/api/game/signin/' + (gameId ? gameId : '');
  console.log('Ganmeservice' + username);
  return axios.post(url, { name: username }, header(token));
};

export const startGame = (token, gameId) => {
  axios.post('/api/game/startgame/' + gameId, null, header(token));
};
