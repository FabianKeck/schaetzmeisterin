import React from 'react';
import styled from 'styled-components/macro';

export default function PlayerCard({ player, active }) {
  return (
    <PlayerCardStyled active={active}>
      <div>{player.name}</div>
      <div>cash: {player.cash}</div>
      <div>bet: {player.currentBet}</div>
      <div>{active.toString()}</div>
    </PlayerCardStyled>
  );
}

const PlayerCardStyled = styled.div`
  display: grid;
  border-radius: var(--size-s);
  background-color: ${(props) =>
    props.active ? 'var(--green-main)' : 'var(--blue-25)'};
`;
