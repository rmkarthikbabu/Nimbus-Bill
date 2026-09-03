import {apiRequest} from './customers';
export interface AssistantAnswer{intent:string;answer:string;sources:string[];confidence:number;generatedAt:string}
export const askAssistant=(question:string,customerId:string)=>apiRequest<AssistantAnswer>('/ai/ask',{method:'POST',body:JSON.stringify({question,customerId:customerId||null})});
