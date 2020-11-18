import React, { useState } from 'react';
import GameContext from './GameContext';
import { signIn } from '../Service/SignInService';

export default function GameContextProvider({ children }) {
  const [game, setGame] = useState({});
  const [user, setUser] = useState('');

  const signInWithUser = (user) => {
    return signIn({ user })
      .then((response) => response.data)
      .then((game) => setGame(game))
      .then(() => setUser(user));
  };

  return (
    <GameContext.Provider value={{ game, user, signInWithUser }}>
      {children}
    </GameContext.Provider>
  );
}
