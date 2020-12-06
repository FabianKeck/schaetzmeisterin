import React from 'react';
import styled from 'styled-components/macro';

export default function ActionButton({ children, ...rest }) {
  return <ActionButtonStyled {...rest}>{children}</ActionButtonStyled>;
}

const ActionButtonStyled = styled.button`
  border: 1px solid var(--color-golden);
  box-shadow: none;
  border-radius: var(--size-xs);
  background-color: var(--color-active);
  padding: var(--size-xs);
  :disabled {
    background: none;
  }
`;
