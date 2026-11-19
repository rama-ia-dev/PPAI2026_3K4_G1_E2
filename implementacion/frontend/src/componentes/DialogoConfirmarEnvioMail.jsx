import { Button, Dialog, DialogActions, DialogContent, DialogTitle, Typography } from '@mui/material'
import MailOutlineIcon from '@mui/icons-material/MailOutline'

// Paso 8: consulta si se desea enviar el correo al Gerente de la CM destino
export default function DialogoConfirmarEnvioMail({ bolsin, alResponder }) {
  return (
    <Dialog open={!!bolsin} onClose={() => alResponder(false)} maxWidth="xs" fullWidth>
      <DialogTitle sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
        <MailOutlineIcon color="primary" /> Informar ubicación
      </DialogTitle>
      {bolsin && (
        <DialogContent>
          <Typography>
            ¿Desea enviar un correo al Gerente de <b>{bolsin.nombreCMDestino}</b> informando la ubicación
            del bolsín <b>{bolsin.numeroBolsin}</b>?
          </Typography>
        </DialogContent>
      )}
      <DialogActions sx={{ px: 3, pb: 2 }}>
        <Button color="inherit" onClick={() => alResponder(false)}>No enviar</Button>
        <Button variant="contained" onClick={() => alResponder(true)}>Enviar correo</Button>
      </DialogActions>
    </Dialog>
  )
}
