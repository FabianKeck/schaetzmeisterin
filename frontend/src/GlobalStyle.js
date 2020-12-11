import { createGlobalStyle } from 'styled-components';

export default createGlobalStyle`
  :root{
  --color-red:#6A0000;
  --color-green:#0A761D;
  --color-black:#1D1B1A;
  --color-golden:#C59135;
  

  
  --size-xxs: 2px;
   --size-xs: 4px;
   --size-s: 8px;
   --size-m: 12px;
   --size-l: 16px;
   --size-xl: 24px;
   --size-xxl: 32px;
  }
  
  html, body {
    margin: 0;
    font-family: 'EB Garamond', serif;
  }
  
 
  body{
    background: radial-gradient(#aA0000, #5A0000);
    max-width: 800px;
    margin:auto;
    color: var(--color-golden);
  }
  
    input, textarea {
    font-size: 1em;
    font-family: inherit;
  }
 
`;
