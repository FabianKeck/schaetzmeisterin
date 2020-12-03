import React, { useContext, useEffect } from 'react';
import GameContext from '../../context/GameContext';
import GameStagingPage from './GameStagingPage';
import PlayPage from './PlayPage';

export default function GamePage() {
  const { game, startGameLoop, stopGameLoop } = useContext(GameContext);
  useEffect(() => {
    startGameLoop();
    return stopGameLoop;
  }, [startGameLoop, stopGameLoop]);

  if (!game) return null;
  return game?.started ? <PlayPage /> : <GameStagingPage />;
}
