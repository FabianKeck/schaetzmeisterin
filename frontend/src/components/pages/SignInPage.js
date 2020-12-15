import React, { useContext, useState } from 'react';
import { useParams, useHistory } from 'react-router-dom';
import Header from '../commons/Header';
import styled from 'styled-components/macro';
import GameContext from '../../context/GameContext';
import Input from '../commons/Input';
import { Button } from '../commons/Button';

export default function SignInPage() {
  const { gameid } = useParams();
  const history = useHistory();
  const [name, setName] = useState('');
  const [signInFailed, setSignInFailed] = useState('');
  const { signInGame } = useContext(GameContext);

  return (
    <>
      <Header>Schaetzmeisterin </Header>
      <main>
        <FormStyled onSubmit={handleSubmit}>
          <LabelStyled>
            Please enter your name to sign into{' '}
            {gameid ? 'game ' + gameid : 'a new game'}.
            <Input value={name} onChange={handleNameChange} />
          </LabelStyled>
          <Button>Sign in</Button>
          {signInFailed && <p>{signInFailed}</p>}
        </FormStyled>
      </main>
    </>
  );
  function handleNameChange(event) {
    setName(event.target.value);
  }
  function handleSubmit(event) {
    event.preventDefault();
    signInGame(gameid, name)
      .catch(() => {
        setSignInFailed('Sign in failed, please try again');
      })
      .then((game) => history.push('/game/' + game?.id));
  }
}

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-m);
  grid-auto-rows: min-content;
  grid-template-columns: 1fr;
  padding: var(--size-m);
  input {
  }
`;

const LabelStyled = styled.label`
  display: grid;
  grid-gap: var(--size-xs);
  grid-auto-rows: min-content;
`;
