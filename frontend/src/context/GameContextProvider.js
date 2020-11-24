import React, { useState, useContext } from 'react';
import GameContext from './GameContext';
import { signInGamePost, startGame } from '../service/GameService';
import UserContext from '../context/UserContext';

export default function GameContextProvider({ children }) {
  const [game, setGame] = useState({});
  const { userSignIn } = useContext(UserContext);

  function signInGame(gameId, username) {
    return userSignIn(username)
      .then((token) => {
        return signInGamePost(token, username, gameId);
      })
      .then((response) => response.data)
      .then((game) => {
        setGame(game);
        return game;
      });
  }

  return (
    <GameContext.Provider value={{ game, signInGame, startGame }}>
      {children}
    </GameContext.Provider>
  );
}
