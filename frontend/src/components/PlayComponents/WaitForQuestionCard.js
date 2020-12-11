import React from 'react';
import styled from 'styled-components/macro';
import { Card } from '../commons/Card';

export default function WaitForQuestionCard() {
  return (
    <WaitForQuestionCardStyled>
      Please wait, while dealer enters question ...
    </WaitForQuestionCardStyled>
  );
}

const WaitForQuestionCardStyled = styled(Card)`
  position: absolute;
  bottom: 50%;
  left: 50%;
  transform: translate(-50%, 0);
`;
