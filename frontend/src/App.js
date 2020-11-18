import React from 'react';
import { Switch, Route } from 'react-router-dom';
import SignInPage from './components/Pages/SignInPage';
import GameContextProvider from './context/GameContextProvider';
import GamePage from './components/Pages/GamePage';

export default function App() {
  return (
    <GameContextProvider>
      <Switch>
        <Route path={'/game/:gameid'} component={GamePage} />
        <Route path={'/signin/:gameid?'} component={SignInPage} />
      </Switch>
    </GameContextProvider>
  );
}
