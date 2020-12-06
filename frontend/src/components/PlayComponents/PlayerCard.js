import React from 'react';
import styled from 'styled-components/macro';
import { FaDollarSign, FaUser } from 'react-icons/fa';

export default function PlayerCard({ player, row, column }) {
  return (
    <PlayerCardStyled row={row} column={column}>
      <p>
        <FaUser /> {player.name}
      </p>
      <p>
        <FaDollarSign />
        {player.cash}
      </p>
      <p>bet: {player.currentBet}</p>
      <ShadowDummy />
    </PlayerCardStyled>
  );
}

const PlayerCardStyled = styled.div`
  grid-column: ${(props) => '' + props.column};
  grid-row: ${(props) => '' + props.row};
  position: relative;
  border: 1px solid var(--color-golden);
  font-size: smaller;
  padding: var(--size-s);
  display: grid;
  grid-gap: var(--size-xs);
  grid-auto-rows: min-content;
  border-radius: var(--size-s);
  background-color: var(--color-main);
  p {
    margin: 0;
  }
`;
const ShadowDummy = styled.div`
  position: absolute;
  width: 100%;
  height: 7%;
  background-color: transparent;
  border-radius: var(--size-s);
  bottom: 0;
  box-shadow: 0 var(--size-s) var(--size-s) #000;
`;
