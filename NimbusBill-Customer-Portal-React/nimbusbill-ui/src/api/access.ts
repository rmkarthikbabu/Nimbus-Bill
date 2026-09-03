import {apiRequest} from './customers';
export interface AppUser{id:string;email:string;displayName:string;role:string;status:string;mfaRequired:boolean;customerIds:string[];createdAt:string;updatedAt:string}
export interface AppUserInput{email:string;displayName:string;role:string;mfaRequired:boolean;customerIds:string[]}
export const listUsers=()=>apiRequest<AppUser[]>('/access/users');
export const createUser=(input:AppUserInput)=>apiRequest<AppUser>('/access/users',{method:'POST',body:JSON.stringify(input)});
export const setUserStatus=(id:string,status:string)=>apiRequest<AppUser>(`/access/users/${id}/status/${status}`,{method:'POST'});
