import React, { useContext } from 'react';
import GameContext from '../../context/GameContext';

export default function GamePage() {
  const { user } = useContext(GameContext);
  return <p>You are logged in as {user};</p>;
}
