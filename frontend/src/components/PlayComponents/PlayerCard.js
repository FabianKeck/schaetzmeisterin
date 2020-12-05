import React from 'react';
import styled from 'styled-components/macro';
import { FaDollarSign, FaUser } from 'react-icons/fa';

export default function PlayerCard({ player, index }) {
  return (
    <PlayerCardStyled index={index}>
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
  border: 1px solid var(--color-golden);
  font-size: smaller;
  position: absolute;
  top: 50%;
  left: 50%;
  margin: calc(-0.5 * var(--d));
  width: var(--d);
  --az: calc(
    ${(props) => '' + props.index} * 1turn / var(--m) + 1turn / 4 + 1turn /
      var(--m)
  );
  transform: rotate(var(--az)) translate(var(--r)) rotate(calc(-1 * var(--az)));
  min-width: 50px;
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
  box-shadow: 0 var(--size-m) var(--size-m) #222;
  z-index: -1;
`;
