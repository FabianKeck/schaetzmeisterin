import React, { useContext } from 'react';
import GameContext from '../../context/GameContext';
import Header from '../commons/Header';
import styled from 'styled-components/macro';
import UserContext from '../../context/UserContext';

export default function GameStagingPage() {
  const { game } = useContext(GameContext);
  const { userData } = useContext(UserContext);
  return (
    <>
      <Header>New Game</Header>
      <GamePageStyled>
        <p>You are logged in as {userData?.sub}</p>
        <p>So far these users have joined the game</p>
        <UlStyled>
          {game?.players?.map((player) => (
            <li key={player.name}>{player.name}</li>
          ))}
        </UlStyled>
      </GamePageStyled>
    </>
  );
}
const GamePageStyled = styled.div`
  display: grid;
  grid-gap: var(--size-s);
  grid-auto-rows: min-content;
  padding: var(--size-s);

  p {
    margin: 0;
  }
`;
const UlStyled = styled.ul`
  list-style: none;
  margin: 0;
  padding: var(--size-l);
`;
