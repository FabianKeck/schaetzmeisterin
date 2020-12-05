import React from 'react';
import styled from 'styled-components/macro';
import PlayerCard from './PlayerCard';

export default function CardTable({ players, potValue }) {
  return (
    <CardTableStyled numPlayers={players.length}>
      {players.map((player, index) => (
        <PlayerCard player={player} index={index} />
      ))}
      <PotInfoStyled>{potValue}</PotInfoStyled>
    </CardTableStyled>
  );
}

const CardTableStyled = styled.div`
  --m: ${(props) => '' + (props.numPlayers + 1)};
  --tan: ${(props) => '' + Math.tan(Math.PI / props.numPlayers)};
  --d: 6.5em; /* image size */
  --rel: 1; /* how much extra space we want between images, 1 = one image size */
  --r: calc(0.5 * (1 + var(--rel)) * var(--d) / var(--tan)); /* circle radius */
  --s: calc(2 * var(--r) + var(--d)); /* container size */
  position: relative;
  min-width: var(--s);
`;

const PotInfoStyled = styled.div`
  position: relative;
`;
