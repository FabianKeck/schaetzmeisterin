import React, { useContext } from 'react';
import Header from '../commons/Header';
import UserContext from '../../context/UserContext';
import GameContext from '../../context/GameContext';

export default function PlayPage() {
  const { userData } = useContext(UserContext);
  const { game, bet } = useContext(GameContext);

  return (
    <>
      <Header>Playing</Header>
      <p>{JSON.stringify(getPlayerData)}</p>
      {game?.players[game.activePlayerIndex].id === userData.playerId && (
        <button onClick={bet}>bet!</button>
      )}
    </>
  );
  function getPlayerData() {
    return game?.players?.find((player) => player.id === userData.playerId);
  }
}
