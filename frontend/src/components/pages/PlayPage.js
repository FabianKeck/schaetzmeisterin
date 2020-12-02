import React, { useContext, useEffect, useState } from 'react';
import Header from '../commons/Header';
import UserContext from '../../context/UserContext';
import GameContext from '../../context/GameContext';
import BetCard from '../PlayComponents/BetCard';
import PlayerCard from '../PlayComponents/PlayerCard';
import styled from 'styled-components/macro';

export default function PlayPage() {
  const { userData } = useContext(UserContext);
  const { game, bet } = useContext(GameContext);
  const [active, setActive] = useState(false);
  useEffect(() => {
    setActive(isActive(userData.playerId));
  }, [game, userData.playerId]);

  return (
    <PlayPageStyled>
      <Header>Playing</Header>
      <body>

        <BetCard
          bet={active && bet}
          minBet={calcMinBet()}
          cash={getPlayerData().cash}
        />
        {game.players
          .filter((player) => player.id !== userData.playerId)
          .map((player) => (
            <PlayerCard player={player} active={isActive(player.id)} />
          ))}
      </body>
    </PlayPageStyled>
  );
  function getPlayerData() {
    return game?.players?.find((player) => player.id === userData.playerId);
  }
  function calcMinBet() {
    return (
      Math.max(...game.players.map((player) => player.currentBet)) -
      getPlayerData().currentBet
    );
  }
  function isActive(id) {
    return id === game.players[game.activePlayerIndex].id;
  }
}
const PlayPageStyled = styled.div`
  display: grid;
  grid-gap: var(--size-xs);
  grid-auto-rows: min-content;

  body {
    display: grid;
    grid-gap: var(--size-xs);
    padding: 0 var(--size-xs);
  }
`;
