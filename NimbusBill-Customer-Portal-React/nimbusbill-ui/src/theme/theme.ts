import { alpha, createTheme } from '@mui/material/styles';
export const theme = createTheme({
  palette: { mode: 'light', primary: { main: '#3157D5', dark: '#223FA8', light: '#6F8BF2' }, secondary: { main: '#00A58E' }, background: { default: '#F5F7FB', paper: '#FFFFFF' }, text: { primary: '#17233C', secondary: '#667085' }, divider: '#E6EAF1', success: { main: '#16805D' }, warning: { main: '#B76E00' }, error: { main: '#C43C4B' } },
  typography: { fontFamily: 'Inter, ui-sans-serif, system-ui, -apple-system, Segoe UI, sans-serif', h4: { fontWeight: 800, fontSize: 'clamp(1.7rem, 2.4vw, 2.15rem)', letterSpacing: '-0.035em' }, h5: { fontWeight: 800, letterSpacing: '-0.025em' }, h6: { fontWeight: 750, letterSpacing: '-0.015em' }, subtitle1: { fontWeight: 700 }, button: { textTransform: 'none', fontWeight: 750, letterSpacing: '-0.01em' } },
  shape: { borderRadius: 14 },
  components: {
    MuiCssBaseline: { styleOverrides: { body: { backgroundImage: 'radial-gradient(circle at 92% 0%, rgba(49,87,213,.055), transparent 26%)' }, '::selection': { background: alpha('#3157D5', .18) }, '*::-webkit-scrollbar': { width: 8, height: 8 }, '*::-webkit-scrollbar-thumb': { background: '#C9D1DF', borderRadius: 8 } } },
    MuiCard: { styleOverrides: { root: { boxShadow: '0 1px 2px rgba(16,24,40,.04), 0 8px 24px rgba(16,24,40,.045)', border: '1px solid #E7EAF0', backgroundImage: 'none' } } },
    MuiCardContent: { styleOverrides: { root: { padding: 24, '&:last-child': { paddingBottom: 24 } } } },
    MuiButton: { defaultProps: { disableElevation: true }, styleOverrides: { root: { minHeight: 40, borderRadius: 10, paddingInline: 16 }, containedPrimary: { boxShadow: '0 4px 12px rgba(49,87,213,.22)' } } },
    MuiTextField: { defaultProps: { size: 'small' } },
    MuiOutlinedInput: { styleOverrides: { root: { borderRadius: 10, background: '#FFFFFF', '&:hover .MuiOutlinedInput-notchedOutline': { borderColor: '#98A2B3' }, '&.Mui-focused': { boxShadow: '0 0 0 3px rgba(49,87,213,.11)' } } } },
    MuiChip: { styleOverrides: { root: { fontWeight: 750, borderRadius: 8 } } },
    MuiTableHead: { styleOverrides: { root: { background: '#F8FAFC' } } },
    MuiTableCell: { styleOverrides: { root: { borderColor: '#E8ECF2', padding: '13px 16px' }, head: { color: '#475467', fontSize: '.75rem', fontWeight: 800, letterSpacing: '.045em', textTransform: 'uppercase' } } },
    MuiTableRow: { styleOverrides: { root: { '&.MuiTableRow-hover:hover': { backgroundColor: '#F7F9FD' } } } },
    MuiAlert: { styleOverrides: { root: { borderRadius: 12, alignItems: 'center' } } }
  }
});
