import { FormEvent, useMemo, useState } from 'react';
import {
  Alert, Box, Button, Card, CardContent, Chip, Dialog, DialogActions, DialogContent,
  DialogTitle, MenuItem, Stack, Table, TableBody, TableCell, TableHead, TableRow,
  TextField, Typography,
} from '@mui/material';
import AddRounded from '@mui/icons-material/AddRounded';
import BlockRounded from '@mui/icons-material/BlockRounded';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import {
  createPricingOverride, deactivatePricingOverride, getPricingOverrides, listCustomers,
  PricingOverrideInput,
} from '../api/customers';
import { listProducts } from '../api/products';

const today = new Date().toISOString().slice(0, 10);
const initialForm: PricingOverrideInput = {
  productId: '', chargeType: 'FIXED', fixedFee: 0, percentageRate: null,
  minimumFee: null, maximumFee: null, taxRate: 18, effectiveFrom: today,
  effectiveTo: null, reason: '',
};

export default function PricingOverrides() {
  const queryClient = useQueryClient();
  const [customerId, setCustomerId] = useState('');
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState<PricingOverrideInput>(initialForm);
  const customers = useQuery({ queryKey: ['customers', 'override-select'], queryFn: () => listCustomers('', 'ACTIVE') });
  const products = useQuery({ queryKey: ['products', 'ACTIVE'], queryFn: () => listProducts('ACTIVE') });
  const overrides = useQuery({
    queryKey: ['pricing-overrides', customerId],
    queryFn: () => getPricingOverrides(customerId),
    enabled: Boolean(customerId),
  });
  const create = useMutation({
    mutationFn: () => createPricingOverride(customerId, form),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['pricing-overrides', customerId] });
      setOpen(false); setForm(initialForm);
    },
  });
  const deactivate = useMutation({
    mutationFn: (overrideId: string) => deactivatePricingOverride(customerId, overrideId),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['pricing-overrides', customerId] }),
  });
  const error = customers.error || products.error || overrides.error || create.error || deactivate.error;
  const productOptions = useMemo(() => products.data ?? [], [products.data]);
  const set = <K extends keyof PricingOverrideInput>(key: K, value: PricingOverrideInput[K]) => setForm(v => ({ ...v, [key]: value }));
  const submit = (event: FormEvent) => { event.preventDefault(); create.mutate(); };

  return <Stack spacing={3}>
    <Box display="flex" justifyContent="space-between" alignItems="center" gap={2}>
      <Box><Typography variant="h4">Customer pricing overrides</Typography><Typography color="text.secondary">Apply effective-dated product pricing for individual customers.</Typography></Box>
      <Button variant="contained" startIcon={<AddRounded />} disabled={!customerId} onClick={() => setOpen(true)}>New override</Button>
    </Box>
    {error && <Alert severity="error">{error.message}</Alert>}
    <Card><CardContent><TextField select fullWidth label="Customer" value={customerId} onChange={e => setCustomerId(e.target.value)}>
      {customers.data?.content.map(c => <MenuItem key={c.id} value={c.id}>{c.customerName} · {c.customerCode}</MenuItem>)}
    </TextField></CardContent></Card>
    <Card><CardContent>
      {!customerId ? <Typography color="text.secondary">Select a customer to view pricing overrides.</Typography> :
        <Table><TableHead><TableRow>{['Product', 'Charge', 'Fee', 'Effective period', 'Reason', 'Status', 'Action'].map(x => <TableCell key={x} sx={{ fontWeight: 800 }}>{x}</TableCell>)}</TableRow></TableHead>
          <TableBody>{overrides.data?.map(o => <TableRow key={o.id}>
            <TableCell><Typography fontWeight={800}>{o.productName}</Typography><Typography variant="caption">{o.productCode}</Typography></TableCell>
            <TableCell>{o.chargeType}</TableCell>
            <TableCell>{o.fixedFee != null ? `Fixed ${o.fixedFee}` : ''}{o.fixedFee != null && o.percentageRate != null ? ' + ' : ''}{o.percentageRate != null ? `${o.percentageRate}%` : ''}</TableCell>
            <TableCell>{o.effectiveFrom} → {o.effectiveTo ?? 'Open ended'}</TableCell><TableCell>{o.reason ?? '—'}</TableCell>
            <TableCell><Chip size="small" color={o.active ? 'success' : 'default'} label={o.active ? 'ACTIVE' : 'INACTIVE'} /></TableCell>
            <TableCell>{o.active && <Button color="warning" startIcon={<BlockRounded />} onClick={() => deactivate.mutate(o.id)}>Deactivate</Button>}</TableCell>
          </TableRow>)}{overrides.data?.length === 0 && <TableRow><TableCell colSpan={7}><Typography color="text.secondary">No overrides configured.</Typography></TableCell></TableRow>}</TableBody>
        </Table>}
    </CardContent></Card>
    <Dialog open={open} onClose={() => setOpen(false)} fullWidth maxWidth="md"><Box component="form" onSubmit={submit}>
      <DialogTitle>Create customer pricing override</DialogTitle><DialogContent><Stack spacing={2} mt={1}>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}><TextField required select fullWidth label="Product" value={form.productId} onChange={e => set('productId', e.target.value)}>{productOptions.map(p => <MenuItem key={p.id} value={p.id}>{p.productName}</MenuItem>)}</TextField>
          <TextField select fullWidth label="Charge type" value={form.chargeType} onChange={e => { const type=e.target.value as PricingOverrideInput['chargeType'];set('chargeType',type);set('fixedFee',type==='PERCENTAGE'?null:0);set('percentageRate',type==='FIXED'?null:0); }}><MenuItem value="FIXED">Fixed</MenuItem><MenuItem value="PERCENTAGE">Percentage</MenuItem><MenuItem value="HYBRID">Hybrid</MenuItem></TextField></Stack>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>{form.chargeType !== 'PERCENTAGE' && <TextField required fullWidth type="number" label="Fixed fee" value={form.fixedFee ?? ''} onChange={e => set('fixedFee', Number(e.target.value))} />}{form.chargeType !== 'FIXED' && <TextField required fullWidth type="number" label="Percentage rate" value={form.percentageRate ?? ''} onChange={e => set('percentageRate', Number(e.target.value))} />}<TextField required fullWidth type="number" label="Tax rate %" value={form.taxRate} onChange={e => set('taxRate', Number(e.target.value))} /></Stack>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}><TextField fullWidth type="number" label="Minimum fee" value={form.minimumFee ?? ''} onChange={e => set('minimumFee', e.target.value === '' ? null : Number(e.target.value))} /><TextField fullWidth type="number" label="Maximum fee" value={form.maximumFee ?? ''} onChange={e => set('maximumFee', e.target.value === '' ? null : Number(e.target.value))} /></Stack>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}><TextField required fullWidth type="date" label="Effective from" value={form.effectiveFrom} onChange={e => set('effectiveFrom', e.target.value)} slotProps={{ inputLabel: { shrink: true } }} /><TextField fullWidth type="date" label="Effective to" value={form.effectiveTo ?? ''} onChange={e => set('effectiveTo', e.target.value || null)} slotProps={{ inputLabel: { shrink: true } }} /></Stack>
        <TextField fullWidth label="Business reason" value={form.reason} onChange={e => set('reason', e.target.value)} multiline minRows={2} />
      </Stack></DialogContent><DialogActions><Button onClick={() => setOpen(false)}>Cancel</Button><Button type="submit" variant="contained" disabled={create.isPending}>{create.isPending ? 'Creating…' : 'Create override'}</Button></DialogActions>
    </Box></Dialog>
  </Stack>;
}
