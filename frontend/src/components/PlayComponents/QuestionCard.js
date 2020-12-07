import React from 'react';
import styled from 'styled-components/macro';
import { Card } from '../commons/Card';

export default function QuestionCard({ children }) {
  return (
    <QuestionCardStyled>
      <div>?</div>
      <p>{children}</p>
    </QuestionCardStyled>
  );
}

const QuestionCardStyled = styled(Card)`
  padding: var(--size-xs);
  display: grid;
  grid-gap: var(--size-xs);
  grid-template-columns: min-content 1fr min-content;
  align-items: center;

  div {
    padding: 0 var(--size-xxs);
    font-size: xx-large;
  }
  p {
    margin: 0;
  }
`;
