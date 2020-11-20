import axios from 'axios';
import React from 'react';
import { render, waitFor } from '@testing-library/react';
import { MemoryRouter as Router } from 'react-router-dom';
import App from '../App';
import userEvent from '@testing-library/user-event';

jest.mock('axios');

describe('Sign in integration-test:', () => {
  it('sign in new game', async () => {
    //given
    const player = { name: 'john' };
    const game = { id: 'gameId', players: [player] };
    axios.post = jest.fn(
      () =>
        new Promise((resolved) => {
          resolved({ data: game });
        })
    );

    //then
    const { getByRole, getByLabelText, getAllByText } = render(
      <Router initialEntries={['/signin/']}>
        <App />
      </Router>
    );
    const input = getByLabelText(/name/i);
    userEvent.type(input, 'john');
    const button = getByRole('button');
    userEvent.click(button);

    await waitFor(() => {
      expect(axios.post).toBeCalledWith('/signin/', player);
    });
    await waitFor(() => {
      expect(getAllByText(/john/i)[0]).toBeInTheDocument();
    });
  });
});
