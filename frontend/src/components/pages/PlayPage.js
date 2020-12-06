import React, { useContext, useEffect, useState } from 'react';
import Header from '../commons/Header';
import UserContext from '../../context/UserContext';
import GameContext from '../../context/GameContext';
import SelfCard from '../PlayComponents/SelfCard';
import styled from 'styled-components/macro';
import CardTable from '../PlayComponents/CardTable';

export default function PlayPage() {
  const { userData } = useContext(UserContext);
  const { game, bet, fold } = useContext(GameContext);
  const [active, setActive] = useState(false);
  useEffect(() => {
    setActive(isActive(userData.playerId));
    // eslint-disable-next-line
  }, [game, userData.playerId]);

  return (
    <>
      <Header>Schaetzmeisterin</Header>
      <PlayPageStyled>
        <CardTable
          players={game.betSession.players.filter(
            (player) => player.id !== userData.playerId
          )}
          potValue={calcPot()}
        />

        <SelfCard
          bet={bet}
          fold={fold}
          minBet={calcMinBet()}
          cash={getPlayerData().cash}
          active={active}
        />
      </PlayPageStyled>
    </>
  );
  function getPlayerData() {
    return game.betSession.players.find(
      (player) => player.id === userData.playerId
    );
  }
  function calcMinBet() {
    return (
      Math.max(...game.betSession.players.map((player) => player.currentBet)) -
      getPlayerData().currentBet
    );
  }
  function isActive(id) {
    return id === game.betSession.players[game.betSession.activePlayerIndex].id;
  }

  function calcPot() {
    return game.betSession.players
      .map((player) => player.currentBet)
      .reduce((sum, currentBet) => sum + currentBet);
  }
}
const PlayPageStyled = styled.main`
  display: grid;
  grid-gap: var(--size-s);
  padding: var(--size-xs);
  grid-template-rows: 1fr min-content;
`;
