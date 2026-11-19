import { useState } from 'react'
import { Button, MenuItem, Paper, Stack, TextField, Typography } from '@mui/material'
import SearchIcon from '@mui/icons-material/Search'

// Filtro por número de precinto y/o CM destino
export default function PanelFiltros({ comisionesDestino, alFiltrar, alLimpiar }) {
  const [precinto, setPrecinto] = useState('')
  const [cmDestino, setCmDestino] = useState('')

  const buscar = (evento) => {
    evento.preventDefault()
    alFiltrar(precinto ? Number(precinto) : null, cmDestino || null)
  }

  const limpiar = () => {
    setPrecinto('')
    setCmDestino('')
    alLimpiar()
  }

  return (
    <Paper variant="outlined" sx={{ p: 2 }} component="form" onSubmit={buscar}>
      <Typography fontWeight={600} sx={{ mb: 1.5 }}>Filtrar bolsines</Typography>
      <Stack gap={1.5}>
        <TextField
          size="small"
          label="N° de precinto"
          value={precinto}
          onChange={(e) => setPrecinto(e.target.value.replace(/\D/g, ''))}
          inputProps={{ inputMode: 'numeric' }}
        />
        <TextField
          select
          size="small"
          label="CM destino"
          value={cmDestino}
          onChange={(e) => setCmDestino(e.target.value)}
        >
          <MenuItem value="">Todas</MenuItem>
          {comisionesDestino.map((cm) => (
            <MenuItem key={cm.codigo} value={cm.codigo}>{cm.nombre}</MenuItem>
          ))}
        </TextField>
        <Stack direction="row" gap={1}>
          <Button type="submit" variant="contained" startIcon={<SearchIcon />} fullWidth>Buscar</Button>
          <Button onClick={limpiar} color="inherit">Limpiar</Button>
        </Stack>
      </Stack>
    </Paper>
  )
}
