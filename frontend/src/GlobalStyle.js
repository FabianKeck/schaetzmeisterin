import { createGlobalStyle } from 'styled-components';
import backgroundImage from './assets/poker-table-long.jpg';

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
    max-width: 800px;
  }
  body{
  background-image: url(${backgroundImage});
    background-repeat: no-repeat;
    background-position: center bottom;
    background-size: auto 100%;
    background-color: var(--color-red);
  }
  
    input, textarea {
    font-size: 1em;
    font-family: inherit;
  }
 
`;
