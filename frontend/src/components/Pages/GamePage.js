import React, { useContext } from 'react';
import GameContext from '../../context/GameContext';

export default function GamePage() {
  const { user, game } = useContext(GameContext);
  return (
    <>
      <p>You are logged in as {user}</p>
      <p>So for these users have joined the game</p>
      <ul>
        {game?.players?.map((player) => (
          <li key={player.name}>{player.name}</li>
        ))}
      </ul>
    </>
  );
}
