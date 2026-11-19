import { Alert, AlertTitle } from '@mui/material'

// A3: bolsines cuyo GPS Tracker no pudo informar la ubicación
export default function AvisoBolsinesSinUbicacion({ bolsines }) {
  if (!bolsines || bolsines.length === 0) return null

  return (
    <Alert severity="warning" sx={{ textAlign: 'left' }}>
      <AlertTitle>Sin ubicación del GPS Tracker</AlertTitle>
      {bolsines.length === 1 ? 'Bolsín' : 'Bolsines'}: {bolsines.join(', ')}
    </Alert>
  )
}
