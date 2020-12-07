import React from 'react';
import styled from 'styled-components/macro';
import PlayerCard from './PlayerCard';

export default function OtherPlayersView({ players }) {
  return (
    <OtherPlayersViewStyled>
      {players.map((player) => (
        <PlayerCard key={player.id} player={player} />
      ))}
    </OtherPlayersViewStyled>
  );
}

const OtherPlayersViewStyled = styled.div`
  overflow-x: scroll;
  align-self: end;
  display: grid;
  grid-gap: var(--size-xs);
  grid-auto-flow: column;
  grid-auto-rows: min-content;
`;
