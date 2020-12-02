import React, { useContext } from 'react';
import GameContext from '../../context/GameContext';
import GameStagingPage from './GameStagingPage';
import PlayPage from './PlayPage';

export default function GamePage() {
  const { game } = useContext(GameContext);

  if (!game) return null;
  return game?.started ? <PlayPage /> : <GameStagingPage />;
}
