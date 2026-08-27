import {apiRequest} from './customers';
export interface TransactionStatusEntry{status:string;detail:string;createdAt:string}
export interface PaymentTransaction{id:string;customerId:string;clientReferenceId:string;productCode:string;transactionType:string;amount:number;currency:string;sourceAccount:string|null;destinationAccount:string|null;status:string;failureReason:string|null;baseFee:number|null;taxAmount:number|null;totalCharge:number|null;pricingPlanVersionId:string|null;chargeStatus:string|null;transactionKind:string;ingestionSource:string;originalTransactionId:string|null;reconciliationStatus:string;externalSettlementReference:string|null;createdAt:string;completedAt:string|null;history:TransactionStatusEntry[]}
export interface PaymentTransactionInput{customerId:string;clientReferenceId:string;productCode:string;transactionType:string;amount:number;currency:string;sourceAccount:string;destinationAccount:string}
export interface TransactionFilters{status?:string;product?:string;reference?:string;from?:string;to?:string}
export const listTransactions=(customerId:string,filters:TransactionFilters={})=>{const params=new URLSearchParams({customerId});Object.entries(filters).forEach(([key,value])=>{if(value)params.set(key,value)});return apiRequest<PaymentTransaction[]>(`/transactions?${params}`)};
export const getTransaction=(id:string)=>apiRequest<PaymentTransaction>(`/transactions/${id}`);
export const createTransaction=(input:PaymentTransactionInput)=>apiRequest<PaymentTransaction>('/transactions',{method:'POST',body:JSON.stringify(input)});
export interface TransactionBatchResponse{received:number;completed:number;rejected:number;failed:number;errors:string[];transactions:PaymentTransaction[]}
export const createTransactionBatch=(items:PaymentTransactionInput[])=>apiRequest<TransactionBatchResponse>('/transactions/batch',{method:'POST',body:JSON.stringify(items)});
export const reverseTransaction=(id:string,clientReferenceId:string,reason:string)=>apiRequest<PaymentTransaction>(`/transactions/${id}/reversal`,{method:'POST',body:JSON.stringify({clientReferenceId,reason})});
export const reconcileTransaction=(id:string,status:'MATCHED'|'EXCEPTION',externalSettlementReference:string,detail:string)=>apiRequest<PaymentTransaction>(`/transactions/${id}/reconciliation`,{method:'POST',body:JSON.stringify({status,externalSettlementReference,detail})});
export interface TransactionSummary{total:number;completed:number;rejected:number;pendingReconciliation:number;reconciliationExceptions:number;completedAmount:number;totalCharges:number}
export const getTransactionSummary=(customerId:string)=>apiRequest<TransactionSummary>(`/transactions/summary?customerId=${encodeURIComponent(customerId)}`);
export interface TransactionOutboxEvent{id:string;transactionId:string;eventType:string;status:string;attemptCount:number;lastError:string|null;nextAttemptAt:string|null;createdAt:string;publishedAt:string|null}
export const listTransactionOutbox=()=>apiRequest<TransactionOutboxEvent[]>('/transaction-outbox');
export const retryTransactionOutbox=(id:string)=>apiRequest<void>(`/transaction-outbox/${id}/retry`,{method:'POST'});
