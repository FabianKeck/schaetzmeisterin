import React, { useContext } from 'react';
import GameContext from '../../context/GameContext';
import Header from '../commons/Header';
import styled from 'styled-components/macro';
import UserContext from '../../context/UserContext';
import ActionButton from '../commons/ActionButton';

export default function GameStagingPage() {
  const { game, startGame } = useContext(GameContext);
  const { userData } = useContext(UserContext);

  return (
    <>
      <Header>New Game</Header>
      <GamePageStyled>
        <p>You are logged in as {userData?.sub}</p>
        <p>So far these users have joined the game</p>
        <UlStyled>
          {game?.players?.map((player) => (
            <li key={player.id}>{player.name}</li>
          ))}
        </UlStyled>
        <ActionButton onClick={() => startGame(game.id)}>
          Start Game!
        </ActionButton>
      </GamePageStyled>
    </>
  );
}
const GamePageStyled = styled.main`
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
