import React, { useState, useContext } from 'react';
import GameContext from './GameContext';
import { signInGamePost } from '../service/GameService';
import UserContext from '../context/UserContext';

export default function GameContextProvider({ children }) {
  const [game, setGame] = useState({});
  const { token } = useContext(UserContext);

  const signInGame = (gameId) => {
    return signInGamePost(token, gameId)
      .then((response) => response.data)
      .then((game) => {
        setGame(game);
        return game;
      });
  };

  return (
    <GameContext.Provider value={{ game, signInGame }}>
      {children}
    </GameContext.Provider>
  );
}
