import {UserInformationDTO} from "../user_information/UserInformation.dto";
import {BankAccountDTO} from "../bank_account/BankAccount.dto";

export interface ClientDashboardDTO {
    client: UserInformationDTO;
    bankAccounts: BankAccountDTO[];
}
