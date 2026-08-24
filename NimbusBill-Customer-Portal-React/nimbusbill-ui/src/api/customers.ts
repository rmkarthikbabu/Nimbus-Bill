export type CustomerStatus = 'PENDING' | 'ACTIVE' | 'SUSPENDED' | 'INACTIVE';
export type BillingCycle = 'WEEKLY' | 'MONTHLY' | 'QUARTERLY';
export interface Customer { id:string; customerCode:string; customerName:string; legalName:string; customerType:string; industry:string|null; country:string; currency:string; billingCycle:BillingCycle; status:CustomerStatus; taxIdentifier:string|null; website:string|null; createdAt:string; updatedAt:string; version:number; }
export type CustomerInput = Pick<Customer,'customerCode'|'customerName'|'legalName'|'customerType'|'industry'|'country'|'currency'|'billingCycle'|'status'|'taxIdentifier'|'website'>;
export interface CustomerPage { content:Customer[]; page:number; size:number; totalElements:number; totalPages:number; }
export interface AuditLog { id:string; customerId:string; action:string; oldValue:string|null; newValue:string|null; actor:string; ipAddress:string|null; createdAt:string; }
export interface CustomerProduct { id:string; productId:string; productCode:string; productName:string; enabled:boolean; activationDate:string; expiryDate:string|null; }
export interface CustomerPricing { id:string; planId:string; planCode:string; planName:string; effectiveFrom:string; effectiveTo:string|null; active:boolean; }
export interface PricingOverride { id:string; customerId:string; productId:string; productCode:string; productName:string; chargeType:'FIXED'|'PERCENTAGE'|'HYBRID'; fixedFee:number|null; percentageRate:number|null; minimumFee:number|null; maximumFee:number|null; taxRate:number; effectiveFrom:string; effectiveTo:string|null; active:boolean; reason:string|null; createdBy:string; createdAt:string; updatedAt:string; }
export interface PricingOverrideInput { productId:string; chargeType:'FIXED'|'PERCENTAGE'|'HYBRID'; fixedFee:number|null; percentageRate:number|null; minimumFee:number|null; maximumFee:number|null; taxRate:number; effectiveFrom:string; effectiveTo:string|null; reason:string; }
const baseUrl=(import.meta.env.VITE_API_BASE_URL??'/api/v1').replace(/\/$/,'');
export async function apiRequest<T>(path:string,init?:RequestInit):Promise<T>{const token=sessionStorage.getItem('access_token');const response=await fetch(`${baseUrl}${path}`,{...init,headers:{'Content-Type':'application/json',...(token?{Authorization:`Bearer ${token}`}:{ }),...init?.headers}});if(!response.ok){const body=await response.json().catch(()=>null);throw new Error(body?.message??body?.detail??`Request failed (${response.status})`);}return response.status===204?undefined as T:response.json();}
export function listCustomers(search:string,status:string):Promise<CustomerPage>{const params=new URLSearchParams({page:'0',size:'100',sortBy:'customerName',direction:'asc'});if(search.trim())params.set('search',search.trim());if(status)params.set('status',status);return apiRequest(`/customers?${params}`);}
export const getCustomer=(id:string)=>apiRequest<Customer>(`/customers/${id}`);
export const createCustomer=(input:CustomerInput)=>apiRequest<Customer>('/customers',{method:'POST',body:JSON.stringify(input)});
export const updateCustomer=(id:string,input:CustomerInput)=>apiRequest<Customer>(`/customers/${id}`,{method:'PUT',body:JSON.stringify(input)});
export const activateCustomer=(id:string)=>apiRequest<Customer>(`/customers/${id}/activate`,{method:'POST'});
export const suspendCustomer=(id:string)=>apiRequest<Customer>(`/customers/${id}/suspend`,{method:'POST'});
export const deleteCustomer=(id:string)=>apiRequest<void>(`/customers/${id}`,{method:'DELETE'});
export const getCustomerHistory=(id:string)=>apiRequest<AuditLog[]>(`/customers/${id}/history`);
export const getCustomerProducts=(id:string)=>apiRequest<CustomerProduct[]>(`/customers/${id}/products`);
export const setCustomerProducts=(id:string,items:{productId:string;enabled:boolean;activationDate:string;expiryDate:null}[])=>apiRequest<CustomerProduct[]>(`/customers/${id}/products`,{method:'PUT',body:JSON.stringify(items)});
export const getCustomerPricing=(id:string)=>apiRequest<CustomerPricing[]>(`/customers/${id}/pricing`);
export const assignCustomerPricing=(id:string,planId:string,effectiveFrom:string)=>apiRequest<CustomerPricing>(`/customers/${id}/pricing`,{method:'POST',body:JSON.stringify({planId,effectiveFrom,effectiveTo:null})});
export const getPricingOverrides=(id:string)=>apiRequest<PricingOverride[]>(`/customers/${id}/pricing-overrides`);
export const createPricingOverride=(id:string,input:PricingOverrideInput)=>apiRequest<PricingOverride>(`/customers/${id}/pricing-overrides`,{method:'POST',headers:{'X-Actor':'billing-manager'},body:JSON.stringify(input)});
export const deactivatePricingOverride=(customerId:string,overrideId:string)=>apiRequest<void>(`/customers/${customerId}/pricing-overrides/${overrideId}`,{method:'DELETE'});
