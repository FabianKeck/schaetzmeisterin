import axios from 'axios';
import React from 'react';
import { render, waitFor } from '@testing-library/react';
import { MemoryRouter as Router } from 'react-router-dom';
import App from '../App';
import userEvent from '@testing-library/user-event';
import jwt from 'jsonwebtoken';

jest.mock('axios');

describe('Sign in integration-test:', () => {
  it('sign in new game', async () => {
    //given
    const playerName = 'john';
    const playerId = '123';
    const game = { id: 'gameId', players: [{ name: playerName }] };
    //then
    axios.get = jest.fn(
      () =>
        new Promise((resolved) => {
          resolved({ data: generateValidToken(playerName, playerId) });
        })
    );
    axios.post = jest.fn(
      () =>
        new Promise((resolved) => {
          resolved({ data: game });
        })
    );

    const { getByRole, getByLabelText, getAllByText } = render(
      <Router initialEntries={['/signin/']}>
        <App />
      </Router>
    );
    const input = getByLabelText(/name/i);
    userEvent.type(input, 'john');
    const button = getByRole('button');
    await userEvent.click(button);

    await waitFor(() => {
      expect(axios.get).toBeCalled();
    });
    await waitFor(() => {
      expect(axios.post).toBeCalled();
    });
    setTimeout(() => {
      expect(getAllByText(/john/i)[0]).toBeInTheDocument();
    }, 2000);
  });
});
const generateValidToken = (username, userid) =>
  jwt.sign(
    {
      sub: username,
      playerId: userid,
      exp: Math.floor(Date.now() / 1000) + 60 * 60,
      data: 'foobar',
    },
    'secret'
  );
