import React from 'react';
import { Switch, Route } from 'react-router-dom';
import SignInPage from './components/pages/SignInPage';
import GameContextProvider from './context/GameContextProvider';
import GameStagingPage from './components/pages/GameStagingPage';
import styled from 'styled-components/macro';
import UserContextProvider from './context/UserContextProvider';
import PlayPage from './components/pages/PlayPage';

export default function App() {
  return (
    <UserContextProvider>
      <GameContextProvider>
        <PageLayout>
          <Switch>
            <Route path={'/game/staged/:gameid'} component={GameStagingPage} />
            <Route path={'/signin/:gameid?'} component={SignInPage} />
            <Route path={'/play/:gameid'} component={PlayPage} />
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
