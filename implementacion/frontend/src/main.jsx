import ReactDOM from 'react-dom/client'
import { ThemeProvider, CssBaseline } from '@mui/material'
import 'leaflet/dist/leaflet.css'
import './estilos.css'
import tema from './tema'
import App from './App'

ReactDOM.createRoot(document.getElementById('root')).render(
  <ThemeProvider theme={tema}>
    <CssBaseline />
    <App />
  </ThemeProvider>
)
