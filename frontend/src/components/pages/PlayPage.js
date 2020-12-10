import React, { useContext, useEffect, useState } from 'react';
import Header from '../commons/Header';
import UserContext from '../../context/UserContext';
import GameContext from '../../context/GameContext';
import SelfCard from '../PlayComponents/SelfCard';
import styled from 'styled-components/macro';
import CardTable from '../PlayComponents/CardTable';
import QuestionCard from '../PlayComponents/QuestionCard';
import AskingAndGuessIng from '../PlayComponents/AskingAndGuessIng';

export default function PlayPage() {
  const { userData } = useContext(UserContext);
  const { game, ask, guess, bet, fold } = useContext(GameContext);
  const [active, setActive] = useState(false);
  useEffect(() => {
    setActive(isActive(userData.playerId));
    // eslint-disable-next-line
  }, [game, userData.playerId]);
  const askingAndGuessingInProgress = game.betSession.players.some(
    (player) => !(player.dealing || player.guessed)
  );
  const playerData = game.betSession.players.find(
    (player) => player.id === userData.playerId
  );

  return (
    <>
      <Header>Schaetzmeisterin</Header>
      <PlayPageStyled>
        <CardTable
          players={game.betSession.players.filter(
            (player) => player.id !== userData.playerId
          )}
          potValue={calcPot()}
          activePlayerId={
            game.betSession.players[game.betSession.activePlayerIndex]
          }
        />
        {askingAndGuessingInProgress && (
          <AskingAndGuessIng
            name={playerData.name}
            dealing={playerData.dealing}
            guessed={playerData.guessed}
            question={game.betSession.question?.question}
            ask={ask}
            guess={guess}
          />
        )}

        {game.betSession.question && (
          <QuestionCard>{game.betSession.question.question}</QuestionCard>
        )}
        <SelfCard
          name={playerData.name}
          guess={playerData.guess}
          bet={bet}
          fold={fold}
          minBet={calcMinBet()}
          cash={playerData.cash}
          active={active}
          disableActions={
            askingAndGuessingInProgress || game.betSession.finished
          }
        />
      </PlayPageStyled>
    </>
  );

  function calcMinBet() {
    return (
      Math.max(...game.betSession.players.map((player) => player.currentBet)) -
      playerData.currentBet
    );
  }
  function isActive(id) {
    return id === game.betSession.players[game.betSession.activePlayerIndex].id;
  }

  function calcPot() {
    return game.betSession.players
      .map((player) => player.currentBet)
      .reduce((sum, currentBet) => sum + currentBet);
  }
}
const PlayPageStyled = styled.main`
  display: grid;
  grid-gap: var(--size-xs);
  padding: var(--size-xs);
  grid-template-rows: 1fr min-content;
`;
