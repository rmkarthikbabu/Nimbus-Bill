import { createTheme } from '@mui/material/styles';
export const theme = createTheme({
  palette: { mode: 'light', primary: { main: '#2251CC' }, secondary: { main: '#00A3A3' }, background: { default: '#F4F7FB', paper: '#FFFFFF' }, text: { primary: '#17243A', secondary: '#64748B' }, success: { main: '#12805C' }, warning: { main: '#C77D00' }, error: { main: '#C73E3A' } },
  typography: { fontFamily: 'Inter, ui-sans-serif, system-ui, -apple-system, Segoe UI, sans-serif', h4: { fontWeight: 800 }, h5: { fontWeight: 800 }, h6: { fontWeight: 700 }, button: { textTransform: 'none', fontWeight: 700 } },
  shape: { borderRadius: 14 },
  components: {
    MuiCard: { styleOverrides: { root: { boxShadow: '0 10px 30px rgba(15,23,42,.06)', border: '1px solid #E7ECF3' } } },
    MuiButton: { defaultProps: { disableElevation: true }, styleOverrides: { root: { borderRadius: 10 } } },
    MuiTextField: { defaultProps: { size: 'small' } },
    MuiChip: { styleOverrides: { root: { fontWeight: 700 } } }
  }
});
