import { createTheme } from '@mui/material/styles'

// Colores y tipografía de la aplicación
const tema = createTheme({
  palette: {
    primary: { main: '#1d4e89' },   // azul institucional
    secondary: { main: '#f2a541' }, // amarillo del precinto
    background: { default: '#f4f6fa' }
  },
  typography: {
    fontFamily: 'Inter, Roboto, Arial, sans-serif',
    button: { textTransform: 'none', fontWeight: 600 }
  },
  shape: { borderRadius: 10 }
})

export default tema
