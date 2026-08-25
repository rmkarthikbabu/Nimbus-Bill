import { apiRequest } from './customers';
export type AccountType='CLIENT'|'INTERNAL';
export interface TransferAccount{id:string;customerId:string|null;accountNumber:string;accountName:string;accountType:AccountType;currency:string;ledgerBalance:number;availableBalance:number;status:string;}
export interface TransferLeg{legId:string;sequence:number;type:string;debitAccountId:string;creditAccountId:string;status:string;ledgerReference:string;postedAt:string;}
export interface InternalTransfer{transferId:string;clientReferenceId:string;customerId:string;amount:number;currency:string;status:string;currentStep:number;billingStatus:string;pricingPlanVersionId:string|null;chargeAmount:number|null;createdAt:string;completedAt:string|null;legs:TransferLeg[];}
export interface TransferAccountInput{customerId:string|null;accountNumber:string;accountName:string;accountType:AccountType;currency:string;openingBalance:number;}
export interface InternalTransferInput{customerId:string;clientReferenceId:string;sourceAccountId:string;sourceBridgeAccountId:string;destinationBridgeAccountId:string;destinationAccountId:string;productCode:string;amount:number;currency:string;}
export const listTransferAccounts=(customerId:string)=>apiRequest<TransferAccount[]>(`/transfer-accounts?customerId=${customerId}`);
export const createTransferAccount=(input:TransferAccountInput)=>apiRequest<TransferAccount>('/transfer-accounts',{method:'POST',body:JSON.stringify(input)});
export const listInternalTransfers=(customerId:string)=>apiRequest<InternalTransfer[]>(`/internal-transfers?customerId=${customerId}`);
export const initiateInternalTransfer=(input:InternalTransferInput)=>apiRequest<InternalTransfer>('/internal-transfers',{method:'POST',body:JSON.stringify(input)});
