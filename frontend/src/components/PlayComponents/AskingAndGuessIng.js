import React from 'react';
import AskOrWait from './AskOrWait';
import GuessOrWait from './GuessOrWait';

export default function AskingAndGuessing({
  dealing,
  ask,
  question,
  guess,
  guessed,
}) {
  return dealing ? (
    <AskOrWait ask={ask} asked={!!question} />
  ) : (
    <GuessOrWait question={question} guess={guess} guessed={guessed} />
  );
}
