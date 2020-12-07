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
  display: flex;
  justify-content: center;
  align-items: center;
  border-color: #aaa;
  background-color: var(--color-table);
  padding: var(--size-s);
  box-shadow: 3px 3px 3px #111;
  font-size: large;
`;
