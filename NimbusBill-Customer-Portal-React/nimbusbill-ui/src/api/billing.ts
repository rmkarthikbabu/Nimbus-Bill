import {apiRequest} from './customers';
export interface BillingAccount{id:string;customerId:string;accountCode:string;accountName:string;currency:string;billingCycle:string;paymentTermsDays:number;status:string;createdAt:string}
export interface BillingPeriod{id:string;billingAccountId:string;periodStart:string;periodEnd:string;status:string;closedAt:string|null}
export interface BillingRun{id:string;billingPeriodId:string;billingAccountId:string;status:string;chargeCount:number;subtotal:number;taxTotal:number;grandTotal:number;startedAt:string;completedAt:string|null;failureReason:string|null}
export const listBillingAccounts=(customerId:string)=>apiRequest<BillingAccount[]>(`/billing-accounts?customerId=${customerId}`);
export const createBillingAccount=(input:{customerId:string;accountCode:string;accountName:string;currency:string;billingCycle:string;paymentTermsDays:number})=>apiRequest<BillingAccount>('/billing-accounts',{method:'POST',body:JSON.stringify(input)});
export const listBillingPeriods=(accountId:string)=>apiRequest<BillingPeriod[]>(`/billing-periods?billingAccountId=${accountId}`);
export const createBillingPeriod=(input:{billingAccountId:string;periodStart:string;periodEnd:string})=>apiRequest<BillingPeriod>('/billing-periods',{method:'POST',body:JSON.stringify(input)});
export const previewBillingPeriod=(id:string)=>apiRequest<BillingRun>(`/billing-periods/${id}/preview`,{method:'POST'});
export const executeBillingRun=(id:string)=>apiRequest<BillingRun>(`/billing-runs/${id}/execute`,{method:'POST'});
export const listBillingRuns=(customerId:string)=>apiRequest<BillingRun[]>(`/billing-runs?customerId=${customerId}`);
