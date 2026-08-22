export type CustomerStatus = 'PENDING' | 'ACTIVE' | 'SUSPENDED' | 'INACTIVE';
export type BillingCycle = 'WEEKLY' | 'MONTHLY' | 'QUARTERLY';
export interface Customer { id:string; customerCode:string; customerName:string; legalName:string; customerType:string; industry:string|null; country:string; currency:string; billingCycle:BillingCycle; status:CustomerStatus; taxIdentifier:string|null; website:string|null; createdAt:string; updatedAt:string; version:number; }
export type CustomerInput = Pick<Customer,'customerCode'|'customerName'|'legalName'|'customerType'|'industry'|'country'|'currency'|'billingCycle'|'status'|'taxIdentifier'|'website'>;
export interface CustomerPage { content:Customer[]; page:number; size:number; totalElements:number; totalPages:number; }
export interface AuditLog { id:string; customerId:string; action:string; oldValue:string|null; newValue:string|null; actor:string; ipAddress:string|null; createdAt:string; }
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
