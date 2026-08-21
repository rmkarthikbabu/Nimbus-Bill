import { Chip } from '@mui/material';
export function StatusChip({status}:{status:string}){
 const normalized=status.toUpperCase(); const color = normalized==='ACTIVE'?'success':normalized==='PENDING'?'warning':normalized==='SUSPENDED'?'error':'default';
 return <Chip size="small" label={normalized.charAt(0)+normalized.slice(1).toLowerCase()} color={color as any} variant="outlined"/>;
}
