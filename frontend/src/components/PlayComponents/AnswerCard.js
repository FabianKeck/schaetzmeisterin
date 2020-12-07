import React from 'react';

export default function AnswerCard({ question }) {
  return (
    <div>{question ? '' + question : 'Wait for dealer to aks question.'}</div>
  );
}
