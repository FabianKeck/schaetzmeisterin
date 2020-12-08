import React, { useState } from 'react';
import styled from 'styled-components/macro';
import ActionButton from '../commons/ActionButton';
import Input from '../commons/Input';
import { GiTwoCoins, GiCardAceSpades } from 'react-icons/gi';
import { Card } from '../commons/Card';

export default function SelfCard({ guess, bet, fold, minBet, cash, active }) {
  const [betValue, setBetValue] = useState(minBet);
  const betTooSmall = betValue < minBet;
  const betTooLarge = betValue > cash;
  return (
    <SelfCardStyled active={active}>
      <section>
        <p>
          <GiTwoCoins /> {cash}
        </p>
        <p>Minimum Bet: {minBet}</p>
        {!!guess && (
          <p>
            <GiCardAceSpades /> {guess}
          </p>
        )}
      </section>
      <Actions>
        {minBet ? (
          <ActionButton
            disabled={!active || minBet > cash}
            onClick={handleCall}
          >
            call
          </ActionButton>
        ) : (
          <ActionButton disabled={!active} onClick={handleCheck}>
            check
          </ActionButton>
        )}
        <ActionButton disabled={!active} onClick={fold}>
          fold
        </ActionButton>
        {betTooSmall && <p>Your Bet is too small. Please enter a larger bet</p>}
        {betTooLarge && (
          <p>Your bet exceeds your cash. Please enter a smaller Bet</p>
        )}
        <form onSubmit={handleRaiseSubmit}>
          <Input value={betValue} onChange={handleBetValueChange} />
          <ActionButton disabled={betTooSmall || betTooLarge || !active}>
            raise
          </ActionButton>
        </form>
      </Actions>
    </SelfCardStyled>
  );

  function handleBetValueChange(event) {
    setBetValue(event.target.value);
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
  grid-gap: var(--size-s);
  grid-template-columns: 1fr 1fr;
  grid-auto-rows: 1fr;

  p {
    display: flex;
    align-items: center;
    margin: 0;
  }
`;

const Actions = styled.div`
  display: grid;
  grid-gap: var(--size-s);
  form {
    display: flex;
    justify-content: space-between;
  }
`;
