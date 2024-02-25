import {IndividualDTO} from "./Individual.dto";
import {LegalEntityDTO} from "./LegalEntity.dto";

export interface DirectDebitDTO {
    recurrence: string;
    idDirectDebit: number;
    individual: IndividualDTO | undefined;
    legalEntity: LegalEntityDTO | undefined;
}
