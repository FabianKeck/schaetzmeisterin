import styled from 'styled-components/macro';

export const Button = styled.button`
  border: 1px solid var(--color-golden);
  border-radius: var(--size-xs);
  background-color: var(--color-green);
  box-shadow: 2px 2px 2px #222;
  font-family: 'EB Garamond', serif;
  padding: var(--size-xs);
  :disabled {
    background: none;
  }
`;
