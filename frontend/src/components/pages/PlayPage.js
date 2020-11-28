import React, { useContext, useEffect, useState } from 'react';
import Header from '../commons/Header';
import GameContext from '../../context/GameContext';
import UserContext from '../../context/UserContext';

export default function PlayPage() {
  const { userData } = useContext(UserContext);
  const { game, bet } = useContext(GameContext);
  const [playerData, setPlayerData] = useState();
  useEffect(() => {
    setPlayerData(
      game?.players?.find((player) => player.id === userData.playerId)
    );
  }, [game, userData]);

  return (
    <>
      <Header>Playing</Header>
      <p>{JSON.stringify(playerData)}</p>
      {userData.gameAction === 'BET' && <button onClick={bet}>bet!</button>}
    </>
  );
}
