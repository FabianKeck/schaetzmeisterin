import React, { useState } from 'react';
import styled from 'styled-components/macro';
import { Button } from '../commons/Button';
import Input from '../commons/Input';
import {
  GiTwoCoins,
  GiCardAceSpades,
  GiCrossMark,
  GiCheckMark,
} from 'react-icons/gi';
import { Card } from '../commons/Card';

export default function SelfCard({
  player,
  guess,
  bet,
  fold,
  minBet,
  active,
  disableActions,
}) {
  const [betValue, setBetValue] = useState(minBet);
  const betTooSmall = betValue < minBet;
  const betTooLarge = betValue > player.cash;
  return (
    <SelfCardStyled active={active}>
      <section>
        <p>
          {player.dealing ? (
            <div>you deal</div>
          ) : player.folded ? (
            <GiCrossMark />
          ) : (
            <GiCheckMark />
          )}
          {player.name}
        </p>
        <p>
          <GiTwoCoins /> {player.cash}
        </p>
        <p>
          <div>Minimum Bet:</div> {minBet}
        </p>
        {!!guess && (
          <p>
            <GiCardAceSpades /> {guess}
          </p>
        )}
      </section>
      <Actions>
        {minBet ? (
          <Button
            disabled={!active || minBet > player.cash || disableActions}
            onClick={handleCall}
          >
            call
          </Button>
        ) : (
          <Button disabled={!active || disableActions} onClick={handleCheck}>
            check
          </Button>
        )}
        <Button disabled={!active || disableActions} onClick={handleFold}>
          fold
        </Button>
        {betTooSmall && <p>Your Bet is too small. Please enter a larger bet</p>}
        {betTooLarge && (
          <p>Your bet exceeds your cash. Please enter a smaller Bet</p>
        )}
        <form onSubmit={handleRaiseSubmit}>
          <Input value={betValue} onChange={handleBetValueChange} />
          <Button
            disabled={betTooSmall || betTooLarge || !active || disableActions}
          >
            raise
          </Button>
        </form>
      </Actions>
    </SelfCardStyled>
  );

  function handleBetValueChange(event) {
    setBetValue(event.target.value);
  }

  function handleFold() {
    fold();
  }

  function handleRaiseSubmit(event) {
    event.preventDefault();
    bet(betValue);
  }
  function handleCall() {
    bet(minBet);
  }
  function handleCheck() {
    bet(0);
  }
}

const SelfCardStyled = styled(Card)`
  display: grid;
  grid-gap: var(--size-m);
  grid-template-columns: 1fr 2fr;
  grid-auto-rows: 1fr;

  p {
    justify-content: space-between;
    display: flex;
    align-items: center;
    margin: 0;
  }
`;

const Actions = styled.div`
  display: grid;
  grid-gap: var(--size-s);
  form {
    display: grid;
    grid-template-columns: 70% 1fr;
    grid-gap: var(--size-s);
  }
`;
