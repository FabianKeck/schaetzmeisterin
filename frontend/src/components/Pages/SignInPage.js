import React, { useContext, useState } from 'react';
import { useHistory } from 'react-router-dom';
import GameContext from '../../context/GameContext';

export default function SignInPage() {
  const history = useHistory();
  const [name, setName] = useState('');
  const [signInFailed, setSignInFailed] = useState('');
  const { signInWithUser } = useContext(GameContext);

  return (
    <>
      <h1>sign In Page</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Please Enter Your Name to sign in to a new game
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
    signInWithUser(name)
      .then(() => {
        history.push('/game');
      })
      .catch(() => {
        setSignInFailed('Sign in failed, please try again');
      });
  }
}
