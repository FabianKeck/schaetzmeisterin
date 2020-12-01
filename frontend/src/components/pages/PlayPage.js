import React, { useContext, useEffect, useState } from 'react';
import Header from '../commons/Header';
import UserContext from '../../context/UserContext';
import GameContext from '../../context/GameContext';
import BetCard from '../PlayComponents/BetCard';

export default function PlayPage() {
  const { userData } = useContext(UserContext);
  const { game, bet } = useContext(GameContext);
  const [active, setActive] = useState(false);
  useEffect(() => {
    setActive(game?.players[game.activePlayerIndex].id === userData.playerId);
  }, [game]);

  return (
    <>
      <Header>Playing</Header>
      <BetCard
        bet={active && bet}
        minBet={getMaxOfBets()}
        cash={getPlayerData().cash}
      />
    </>
  );
  function getPlayerData() {
    return game?.players?.find((player) => player.id === userData.playerId);
  }
  function getMaxOfBets() {
    Math.max(...game.players.map((player) => player.currentBet));
  }
}
