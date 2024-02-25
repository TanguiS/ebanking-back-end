import {SoftwareCardDTO} from "./SoftwareCard.dto";
import {AuthorizationPolicyDTO} from "./AuthorizationPolicyDTO";

export interface PreFilledCardInformationDTO {
    expirationDateInYears: number;
    upperLimitPerMonth: number;
    upperLimitPerTransaction: number;
}
