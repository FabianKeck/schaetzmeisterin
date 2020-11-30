import React, { useState, useContext } from 'react';
import GameContext from './GameContext';
import {
  signInGamePost,
  startGamePost,
  betPost,
  getGame,
} from '../service/GameService';
import UserContext from '../context/UserContext';
import {
  loadGameDataFromLocalStorage,
  saveGameDataToLocalStorage,
} from '../service/LocalStorage';

export default function GameContextProvider({ children }) {
  const [game, setGame] = useState(loadGameDataFromLocalStorage);
  const { userSignIn, token } = useContext(UserContext);

  function signInGame(gameId, username) {
    return userSignIn(username)
      .then((token) => signInGamePost(token, gameId))
      .then((response) => response.data)
      .then((game) => {
        saveGameDataToLocalStorage(game);
        setGame(game);
        return game;
      });
  }
  const startGame = (gameId) =>
    startGamePost(token, gameId)
      .then((response) => response.data)
      .then(saveGameDataToLocalStorage)
      .then(setGame);

  const startGameLoop = () =>
    setInterval(
      () => getGame(token, game.id).then((response) => setGame(response.data)),
      5000
    );

  const bet = () =>
    betPost(token, game.id)
      .then((response) => response.data)
      .then(setGame);

  return (
    <GameContext.Provider
      value={{ game, signInGame, startGame, bet, startGameLoop }}
    >
      {children}
    </GameContext.Provider>
  );
}
