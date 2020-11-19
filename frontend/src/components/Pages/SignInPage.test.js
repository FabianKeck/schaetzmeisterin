import React from 'react';
import { render } from '@testing-library/react';
import SignInPage from './SignInPage';
import userEvent from '@testing-library/user-event';
import { Route, MemoryRouter as Router } from 'react-router-dom';
import GameContext from '../../context/GameContext';

describe('Component-test:: SingInPage: ', () => {
  xit('calls signInInWithUser, when Button is clicked', () => {
    //given
    const signInWithUser = jest.fn();

    const { getByRole } = render(
      <Router initialEntries={['/signin/']}>
        <GameContext.Provider value={signInWithUser}>
          <Route path={'/signin/:gameid?'}>
            <SignInPage />
          </Route>
        </GameContext.Provider>
      </Router>
    );

    //when
    const button = getByRole('button');
    userEvent.click(button);

    //then
    expect(signInWithUser).toBeCalledWith('');
  });
});
