import {SoftwareCardDTO} from "./SoftwareCard.dto";
import {AuthorizationPolicyDTO} from "./AuthorizationPolicyDTO";

export interface CardProductDTO {
    idCardProduct: number;
    name: string;
    cardType: string;
    contactlessUpperLimitPerTransaction: number;
    numberOfContactlessTransactionBeforeAskingPin: number;
    softwareCards: SoftwareCardDTO[];
    authorizationPolicies: AuthorizationPolicyDTO[];
}
