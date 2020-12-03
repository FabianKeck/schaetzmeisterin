import React, { useEffect, useState } from 'react';
import styled from 'styled-components/macro';
import ActionButton from '../commons/ActionButton';
import Input from '../commons/Input';

export default function BetCard({ bet, fold, minBet, cash, active }) {
  const [betValue, setBetValue] = useState(minBet);
  const [betTooSmall, setBetTooSmall] = useState(false);
  const [betToLarge, setBetTooLarge] = useState(false);

  useEffect(() => {
    setBetTooLarge(betValue > cash);
    setBetTooSmall(betValue < minBet);
  }, [betValue, cash, minBet]);

  return (
    <BetCardStyled active={active}>
      <p> your cash: {cash}</p>
      <p>Minimum Bet: {minBet}</p>
      {active && (
        <>
          {minBet ? (
            <ActionButton onClick={handleCall}>call</ActionButton>
          ) : (
            <ActionButton onClick={handleCheck}>check</ActionButton>
          )}
          <ActionButton onClick={fold}> fold</ActionButton>
          {betTooSmall && (
            <p>Your Bet is too small. Please enter a larger bet</p>
          )}
          {betToLarge && (
            <p> Your bet exceeds your cash. Please enter a smaller Bet</p>
          )}
          <form onSubmit={handleSubmit}>
            <label>
              {' '}
              Your Raise: <Input value={betValue} onChange={handleChange} />
            </label>
            <ActionButton disabled={betTooSmall || betToLarge}>
              {' '}
              raise
            </ActionButton>
          </form>
        </>
      )}
    </BetCardStyled>
  );

  function handleChange(event) {
    setBetValue(event.target.value);
  }

  function handleSubmit(event) {
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

const BetCardStyled = styled.div`
  background-color: ${(props) =>
    props.active ? 'var(--green-main)' : 'var(--blue-25)'};
  border-radius: var(--size-s);
  display: grid;
  grid-gap: var(--size-s);
  grid-auto-rows: min-content;
  padding: var(--size-s);

  p {
    margin: 0;
  }
  form {
    display: flex;
    justify-content: flex-start;
  }
  label {
    display: grid;
    grid-gap: var(--size-s);
    grid-template-columns: 1fr 1fr;
  }
  input {
    max-width: 5em;
  }
`;
