import React, { useContext } from 'react';
import { useHistory } from 'react-router-dom';
import GameContext from '../../context/GameContext';
import Header from '../commons/Header';
import styled from 'styled-components/macro';
import UserContext from '../../context/UserContext';

export default function GameStagingPage() {
  const { game, startGame } = useContext(GameContext);
  const { userData } = useContext(UserContext);
  const history = useHistory();
  return (
    <>
      <Header>New Game</Header>
      <GamePageStyled>
        <p>You are logged in as {userData?.sub}</p>
        <p>So far these users have joined the game</p>
        <UlStyled>
          {game?.players.map((player) => (
            <li key={player.id}>{player.name}</li>
          ))}
        </UlStyled>
        <ButtonStyled onClick={onStart}>Start Game!</ButtonStyled>
      </GamePageStyled>
    </>
  );
  function onStart() {
    console.log(game.id);
    startGame(game.id).then(history.push('/play'));
  }
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

const ButtonStyled = styled.button`
  font-size: 1em;
  border-radius: var(--size-s);
  background-color: var(--green-75);
  border: 1px solid var(--green-main);
`;
const UlStyled = styled.ul`
  list-style: none;
  margin: 0;
  padding: var(--size-l);
`;
