export type AppRole='ADMIN'|'BILLING_MANAGER'|'FINANCE'|'OPERATIONS'|'AUDITOR'|'READ_ONLY';
export const currentRole=(import.meta.env.VITE_USER_ROLE??'ADMIN') as AppRole;
export const canEdit=['ADMIN','BILLING_MANAGER'].includes(currentRole);
export const canChangeStatus=['ADMIN','OPERATIONS'].includes(currentRole);
export const canDelete=currentRole==='ADMIN';
export const canViewAudit=['ADMIN','BILLING_MANAGER','AUDITOR'].includes(currentRole);
