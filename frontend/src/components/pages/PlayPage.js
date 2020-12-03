import React, { useContext, useEffect, useState } from 'react';
import Header from '../commons/Header';
import UserContext from '../../context/UserContext';
import GameContext from '../../context/GameContext';
import BetCard from '../PlayComponents/BetCard';
import PlayerCard from '../PlayComponents/PlayerCard';
import styled from 'styled-components/macro';
import PotInfo from '../PlayComponents/PotInfo';

export default function PlayPage() {
  const { userData } = useContext(UserContext);
  const { game, bet, fold } = useContext(GameContext);
  const [active, setActive] = useState(false);
  useEffect(() => {
    setActive(isActive(userData.playerId));
    // eslint-disable-next-line
  }, [game, userData.playerId]);

  return (
    <PlayPageStyled>
      <Header>Playing</Header>
      <body>
        <PotInfo value={calcPot()} />
        <BetCard
          bet={bet}
          fold={fold}
          minBet={calcMinBet()}
          cash={getPlayerData().cash}
          active={active}
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
    return game.players.find((player) => player.id === userData.playerId);
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

  function calcPot() {
    return game.players
      .map((player) => player.currentBet)
      .reduce((sum, currentBet) => sum + currentBet);
  }
}
const PlayPageStyled = styled.div`
  display: grid;
  grid-gap: var(--size-xs);
  grid-auto-rows: min-content;

  body {
    display: grid;
    overflow: scroll;
    grid-gap: var(--size-xs);
    padding: 0 var(--size-xs);
  }
`;
