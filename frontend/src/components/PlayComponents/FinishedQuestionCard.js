import React from 'react';
import styled from 'styled-components/macro';
import { Card } from '../commons/Card';
import { GiPartyPopper, GiTwoCoins } from 'react-icons/gi';

export default function FinishedQuestionCard({
  winning,
  winnerName,
  question,
  potSize,
}) {
  return (
    <FinishedQuestionViewStyled>
      {winning && (
        <p className="party">
          <GiPartyPopper />
          <GiPartyPopper />
        </p>
      )}
      <p>Question finished!</p>
      <p>
        {winning ? 'You win ' : ' ' + winnerName + ' wins '} <GiTwoCoins />{' '}
        {potSize}!
      </p>
      <p>The correct answer was: {question.answer}</p>
      {winning && (
        <p className="party">
          <GiPartyPopper />
          <GiPartyPopper />
        </p>
      )}
    </FinishedQuestionViewStyled>
  );
}
const FinishedQuestionViewStyled = styled(Card)`
  box-shadow: var(--size-s) var(--size-s) var(--size-xl) var(--size-l) #222;
  position: absolute;
  bottom: 40%;
  left: 50%;
  transform: translate(-50%, -40%);
  z-index: 1;
  p {
    display: flex;
    justify-content: space-around;
  }
  .party {
    margin: 0;
    display: flex;
    justify-content: space-between;
    font-size: xx-large;
  }
`;
