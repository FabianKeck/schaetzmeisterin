import React, { useEffect, useState } from 'react';
import styled from 'styled-components/macro';
import Input from '../commons/Input';
import ActionButton from '../commons/ActionButton';
import { Card } from '../commons/Card';

const initialState = { question: '', answer: '' };
export default function AskCard({ ask }) {
  const [formData, setFormData] = useState(initialState);
  const [validData, setValidData] = useState(false);
  useEffect(() => {
    setValidData(validateData());
    // validateData should be ignored
    // eslint-disable-next-line
  }, [formData]);
  return (
    <AskCardStyled>
      <form onSubmit={handleSubmit}>
        <label>
          Your question:{' '}
          <textarea
            name="question"
            value={formData.question}
            cols="35"
            rows="5"
            onChange={handleChange}
          />
        </label>
        <label className="answer">
          The answer:{' '}
          <Input
            name="answer"
            value={formData.answer}
            onChange={handleChange}
            placeholder="only numbers. eg: '3', '4.5' "
          />
        </label>
        <ActionButton disabled={!validData}>Submit question</ActionButton>
      </form>
    </AskCardStyled>
  );
  function handleChange(event) {
    setFormData({ ...formData, [event.target.name]: event.target.value });
  }
  function handleSubmit(event) {
    event.preventDefault();
    ask(formData);
  }

  function validateData() {
    const allFieldsFilled = !Object.values(formData).some((value) => !value);
    const answerContainsOnlyNumbers = !isNaN(formData.answer);
    return allFieldsFilled && answerContainsOnlyNumbers;
    //formData.answer.match(/^[0-9]+$/);
  }
}

const AskCardStyled = styled(Card)`
  box-shadow: var(--size-s) var(--size-s) var(--size-l) var(--size-s) #222;
  position: absolute;
  bottom: 20%;
  left: 50%;
  transform: translate(-50%, 0);
  z-index: 1;

  form {
    display: grid;
    grid-template-columns: min-content;
    grid-gap: var(--size-s);
    padding: var(--size-s);
    grid-auto-rows: min-content;

    .answer {
      display: flex;
      justify-content: space-between;
    }
  }
  textarea {
    width: available;
    padding-left: var(--size-xs);
    border: 1px solid var(--color-golden);
    border-radius: var(--size-xxs);
    box-shadow: inset 0 0 var(--size-xs) #000;
    background-color: var(--color-golden);
    outline: none;
    resize: none;
  }
`;
