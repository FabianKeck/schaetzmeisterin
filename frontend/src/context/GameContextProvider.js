import React, { useState } from 'react';
import GameContext from './GameContext';
import { signIn } from '../Service/SignInService';

export default function GameContextProvider({ children }) {
  const [game, setGame] = useState({});
  const [user, setUser] = useState('');

  const signInWithUser = (user, gameId) => {
    return signIn({ name: user }, gameId).then((game) => {
      setGame(game);
      setUser(user);
      return game;
    });
  };

  return (
    <GameContext.Provider value={{ game, user, signInWithUser }}>
      {children}
    </GameContext.Provider>
  );
}
