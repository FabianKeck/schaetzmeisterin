import React from 'react';
import styled from 'styled-components/macro';

export default function Input(props) {
  return <InputStyled {...props} />;
}

const InputStyled = styled.input`
  padding-left: var(--size-xs);
  border: 1px solid var(--color-golden);
  border-radius: var(--size-xxs);
  box-shadow: inset 0 0 var(--size-xs) #000;
  background-color: var(--color-golden);
  width: 70%;
`;
