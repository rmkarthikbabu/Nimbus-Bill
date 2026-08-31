import {apiRequest} from './customers';
export interface InvoiceLine{id:string;transactionId:string;productCode:string;description:string;baseAmount:number;taxAmount:number;totalAmount:number;currency:string}
export interface Invoice{id:string;billingRunId:string;customerId:string;billingAccountId:string;invoiceNumber:string;status:string;invoiceDate:string;dueDate:string;currency:string;subtotal:number;taxTotal:number;adjustmentTotal:number;grandTotal:number;createdAt:string;submittedAt:string|null;approvedAt:string|null;issuedAt:string|null;lines:InvoiceLine[]}
export const listInvoices=(customerId:string)=>apiRequest<Invoice[]>('/invoices?customerId='+encodeURIComponent(customerId));
export const generateInvoice=(billingRunId:string)=>apiRequest<Invoice>('/billing-runs/'+billingRunId+'/invoice',{method:'POST'});
export const invoiceAction=(id:string,action:'submit'|'approve'|'issue'|'cancel',comment:string)=>apiRequest<Invoice>('/invoices/'+id+'/'+action,{method:'POST',body:JSON.stringify({comment})});
