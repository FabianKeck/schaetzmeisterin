import React from 'react';
import styled from 'styled-components/macro';

export default function PotInfo({ value }) {
  return <PotInfoStyled>Current Pot: {value}</PotInfoStyled>;
}
const PotInfoStyled = styled.div`
  background-color: var(--blue-75);
  border-radius: var(--size-xs);
`;
