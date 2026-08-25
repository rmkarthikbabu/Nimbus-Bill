import { apiRequest } from './customers';
export type PricingUnit='FIXED'|'PERCENTAGE'|'SLAB'|'HYBRID'; export type ProductStatus='DRAFT'|'ACTIVE'|'INACTIVE';
export interface PaymentProduct{id:string;productCode:string;productName:string;description:string|null;pricingUnit:PricingUnit;minimumFee:number|null;maximumFee:number|null;taxApplicable:boolean;status:ProductStatus;effectiveFrom:string;effectiveTo:string|null;transactionTypes:string[];currencies:string[];version:number;}
export interface ProductInput{productCode:string;productName:string;description:string;pricingUnit:PricingUnit;minimumFee:number|null;maximumFee:number|null;taxApplicable:boolean;status:ProductStatus;effectiveFrom:string;effectiveTo:string|null;transactionTypes:string[];currencies:string[];}
export const listProducts=(status='')=>apiRequest<PaymentProduct[]>(`/products${status?`?status=${status}`:''}`);
export const createProduct=(input:ProductInput)=>apiRequest<PaymentProduct>('/products',{method:'POST',body:JSON.stringify(input)});
export const setProductActive=(id:string,active:boolean)=>apiRequest<PaymentProduct>(`/products/${id}/${active?'activate':'deactivate'}`,{method:'POST'});
