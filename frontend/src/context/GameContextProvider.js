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
  const [gameLoopId, setGameLoopId] = useState(0);

  function signInGame(gameId, username) {
    return userSignIn(username)
      .then((token) => signInGamePost(token, gameId))
      .then((response) => response.data)
      .then(setGameAndSaveToLocalStorage);
  }

  const startGame = (gameId) =>
    startGamePost(token, gameId)
      .then((response) => response.data)
      .then(setGameAndSaveToLocalStorage);

  const startGameLoop = () =>
    setGameLoopId(
      setInterval(
        () =>
          getGame(token, game.id)
            .then((response) => response.data)
            .then(setGameAndSaveToLocalStorage),
        5000
      )
    );

  const stopGameLoop = () => {
    clearInterval(gameLoopId);
    setGameLoopId(0);
  };

  const bet = (betValue) =>
    betPost(token, game.id, betValue)
      .then((response) => response.data)
      .then(setGameAndSaveToLocalStorage);

  function setGameAndSaveToLocalStorage(game) {
    setGame(game);
    saveGameDataToLocalStorage(game);
    return game;
  }

  return (
    <GameContext.Provider
      value={{ game, signInGame, startGame, bet, startGameLoop, stopGameLoop }}
    >
      {children}
    </GameContext.Provider>
  );
}
