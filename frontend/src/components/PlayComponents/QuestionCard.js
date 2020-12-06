import React from 'react';
import styled from 'styled-components/macro';

export default function QuestionCard() {
  return (
    <QuestionCardStyled>
      <div>?</div>
      <p>
        How many pull requests have been opened by the HH-2020-j1 students
        during their capstone projetcs?
      </p>
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
