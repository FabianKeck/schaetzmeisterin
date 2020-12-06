import React from 'react';
import styled from 'styled-components/macro';
import PlayerCard from './PlayerCard';
import { GiCoins } from 'react-icons/gi';

export default function CardTable({ players, potValue }) {
  return (
    <CardTableStyled numPlayers={players.length}>
      {players.map((player, index) => (
        <PlayerCard
          row={getPlayerRowIndex(index, players.length)}
          column={getPlayerColumnIndex(index, players.length)}
          player={player}
        />
      ))}
      <PotInfoStyled>
        <GiCoins />
        {potValue}
      </PotInfoStyled>
    </CardTableStyled>
  );

  function getPlayerColumnIndex(playerIndex, numPlayers) {
    return (
      Math.max(Math.min(playerIndex - Math.floor(numPlayers / 2 - 2), 3), 0) + 1
    );
  }

  function getPlayerRowIndex(playerIndex, numPlayers) {
    const delta = Math.floor(numPlayers / 2) - 0.5;
    const dif = playerIndex - delta;
    const abs = Math.abs(dif);
    return Math.floor(abs) + 1;
  }
}

const CardTableStyled = styled.div`
  position: relative;
  display: grid;

  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: repeat(4, 1fr);
  grid-gap: var(--size-s);
`;

const PotInfoStyled = styled.div`
  display: flex;
  align-items: center;
  position: absolute;
  font-size: x-large;
  bottom: 20%;
  left: 50%;
  transform: translateX(-50%);
`;
