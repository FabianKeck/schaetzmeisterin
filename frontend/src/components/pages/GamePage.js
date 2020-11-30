import React, { useContext } from 'react';
import GameContext from '../../context/GameContext';
import GameStagingPage from './GameStagingPage';
import PlayPage from './PlayPage';

export default function GamePage() {
  const { game } = useContext(GameContext);

  return game?.started ? <PlayPage /> : <GameStagingPage />;
}
