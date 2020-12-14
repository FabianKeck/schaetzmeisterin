import React, { useContext } from 'react';
import GameContext from '../../context/GameContext';
import Header from '../commons/Header';
import styled from 'styled-components/macro';
import UserContext from '../../context/UserContext';
import { Button } from '../commons/Button';
import { Card } from '../commons/Card';

export default function GameStagingPage() {
  const { game, startGame } = useContext(GameContext);
  const { userData } = useContext(UserContext);
  const lessThanThreePlayers = game.players.length < 3;
  const signInUrl = 'https://schaetzmeisterin.herokuapp.com/signin/' + game.id;

  return (
    <>
      <Header>New Game</Header>
      <GameStaginCardStyled>
        <p>Welcome {userData?.sub}!</p>
        <p>So far these users have joined the game</p>
        <Card>
          <UlStyled>
            {game?.players?.map((player) => (
              <li key={player.id}>{player.name}</li>
            ))}
          </UlStyled>
        </Card>
        <Button
          disabled={lessThanThreePlayers}
          onClick={() => startGame(game.id)}
        >
          Start Game!
        </Button>

        {lessThanThreePlayers && (
          <p>
            There need to be at least three players to start the game. Please
            wait for more players to join!
          </p>
        )}
        <p>To invite more players share the invitaion-link below:</p>
        <p>{signInUrl}</p>
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
  form {
    display: grid;
    grid-template-columns: 4fr 1fr;
    grid-gap: var(--size-s);
  }
`;

const UlStyled = styled.ul`
  list-style: none;
  margin: 0;
  padding: var(--size-s);
`;
