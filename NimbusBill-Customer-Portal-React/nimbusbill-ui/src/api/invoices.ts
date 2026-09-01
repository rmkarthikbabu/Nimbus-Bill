import {apiRequest} from './customers';
export interface InvoiceLine{id:string;transactionId:string;productCode:string;description:string;baseAmount:number;taxAmount:number;totalAmount:number;currency:string}
export interface Invoice{id:string;billingRunId:string;customerId:string;billingAccountId:string;invoiceNumber:string;status:string;invoiceDate:string;dueDate:string;currency:string;subtotal:number;taxTotal:number;adjustmentTotal:number;grandTotal:number;createdAt:string;submittedAt:string|null;approvedAt:string|null;issuedAt:string|null;lines:InvoiceLine[]}
export interface TaxProfile{id:string;customerId:string;taxIdentifier:string|null;taxCountry:string;taxRegion:string|null;placeOfSupply:string|null;defaultTaxRate:number;taxExempt:boolean;effectiveFrom:string}
export interface TaxProfileInput{taxIdentifier:string;taxCountry:string;taxRegion:string;placeOfSupply:string;defaultTaxRate:number;taxExempt:boolean;effectiveFrom:string}
export interface Delivery{id:string;channel:string;destination:string;status:string;attemptCount:number;lastError:string|null;createdAt:string;deliveredAt:string|null}
export const listInvoices=(customerId:string)=>apiRequest<Invoice[]>('/invoices?customerId='+encodeURIComponent(customerId));
export const generateInvoice=(billingRunId:string)=>apiRequest<Invoice>('/billing-runs/'+billingRunId+'/invoice',{method:'POST'});
export const invoiceAction=(id:string,action:'submit'|'approve'|'issue'|'cancel',comment:string)=>apiRequest<Invoice>('/invoices/'+id+'/'+action,{method:'POST',body:JSON.stringify({comment})});
export const lifecycleAction=(id:string,action:'reject'|'reopen'|'dispute'|'paid',comment:string)=>apiRequest<void>('/invoices/'+id+'/'+action,{method:'POST',body:JSON.stringify({comment})});
export const addAdjustment=(id:string,input:{adjustmentType:'CREDIT'|'DEBIT';amount:number;reason:string})=>apiRequest<void>('/invoices/'+id+'/adjustments',{method:'POST',body:JSON.stringify(input)});
export const getTaxProfile=(customerId:string)=>apiRequest<TaxProfile>('/customers/'+customerId+'/tax-profile');
export const saveTaxProfile=(customerId:string,input:TaxProfileInput)=>apiRequest<TaxProfile>('/customers/'+customerId+'/tax-profile',{method:'PUT',body:JSON.stringify(input)});
export const listDeliveries=(id:string)=>apiRequest<Delivery[]>('/invoices/'+id+'/deliveries');
export const deliverInvoice=(id:string,channel:'EMAIL'|'WEBHOOK'|'API',destination:string)=>apiRequest<Delivery>('/invoices/'+id+'/deliveries',{method:'POST',body:JSON.stringify({channel,destination})});

export async function downloadInvoiceDocument(id:string,invoiceNumber:string){const base=(import.meta.env.VITE_API_BASE_URL??'/api/v1').replace(/\/$/,'');const token=sessionStorage.getItem('access_token');const response=await fetch(`${base}/invoices/${id}/document`,{headers:token?{Authorization:`Bearer ${token}`}:{}});if(!response.ok)throw new Error(`Request failed (${response.status})`);const blob=await response.blob();const url=URL.createObjectURL(blob);const link=document.createElement('a');link.href=url;link.download=`${invoiceNumber}.pdf`;link.click();URL.revokeObjectURL(url);}