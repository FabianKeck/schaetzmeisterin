import React from 'react';
import { Switch, Route } from 'react-router-dom';
import GameContextProvider from './context/GameContextProvider';
import styled from 'styled-components/macro';
import UserContextProvider from './context/UserContextProvider';
import GamePage from './components/pages/GamePage';
import SignInPage from './components/pages/SignInPage';

export default function App() {
  return (
    <UserContextProvider>
      <GameContextProvider>
        <PageLayout>
          <Switch>
            <Route path={'/signin/:gameid?'} component={SignInPage} />
            <Route path={'/game'} component={GamePage} />
          </Switch>
        </PageLayout>
      </GameContextProvider>
    </UserContextProvider>
  );
}

const PageLayout = styled.div`
  display: grid;
  grid-template-rows: min-content 1fr;
  height: 100vh;
  background-color: var(--brown-25);
`;
