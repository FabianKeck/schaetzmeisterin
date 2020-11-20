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
  background: var(--blue-main);
`;

const HeadingStyled = styled.h1`
  margin: 0;
  color: white;
`;
