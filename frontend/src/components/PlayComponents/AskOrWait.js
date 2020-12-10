import React from 'react';
import { Card } from '../commons/Card';
import AskCard from './AskCard';

export default function AskOrWait({ ask, asked }) {
  return asked ? (
    <Card>
      The other players are currently answering your question. Please wait.
    </Card>
  ) : (
    <AskCard ask={ask} />
  );
}
