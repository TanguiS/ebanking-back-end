import {AuthorizationPolicyRequirementDTO} from "./AuthorizationPolicyRequirement.dto";

export interface AuthorizationPolicyDTO {
    idAuthorizationPolicy: number;
    authorizationType: string;
    priorityUseLevel: string;
    requirement: AuthorizationPolicyRequirementDTO;
}
