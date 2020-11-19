import React, { useContext, useState } from 'react';
import { useHistory, useParams } from 'react-router-dom';
import GameContext from '../../context/GameContext';

export default function SignInPage() {
  const { gameid } = useParams();
  const history = useHistory();
  const [name, setName] = useState('');
  const [signInFailed, setSignInFailed] = useState('');
  const { signInWithUser } = useContext(GameContext);

  return (
    <>
      <h1>sign In Page</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Please Enter your name to sign in to game {gameid}
          <input value={name} onChange={handleChange} />
        </label>
        <button>Sign in</button>
        <p>{signInFailed}</p>
      </form>
    </>
  );
  function handleChange(event) {
    setName(event.target.value);
  }
  function handleSubmit(event) {
    event.preventDefault();
    signInWithUser(name, gameid ? gameid : '')
      .then((data) => {
        history.push('/game/' + data.id);
      })
      .catch(() => {
        setSignInFailed('Sign in failed, please try again');
      });
  }
}
