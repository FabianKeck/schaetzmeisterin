import { createGlobalStyle } from 'styled-components';
import backgroundImage from './assets/poker-table.jpg';

export default createGlobalStyle`
  :root{
  --color-main:#6A0000;
  --color-active:#0A761D;
  --color-black:#1D1B1A;
  --color-golden:#C59135;
  
 --red-light: #8B0000;
 --red-dark: #6A0000;
  

  
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
    font-family: "Lucida Sans Unicode", "Lucida Grande", sans-serif;

  }
  
  body{
    max-width: 800px;
  }
  main{
  background-image: url(${backgroundImage});
    background-repeat: no-repeat;
    background-position: center bottom;
    background-size: cover;
  }
  
    input, textarea {
    font-size: 1em;
    font-family: inherit;
  }
  html{
  justify-content: center;
  }
`;
