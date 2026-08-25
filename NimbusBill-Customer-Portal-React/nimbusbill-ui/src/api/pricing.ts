import { apiRequest } from './customers'; import { PricingUnit } from './products';
export type PricingStatus='DRAFT'|'PENDING_APPROVAL'|'APPROVED'|'ACTIVE'|'REJECTED'|'EXPIRED';
export interface PricingSlabInput{lowerBound:number;upperBound:number|null;flatFee:number|null;percentageRate:number|null;sequenceNo:number;}
export interface PricingRuleInput{productId:string;chargeType:PricingUnit;fixedFee:number|null;percentageRate:number|null;minimumFee:number|null;maximumFee:number|null;taxRate:number;priority:number;slabs:PricingSlabInput[];}
export interface PricingPlanInput{planCode:string;planName:string;description:string;currency:string;effectiveFrom:string;effectiveTo:string|null;rules:PricingRuleInput[];}
export interface PricingRule extends PricingRuleInput{id:string;productCode:string;productName:string;}
export interface PricingVersion{id:string;versionNumber:number;currency:string;effectiveFrom:string;effectiveTo:string|null;status:PricingStatus;createdBy:string;approvedBy:string|null;submittedBy:string|null;createdAt:string;submittedAt:string|null;approvedAt:string|null;decisionComment:string|null;rules:PricingRule[];}
export interface PricingPlan{id:string;planCode:string;planName:string;description:string|null;versions:PricingVersion[];}
export interface PricingAudit{id:string;entityType:string;entityId:string;action:string;actor:string;actorRole:string|null;oldValue:string|null;newValue:string|null;comments:string|null;ipAddress:string|null;createdAt:string;}
export interface Preview{planId:string;versionId:string;versionNumber:number;productCode:string;chargeType:PricingUnit;amount:number;baseFee:number;taxRate:number;taxAmount:number;totalCharge:number;currency:string;overrideApplied:boolean;overrideId:string|null;}
export const listPricingPlans=()=>apiRequest<PricingPlan[]>('/pricing-plans');
export const createPricingPlan=(input:PricingPlanInput)=>apiRequest<PricingPlan>('/pricing-plans',{method:'POST',headers:{'X-Actor':'billing-manager'},body:JSON.stringify(input)});
export const transitionPricing=(planId:string,versionId:string,action:'submit'|'approve'|'reject'|'activate',comments?:string)=>apiRequest<PricingPlan>(`/pricing-plans/${planId}/versions/${versionId}/${action}`,{method:'POST',headers:{'X-Actor':action==='approve'||action==='reject'?'finance-approver':'billing-manager'},body:JSON.stringify({comments:comments||null})});
export const getPricingHistory=(planId:string)=>apiRequest<PricingAudit[]>(`/pricing-plans/${planId}/history`);
export const clonePricingVersion=(planId:string,versionId:string,effectiveFrom:string,effectiveTo:string|null)=>apiRequest<PricingPlan>(`/pricing-plans/${planId}/versions/${versionId}/clone`,{method:'POST',headers:{'X-Actor':'billing-manager'},body:JSON.stringify({effectiveFrom,effectiveTo})});
export const previewPricing=(planId:string,productCode:string,amount:number,transactionDate:string,currency?:string)=>apiRequest<Preview>('/pricing/preview',{method:'POST',body:JSON.stringify({planId,productCode,amount,transactionDate,currency})});
