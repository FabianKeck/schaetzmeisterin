import React from 'react';
import styled from 'styled-components/macro';
import { FaDollarSign, FaUser } from 'react-icons/fa';

export default function PlayerCard({ player, active }) {
  return (
    <PlayerCardStyled active={active}>
      <p>
        <FaUser /> {player.name}
      </p>
      <p>
        {' '}
        <FaDollarSign /> : {player.cash}
      </p>
      <p>bet: {player.currentBet}</p>
    </PlayerCardStyled>
  );
}

const PlayerCardStyled = styled.div`
  padding: var(--size-s);
  display: grid;
  grid-gap: var(--size-xs);
  border-radius: var(--size-s);
  background-color: ${(props) =>
    props.active ? 'var(--green-main)' : 'var(--blue-25)'};
  p {
    margin: 0;
  }
`;
