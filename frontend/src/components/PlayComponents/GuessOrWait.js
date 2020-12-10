import React from 'react';
import { Card } from '../commons/Card';
import AnswerCard from './AnswerCard';

export default function GuessOrWait({ question, guess, guessed }) {
  const questionAskedButNotAnswered = question && !guessed;
  return questionAskedButNotAnswered ? (
    <AnswerCard question={question} guess={guess} />
  ) : (
    <Card>
      please wait for{' '}
      {guessed ? 'other players to guess' : 'dealer to ask Question...'}
    </Card>
  );
}
