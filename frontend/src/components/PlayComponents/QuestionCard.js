import React from 'react';
import styled from 'styled-components/macro';

export default function QuestionCard({ children }) {
  return (
    <QuestionCardStyled>
      <div>?</div>
      <p>{children}</p>
    </QuestionCardStyled>
  );
}

const QuestionCardStyled = styled.div`
  background-color: var(--color-main);
  border-radius: var(--size-s);
  border: 1px solid var(--color-golden);
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
