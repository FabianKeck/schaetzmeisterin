import React from 'react';
import styled from 'styled-components/macro';

export default function Header({ children }) {
  return (
    <HeaderStyled>
      <HeadingStyled>{children}</HeadingStyled>
    </HeaderStyled>
  );
}

const HeaderStyled = styled.header`
  display: flex;
  justify-content: center;
  background: var(--color-main);
  padding: var(--size-s);
`;

const HeadingStyled = styled.h1`
  margin: 0;
  color: var(--color-golden);
  letter-spacing: 4px;
`;
