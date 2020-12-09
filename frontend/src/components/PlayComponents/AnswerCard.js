import React, { useState } from 'react';
import styled from 'styled-components/macro';
import { Card } from '../commons/Card';
import Input from '../commons/Input';
import ActionButton from '../commons/ActionButton';

export default function AnswerCard({ question, guess }) {
  const [guessString, setGuessString] = useState('');
  const guessIsValidNumberString = !isNaN(guessString) && guessString;
  return (
    <AnswerCardStyled>
      <p>{question.question}</p>
      <form onSubmit={handleAnswerSubmit}>
        <label>
          Your guess:
          <Input
            value={guessString}
            onChange={(event) => setGuessString(event.target.value)}
          />
        </label>
        <ActionButton disabled={!guessIsValidNumberString}>
          Submit!
        </ActionButton>
      </form>
    </AnswerCardStyled>
  );

  function handleAnswerSubmit(event) {
    event.preventDefault();
    guess(guessString);
  }
}

const AnswerCardStyled = styled(Card)`
  position: absolute;
  bottom: 20%;
  left: 50%;
  transform: translate(-50%, 0);

  form {
    display: flex;
  }
  p {
    margin: 0;
  }
`;
