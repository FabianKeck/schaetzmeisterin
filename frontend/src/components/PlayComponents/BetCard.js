import React, { useEffect, useState } from 'react';
import styled from 'styled-components/macro';

export default function BetCard({ bet, minBet, cash }) {
  const [betValue, setBetValue] = useState(minBet);
  const [betTooSmall, setBetTooSmall] = useState(false);
  const [betToLarge, setBetTooLarge] = useState(false);

  useEffect(() => {
    setBetTooLarge(betValue > cash);
    setBetTooSmall(betValue < minBet);
  }, [betValue, cash, minBet]);

  return (
    <BetCardStyled active={bet}>
      <div> your cash: {cash}</div>
      <div>Minimum Bet: {minBet}</div>
      {bet && (
        <form onSubmit={handleSubmit}>
          <label>
            {' '}
            YourBet: <input value={betValue} onChange={handleChange} />
          </label>
          <button disabled={betTooSmall || betToLarge}> Bet!</button>
        </form>
      )}
      {betTooSmall && (
        <div>Your Bet is too small. Please enter a larger bet</div>
      )}
      {betToLarge && (
        <div> Your bet exceeds your cash. Please enter a smaller Bet</div>
      )}
    </BetCardStyled>
  );

  function handleChange(event) {
    setBetValue(event.target.value);
  }
  function handleSubmit(event) {
    event.preventDefault();
    bet(betValue);
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
`;
