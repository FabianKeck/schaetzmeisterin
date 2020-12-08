import React from 'react';
import styled from 'styled-components/macro';
import { Card } from '../commons/Card';
import Input from '../commons/Input';
import ActionButton from '../commons/ActionButton';

export default function AnswerCard({ question }) {
  return (
    <AnswerCardStyled>
      <p>{question.question}</p>
      <form>
        <label>
          Your guess: <Input />
        </label>
        <ActionButton>Submit!</ActionButton>
      </form>
    </AnswerCardStyled>
  );
}

const AnswerCardStyled = styled(Card)`
  position: absolute;
  bottom: 50%;

  form {
    display: flex;
  }
  p {
    margin: 0;
  }
`;
