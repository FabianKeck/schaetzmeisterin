import React, { useEffect, useState } from 'react';
import styled from 'styled-components/macro';
import ActionButton from '../commons/ActionButton';
import Input from '../commons/Input';
import { FaDollarSign } from 'react-icons/fa';

export default function SelfCard({ bet, fold, minBet, cash, active }) {
  const [betValue, setBetValue] = useState(minBet);
  const [betTooSmall, setBetTooSmall] = useState(false);
  const [betToLarge, setBetTooLarge] = useState(false);

  useEffect(() => {
    setBetTooLarge(betValue > cash);
    setBetTooSmall(betValue < minBet);
  }, [betValue, cash, minBet]);

  return (
    <SelfCardStyled active={active}>
      <Info>
        <p>
          <FaDollarSign /> : {cash}
          <p>Minimum Bet: {minBet}</p>
        </p>
      </Info>
      <Actions>
        {minBet ? (
          <ActionButton disabled={!active} onClick={handleCall}>
            call
          </ActionButton>
        ) : (
          <ActionButton disabled={!active} onClick={handleCheck}>
            check
          </ActionButton>
        )}
        <ActionButton disabled={!active} onClick={fold}>
          {' '}
          fold
        </ActionButton>
        {betTooSmall && <p>Your Bet is too small. Please enter a larger bet</p>}
        {betToLarge && (
          <p> Your bet exceeds your cash. Please enter a smaller Bet</p>
        )}
        <form onSubmit={handleRaise}>
          <Input value={betValue} onChange={handleBetValueChange} />
          <ActionButton disabled={betTooSmall || betToLarge || !active}>
            raise
          </ActionButton>
        </form>
      </Actions>
    </SelfCardStyled>
  );

  function handleBetValueChange(event) {
    setBetValue(event.target.value);
  }

  function handleRaise(event) {
    event.preventDefault();
    if (betToLarge || betTooSmall) {
      return;
    }
    bet(betValue);
  }
  function handleCall() {
    if (minBet > cash) {
      return;
    }
    bet(minBet);
  }
  function handleCheck() {
    bet(0);
  }
}

const SelfCardStyled = styled.div`
  background-color: var(--color-main);
  border-radius: var(--size-s);
  display: grid;
  grid-gap: var(--size-s);
  grid-template-columns: 1fr 1fr;
  grid-auto-rows: 1fr;
  padding: var(--size-s);
  box-shadow: 2px 2px 2px #222;
  border: 1px solid var(--color-golden);
`;
const Info = styled.div``;

const Actions = styled.div`
  display: grid;
  grid-gap: var(--size-s);
  form {
    display: flex;
    justify-content: space-between;
  }
`;
