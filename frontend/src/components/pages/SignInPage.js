import React, { useContext, useState } from 'react';
import { useParams, useHistory } from 'react-router-dom';
import Header from '../commons/Header';
import styled from 'styled-components/macro';
import GameContext from '../../context/GameContext';

export default function SignInPage() {
  const { gameid } = useParams();
  const history = useHistory();
  const [name, setName] = useState('');
  const [signInFailed, setSignInFailed] = useState('');
  const { signInGame } = useContext(GameContext);

  return (
    <>
      <Header>Sign in </Header>
      <FormStyled onSubmit={handleSubmit}>
        <LabelStyled>
          Please Enter Your Name to sign in to game {gameid}:
          <InputStyled value={name} onChange={handleChange} />
        </LabelStyled>
        <ButtonStyled>Sign in</ButtonStyled>
        <p>{signInFailed}</p>
      </FormStyled>
    </>
  );
  function handleChange(event) {
    setName(event.target.value);
  }
  function handleSubmit(event) {
    event.preventDefault();
    signInGame(gameid, name)
      .catch(() => {
        setSignInFailed('Sign in failed, please try again');
      })
      .then((game) => history.push('/game/staged/' + game?.id));
  }
}

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-m);
  grid-auto-rows: min-content;
  grid-template-columns: 1fr;
  padding: var(--size-m);
`;

const ButtonStyled = styled.button`
  font-size: 1em;
  border-radius: var(--size-s);
  background-color: var(--green-75);
  border: 1px solid var(--green-main);
`;

const InputStyled = styled.input`
  display: block;
`;

const LabelStyled = styled.label`
  display: grid;
  grid-auto-rows: min-content;
`;
