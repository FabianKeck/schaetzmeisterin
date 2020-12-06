import React from 'react';
import styled from 'styled-components/macro';
import Rooster from '../../assets/rooster-golden.svg';

export default function Header({ children }) {
  return (
    <HeaderStyled>
      <img src={Rooster} alt={'golden rooster'} />
      <HeadingStyled>{children}</HeadingStyled>
    </HeaderStyled>
  );
}

const HeaderStyled = styled.header`
  display: flex;
  justify-content: center;
  background: var(--color-main);
  padding: var(--size-s);
  img {
    position: absolute;
    left: var(--size-s);
    height: 40px;
  }
`;

const HeadingStyled = styled.h1`
  margin: 0;
  color: var(--color-golden);
  letter-spacing: 3px;
`;
