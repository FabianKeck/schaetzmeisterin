import React from 'react';
import styled from 'styled-components/macro';

export default function Input(props) {
  return <InputStyled {...props} />;
}

const InputStyled = styled.input`
  padding-left: var(--size-xxs);
  border: none;
  border-radius: var(--size-xs);
  box-shadow: inset 0 0 var(--size-xs) #000;
  width: 70%;
`;
