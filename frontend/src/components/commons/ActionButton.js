import React from 'react';
import styled from 'styled-components/macro';

export default function ActionButton({ children, ...rest }) {
  return <ActionButtonStyled {...rest}>{children}</ActionButtonStyled>;
}

const ActionButtonStyled = styled.button`
  border: 1px solid var(--blue-heavy);
  box-shadow: none;
  border-radius: var(--size-xs);
  background-color: var(--blue-75);
  padding: var(--size-xs);
`;
