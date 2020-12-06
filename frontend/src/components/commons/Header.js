import React from 'react';
import styled from 'styled-components/macro';
import Rooster from '../../assets/rooster-golden.svg';

export default function Header({ children }) {
  return (
    <HeaderStyled>
      <img src={Rooster} alt={'golden rooster'} />
      <HeadingStyled>{children}</HeadingStyled>
      <img src={Rooster} alt={'golden rooster'} />
    </HeaderStyled>
  );
}

const HeaderStyled = styled.header`
  display: flex;
  justify-content: space-between;
  background: var(--color-main);
  padding: var(--size-s);
  img {
    height: 40px;
  }
  img:nth-of-type(1) {
    transform: scaleX(-1);
  }
`;

const HeadingStyled = styled.h1`
  margin: 0;
  color: var(--color-golden);
  letter-spacing: 2px;
  font-weight: normal;
`;
