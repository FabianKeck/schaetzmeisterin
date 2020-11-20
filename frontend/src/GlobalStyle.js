import { createGlobalStyle } from 'styled-components';

export default createGlobalStyle`
  :root{
  --brown-main: #6B493B;
  --brown-75: #8C5F4D;
  --brown-50: #B37962;
  --brown-25: #D99377;
  
  
  --blue-main: #30566B;
  --blue-75: #3F718C;
  --blue-50: #5090B3;
  --blue-25: #62AFD9;
  
  
  --green-main: #42664C;
  --green-75: #5B8C69;
  --green-50: #74B386;
  --green-25: #8DD9A2;
  
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
  
    input, textarea {
    font-size: 1em;
    font-family: inherit;
  }
`;
