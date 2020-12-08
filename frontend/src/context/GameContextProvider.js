import React, { useState, useContext } from 'react';
import GameContext from './GameContext';
import {
  signInGamePost,
  startGamePost,
  askPost,
  guessPost,
  betPost,
  foldPost,
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

  const startGameLoop = () => {
    gameLoopId ||
      setGameLoopId(
        setInterval(
          () =>
            getGame(token, game.id)
              .then((response) => response.data)
              .then(setGameAndSaveToLocalStorage),
          5000
        )
      );
  };

  const stopGameLoop = () => {
    clearInterval(gameLoopId);
    setGameLoopId(0);
  };

  const ask = (question) =>
    askPost(token, game.id, question)
      .then((response) => response.data)
      .then(setGameAndSaveToLocalStorage);

  const guess = (guess) =>
    guessPost(token, game.id, guess)
      .then((response) => response.data)
      .then(setGameAndSaveToLocalStorage);

  const bet = (betValue) =>
    betPost(token, game.id, betValue)
      .then((response) => response.data)
      .then(setGameAndSaveToLocalStorage);

  const fold = () => {
    foldPost(token, game.id)
      .then((response) => response.data)
      .then(setGameAndSaveToLocalStorage());
  };

  function setGameAndSaveToLocalStorage(game) {
    setGame(game);
    saveGameDataToLocalStorage(game);
    return game;
  }

  return (
    <GameContext.Provider
      value={{
        game,
        signInGame,
        startGame,
        ask,
        guess,
        bet,
        startGameLoop,
        stopGameLoop,
        fold,
      }}
    >
      {children}
    </GameContext.Provider>
  );
}
