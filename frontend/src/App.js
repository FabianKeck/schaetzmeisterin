import React from 'react';
import { Switch, Route } from 'react-router-dom';
import SignInPage from './components/Pages/SignInPage';
import GameContextProvider from './context/GameContextProvider';
import GamePage from './components/Pages/GamePage';
import styled from 'styled-components/macro';

export default function App() {
  return (
    <GameContextProvider>
      <PageLayout>
        <Switch>
          <Route path={'/game/:gameid'} component={GamePage} />
          <Route path={'/signin/:gameid?'} component={SignInPage} />
        </Switch>
      </PageLayout>
    </GameContextProvider>
  );
}

const PageLayout = styled.div`
  display: grid;
  grid-template-rows: min-content 1fr;
  height: 100vh;
  background-color: var(--brown-25);
`;
