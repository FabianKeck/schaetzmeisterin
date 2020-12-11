import React, { useContext } from 'react';
import GameContext from '../../context/GameContext';
import Header from '../commons/Header';
import styled from 'styled-components/macro';
import UserContext from '../../context/UserContext';
import ActionButton from '../commons/ActionButton';
import { BsClipboard } from 'react-icons/bs';

export default function GameStagingPage() {
  const { game, startGame } = useContext(GameContext);
  const { userData } = useContext(UserContext);
  const lessThanThreePlayers = game.players.length < 3;

  return (
    <>
      <Header>New Game</Header>
      <GameStaginCardStyled>
        <p>You are logged in as {userData?.sub}</p>
        <p>So far these users have joined the game</p>
        <UlStyled>
          {game?.players?.map((player) => (
            <li key={player.id}>{player.name}</li>
          ))}
        </UlStyled>
        {lessThanThreePlayers && (
          <p>
            There need to be at least three players to start the game. Please
            wait for more players to join or share the invitation-link!
          </p>
        )}
        <ActionButton
          disabled={lessThanThreePlayers}
          onClick={() => startGame(game.id)}
        >
          Start Game!
        </ActionButton>
        <label>
          {'https://schaetzmeisterin.herokuapp.com/signin/' + game.id}
        </label>
        <button>
          <BsClipboard />
        </button>
      </GameStaginCardStyled>
    </>
  );
}
const GameStaginCardStyled = styled.section`
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
