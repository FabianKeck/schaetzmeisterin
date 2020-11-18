import React from 'react';
import { Switch, Route } from 'react-router-dom';
import SignInPage from './components/Pages/SignInPage';
import GameContextProvider from './context/GameContextProvider';
import GamePage from './components/Pages/GamePage';

export default function App() {
  return (
    <GameContextProvider>
      <Switch>
        <Route path={'/game'} component={GamePage} />
        <Route path={'/'} component={SignInPage} />
      </Switch>
    </GameContextProvider>
  );
}
