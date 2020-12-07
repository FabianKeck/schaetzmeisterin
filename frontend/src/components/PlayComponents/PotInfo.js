import React from 'react';
import styled from 'styled-components/macro';
import { FaCoins } from 'react-icons/fa';

export default function PotInfo({ value }) {
  return (
    <PotInfoStyled>
      {' '}
      <FaCoins /> : {value}
    </PotInfoStyled>
  );
}
const PotInfoStyled = styled.div`
  background-color: var(--blue-75);
  border-radius: var(--size-xs);
  padding: var(--size-s);
`;
