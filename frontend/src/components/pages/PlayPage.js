import React, { useContext, useEffect, useState } from 'react';
import Header from '../commons/Header';
import UserContext from '../../context/UserContext';
import GameContext from '../../context/GameContext';
import SelfCard from '../PlayComponents/SelfCard';
import styled from 'styled-components/macro';
import CardTable from '../PlayComponents/CardTable';
import QuestionCard from '../PlayComponents/QuestionCard';
import AskCard from '../PlayComponents/AskCard';
import AnswerCard from '../PlayComponents/AnswerCard';

export default function PlayPage() {
  const { userData } = useContext(UserContext);
  const { game, ask, bet, fold } = useContext(GameContext);
  const [active, setActive] = useState(false);
  useEffect(() => {
    setActive(isActive(userData.playerId));
    // eslint-disable-next-line
  }, [game, userData.playerId]);

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
        {game.betSession.question ? (
          getQuestionCardOrAnswerCard()
        ) : (
          <AskCard ask={ask} />
        )}

        <SelfCard
          bet={bet}
          fold={fold}
          minBet={calcMinBet()}
          cash={getPlayerData().cash}
          active={active}
        />
      </PlayPageStyled>
    </>
  );
  function getPlayerData() {
    return game.betSession.players.find(
      (player) => player.id === userData.playerId
    );
  }
  function calcMinBet() {
    return (
      Math.max(...game.betSession.players.map((player) => player.currentBet)) -
      getPlayerData().currentBet
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

  function getQuestionCardOrAnswerCard() {
    return getPlayerData.dealing ? (
      <QuestionCard>{game.betSession.question.question}</QuestionCard>
    ) : (
      <AnswerCard question={game.betSession.question.question} />
    );
  }
}
const PlayPageStyled = styled.main`
  display: grid;
  grid-gap: var(--size-xs);
  padding: var(--size-xs);
  grid-template-rows: 1fr min-content;
`;
